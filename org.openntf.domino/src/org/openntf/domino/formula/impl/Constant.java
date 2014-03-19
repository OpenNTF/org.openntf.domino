package org.openntf.domino.formula.impl;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

public enum Constant {
	;

	@ParamCount(0)
	@Incomplete
	// Do not know if that will work
	public static Object atAll(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	@Incomplete
	// Do not know if that will work
	public static ValueHolder atNothing() {
		return null;
	}

	@ParamCount(0)
	@Incomplete
	// Do not know if that will work
	public static ValueHolder atDeleteField() {
		return null;
	}

	@ParamCount(0)
	public static Object atFalse(final FormulaContext ctx) {
		return ctx.FALSE;
	}

	@ParamCount(0)
	public static Object atNo(final FormulaContext ctx) {
		return ctx.FALSE;
	}

	@ParamCount(0)
	public static Object atTrue(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	public static Object atYes(final FormulaContext ctx) {
		return ctx.TRUE;
	}

}
