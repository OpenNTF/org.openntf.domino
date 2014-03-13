package org.openntf.domino.tests.rpr.formula.eval;


public interface AtFunction {

	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) throws Exception;

	public String getImage();

}
