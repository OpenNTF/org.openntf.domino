package org.openntf.domino.tests.rpr.formula.parse;

public class EvaluateException extends Exception {

	public EvaluateException(final int codeLine, final int codeColumn, final Throwable cause) {
		super(initialise(codeLine, codeColumn, cause.getMessage()), cause);
	}

	/**
	 * 
	 * @param cause
	 */
	private static String initialise(final int codeLine, final int codeColumn, final String message) {
		String eol = System.getProperty("line.separator", "\n");

		String retval = "Encountered \"";
		retval += message;
		retval += "\" at line " + codeLine + ", column " + codeColumn;
		retval += "." + eol;
		return retval;
	}
}
