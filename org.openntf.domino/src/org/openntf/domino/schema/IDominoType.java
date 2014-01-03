/**
 * 
 */
package org.openntf.domino.schema;

import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public interface IDominoType {

	public String getUITypeName();

	public boolean validateItem(org.openntf.domino.Item item) throws ItemException;

	public boolean validateItem(org.openntf.domino.Item item, IItemDefinition definition) throws ItemException;

	public void setItemToDefault(org.openntf.domino.Item item);

}
