package org.openntf.domino.formula;

public interface AtFunction {

	public ValueHolder evaluate(final FormulaContext ctx, final ValueHolder[] params) throws Exception;

	public String getImage();

}
