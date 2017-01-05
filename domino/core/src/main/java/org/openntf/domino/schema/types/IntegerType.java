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
public class IntegerType extends AbstractDominoType {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(IntegerType.class.getName());

	IntegerType() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "Number";
	}

	@Override
	public boolean validateValue(final Object value) throws ItemException {
		if (value instanceof Integer) {
			return true;
		} else if (value instanceof Double) {
			return ((Double) value).doubleValue() == ((Double) value).intValue();
		} else if (value instanceof String) {
			Integer.parseInt((String) value);
			return true;
		} else {
			return false;
		}
	}
}
