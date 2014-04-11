/*
 *  © Copyright FOCONIS AG, 2014
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
 * This is an abstract class, to separate parser/ast/impl package. This Exception (or a subclass) is thrown, if you parse a wrong formula
 */
public abstract class FormulaParseException extends Exception {

	/**
	 * The version identifier for this Serializable class. Increment only if the <i>serialized</i> form of the class changes.
	 */
	private static final long serialVersionUID = 1L;

	protected FormulaParseException(final String arg0) {
		super(arg0);
	}

	protected FormulaParseException() {
		super();
	}
}
/* JavaCC - OriginalChecksum=51dbed57548ab5839ac93a7ec5e5f7dd (do not edit this line) */
