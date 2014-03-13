package org.openntf.domino.exceptions;

public class OpenNTFNotesException extends RuntimeException {

	public OpenNTFNotesException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public OpenNTFNotesException(final Throwable cause) {
		super(cause);
	}

}