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
import org.openntf.domino.formula.impl.AtFunction;
import org.openntf.domino.formula.impl.AtFunctionGeneric;
import org.openntf.domino.formula.impl.AtFunctionSimple;
import org.openntf.domino.formula.impl.Comparators;
import org.openntf.domino.formula.impl.Constant;
import org.openntf.domino.formula.impl.DateTimeFunctions;
//import org.openntf.domino.formula.impl.DateTimeFunctions;
//import org.openntf.domino.formula.impl.DocProperties;
//import org.openntf.domino.formula.impl.NativeEvaluateFunctions;
import org.openntf.domino.formula.impl.Negators;
//import org.openntf.domino.formula.impl.NotImplemented;
//import org.openntf.domino.formula.impl.NotSupported;
import org.openntf.domino.formula.impl.Operators;
import org.openntf.domino.formula.impl.OperatorsBool;
import org.openntf.domino.formula.impl.TextFunctions;

/**
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class FunctionFactory {

	private Map<String, Function> functions = new HashMap<String, Function>();
	private boolean immutable;
	private static FunctionFactory instance;

	/**
	 * Default constructor
	 */
	public FunctionFactory() {
		super();
	}

	/**
	 * This Constructor scans the class for apropriate atFunctions.
	 * 
	 * @param cls
	 */
	protected FunctionFactory(final Class<?> cls) {
		super();
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {

			String methodName = method.getName();
			if (methodName.startsWith("at")) {
				if (Modifier.isPrivate(method.getModifiers())) {
					// skip methods declared as private
				} else if (Modifier.isStatic(method.getModifiers())) {

					methodName = "@".concat(methodName.substring(2));

					// here the magic happens. If the return type of the implemented function is
					// a ValueHolder then we create an AtFunctionGeneric. You have to do multi value handling
					// otherwise an AtFunctionSimple is created that does multi value handling for you.
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

	/**
	 * Returns the atFunction object that can evaluate 'funcName'
	 */
	public AtFunction getFunction(final String funcName) {
		return (AtFunction) functions.get(funcName.toLowerCase());
	}

	/**
	 * Returns all avaliable at-Functions
	 */
	public Map<String, Function> getFunctions() {
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
		setImmutable();
	}

	/**
	 * Sets the factory to immutable, so that it is safe to cache them.
	 */
	public void setImmutable() {
		immutable = true;

	}

	/**
	 * Adds an other Factory to this Factory
	 * 
	 * @param fact
	 */
	protected void addFactory(final org.openntf.domino.formula.FunctionFactory fact) {
		if (immutable)
			throw new UnsupportedOperationException("Cannot add Factory, because this Factory is immutable");
		functions.putAll(fact.getFunctions());
	}

	/**
	 * This is the global "default"-instance.
	 */
	public static synchronized FunctionFactory getDefaultInstance() {
		if (instance == null) {
			instance = new FunctionFactory();
			instance.addFactory(new Operators.Factory());
			instance.addFactory(new OperatorsBool.Factory());
			instance.addFactory(new Comparators.Factory());
			instance.addFactory(new Negators.Factory());
			//instance.addFactory(new NotImplemented.Factory());
			instance.addFactory(new FunctionFactory(Arithmetic.class));
			//instance.addFactory(new FunctionFactory(DocProperties.class));
			instance.addFactory(new FunctionFactory(TextFunctions.class));
			instance.addFactory(new FunctionFactory(DateTimeFunctions.class));
			//instance.addFactory(new FunctionFactory(NativeEvaluateFunctions.class));
			instance.addFactory(new FunctionFactory(Constant.class));
			//instance.addFactory(new FunctionFactory(NotSupported.class));
			instance.setImmutable();
		}
		return instance;
	}

}
