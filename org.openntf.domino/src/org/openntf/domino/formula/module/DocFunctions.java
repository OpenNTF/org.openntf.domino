package org.openntf.domino.formula.module;

import java.util.Map;

import org.openntf.domino.Document;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FunctionFactory;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.formula.annotation.OpenNTF;
import org.openntf.domino.formula.annotation.ParamCount;
import org.openntf.domino.utils.Strings;

public enum DocFunctions {
	;

	public static class Factory extends FunctionFactory {
		public Factory() {
			super(DocFunctions.class);
		}
	}

	@ParamCount(0)
	public static ValueHolder atAuthor(final FormulaContext ctx) {
		return ctx.getVar("@author");
	}

	@ParamCount(0)
	public static ValueHolder atAllChildren(final FormulaContext ctx) {
		return ctx.getVar("@allchildren");
	}

	@ParamCount(0)
	public static ValueHolder atAllDescendants(final FormulaContext ctx) {
		return ctx.getVar("@alldescendants");
	}

	@ParamCount(0)
	public static ValueHolder atAttachmentLengths(final FormulaContext ctx) {
		return ctx.getVar("@attachmentlengths");
	}

	@ParamCount(0)
	public static ValueHolder atAttachmentNames(final FormulaContext ctx) {
		return ctx.getVar("@attachmentnames");
	}

	@ParamCount(0)
	public static ValueHolder atAttachmentModifiedTimes(final FormulaContext ctx) {
		return ctx.getVar("@attachmentmodifiedtimes");
	}

	@ParamCount(0)
	public static ValueHolder atAttachments(final FormulaContext ctx) {
		return ctx.getVar("@attachments");
	}

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
		ValueHolder vh = ctx.getVar("@noteid");
		if ("0".equals(vh.getString(0)))
			vh = ValueHolder.valueOf("NT00000000");
		return vh;
	}

	@ParamCount(0)
	public static ValueHolder atDocLength(final FormulaContext ctx) {
		return ctx.getVar("@doclength");
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

	@SuppressWarnings("deprecation")
	@ParamCount(0)
	public static ValueHolder atDocFields(final FormulaContext ctx) {
		Map<String, Object> doc = ctx.getDocument();

		if (doc == null) {
			return ValueHolder.valueDefault();
		} else {
			return ValueHolder.valueOf(doc.keySet());
		}
	}

	@ParamCount(1)
	public static ValueHolder atGetField(final FormulaContext ctx, final ValueHolder[] params) {
		return ctx.getField(params[0].getString(0));
	}

	@ParamCount(2)
	public static ValueHolder atGetDocField(final FormulaContext ctx, final ValueHolder[] params) {
		return ctx.getDocField(params[0].getString(0), params[1].getString(0));
	}

	@ParamCount(2)
	public static boolean atAddToFolder(final FormulaContext ctx, final String to, final String from) {
		Map<String, Object> map = ctx.getDocument();
		if (map instanceof Document) {
			Document doc = (Document) map;
			if (!Strings.isBlankString(to)) {
				doc.putInFolder(to);
			}
			if (!Strings.isBlankString(from)) {
				doc.removeFromFolder(from);
			}
			return true;
		}
		return false;
	}

}
