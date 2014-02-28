package org.openntf.domino.tests.rpr.formula.eval;


public interface AtFunction {

	public Value evaluate(final FormulaContext ctx, final Value[] params);

}
