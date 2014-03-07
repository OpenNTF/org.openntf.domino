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
public class PhoneType extends AbstractDominoType {
	private static final Logger log_ = Logger.getLogger(PhoneType.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PhoneType() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "Phone Number";
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
