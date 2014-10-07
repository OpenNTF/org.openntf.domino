package org.openntf.domino.exceptions;

import java.util.logging.Logger;

public class ItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ItemNotFoundException.class.getName());

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public ItemNotFoundException(final String message) {
		super(message);
	}
}
