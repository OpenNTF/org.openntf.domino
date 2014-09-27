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
 * This exception is wrapped into a ValueHolder and passed to the caller. The caller must extract the Exception from the ValueHolder. This
 * is neccessary to store Exceptions in a variable
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class EvaluateException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception with reference to codeLine and codeColumn
	 * 
	 */
	public EvaluateException(final int codeLine, final int codeColumn, final Throwable cause) {
		super(initialise(codeLine, codeColumn, cause), cause);
	}

	private static String initialise(final int codeLine, final int codeColumn, final Throwable cause) {
		String eol = System.getProperty("line.separator", "\n");

		String retval = "Encountered \"";
		if (cause != null) {
			if (cause.getMessage() == null) {
				retval += cause.getClass().getName();
			} else {
				retval += cause.getMessage();
			}
		}
		retval += "\" at line " + codeLine + ", column " + codeColumn;
		retval += "." + eol;
		return retval;
	}
}
