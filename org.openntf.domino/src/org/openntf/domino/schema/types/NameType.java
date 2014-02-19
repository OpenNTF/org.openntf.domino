/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.logging.Logger;

import org.openntf.domino.Item;
import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public class NameType extends StringType {
	private static final Logger log_ = Logger.getLogger(NameType.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NameType() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "Name";
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.schema.types.IDominoType#setItemToDefault(org.openntf.domino.Item)
	 */
	@Override
	public void setItemToDefault(final Item item) {
		item.setValueString("");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.schema.types.AbstractDominoType#validateValue(java.lang.Object)
	 */
	@Override
	public boolean validateValue(final Object value) throws ItemException {
		if (super.validateValue(value)) {
			//TODO further validation
			return true;
		}
		return false;
	}
}
