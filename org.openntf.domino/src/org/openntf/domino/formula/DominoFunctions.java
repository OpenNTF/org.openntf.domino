package org.openntf.domino.formula;

import java.util.Map;
import java.util.NoSuchElementException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.annotation.ParamCount;

public enum DominoFunctions {

	;

	public static class Factory extends FunctionFactory {
		public Factory() {
			super(DominoFunctions.class);
		}
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 2 })
	public static ValueHolder atEnvironment(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder vh = params[0];
		String value = (params.length == 1) ? null : params[1].getString(0);
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		for (int i = 0; i < vh.size; i++) {
			String keyLC = vh.getString(i).toLowerCase();
			ret.add(ctx.getEnvLC(keyLC));
			if (value != null)
				ctx.setEnvLC(keyLC, value);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atSetEnvironment(final FormulaContext ctx, final ValueHolder[] params) {
		return atEnvironment(ctx, params);
	}

	/*----------------------------------------------------------------------------*/
	@SuppressWarnings("deprecation")
	@ParamCount(2)
	public static ValueHolder atGetDocField(final FormulaContextNotes ctx, final ValueHolder[] params) {
		Database db = ctx.getDatabase();
		if (db == null)
			throw new UnsupportedOperationException("No database set: Can't execute @GetDocField");
		String unid = params[0].getString(0);
		Map<String, Object> doc;
		if (unid.equals(ctx.getVar("@documentuniqueid").getString(0)))
			doc = ctx.getDocument();
		else if ((doc = db.getDocumentByUNID(unid)) == null)
			return ValueHolder.valueDefault();
		return ValueHolder.valueOf(doc.get(params[1].getString(0)));
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(3)
	public static ValueHolder atSetDocField(final FormulaContextNotes ctx, final ValueHolder[] params) {
		Database db = ctx.getDatabase();
		if (db == null)
			throw new UnsupportedOperationException("No database set: Can't execute @SetDocField");
		String unid = params[0].getString(0);
		Map<String, Object> doc;
		if (unid.equals(ctx.getVar("@documentuniqueid").getString(0)))
			doc = ctx.getDocument();
		else if ((doc = db.getDocumentByUNID(unid)) == null)
			throw new NoSuchElementException("Document with UNID '" + unid + "' not found");
		doc.put(params[1].getString(0), params[2]);
		if (doc != ctx.getDocument())
			((Document) doc).save();
		return params[2];
	}

	/*----------------------------------------------------------------------------*/
}
