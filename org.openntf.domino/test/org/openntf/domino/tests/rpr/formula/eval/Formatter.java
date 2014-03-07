package org.openntf.domino.tests.rpr.formula.eval;

import org.openntf.domino.DateTime;
import org.openntf.domino.tests.rpr.formula.parse.AtFormulaParser;
import org.openntf.domino.tests.rpr.formula.parse.ParseException;

public interface Formatter {

	DateTime parseDate(AtFormulaParser parser, String image) throws ParseException;

}
