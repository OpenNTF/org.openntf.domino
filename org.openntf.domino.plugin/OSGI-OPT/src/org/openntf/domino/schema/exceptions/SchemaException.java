/**
 * 
 */
package org.openntf.domino.schema.exceptions;

import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 */
public class SchemaException extends RuntimeException {
	private static final Logger log_ = Logger.getLogger(SchemaException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public SchemaException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public SchemaException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public SchemaException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
