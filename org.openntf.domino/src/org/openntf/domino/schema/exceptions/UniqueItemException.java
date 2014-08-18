/**
 * 
 */
package org.openntf.domino.schema.exceptions;

import java.util.logging.Logger;

/**
 * @author nfreeman
 *
 */
public class UniqueItemException extends ItemException {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(UniqueItemException.class.getName());
	private static final long serialVersionUID = 1L;

	public UniqueItemException() {
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public UniqueItemException(final String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public UniqueItemException(final Throwable cause) {
		super(cause);
	}
}
