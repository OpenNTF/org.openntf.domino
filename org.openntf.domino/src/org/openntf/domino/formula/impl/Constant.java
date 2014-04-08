package org.openntf.domino.formula.impl;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

public enum Constant {
	;

	@ParamCount(0)
	@Incomplete
	// Do not know if that will work
	public static ValueHolder atAll(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	public static ValueHolder atNothing() {
		return ValueHolder.valueDefault();
	}

	@ParamCount(0)
	@Incomplete
	// Do not know if that will work
	public static ValueHolder atDeleteField() {
		return null;
	}

	@ParamCount(0)
	public static ValueHolder atFalse(final FormulaContext ctx) {
		return ctx.FALSE;
	}

	@ParamCount(0)
	public static ValueHolder atNo(final FormulaContext ctx) {
		return ctx.FALSE;
	}

	@ParamCount(0)
	public static ValueHolder atTrue(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	public static ValueHolder atYes(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	public static ValueHolder atSuccess(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	public static ValueHolder atError(final FormulaContext ctx) {
		throw new IllegalArgumentException("@Error");
	}
}
