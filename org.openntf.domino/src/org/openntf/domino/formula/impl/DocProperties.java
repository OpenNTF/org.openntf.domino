package org.openntf.domino.formula.impl;

import java.util.Map;

import org.openntf.domino.Document;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.utils.Strings;

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
	@OpenNTF
	public static ValueHolder atAccessedDate(final FormulaContext ctx) {
		return ctx.getVar("@accesseddate");
	}

	@ParamCount(0)
	public static ValueHolder atModifiedDate(final FormulaContext ctx) {
		return ctx.getVar("@modifieddate");
	}

	@ParamCount(0)
	@OpenNTF
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

	@ParamCount(0)
	public static ValueHolder atIsNewDoc(final FormulaContext ctx) {
		return ctx.getVar("@isnewdoc");
	}

	@ParamCount(0)
	public static ValueHolder atInheritedDocumentUniqueID(final FormulaContext ctx) {
		return ctx.getVar("@inheriteddocumentuniqueid");
	}

	@ParamCount(0)
	public static ValueHolder atDocFields(final FormulaContext ctx) {
		Map<String, Object> doc = ctx.getDocument();

		if (doc == null) {
			return new ValueHolder();
		} else {
			return new ValueHolder(doc.keySet());
		}
	}

	@ParamCount(0)
	public static Object AddToFolder(final FormulaContext ctx, final String to, final String from) {
		Map<String, Object> map = ctx.getDocument();
		if (map instanceof Document) {
			Document doc = (Document) map;
			if (!Strings.isBlankString(to)) {
				doc.putInFolder(to);
			}
			if (!Strings.isBlankString(from)) {
				doc.removeFromFolder(from);
			}
			return ctx.TRUE;
		}
		return ctx.FALSE;
	}

}
