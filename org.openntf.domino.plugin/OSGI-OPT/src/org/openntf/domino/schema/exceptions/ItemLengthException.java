/**
 * 
 */
package org.openntf.domino.schema.exceptions;
import java.util.logging.Logger;
/**
 * @author nfreeman
 *
 */
public class ItemLengthException extends ItemException {
	private static final Logger log_ = Logger.getLogger(ItemLengthException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ItemLengthException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ItemLengthException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ItemLengthException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
