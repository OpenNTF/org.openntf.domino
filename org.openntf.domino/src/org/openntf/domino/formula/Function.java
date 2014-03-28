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

import javax.annotation.Nonnull;

/**
 * This repesents a "real" formulaexpression. A real formulaexpression is a formula, that does NOT control the program flow (like
 * {@literal @}if, {@literal @}for or {@literal @}transform". Formulas that controls program flow must be implemented as own AST-node).
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public interface Function {

	/**
	 * Evaluates the formula.
	 * 
	 * @param ctx
	 *            the context that contains "database" and so on
	 * @param params
	 *            the parameters as ValueHolder
	 * @return a ValueHolder
	 * @throws FormulaReturnException
	 *             if a {@literal @}return Statement was executed
	 */
	@Nonnull
	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) throws FormulaReturnException;

	/**
	 * returns the Image of the AtFunction (with at-sign)
	 * 
	 * @return a String
	 */
	@Nonnull
	public String getImage();

	public boolean checkParamCount(int i);
}
