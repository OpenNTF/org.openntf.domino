/**
 * 
 */
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 */
public class UnableToAcquireSessionException extends RuntimeException {
	private static final Logger log_ = Logger.getLogger(UnableToAcquireSessionException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param arg0
	 */
	public UnableToAcquireSessionException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public UnableToAcquireSessionException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UnableToAcquireSessionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
}
