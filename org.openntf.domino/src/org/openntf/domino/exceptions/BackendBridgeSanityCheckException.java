/**
 * 
 */
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * This Exception is used to indicate/detect that some BackendBridge-functions will work properly
 * 
 * @see org.openntf.domino.plugin.Activator#verifyIGetEntryByKey()
 * @see org.openntf.domino.impl.View#iGetEntryByKey
 * @author Roland Praml
 * 
 */
public class BackendBridgeSanityCheckException extends RuntimeException {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(BackendBridgeSanityCheckException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public BackendBridgeSanityCheckException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public BackendBridgeSanityCheckException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public BackendBridgeSanityCheckException(final String message, final Throwable cause) {
		super(cause);
	}
}
