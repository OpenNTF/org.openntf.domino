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
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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

	public interface AppServiceLocator {
		public <T> List<T> findApplicationServices(final Class<T> serviceClazz);
	}

	private static ThreadLocal<AppServiceLocator> currentServiceLocator_ = new ThreadLocal<AppServiceLocator>();

	private final Map<String, Function> functions = new HashMap<String, Function>();
	private boolean immutable;

	/**
	 * Default constructor
	 */
	private FunctionFactory() {
		super();
	}

	public static FunctionFactory createInstance() {

		FunctionFactory instance = new FunctionFactory();
		//		ServiceLoader<FunctionSet> loader = ServiceLoader.load(FunctionSet.class);

		List<FunctionSet> loaderList = findApplicationServices(FunctionSet.class);

		//		if (loader.iterator().hasNext()) {
		//			System.out.println("FunctionSet Service found.");
		//			for (FunctionSet fact : loader) {
		//				loaderList.add(fact);
		//				System.out.println("Using " + fact.getClass().getName());
		//			}
		//		} else {
		//			// case if serviceLoader does not work (notesAgent)
		//			System.out.println("FunctionSet Service not found. Using defaults");
		//			loaderList.add(new org.openntf.formula.function.Operators.Functions());
		//			loaderList.add(new org.openntf.formula.function.OperatorsBool.Functions());
		//			loaderList.add(new org.openntf.formula.function.Negators.Functions());
		//			loaderList.add(new org.openntf.formula.function.Comparators.Functions());
		//			loaderList.add(new org.openntf.formula.function.Constants.Functions());
		//			loaderList.add(new org.openntf.formula.function.MathFunctions.Functions());
		//			loaderList.add(new org.openntf.formula.function.DateTimeFunctions.Functions());
		//			loaderList.add(new org.openntf.formula.function.TextFunctions.Functions());
		//		}
		//
		Collections.sort(loaderList, new Comparator<FunctionSet>() {
			public int compare(final FunctionSet paramT1, final FunctionSet paramT2) {
				return paramT2.getPriority() - paramT1.getPriority();
			}
		});

		for (FunctionSet fact : loaderList) {
			instance.functions.putAll(fact.getFunctions());
		}

		instance.setImmutable();

		return instance;
	}

	public static void setServiceLocator(final AppServiceLocator locator) {
		currentServiceLocator_.set(locator);
	}

	@SuppressWarnings("rawtypes")
	private static Map<Class, List> nonOSGIServicesCache;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> findApplicationServices(final Class<T> serviceClazz) {

		AppServiceLocator serviceLocator = currentServiceLocator_.get();
		if (serviceLocator != null) {
			return serviceLocator.findApplicationServices(serviceClazz);
		}

		// this is the non OSGI case:
		if (nonOSGIServicesCache == null)
			nonOSGIServicesCache = new HashMap<Class, List>();

		@SuppressWarnings("unchecked")
		List<T> ret = nonOSGIServicesCache.get(serviceClazz);
		if (ret == null) {
			ret = new ArrayList<T>();
			nonOSGIServicesCache.put(serviceClazz, ret);

			ServiceLoader<T> loader = ServiceLoader.load(serviceClazz);
			Iterator<T> it = loader.iterator();
			while (it.hasNext()) {
				ret.add(it.next());
			}
			if (Comparable.class.isAssignableFrom(serviceClazz)) {
				Collections.sort((List<? extends Comparable>) ret);
			}
		}
		return ret;
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
	 * Sets the factory to immutable, so that it is safe to cache them.
	 */
	public void setImmutable() {
		immutable = true;

	}

	/**
	 * Initializes a class
	 * 
	 * @throws
	 */
	public static Map<String, Function> getFunctions(final Class<?> cls) {
		final Map<String, Function> ret = new HashMap<String, Function>();
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {

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
								Function f;
								if (ValueHolder.class.isAssignableFrom(method.getReturnType())) {
									f = new AtFunctionGeneric(methodName, method);
								} else {
									f = new AtFunctionSimple(methodName, method);
								}
								ret.put(f.getImage().toLowerCase(), f);
							} else {
								throw new IllegalAccessError("Method " + methodName + " is either not static.");
							}
						}
					}
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
		}
		return ret;
	}

	public static FunctionFactory getMinimalFF() {
		return minimalFF;
	}

	private static FunctionFactory minimalFF;
	static {
		minimalFF = new FunctionFactory();
		ArrayList<FunctionSet> fsl = new ArrayList<FunctionSet>();
		fsl.add(new org.openntf.formula.function.Operators.Functions());
		fsl.add(new org.openntf.formula.function.OperatorsBool.Functions());
		fsl.add(new org.openntf.formula.function.Negators.Functions());
		fsl.add(new org.openntf.formula.function.Comparators.Functions());
		fsl.add(new org.openntf.formula.function.Constants.Functions());
		fsl.add(new org.openntf.formula.function.MathFunctions.Functions());
		fsl.add(new org.openntf.formula.function.DateTimeFunctions.Functions());
		fsl.add(new org.openntf.formula.function.TextFunctions.Functions());
		for (FunctionSet fact : fsl)
			minimalFF.functions.putAll(fact.getFunctions());
		minimalFF.setImmutable();
	}
}
