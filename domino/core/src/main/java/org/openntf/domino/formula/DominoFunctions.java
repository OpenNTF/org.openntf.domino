/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.formula;

import java.util.Map;
import java.util.NoSuchElementException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.Function;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.annotation.ParamCount;

public enum DominoFunctions {

	;

	public static class Functions extends FunctionSet {
		private static final Map<String, Function> functionSet = FunctionFactory.getFunctions(DominoFunctions.class);

		@Override
		public Map<String, Function> getFunctions() {
			return functionSet;
		}
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 2 })
	public static ValueHolder atEnvironment(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder vh = params[0];
		String value = (params.length == 1) ? null : params[1].getString(0);
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		for (int i = 0; i < vh.size; i++) {
			String key = vh.getString(i);
			//String keyLC = key.toLowerCase();
			ret.add(ctx.getEnv(key));
			if (value != null)
				ctx.setEnv(key, value);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atSetEnvironment(final FormulaContext ctx, final ValueHolder[] params) {
		return atEnvironment(ctx, params);
	}

	/*----------------------------------------------------------------------------*/
	@SuppressWarnings("nls")
	@ParamCount(2)
	public static ValueHolder atGetDocField(final FormulaContextNotes ctx, final ValueHolder[] params) {
		Database db = ctx.getDatabase();
		if (db == null)
			throw new UnsupportedOperationException("No database set: Can't execute @GetDocField");
		String unid = params[0].getString(0);
		Map<String, Object> doc;

		if (unid.equals(ctx.getDocument().getUniversalID())) {
			doc = ctx.getDocument();
		} else if ((doc = db.getDocumentByUNID(unid)) == null) {
			return ValueHolder.valueDefault();
		}
		return ValueHolder.valueOf(doc.get(params[1].getString(0)));
	}

	/*----------------------------------------------------------------------------*/
	@SuppressWarnings("nls")
	@ParamCount(3)
	public static ValueHolder atSetDocField(final FormulaContextNotes ctx, final ValueHolder[] params) {
		Database db = ctx.getDatabase();
		if (db == null)
			throw new UnsupportedOperationException("No database set: Can't execute @SetDocField");
		String unid = params[0].getString(0);
		Map<String, Object> doc;

		if (unid.equals(ctx.getDocument().getUniversalID())) {
			doc = ctx.getDocument();
		} else if ((doc = db.getDocumentByUNID(unid)) == null) {
			throw new NoSuchElementException("Document with UNID '" + unid + "' not found");
		}
		doc.put(params[1].getString(0), params[2]);
		if (doc != ctx.getDocument())
			((Document) doc).save();
		return params[2];
	}

	/*----------------------------------------------------------------------------*/
}
