/**
 * 
 */
package org.openntf.domino.schema.exceptions;
import java.util.logging.Logger;
/**
 * @author nfreeman
 *
 */
public class UniqueItemException extends ItemException {
	private static final Logger log_ = Logger.getLogger(UniqueItemException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UniqueItemException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public UniqueItemException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public UniqueItemException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
