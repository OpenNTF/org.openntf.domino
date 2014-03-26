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
package org.openntf.domino.formula.impl;

import org.openntf.domino.formula.AtFormulaParserImpl;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaReturnException;
import org.openntf.domino.formula.ParseException;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.formula.ast.ASTExtendedParameter;
import org.openntf.domino.formula.ast.Node;

/**
 * ExtendedFunctions are custom defined functions like FUNCTION @MyFunction(a;b:="default") := @Do(...)
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class ExtendedFunction extends AtFunction {
	private String functionName;
	private Node function;
	private ASTExtendedParameter[] parameter;

	private int paramUB;
	private int paramLB;

	/**
	 * Constructor
	 * 
	 * @param functionName
	 *            the function-name (with @-sign)
	 * @param parameter
	 *            the parameters for this function
	 * @param function
	 *            the function-node to execute (if @functionName is invoked in formula)
	 * @param parser
	 *            needed to throw propert ParseException
	 * @throws ParseException
	 *             if you do not put optional parameter to the end
	 */
	public ExtendedFunction(final String functionName, final ASTExtendedParameter[] parameter, final Node function,
			final AtFormulaParserImpl parser) throws ParseException {
		super(functionName);
		this.functionName = functionName;
		this.parameter = parameter;
		this.function = function;

		paramUB = parameter.length;

		for (paramLB = 0; paramLB < parameter.length; paramLB++) {
			if (parameter[paramLB].isOptional())
				break;
		}
		for (int i = paramLB; i < parameter.length; i++) {
			if (!parameter[i].isOptional())
				throw new ParseException(parser, "non-optional parameter after optional parameter");
		}
	}

	/**
	 * Return the name of the function
	 * 
	 * @return the name of the function
	 */
	public String getName() {
		return functionName;
	}

	@Override
	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) throws FormulaReturnException {

		ValueHolder paramVals[] = new ValueHolder[parameter.length];

		int inParams = params == null ? 0 : params.length;

		for (int pi = 0; pi < parameter.length; pi++) {
			if (pi < inParams) {
				paramVals[pi] = params[pi];
			} else {
				paramVals[pi] = parameter[pi].evaluate(ctx); //Optional params
			}
		}
		try {
			for (int pi = 0; pi < parameter.length; pi++) {
				paramVals[pi] = ctx.setVarLC(parameter[pi].getName().toLowerCase(), paramVals[pi]);
			}
			return function.evaluate(ctx);
		} finally {
			for (int pi = 0; pi < parameter.length; pi++) {
				ctx.setVarLC(parameter[pi].getName().toLowerCase(), paramVals[pi]);
			}
		}

	}

	@Override
	public boolean checkParamCount(final int i) {
		return paramLB <= i && i <= paramUB;
	}

}
