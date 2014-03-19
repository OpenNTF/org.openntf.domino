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
package org.openntf.domino.formula;

public class EvaluateException extends Exception {
	private static final long serialVersionUID = 1L;

	public EvaluateException(final int codeLine, final int codeColumn, final Throwable cause) {
		super(initialise(codeLine, codeColumn, cause), cause);
	}

	/**
	 * 
	 * @param cause
	 */
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
