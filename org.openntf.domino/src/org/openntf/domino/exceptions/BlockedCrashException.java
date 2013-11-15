/**
 * 
 */
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class BlockedCrashException extends RuntimeException {
	private static final Logger log_ = Logger.getLogger(BlockedCrashException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param arg0
	 */
	public BlockedCrashException(final String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public BlockedCrashException(final Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BlockedCrashException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}
}
