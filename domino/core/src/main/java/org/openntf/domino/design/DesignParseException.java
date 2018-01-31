package org.openntf.domino.design;

/**
 * @author Paul Withers
 * @since ODA 4.1.0
 *
 */
public class DesignParseException extends ClassCastException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 *            Error message to pass
	 */
	public DesignParseException(final String message) {
		super(message);
	}

}
