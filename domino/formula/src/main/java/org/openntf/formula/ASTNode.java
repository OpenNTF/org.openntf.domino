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

import java.util.List;
import java.util.Set;

/**
 * represents an AST node and is returned by a parser.parse (as root of the AST-Node)
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public interface ASTNode {

	/**
	 * Create a dump of the AST-Tree to System.out. Useful for debugging
	 * 
	 * @param prefix
	 *            for indention
	 */
	public void dump(final String prefix);

	/**
	 * Use this method to solve a formula
	 */
	public List<Object> solve(FormulaContext ctx) throws EvaluateException;

	/**
	 * return a set of used functions (all function names are lowercase)
	 * 
	 * @return set of functions
	 */
	public Set<String> getFunctions();

	/**
	 * returns a set of used variables (all names are lowercase)
	 * 
	 * @return set of variables
	 */
	public Set<String> getVariables();

	/**
	 * returns a set of read fields in this document. (all names are lowercase)
	 * 
	 * @return set of fields
	 */
	public Set<String> getReadFields();

	/**
	 * returns a set of modified fields in this document. (all names are lowercase)
	 * 
	 * @return set of fields
	 */
	public Set<String> getModifiedFields();

	/**
	 * Hint for formula (if exception occurs)
	 * 
	 */
	public void setFormula(String formula);

	/**
	 * Returns the formula for the current AST-Tree
	 * 
	 */
	public String getFormula();
}
/* JavaCC - OriginalChecksum=54dec3b6b2c592c5fbe2fc5be72328d2 (do not edit this line) */
