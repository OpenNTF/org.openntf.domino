/**
 * 
 */
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 */
public class MIMEConversionException extends RuntimeException {
	private static final Logger log_ = Logger.getLogger(MIMEConversionException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param arg0
	 */
	public MIMEConversionException(final String arg0, final Throwable t) {
		super(arg0, t);
	}

}
