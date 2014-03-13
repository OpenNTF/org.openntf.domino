/**
 * 
 */
package org.openntf.domino.schema.exceptions;
import java.util.logging.Logger;
/**
 * @author nfreeman
 *
 */
public class RequiredItemException extends ItemException {
	private static final Logger log_ = Logger.getLogger(RequiredItemException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public RequiredItemException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public RequiredItemException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public RequiredItemException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
