/**
 * 
 */
package org.openntf.domino.schema.types;

import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public interface IDominoType {

	public String getUITypeName();

	public boolean validateItem(org.openntf.domino.Item item) throws ItemException;

}
