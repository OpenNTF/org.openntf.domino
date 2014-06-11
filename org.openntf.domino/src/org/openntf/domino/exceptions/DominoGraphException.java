package org.openntf.domino.exceptions;

import org.openntf.domino.graph.DominoElement;

public class DominoGraphException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	final String message_;
	final DominoElement elem1_;
	final DominoElement elem2_;

	public DominoGraphException(final String message) {
		super(message);
		message_ = message;
		elem1_ = null;
		elem2_ = null;
	}

	public DominoGraphException(final String message, final DominoElement element) {
		super(message);
		message_ = message;
		elem1_ = element;
		elem2_ = null;

	}

	public DominoGraphException(final String message, final DominoElement element1, final DominoElement element2) {
		super(message);
		message_ = message;
		elem1_ = element1;
		elem2_ = element2;
	}

}
