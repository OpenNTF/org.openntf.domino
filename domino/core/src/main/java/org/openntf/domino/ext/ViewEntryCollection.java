/**
 *
 */
package org.openntf.domino.ext;

import java.util.Map;

/**
 * OpenNTF extensions to ViewEntryCollection class
 *
 * @author withersp
 *
 */
public interface ViewEntryCollection {

	/**
	 * Change multiple items in entries in a ViewEntryCollection at once. The Map's key is the Item name and the value is the value to set.
	 * the item to.
	 *
	 * <pre>
	 * ViewEntryCollection entries = view.getAllEntriesByKey("Done", true);
	 * if (entries.getCount() > 0) {
	 * 	HashMap&lt;String, Object&gt; itemValues = new HashMap&lt;String, Object&gt;();
	 * 	itemValues.put("Status", "Archived");
	 * 	itemValues.put("ArchivedDate", Calendar.getInstance().getTime());
	 *
	 * 	entries.stampAll(itemValues);
	 * }
	 * </pre>
	 *
	 *
	 * @param map
	 *            Map&lt;String, Object&gt; of item names and values to change
	 * @since org.openntf.domino 3.0.0
	 */
	public void stampAll(final Map<String, Object> map);
}
