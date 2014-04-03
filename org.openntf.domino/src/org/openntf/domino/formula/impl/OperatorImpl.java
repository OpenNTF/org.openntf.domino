package org.openntf.domino.formula.impl;

import org.openntf.domino.ISimpleDateTime;

public abstract class OperatorImpl {
	private String image;

	public OperatorImpl(final String im) {
		image = im;
	}

	public double compute(final double v1, final double v2) {
		throw new UnsupportedOperationException("'" + image + "' is not supported for DOUBLE");
	}

	public boolean compute(final boolean b1, final boolean b2) {
		throw new UnsupportedOperationException("'" + image + "' is not supported for BOOLEAN");
	}

	public int compute(final int v1, final int v2) throws IntegerOverflowException {
		throw new UnsupportedOperationException("'" + image + "' is not supported for INTEGER");
	}

	public String compute(final String v1, final String v2) {
		throw new UnsupportedOperationException("'" + image + "' is not supported for STRING");
	}

	public ISimpleDateTime compute(final ISimpleDateTime d1, final ISimpleDateTime d2) {
		throw new UnsupportedOperationException("'" + image + "' is not supported for DATETIME");
	}

}
