/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.Vector;
import java.util.logging.Logger;

import org.openntf.domino.Item;
import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public class CurrencyType extends AbstractDominoType {
	private static final Logger log_ = Logger.getLogger(CurrencyType.class.getName());
	private static final long serialVersionUID = 1L;

	public CurrencyType() {
		// TODO Auto-generated constructor stub
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
		Vector v = item.getValues();
		for (Object o : v) {

		}
		return super.validateItem(item);
	}
}
