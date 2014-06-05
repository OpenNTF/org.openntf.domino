/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Map;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to ViewEntryCollection class
 */
public interface ViewEntryCollection {

	/**
	 * Stamps entries in a ViewEntryCollection with multiple Items, where the Map's key is the Item name and the value is the value to set
	 * the item to.
	 * 
	 * @param map
	 *            Map<String, Object> of item names and values to stamp
	 * @since org.openntf.domino 3.0.0
	 */
	public void stampAll(final Map<String, Object> map);
}
