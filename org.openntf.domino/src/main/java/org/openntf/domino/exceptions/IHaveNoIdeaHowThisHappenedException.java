/**
 * 
 */
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class IHaveNoIdeaHowThisHappenedException extends RuntimeException {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(IHaveNoIdeaHowThisHappenedException.class.getName());
	private static final long serialVersionUID = 1L;

	public IHaveNoIdeaHowThisHappenedException() {
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public IHaveNoIdeaHowThisHappenedException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public IHaveNoIdeaHowThisHappenedException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public IHaveNoIdeaHowThisHappenedException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
