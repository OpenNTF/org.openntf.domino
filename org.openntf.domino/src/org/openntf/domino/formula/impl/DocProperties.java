package org.openntf.domino.formula.impl;

import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

public enum DocProperties {
	;
	@ParamCount(0)
	public static ValueHolder atAccessed(final FormulaContext ctx) {
		return ctx.getVar("@accessed");
	}

	@ParamCount(0)
	public static ValueHolder atModified(final FormulaContext ctx) {
		return ctx.getVar("@modified");
	}

	@ParamCount(0)
	public static ValueHolder atCreated(final FormulaContext ctx) {
		return ctx.getVar("@created");
	}

	@ParamCount(0)
	public static ValueHolder atAccessedDate(final FormulaContext ctx) {
		return ctx.getVar("@accesseddate");
	}

	@ParamCount(0)
	public static ValueHolder atModifiedDate(final FormulaContext ctx) {
		return ctx.getVar("@modifieddate");
	}

	@ParamCount(0)
	public static ValueHolder atCreatedDate(final FormulaContext ctx) {
		return ctx.getVar("@createddate");
	}

	@ParamCount(0)
	public static ValueHolder atDocumentUniqueid(final FormulaContext ctx) {
		return ctx.getVar("@documentuniqueid");
	}

	@ParamCount(0)
	public static ValueHolder atNoteId(final FormulaContext ctx) {
		return ctx.getVar("@noteid");
	}

	@ParamCount(0)
	public static ValueHolder atDocLength(final FormulaContext ctx) {
		return ctx.getVar("@noteid");
	}

	@ParamCount(0)
	public static ValueHolder atIsResponseDoc(final FormulaContext ctx) {
		return ctx.getVar("@isresponsedoc");
	}

	@ParamCount(0)
	public static ValueHolder atReplicaId(final FormulaContext ctx) {
		return ctx.getVar("@replicaid");
	}

	@ParamCount(0)
	public static ValueHolder atResponses(final FormulaContext ctx) {
		return ctx.getVar("@responses");
	}

}
