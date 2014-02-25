package org.openntf.domino.tests.rpr.formula;

public interface AtFunction {

	public Value evaluate(final FormulaContext ctx, final Value[] params);

}
