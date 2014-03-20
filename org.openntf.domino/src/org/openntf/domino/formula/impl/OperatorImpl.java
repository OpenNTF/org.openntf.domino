package org.openntf.domino.formula.impl;

import java.util.Calendar;

import org.openntf.domino.DateTime;

public abstract class OperatorImpl {

	public abstract double compute(double v1, double v2);

	public abstract int compute(int v1, int v2) throws IntegerOverflowException;

	public String compute(final String v1, final String v2) {
		throw new UnsupportedOperationException();
	}

	public DateTime compute(final DateTime d1, final DateTime d2) {
		throw new UnsupportedOperationException();
	}

	public Calendar compute(final Calendar d1, final Calendar d2) {
		throw new UnsupportedOperationException();
	}
}
