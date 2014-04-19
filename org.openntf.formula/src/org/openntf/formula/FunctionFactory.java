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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import org.openntf.formula.impl.AtFunction;

/**
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class FunctionFactory {

	private final Map<String, Function> functions = new HashMap<String, Function>();

	public static FunctionFactory createInstance() {
		FunctionFactory instance = null;
		ServiceLoader<FunctionFactory> loader = ServiceLoader.load(FunctionFactory.class);
		Iterator<FunctionFactory> it = loader.iterator();
		if (it.hasNext()) {
			instance = it.next();
		} else {
			// case if serviceLoader does not work (notesAgent)
			instance = new FunctionFactory();
		}

		return instance;
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
	 * Initializes a class
	 * 
	 * @param cls
	 */
	protected void init() {

		addFunctionSet(new org.openntf.formula.function.Operators.FunctionSet());
		addFunctionSet(new org.openntf.formula.function.OperatorsBool.FunctionSet());
		addFunctionSet(new org.openntf.formula.function.Negators.FunctionSet());
		addFunctionSet(new org.openntf.formula.function.Comparators.FunctionSet());
		addFunctionSet(new org.openntf.formula.function.Constants.FunctionSet());
		addFunctionSet(new org.openntf.formula.function.MathFunctions.FunctionSet());
		addFunctionSet(new org.openntf.formula.function.DateTimeFunctions.FunctionSet());
		addFunctionSet(new org.openntf.formula.function.TextFunctions.FunctionSet());

	}

	protected void addFunctionSet(final AbstractFunctionSet functionSet) {
		// TODO Auto-generated method stub
		functions.putAll(functionSet.getFunctions());
	}

}
