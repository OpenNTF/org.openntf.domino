/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.Collection;
import java.util.logging.Logger;

import org.openntf.domino.schema.IDominoType;
import org.openntf.domino.schema.IItemDefinition;
import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public abstract class AbstractDominoType implements IDominoType {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDominoType.class.getName());

	protected AbstractDominoType() {

	}

	@Override
	public boolean validateItem(final org.openntf.domino.Item item) throws ItemException {
		Collection<Object> values = item.getValues();
		for (Object value : values) {
			if (!validateValue(value))
				return false;
		}
		return true;
	}

	@Override
	public boolean validateItem(final org.openntf.domino.Item item, final IItemDefinition defintion) throws ItemException {
		//TODO make this work!
		Collection<Object> values = item.getValues();
		for (Object value : values) {
			if (!validateValue(value))
				return false;
		}
		return true;
	}

	public abstract boolean validateValue(final Object value) throws ItemException;

	@Override
	public void setItemToDefault(final org.openntf.domino.Item item) {
		item.setValueString("");
	}
}
