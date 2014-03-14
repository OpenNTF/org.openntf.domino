package org.openntf.domino.formula.impl;

public enum Arithmetic {
	;
	@ParamCount({ 1, 3 })
	public static Number atAbs(final Number args) {
		return Math.abs(args.doubleValue());
	}
}
