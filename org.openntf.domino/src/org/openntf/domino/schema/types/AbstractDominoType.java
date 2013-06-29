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

	public AbstractDominoType() {

	}

	public boolean validateItem(final org.openntf.domino.Item item) throws ItemException {
		boolean result = true;

		return result;
	}

	public void setItemToDefault(final org.openntf.domino.Item item) {
		item.setValueString("");
	}
}
