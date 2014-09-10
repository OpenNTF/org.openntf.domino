/**
 * 
 */
package org.openntf.domino.ext;

import java.util.Collection;
import java.util.Map;

import org.openntf.domino.View;

/**
 * @author withersp
 * 
 *         OpenNTF extensions to DocumentCollection class
 * 
 */
public interface DocumentCollection {
	/**
	 * Stamps documents in a DocumentCollection with multiple Items, where the Map's key is the Item name and the value is the value to set
	 * the item to.
	 * 
	 * @param map
	 *            Map<String, Object> of item names and values to stamp
	 * @since org.openntf.domino 3.0.0
	 */
	public void stampAll(final Map<String, Object> map);

	/**
	 * Where a DocumentCollection has a View associated to it, gets that parent View object. Use
	 * 
	 * @return View parent
	 * @since org.openntf.domino 3.0.0
	 */
	public View getParentView();

	/**
	 * Sets a View as the parent of this DocumentCollection
	 * 
	 * @param view
	 *            View to use as the parent of this DocumentCollection
	 * @since org.openntf.domino 3.0.0
	 */
	public void setParentView(View view);

	/**
	 * Filters a DocumentCollection returning a collection of only those Documents that contain the specified value
	 * 
	 * @param value
	 *            Object to search for in the Documents using {@link java.util.Map#containsValue()}
	 * @return DocumentCollection filtered on the value
	 * @since org.openntf.domino 3.0.0
	 */
	public org.openntf.domino.DocumentCollection filter(final Object value);

	/**
	 * Filters a DocumentCollection returning a collection of only those Documents that contain the specified value in one of the specified
	 * Item names
	 * 
	 * @param value
	 *            Object to search for in the Documents using {@link java.util.Map#containsValue()}
	 * @param itemnames
	 *            String[] of Item names to restrict the search to
	 * @return DocumentCollection filtered on the value
	 * @since org.openntf.domino 3.0.0
	 */
	public org.openntf.domino.DocumentCollection filter(final Object value, String[] itemnames);

	/**
	 * Filters a DocumentCollection returning a collection of only those Documents that contain the specified value in one of the specified
	 * Item names
	 * 
	 * @param value
	 *            Object to search for in the Documents using {@link java.util.Map#containsValue()}
	 * @param itemnames
	 *            Collection<String> of Item names to restrict the search to
	 * @return DocumentCollection filtered on the value
	 * @since org.openntf.domino 3.0.0
	 */
	public org.openntf.domino.DocumentCollection filter(final Object value, Collection<String> itemnames);

	/**
	 * Filters a DocumentCollection returning a collection of only those Documents that contain one of the values in the Map.<br/>
	 * 
	 * @see org.openntf.domino.Document#containsValues(Map) for more information on how this Map filter works
	 * 
	 * @param value
	 *            Map<String, Object> of items and values to search for in the Documents using {@link java.util.Map#containsValues(Map)}
	 * @return DocumentCollection filtered on the value
	 * @since org.openntf.domino 3.0.0
	 */
	public org.openntf.domino.DocumentCollection filter(final Map<String, Object> filterMap);
}
