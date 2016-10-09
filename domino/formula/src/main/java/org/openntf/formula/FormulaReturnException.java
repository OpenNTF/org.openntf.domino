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
 */
package org.openntf.formula;

/**
 * This Exception is internally used to implement the {@literal @}return Function
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class FormulaReturnException extends Exception {
	private static final long serialVersionUID = 1L;
	private ValueHolder ret;

	/**
	 * Constructor
	 * 
	 * @param vh
	 *            the ValueHolder to return
	 */
	public FormulaReturnException(final ValueHolder vh) {
		ret = vh;
	}

	/**
	 * returns the ValueHolder specified in constructor
	 * 
	 * @return valueholder
	 */
	public ValueHolder getValue() {
		return ret;
	}

}
