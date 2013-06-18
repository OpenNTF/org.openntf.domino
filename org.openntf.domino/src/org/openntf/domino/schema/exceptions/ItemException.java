/**
 * 
 */
package org.openntf.domino.schema.exceptions;

import java.util.logging.Logger;

import org.openntf.domino.schema.ItemDefinition;

/**
 * @author nfreeman
 * 
 */
public abstract class ItemException extends SchemaException {
	private static final Logger log_ = Logger.getLogger(ItemException.class.getName());
	private static final long serialVersionUID = 1L;

	private Object value_;
	private ItemDefinition itemdef_;

	/**
	 * 
	 */
	public ItemException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ItemException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ItemException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
