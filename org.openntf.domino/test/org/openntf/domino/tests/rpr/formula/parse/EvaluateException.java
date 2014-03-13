package org.openntf.domino.tests.rpr.formula.parse;

public class EvaluateException extends Exception {

	public EvaluateException(final int codeLine, final int codeColumn, final Throwable cause) {
		super(initialise(codeLine, codeColumn, cause), cause);
	}

	/**
	 * 
	 * @param cause
	 */
	private static String initialise(final int codeLine, final int codeColumn, final Throwable cause) {
		String eol = System.getProperty("line.separator", "\n");

		String retval = "Encountered \"";
		if (cause != null) {
			if (cause.getMessage() == null) {
				retval += cause.getClass().getName();
			} else {
				retval += cause.getMessage();
			}
		}
		retval += "\" at line " + codeLine + ", column " + codeColumn;
		retval += "." + eol;
		return retval;
	}
}
