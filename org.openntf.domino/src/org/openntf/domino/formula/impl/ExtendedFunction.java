package org.openntf.domino.formula.impl;

import org.openntf.domino.formula.AtFormulaParserImpl;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaReturnException;
import org.openntf.domino.formula.ParseException;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.formula.ast.ASTExtendedParameter;
import org.openntf.domino.formula.ast.Node;

public class ExtendedFunction extends AtFunction {

	private Node function;
	private ASTExtendedParameter[] parameter;
	private String functionName;

	private int paramUB;
	private int paramLB;

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
		System.out.println("Params: " + paramLB + " - " + paramUB);
	}

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
