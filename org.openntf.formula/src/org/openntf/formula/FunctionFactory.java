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
package org.openntf.formula;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.openntf.formula.impl.AtFunction;
import org.openntf.formula.impl.AtFunctionGeneric;
import org.openntf.formula.impl.AtFunctionSimple;

/**
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class FunctionFactory {

	private static final Map<Class<?>, Map<String, Function>> functionCache = new HashMap<Class<?>, Map<String, Function>>();
	private final Map<String, Function> functions;
	private boolean immutable;

	/**
	 * Default constructor
	 */
	private FunctionFactory() {
		super();
		functions = new HashMap<String, Function>();
	}

	public static FunctionFactory createInstance() {
		FunctionFactory instance = new FunctionFactory();
		ServiceLoader<FunctionFactory> loader = ServiceLoader.load(FunctionFactory.class);
		if (loader.iterator().hasNext()) {

			List<FunctionFactory> loaderList = new ArrayList<FunctionFactory>();
			for (FunctionFactory fact : loader) {
				loaderList.add(fact);
			}

			for (int i = loaderList.size() - 1; i >= 0; i--) {
				//TODO RPR Add logger here?
				//System.out.println("ADD Factory " + fact.getClass().getName());
				instance.addFactory(loaderList.get(i));
			}
		} else {
			// case if serviceLoader does not work (notesAgent)
			instance.addFactory(new org.openntf.formula.function.Operators.Factory());
			instance.addFactory(new org.openntf.formula.function.OperatorsBool.Factory());
			instance.addFactory(new org.openntf.formula.function.Negators.Factory());
			instance.addFactory(new org.openntf.formula.function.Comparators.Factory());
			instance.addFactory(new org.openntf.formula.function.Constants.Factory());
			instance.addFactory(new org.openntf.formula.function.MathFunctions.Factory());
			instance.addFactory(new org.openntf.formula.function.DateTimeFunctions.Factory());
			instance.addFactory(new org.openntf.formula.function.TextFunctions.Factory());
		}
		instance.setImmutable();

		return instance;
	}

	/**
	 * This Constructor scans the class for apropriate atFunctions.
	 * 
	 * @param cls
	 */
	protected FunctionFactory(final Class<?> cls) {
		super();
		synchronized (functionCache) {
			Map<String, Function> tmp = functionCache.get(cls);
			if (tmp == null) {
				functions = new HashMap<String, Function>();
			} else {
				functions = tmp;
				return;
			}
			init(cls);
			functionCache.put(cls, functions);
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
	//	private void init(final AtFunction... fs) {
	//		System.out.println(fs[0]);
	//		for (AtFunction f : fs) {
	//			if (functions.put(f.getImage().toLowerCase(), f) != null) {
	//				throw new IllegalArgumentException("Function " + f + " already defined.");
	//			}
	//		}
	//		setImmutable();
	//	}

	protected void add(final AtFunction f) {
		functions.put(f.getImage().toLowerCase(), f);
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
	protected void addFactory(final org.openntf.formula.FunctionFactory fact) {
		if (immutable)
			throw new UnsupportedOperationException("Cannot add Factory, because this Factory is immutable");
		functions.putAll(fact.functions);
	}

	/**
	 * Initializes a class
	 * 
	 * @param cls
	 */
	protected void init(final Class<?> cls) {
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
						add(new AtFunctionGeneric(methodName, method));
					} else {
						add(new AtFunctionSimple(methodName, method));
					}
				} else {
					throw new IllegalAccessError("Method " + methodName + " is either not static.");
				}
			}
		}

	}

}
