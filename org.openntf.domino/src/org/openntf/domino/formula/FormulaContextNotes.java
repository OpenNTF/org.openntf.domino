package org.openntf.domino.formula;

import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Terminatable;
import org.openntf.formula.EvaluateException;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.Formulas;
import org.openntf.formula.ValueHolder;

public class FormulaContextNotes extends FormulaContext {
	private static final Logger log_ = Logger.getLogger(FormulaContextNotes.class.getName());

	static {
		// this is not yet nice, but we assume that this context is always used in Domino/XPage environment
		Factory.onTerminate(new Terminatable() {
			@Override
			public void terminate() {
				Formulas.terminate();
			}
		});
	}

	/**
	 * does a native evaluate. This is needed for all functions that are too complex to implement in java or the algorithm is unknown
	 * 
	 * @param formula
	 *            the formula to evaluate
	 * @param params
	 *            the parameters are mapped to the field p1, p2 and so on
	 * @return the value
	 */
	public ValueHolder evaluateNative(final String formula, final ValueHolder... params) {
		Session session = Factory.getSession();

		Database db = getDatabase();
		if (db == null)
			throw new UnsupportedOperationException("No database set: Can't evaluate Lotus native formula");

		lotus.domino.Document rawDocument = Factory.toLotus(getDocument());
		Document tmpDoc = null;
		if (params.length > 0) {
			tmpDoc = db.createDocument();
			rawDocument = Factory.toLotus(tmpDoc);
			// fill the document
			for (int i = 0; i < params.length; i++) {
				try {
					tmpDoc.replaceItemValue("p" + (i + 1), params[i].toList());
				} catch (EvaluateException e) {
					return params[i];
				}
			}
		} else {
			rawDocument = Factory.toLotus(getDocument());
		}

		try {
			log_.warning("Evaluating native formula: '" + formula + "' This may affect performance");

			lotus.domino.Session rawSession = Factory.toLotus(session);

			Vector<?> v = rawSession.evaluate(formula, rawDocument);
			Vector<Object> wrapped = Factory.wrapColumnValues(v, session);
			rawSession.recycle(v);

			return ValueHolder.valueOf(wrapped);
		} catch (NotesException e) {
			log_.warning("NotesException: " + e.text);
			if (e.text.contains("Could not evaluate formula:"))
				return ValueHolder.valueOf(new EvaluateException(-1, -1, e));
			return ValueHolder.valueOf(new RuntimeException(e));
		}

	}

	@Override
	public String getEnv(final String varNameLC) {
		return Factory.getSession().getEnvironmentString(varNameLC);
	}

	@Override
	public void setEnv(final String varName, final String value) {
		Factory.getSession().setEnvironmentVar(varName, value);
	}

	public Database getDatabase() {
		Document doc = getDocument();
		if (doc == null) {
			return Factory.getSession().getCurrentDatabase();
		} else {
			return doc.getAncestorDatabase();
		}
	}

	public Document getDocument() {
		return dataMap instanceof Document ? ((Document) dataMap) : null;
	}

}
