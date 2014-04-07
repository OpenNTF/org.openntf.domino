package org.openntf.domino.formula;

import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;

public class FormulaContextNotes extends FormulaContext {
	private static final Logger log_ = Logger.getLogger(FormulaContextNotes.class.getName());

	public FormulaContextNotes(final Map<String, Object> document, final Formatter formatter) {
		super(document, formatter);
		// TODO Auto-generated constructor stub
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
	@Override
	@SuppressWarnings("deprecation")
	public ValueHolder evaluateNative(final String formula, final ValueHolder... params) {
		Session session = Factory.getSession();
		Document tmpDoc = null;
		Database db = null;

		lotus.domino.Document rawDocument = null;
		if (document instanceof Document) {
			db = ((Document) document).getAncestorDatabase();
			rawDocument = Factory.toLotus((Document) document);
		} else {
			db = session.getCurrentDatabase();
		}
		if (db == null)
			throw new UnsupportedOperationException("No database set: Can't evaluate Lotus native formula");

		lotus.domino.Session rawSession = Factory.toLotus(session);

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
		}
		try {
			log_.warning("Evaluating native formula: '" + formula + "' This may affect performance");

			Vector<?> v = rawSession.evaluate(formula, rawDocument);
			Vector<Object> wrapped = Factory.wrapColumnValues(v, session);
			rawSession.recycle(v);

			return ValueHolder.valueOf(wrapped);
		} catch (NotesException e) {
			log_.warning("NotesException: " + e.text);
			return ValueHolder.valueOf(new RuntimeException(e));
		}

	}
}
