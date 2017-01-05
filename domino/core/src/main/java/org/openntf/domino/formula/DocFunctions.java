package org.openntf.domino.formula;

import java.util.Map;
import java.util.Vector;

import org.openntf.domino.Document;
import org.openntf.domino.utils.Strings;
import org.openntf.formula.Function;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.annotation.ParamCount;

public enum DocFunctions {
	;

	public static class Functions extends FunctionSet {
		private static final Map<String, Function> functionSet = FunctionFactory.getFunctions(DocFunctions.class);

		@Override
		public Map<String, Function> getFunctions() {
			return functionSet;
		}
	}

	private static ValueHolder valueOf(final Vector<String> vector) {
		if (vector == null)
			return ValueHolder.valueDefault();
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vector.size());
		for (int i = 0; i < vector.size(); i++) {
			ret.add(vector.get(i));
		}
		return ret;
	}

	@ParamCount(0)
	public static ValueHolder atAuthor(final FormulaContextNotes ctx) {
		return valueOf(ctx.getDocument().getAuthors());
	}

	@ParamCount(0)
	public static ValueHolder atAllChildren(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@allchildren");
	}

	@ParamCount(0)
	public static ValueHolder atAllDescendants(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@alldescendants");
	}

	@ParamCount(0)
	public static ValueHolder atAttachmentLengths(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@attachmentlengths");
	}

	@ParamCount(0)
	public static ValueHolder atAttachmentNames(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@attachmentnames");
	}

	@ParamCount(0)
	public static ValueHolder atAttachmentModifiedTimes(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@attachmentmodifiedtimes");
	}

	@ParamCount(0)
	public static ValueHolder atAttachments(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@attachments");
	}

	@ParamCount(0)
	public static ValueHolder atAccessed(final FormulaContextNotes ctx) {
		return ValueHolder.valueOf(ctx.getDocument().getLastAccessed());
	}

	@ParamCount(0)
	public static ValueHolder atModified(final FormulaContextNotes ctx) {
		return ValueHolder.valueOf(ctx.getDocument().getLastModified());
	}

	@ParamCount(0)
	public static ValueHolder atCreated(final FormulaContextNotes ctx) {
		return ValueHolder.valueOf(ctx.getDocument().getCreated());
	}

	//	@ParamCount(0)
	//	@OpenNTF
	//	public static ValueHolder atAccessedDate(final FormulaContextNotes ctx) {
	//		return ctx.getVar("@accesseddate");
	//	}
	//
	//	@ParamCount(0)
	//	public static ValueHolder atModifiedDate(final FormulaContextNotes ctx) {
	//		return ctx.getVar("@modifieddate");
	//	}
	//
	//	@ParamCount(0)
	//	@OpenNTF
	//	public static ValueHolder atCreatedDate(final FormulaContextNotes ctx) {
	//		return ctx.getVar("@createddate");
	//	}

	@ParamCount(0)
	public static ValueHolder atDocumentUniqueid(final FormulaContextNotes ctx) {
		return ValueHolder.valueOf(ctx.getDocument().getUniversalID());
	}

	@ParamCount(0)
	public static ValueHolder atNoteId(final FormulaContextNotes ctx) {
		String nid = ctx.getDocument().getNoteID();

		if ("0".equals(nid))
			nid = "NT00000000";
		return ValueHolder.valueOf(nid);

	}

	@ParamCount(0)
	public static ValueHolder atDocLength(final FormulaContextNotes ctx) {
		return ValueHolder.valueOf(ctx.getDocument().getSize());
	}

	@ParamCount(0)
	public static ValueHolder atIsResponseDoc(final FormulaContextNotes ctx) {
		return ValueHolder.valueOf(ctx.getDocument().isResponse());
	}

	@ParamCount(0)
	public static ValueHolder atReplicaId(final FormulaContextNotes ctx) {
		return ValueHolder.valueOf(ctx.getDatabase().getReplicaID());
	}

	@ParamCount(0)
	public static ValueHolder atResponses(final FormulaContextNotes ctx) {
		return ctx.evaluateNative("@responses");
	}

	@ParamCount(0)
	public static ValueHolder atIsNewDoc(final FormulaContextNotes ctx) {
		return ValueHolder.valueOf(ctx.getDocument().isNewNote());
	}

	@ParamCount(0)
	public static ValueHolder atInheritedDocumentUniqueID(final FormulaContextNotes ctx) {
		return ValueHolder.valueOf(ctx.getDocument().getParentDocumentUNID());
	}

	@ParamCount(0)
	public static ValueHolder atDocFields(final FormulaContextNotes ctx) {
		Map<String, Object> doc = ctx.getDocument();

		if (doc == null) {
			return ValueHolder.valueDefault();
		} else {
			return ValueHolder.valueOf(doc.keySet());
		}
	}

	@ParamCount(1)
	public static ValueHolder atGetField(final FormulaContextNotes ctx, final ValueHolder[] params) {
		return ctx.getField(params[0].getString(0));
	}

	@ParamCount(2)
	public static ValueHolder atSetField(final FormulaContextNotes ctx, final ValueHolder[] params) {
		ctx.setField(params[0].getString(0), params[1]);
		return params[1];
	}

	@ParamCount(2)
	public static boolean atAddToFolder(final FormulaContextNotes ctx, final String to, final String from) {
		Document doc = ctx.getDocument();
		if (doc != null) {
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

	@ParamCount(2)
	public static ValueHolder atSet(final FormulaContextNotes ctx, final ValueHolder[] params) {
		ctx.setVarLC(params[0].getString(0).toLowerCase(), params[1]);
		return params[1];
	}

}
