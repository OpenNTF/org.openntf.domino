/**
 * 
 */
package org.openntf.domino.schema.exceptions;

import java.util.logging.Logger;

/**
 * @author nfreeman
 *
 */
public class ItemMembersException extends ItemException {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ItemMembersException.class.getName());
	private static final long serialVersionUID = 1L;

	public ItemMembersException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public ItemMembersException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public ItemMembersException(final Throwable cause) {
		super(cause);
	}
}
