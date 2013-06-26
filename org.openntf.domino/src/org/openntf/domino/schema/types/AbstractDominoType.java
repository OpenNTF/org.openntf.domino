/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.logging.Logger;

import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public abstract class AbstractDominoType implements IDominoType {
	private static final Logger log_ = Logger.getLogger(AbstractDominoType.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AbstractDominoType() {
		// TODO Auto-generated constructor stub
	}

	public boolean validateItem(org.openntf.domino.Item item) throws ItemException {
		boolean result = true;

		return result;
	}
}
