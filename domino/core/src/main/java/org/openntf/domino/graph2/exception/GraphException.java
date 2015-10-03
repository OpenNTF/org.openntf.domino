package org.openntf.domino.graph2.exception;

public class GraphException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GraphException(final String arg0) {
		super(arg0);
	}

	public GraphException(final Throwable arg0) {
		super(arg0);
	}

	public GraphException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

}
