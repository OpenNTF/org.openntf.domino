package org.openntf.domino.formula.impl;

import java.util.Map;

import org.openntf.domino.Document;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.utils.Strings;

public enum DocProperties {
	;

	@ParamCount(4)
	public static ValueHolder atAbstract(final FormulaContext ctx, final ValueHolder[] params) throws EvaluateException {
		// TODO: can this method operate native?
		return ctx.evaluateNative("@Abstract(" + params[0].quoteValue() + ";" + params[1].quoteValue() + ";" + params[2].quoteValue() + ";"
				+ params[3].quoteValue() + ")");
	}

	@ParamCount(0)
	public static ValueHolder atAuthor(final FormulaContext ctx) {
		return ctx.getVarLC("@author");
	}

	@ParamCount(0)
	public static ValueHolder atAllChildren(final FormulaContext ctx) {
		return ctx.getVarLC("@allchildren");
	}

	@ParamCount(0)
	public static ValueHolder atAllDescendants(final FormulaContext ctx) {
		return ctx.getVarLC("@alldescendants");
	}

	@ParamCount(0)
	public static ValueHolder atAttachmentLengths(final FormulaContext ctx) {
		return ctx.getVarLC("@attachmentlengths");
	}

	@ParamCount(0)
	public static ValueHolder atAttachmentNames(final FormulaContext ctx) {
		return ctx.getVarLC("@attachmentnames");
	}

	@ParamCount(0)
	public static ValueHolder atAttachmentModifiedTimes(final FormulaContext ctx) {
		return ctx.getVarLC("@attachmentmodifiedtimes");
	}

	@ParamCount(0)
	public static ValueHolder atAttachments(final FormulaContext ctx) {
		return ctx.getVarLC("@attachments");
	}

	@ParamCount(0)
	public static ValueHolder atAccessed(final FormulaContext ctx) {
		return ctx.getVarLC("@accessed");
	}

	@ParamCount(0)
	public static ValueHolder atModified(final FormulaContext ctx) {
		return ctx.getVarLC("@modified");
	}

	@ParamCount(0)
	public static ValueHolder atCreated(final FormulaContext ctx) {
		return ctx.getVarLC("@created");
	}

	@ParamCount(0)
	@OpenNTF
	public static ValueHolder atAccessedDate(final FormulaContext ctx) {
		return ctx.getVarLC("@accesseddate");
	}

	@ParamCount(0)
	public static ValueHolder atModifiedDate(final FormulaContext ctx) {
		return ctx.getVarLC("@modifieddate");
	}

	@ParamCount(0)
	@OpenNTF
	public static ValueHolder atCreatedDate(final FormulaContext ctx) {
		return ctx.getVarLC("@createddate");
	}

	@ParamCount(0)
	public static ValueHolder atDocumentUniqueid(final FormulaContext ctx) {
		return ctx.getVarLC("@documentuniqueid");
	}

	@ParamCount(0)
	public static ValueHolder atNoteId(final FormulaContext ctx) {
		return ctx.getVarLC("@noteid");
	}

	@ParamCount(0)
	public static ValueHolder atDocLength(final FormulaContext ctx) {
		return ctx.getVarLC("@noteid");
	}

	@ParamCount(0)
	public static ValueHolder atIsResponseDoc(final FormulaContext ctx) {
		return ctx.getVarLC("@isresponsedoc");
	}

	@ParamCount(0)
	public static ValueHolder atReplicaId(final FormulaContext ctx) {
		return ctx.getVarLC("@replicaid");
	}

	@ParamCount(0)
	public static ValueHolder atResponses(final FormulaContext ctx) {
		return ctx.getVarLC("@responses");
	}

	@ParamCount(0)
	public static ValueHolder atIsNewDoc(final FormulaContext ctx) {
		return ctx.getVarLC("@isnewdoc");
	}

	@ParamCount(0)
	public static ValueHolder atInheritedDocumentUniqueID(final FormulaContext ctx) {
		return ctx.getVarLC("@inheriteddocumentuniqueid");
	}

	@ParamCount(0)
	public static ValueHolder atDocFields(final FormulaContext ctx) {
		Map<String, Object> doc = ctx.getDocument();

		if (doc == null) {
			return ValueHolder.valueDefault();
		} else {
			return ValueHolder.valueOf(doc.keySet());
		}
	}

	@ParamCount(2)
	public static Object atAddToFolder(final FormulaContext ctx, final String to, final String from) {
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
