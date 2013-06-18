/**
 * 
 */
package org.openntf.domino.schema.exceptions;
import java.util.logging.Logger;
/**
 * @author nfreeman
 *
 */
public class ItemMembersException extends ItemException {
	private static final Logger log_ = Logger.getLogger(ItemMembersException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ItemMembersException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ItemMembersException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ItemMembersException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
