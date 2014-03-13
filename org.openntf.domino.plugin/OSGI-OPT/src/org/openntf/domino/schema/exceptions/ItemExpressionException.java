/**
 * 
 */
package org.openntf.domino.schema.exceptions;
import java.util.logging.Logger;
/**
 * @author nfreeman
 *
 */
public class ItemExpressionException extends ItemException {
	private static final Logger log_ = Logger.getLogger(ItemExpressionException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ItemExpressionException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ItemExpressionException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ItemExpressionException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
