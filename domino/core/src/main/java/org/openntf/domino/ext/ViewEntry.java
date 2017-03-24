/**
 *
 */
package org.openntf.domino.ext;

import java.util.Collection;
import java.util.Map;

/**
 * OpenNTF extensions to ViewEntry class
 *
 * @author withersp
 */
public interface ViewEntry {

	/**
	 * Gets the View the ViewEntry is a child of
	 *
	 * @return View parent
	 * @since org.openntf.domino 4.5.0
	 */
	public org.openntf.domino.View getParentView();

	/**
	 * Gets a specific column's value, preferable to using getColumnValues()
	 *
	 * @param columnName
	 *            String programmatic name of the column, found on the Advanced tab (beanie image)
	 * @return Object representing the value for that column
	 * @since org.openntf.domino 3.0.0
	 */
	public Object getColumnValue(final String columnName);

	/**
	 * Gets a Map of column values, where the key is the programmatic name of the column, the value is the relevant column values for the
	 * ViewEntry
	 *
	 * @return Map<String, Object>
	 * @since org.openntf.domino 3.0.0
	 */
	public Map<String, Object> getColumnValuesMap();

	/**
	 * Gets the column values as an unmodifiable collection, useful for caching
	 *
	 * @return Collection<Object> of column values
	 * @since org.openntf.domino 3.0.0
	 */
	public Collection<Object> getColumnValuesEx();

	/**
	 * Gets a column value by programmatic column name, casting the return value to a specific Java class, e.g. <code>
	 * String nameColValue = ent.getColumnValue("name", String.class);
	 * </code>
	 *
	 * @param columnName
	 *            String programmatic column name
	 * @param T
	 *            Class to cast the return value to
	 * @return Column value, cast to the specific class
	 * @since org.openntf.domino 5.0.0
	 */
	public <T> T getColumnValue(String columnName, Class<T> type);

	/**
	 * Gets the position of the entry with each level of the hierarchy separated by a '.'
	 *
	 * <p>
	 * The position is relative to all entries and does not respect Readers fields (so if a non-categorized view contains 140 entries and
	 * you only have access to see 100, getPosition() on the last entry will return "140")
	 * <p/>
	 *
	 * @return String position e.g. 3.4.2.7
	 * @since org.openntf.domino 5.0.0
	 */
	public String getPosition();

	public String getMetaversalID();

	/**
	 * Returns a category value regardless which columns is set as a category column
	 *
	 * @return Value of the category or null if the entry is not a category
	 */
	public Object getCategoryValue();
}
