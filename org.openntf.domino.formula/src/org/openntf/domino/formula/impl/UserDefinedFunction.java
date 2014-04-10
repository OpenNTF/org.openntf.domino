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

import java.util.HashSet;
import java.util.Set;

import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaReturnException;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.formula.ast.ASTUserDefinedFunctionParameter;
import org.openntf.domino.formula.ast.ASTUserDefinedFunctionVariable;
import org.openntf.domino.formula.ast.Node;
import org.openntf.domino.formula.ast.SimpleNode;
import org.openntf.domino.formula.parse.AtFormulaParserImpl;
import org.openntf.domino.formula.parse.ParseException;

/**
 * ExtendedFunctions are custom defined functions like FUNCTION @MyFunction(a;b:="default") := @Do(...)
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class UserDefinedFunction extends AtFunction {
	private String functionName;
	private SimpleNode function;
	private ASTUserDefinedFunctionParameter[] parameter;

	private int paramUB;
	private int paramLB;
	private ASTUserDefinedFunctionVariable[] variable;

	/**
	 * Constructor
	 * 
	 * @param functionName
	 *            the function-name (with @-sign)
	 * @param parameter
	 *            the parameters for this function
	 * @param variable
	 * @param function
	 *            the function-node to execute (if @functionName is invoked in formula)
	 * @param parser
	 *            needed to throw propert ParseException
	 * @throws ParseException
	 *             if you do not put optional parameter to the end
	 */
	public UserDefinedFunction(final String functionName, final ASTUserDefinedFunctionParameter[] parameter,
			final AtFormulaParserImpl parser) throws ParseException {
		super(functionName);
		this.functionName = functionName;
		this.parameter = parameter;

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

	public void setVariables(final ASTUserDefinedFunctionVariable[] v) {
		variable = v;
	}

	public void setFunction(final Node f) {
		function = (SimpleNode) f;
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
		if (function == null)
			throw new UnsupportedOperationException("'" + functionName + "' is not properly declared");

		ValueHolder paramVals[] = new ValueHolder[parameter.length];
		ValueHolder varVals[] = new ValueHolder[variable.length];

		for (int i = 0; i < parameter.length; i++) {
			if (params == null || i >= params.length) {
				paramVals[i] = parameter[i].evaluate(ctx); //Optional params
			} else {
				paramVals[i] = params[i];
			}
		}

		for (int i = 0; i < variable.length; i++) {
			varVals[i] = variable[i].evaluate(ctx);
		}

		try {
			// Initialize params & values & save old state
			for (int i = 0; i < parameter.length; i++) {
				paramVals[i] = ctx.setVarLC(parameter[i].getNameLC(), paramVals[i]);
			}
			for (int i = 0; i < variable.length; i++) {
				varVals[i] = ctx.setVarLC(variable[i].getNameLC(), varVals[i]);
			}

			return function.evaluate(ctx);

		} finally {
			// restore old state
			for (int i = 0; i < parameter.length; i++) {
				ctx.setVarLC(parameter[i].getNameLC(), paramVals[i]);
			}
			for (int i = 0; i < variable.length; i++) {
				ctx.setVarLC(variable[i].getNameLC(), varVals[i]);
			}
		}

	}

	@Override
	public boolean checkParamCount(final int i) {
		return paramLB <= i && i <= paramUB;
	}

	public void inspect(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {
		if (functions.contains(functionName.toUpperCase())) // dirty hack. As functions are normally written in lowercase...
			return;

		try {
			functions.add(functionName.toUpperCase());		// ... we write the function in uppercase here, to detect loops
			Set<String> tmpVariables = new HashSet<String>();
			tmpVariables.addAll(variables);

			for (ASTUserDefinedFunctionParameter param : parameter) {
				tmpVariables.add(param.getNameLC());
				param.inspect(readFields, modifiedFields, variables, functions);
			}
			for (ASTUserDefinedFunctionVariable var : variable) {
				tmpVariables.add(var.getNameLC());
				var.inspect(readFields, modifiedFields, variables, functions);
			}
			function.inspect(readFields, modifiedFields, tmpVariables, functions);

			for (ASTUserDefinedFunctionParameter param : parameter) {
				tmpVariables.remove(param.getNameLC());
			}
			for (ASTUserDefinedFunctionVariable var : variable) {
				tmpVariables.remove(var.getNameLC());
			}
			variables.addAll(tmpVariables);
		} finally {
			functions.remove(functionName.toUpperCase());
		}
	}
}
