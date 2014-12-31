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
public class CurrencyType extends AbstractDominoType {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(CurrencyType.class.getName());

	CurrencyType() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "Currency";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.AbstractDominoType#validateItem(org.openntf.domino.Item)
	 */
	@Override
	public boolean validateItem(final Item item) throws ItemException {
		//		Vector<Object> v = item.getValues();
		//		for (Object o : v) {
		//
		//		}
		return super.validateItem(item);
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
