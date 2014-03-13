/**
 * 
 */
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class TransactionAlreadySetException extends RuntimeException {
	private static final Logger log_ = Logger.getLogger(TransactionAlreadySetException.class.getName());
	private static final long serialVersionUID = 1L;

	public TransactionAlreadySetException(final String dbpath) {
		super("Cannot set a transaction on database " + dbpath + " because it already has a different transaction.");
	}

}
