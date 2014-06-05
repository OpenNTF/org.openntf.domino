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
	private static final Logger log_ = Logger.getLogger(BackendBridgeSanityCheckException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param arg0
	 */
	public BackendBridgeSanityCheckException(final String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public BackendBridgeSanityCheckException(final Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BackendBridgeSanityCheckException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}
}
