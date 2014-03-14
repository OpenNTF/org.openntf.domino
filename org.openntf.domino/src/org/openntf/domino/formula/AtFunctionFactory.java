/*
 * © Copyright FOCONIS AG, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 */
package org.openntf.domino.formula;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openntf.domino.formula.impl.Arithmetic;
import org.openntf.domino.formula.impl.AtFunctionGeneric;
import org.openntf.domino.formula.impl.AtFunctionSimple;
import org.openntf.domino.formula.impl.NotImplemented;
import org.openntf.domino.formula.impl.Operator;

public class AtFunctionFactory {

	private Map<String, AtFunction> functions = new HashMap<String, AtFunction>();
	private static AtFunctionFactory instance;

	/**
	 * Returns the atFunction object that can evaluate 'funcName'
	 * 
	 * @param funcName
	 * @return
	 */
	public AtFunction getFunction(final String funcName) {
		return functions.get(funcName.toLowerCase());
	}

	/**
	 * Returns all avaliable at-Functions
	 * 
	 * @return
	 */
	public Map<String, AtFunction> getFunctions() {
		return Collections.unmodifiableMap(functions);
	}

	/**
	 * If you inherit from this mehtod, you can initialize different at-Functions in the constructor
	 */
	protected void init(final AtFunction... fs) {
		for (AtFunction f : fs) {
			if (functions.put(f.getImage().toLowerCase(), f) != null) {
				throw new IllegalArgumentException("Function " + f + " already defined.");
			}
		}
	}

	public AtFunctionFactory() {
		super();
	}

	/**
	 * This scans the class for apropriate atFunctions.
	 * 
	 * @param cls
	 */
	protected AtFunctionFactory(final Class<?> cls) {
		super();
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {

			String methodName = method.getName();
			if (methodName.startsWith("at")) {
				if (Modifier.isPrivate(method.getModifiers())) {
					// skip methods declared as private
				} else if (Modifier.isStatic(method.getModifiers())) {

					methodName = "@".concat(methodName.substring(2));

					if (ValueHolder.class.isAssignableFrom(method.getReturnType())) {
						init(new AtFunctionGeneric(methodName, method));
					} else {
						init(new AtFunctionSimple(methodName, method));
					}
				} else {
					throw new IllegalAccessError("Method " + methodName + " is either not static.");
				}
			}
		}
	}

	protected void addFactory(final org.openntf.domino.formula.AtFunctionFactory fact) {
		functions.putAll(fact.getFunctions());
	}

	/**
	 * This is the global "default"-instance.
	 * 
	 * @return
	 */
	public static synchronized AtFunctionFactory getInstance() {
		if (instance == null) {
			instance = new AtFunctionFactory();
			instance.addFactory(new Operator.Factory());
			instance.addFactory(new NotImplemented.Factory());
			instance.addFactory(new AtFunctionFactory(Arithmetic.class));
		}
		return instance;
	}

}
