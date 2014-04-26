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
public class AuthorsType extends NameType {
	private static final Logger log_ = Logger.getLogger(AuthorsType.class.getName());
	private static final long serialVersionUID = 1L;

	AuthorsType() {

	}

	@Override
	public boolean validateItem(final Item item) throws ItemException {
		return super.validateItem(item);
	}
}
