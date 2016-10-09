package org.openntf.formula.impl;

public class IntegerOverflowException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IntegerOverflowException() {
		super();

	}

	public IntegerOverflowException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public IntegerOverflowException(final String arg0) {
		super(arg0);
	}

	public IntegerOverflowException(final Throwable arg0) {
		super(arg0);
	}

}
