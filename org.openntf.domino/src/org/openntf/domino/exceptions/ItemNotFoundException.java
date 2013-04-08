package org.openntf.domino.exceptions;

import java.util.logging.Logger;

public class ItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(ItemNotFoundException.class.getName());

	public ItemNotFoundException(String message) {
		super(message);
	}
}
