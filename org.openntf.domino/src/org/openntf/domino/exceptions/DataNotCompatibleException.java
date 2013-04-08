package org.openntf.domino.exceptions;

import java.util.logging.Logger;

public class DataNotCompatibleException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(DataNotCompatibleException.class.getName());

	public DataNotCompatibleException(String message) {
		super(message);
	}
}
