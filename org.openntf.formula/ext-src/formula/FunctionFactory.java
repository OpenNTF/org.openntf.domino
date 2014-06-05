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

import java.util.Map;

/**
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public interface FunctionFactory {

	/**
	 * Returns the atFunction object that can evaluate 'funcName'
	 */
	public Function getFunction(final String funcName);

	/**
	 * Returns all availiable at-Functions
	 */
	public Map<String, Function> getFunctions();

	/**
	 * Sets the factory to immutable, so that it is safe to cache them.
	 */
	public void setImmutable();

}
