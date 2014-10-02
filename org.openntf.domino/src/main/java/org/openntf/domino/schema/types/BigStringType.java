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
public class BigStringType extends AbstractDominoType {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(BigStringType.class.getName());

	BigStringType() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "Multi-line Text";
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.schema.types.AbstractDominoType#validateValue(java.lang.Object)
	 */
	@Override
	public boolean validateValue(final Object value) throws ItemException {
		// TODO Auto-generated method stub
		return false;
	}
}
