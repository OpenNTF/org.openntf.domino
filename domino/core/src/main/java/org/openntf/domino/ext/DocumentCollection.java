/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package org.openntf.domino.ext;

import java.util.Collection;
import java.util.Map;

import org.openntf.domino.View;

/**
 * OpenNTF extensions to DocumentCollection class
 *
 * @author withersp
 *
 *
 */
public interface DocumentCollection {
	/**
	 * Stamps documents in a DocumentCollection with multiple Items, where the Map's key is the Item name and the value is the value to set
	 * the item to.
	 *
	 * <pre>
	 * DocumentCollection docs = view.getAllDocumentsByKey("Done", true);
	 * if (docs.getCount() > 0) {
	 * 	HashMap&lt;String, Object&gt; itemValues = new HashMap&lt;String, Object&gt;();
	 * 	itemValues.put("Status", "Archived");
	 * 	itemValues.put("ArchivedDate", Calendar.getInstance().getTime());
	 *
	 * 	docs.stampAll(itemValues);
	 * }
	 * </pre>
	 *
	 * @param map
	 *            Map<String, Object> of item names and values to stamp
	 * @since org.openntf.domino 3.0.0
	 */
	public void stampAll(final Map<String, Object> map);

	/**
	 * Where a DocumentCollection has a View associated to it, gets that parent View object. The parent View is available when this
	 * collection was built using {@link org.openntf.domino.View#getAllDocumentsByKey(Object)} and similar methods.
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
	 * Filters a DocumentCollection returning a collection of only those Documents that contain the specified value. This is an expensive
	 * operation since it iterates over all items in documents to find the value. Consider using {@link #filter(Object, String[])} to narrow
	 * down the search to a set of items.
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
	 * <p>
	 * <h5>Example</h5>
	 * </p>
	 * The following example filters a collection of documents (possibly returned from a view categorized by status) and further filters
	 * them to only include orders where the current user is either an author of the document or an Account Manager. This is handy if there
	 * is not a view fit for that purpose.
	 *
	 * <pre>
	 * DocumentCollection docs = getActiveOrders();
	 * DocumentCollection filtered = docs.filter(userName, new String[] { "Author", "AccountManager" });
	 * </pre>
	 * </p>
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
	 * Filters a DocumentCollection returning a collection of only those Documents that contain all of the values in the Map.
	 *
	 * @see org.openntf.domino.Document#containsValues(Map) for more information on how this Map filter works
	 *
	 * @param value
	 *            Map<String, Object> of item names and values to search for in the Documents using
	 *            {@link java.util.Map#containsValues(Map)}
	 * @return DocumentCollection filtered on the value
	 * @since org.openntf.domino 3.0.0
	 */
	public org.openntf.domino.DocumentCollection filter(final Map<String, Object> filterMap);
}
