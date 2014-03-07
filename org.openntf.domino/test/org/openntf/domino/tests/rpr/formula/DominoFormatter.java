package org.openntf.domino.tests.rpr.formula;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openntf.domino.DateTime;
import org.openntf.domino.tests.rpr.formula.eval.Formatter;
import org.openntf.domino.tests.rpr.formula.parse.AtFormulaParser;
import org.openntf.domino.tests.rpr.formula.parse.ParseException;

public class DominoFormatter implements Formatter {

	public DateTime parseDate(final AtFormulaParser parser, final String image) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		try {
			Date d = formatter.parse(image);
			DateTime dt = new org.openntf.domino.impl.DateTime(d, null, null, 0);
			dt.setAnyTime();
			return dt;
		} catch (java.text.ParseException e) {
			throw new ParseException(parser, e.getMessage());
		}
	}
}
