package org.openntf.domino.formula.impl;


public abstract class OperatorBoolImpl {
	private String image;

	public OperatorBoolImpl(final String im) {
		image = im;
	}

	public abstract boolean compute(double v1, double v2);

	public abstract boolean compute(boolean b1, boolean b2);

	public abstract boolean compute(int v1, int v2) throws IntegerOverflowException;

}
