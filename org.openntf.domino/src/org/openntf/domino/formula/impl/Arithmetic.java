package org.openntf.domino.formula.impl;

public enum Arithmetic {
	;

	public static Number atAbs(final Number args) {
		return Math.abs(args.doubleValue());
	}
}
