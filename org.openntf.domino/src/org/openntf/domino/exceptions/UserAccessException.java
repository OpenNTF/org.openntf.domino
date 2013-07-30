/**
 * 
 */
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 */
public class UserAccessException extends RuntimeException {
	private static final Logger log_ = Logger.getLogger(UserAccessException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UserAccessException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}
}
