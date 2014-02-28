package org.openntf.domino.tests.rpr.formula;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openntf.domino.tests.rpr.formula.eval.Formatter;

public class DominoFormatter implements Formatter {

	public Date parseDate(final String image) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		try {
			return formatter.parse(image);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
