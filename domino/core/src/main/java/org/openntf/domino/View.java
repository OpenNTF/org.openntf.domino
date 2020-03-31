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
package org.openntf.domino;

import java.io.Externalizable;
import java.util.Map;
import java.util.Vector;

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.Design;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;

/**
 * A view in a Notes application.
 * <p>
 * <h3>Notable enhancements and changes</h3>
 * <ul>
 * <li>When returned by {@link org.openntf.domino.Database#getView(String)}, the {@link #isAutoUpdate() autoUpdate} property is set to false
 * when Khan mode is enabled.</li>
 * <li>Use {@link org.openntf.domino.ext.View#checkUnique(Object, Document)} to check if there is only one document in the view with the
 * given key</li>
 * <li>Use {@link org.openntf.domino.ext.View#containsDocument(Document)} and {@link org.openntf.domino.ext.View#containsEntry(ViewEntry)}
 * to check if a document or view entry are displayed in view.</li>
 * <li>Use {@link org.openntf.domino.ext.View#getFirstDocumentByKey(Object, boolean)} instead of
 * {@link org.openntf.domino.View#getDocumentByKey(Object, boolean)}</li>
 * <li>Check if the view contains formulas based on current time using {@link org.openntf.domino.ext.View#isTimeSensitive()}</li>
 * </ul>
 * </p>
 * <p>
 * See the implementation class {@link org.openntf.domino.impl.View} for details on how the {@link java.util.Map} interface is implemented.
 * </p>
 * <h3>Access</h3>
 * <p>
 * You access a view or folder through the {@link Database} object that contains it. There are two ways:
 * </p>
 * <ul>
 * <li>To access a view or folder when you know its name or alias, use {@link Database#getView(String)}.</li>
 * <li>To access all the views and folders in a database, use {@link Database#getViews()}.</li>
 * </ul>
 * <p>
 * Returned is a <code>View</code> object or a vector of <code>View</code> objects that represent accessible views and/or folders in the
 * database. These <code>View</code> objects may be public views or folders or private views or folders stored in the database that are
 * owned by the effective id running the agent. Personal views and folders stored in the desktop cannot be accessed by programs.
 * </p>
 * <p>
 * To access a view or folder when you have a view entry, use getParent in ViewEntry.
 * </p>
 * <h3>Usage</h3>
 * <p>
 * A View object provides access to {@link ViewEntry}, {@link ViewEntryCollection}, and {@link ViewNavigator} objects:
 * </p>
 * <ul>
 * <li>A {@link ViewEntry} object represents a row in the view and can be a document, category, or total. A document entry provides a handle
 * to the associated {@link Document} object.</li>
 * <li>A {@link ViewEntryCollection} object provides access to selected or all document {@link ViewEntry} objects. (Excluded are category
 * and total <code>ViewEntry</code> objects.)</li>
 * <li>A {@link ViewNavigator} object provides methods for navigating through selected or all {@link ViewEntry} and {@link Document}
 * objects.</li>
 * </ul>
 * <p>
 * A <code>View</code> object provides access to {@link ViewColumn} objects, which contain information on each column in the view.
 * </p>
 * <p>
 * If you create a <code>View</code> object then change a document so that it affects the underlying view, navigation with the
 * <code>View</code> object may produce incorrect results.You must refresh the <code>View</code> object or create a new one. Document
 * changes that affect views include additions, deletions, and changes to fields used by selection formulas. See {@link #refresh()} for
 * further information.
 * </p>
 * <h3>Automatic updates: avoid</h3>
 * <p>
 * Avoid automatically updating the view by explicitly setting {@link #isAutoUpdate()} to false especially if the view is a base for
 * navigators or entry collections. Automatic updates degrade performance and may invalidate entries in child objects ("Entry not found in
 * index"). You can update the view as needed with refresh.
 * </p>
 */
public interface View extends lotus.domino.View, org.openntf.domino.ext.View, Base<lotus.domino.View>, Design, Resurrectable,
		DatabaseDescendant, Externalizable, Map<String, Object> {

	public static class Schema extends FactorySchema<View, lotus.domino.View, Database> {
		@Override
		public Class<View> typeClass() {
			return View.class;
		}

		@Override
		public Class<lotus.domino.View> delegateClass() {
			return lotus.domino.View.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	public enum IndexType {
		SHARED, PRIVATE, SHAREDPRIVATEONSERVER, SHAREDPRIVATEONDESKTOP, SHAREDINCLUDESDELETES, SHAREDNOTINFOLDERS
	}

	/**
	 * Clears the full-text search filtering on a view. Subsequent calls to {@link #getDocument()} methods get all documents in the view,
	 * not just the search results.
	 */
	@Override
	public void clear();

	/**
	 * Creates a new column by copying an existing one. Positioning is 1-based. This differs from the vector returned by
	 * {@link #getColumns()}, which is 0-based.
	 *
	 * @param sourcecolumn
	 *            The position of the view column to be copied. It must be in the same view.
	 * @return A ViewColumn object. The new column.
	 */
	@Override
	public ViewColumn copyColumn(final int sourceColumn);

	/**
	 * Creates a new column by copying an existing one. Positioning is 1-based. This differs from the vector returned by
	 * {@link #getColumns()}, which is 0-based.
	 *
	 * @param sourcecolumn
	 *            The position of the view column to be copied. It must be in the same view.
	 * @param destinationindex
	 *            The position of the new column. Defaults to the last column.
	 * @return A ViewColumn object. The new column.
	 */
	@Override
	public ViewColumn copyColumn(final int sourceColumn, final int destinationIndex);

	/**
	 * Creates a new column by copying an existing one.
	 *
	 * @param sourcecolumn
	 *            The title of the view column to be copied. It must be in the same view.
	 * @return A ViewColumn object. The new column.
	 */
	@Override
	public ViewColumn copyColumn(final String sourceColumn);

	/**
	 * Creates a new column by copying an existing one. Positioning is 1-based. This differs from the vector returned by
	 * {@link #getColumns()}, which is 0-based.
	 *
	 * @param sourcecolumn
	 *            The title of the view column to be copied. It must be in the same view.
	 * @param destinationindex
	 *            The position of the new column. Defaults to the last column.
	 * @return A ViewColumn object. The new column.
	 */
	@Override
	public ViewColumn copyColumn(final String sourceColumn, final int destinationIndex);

	/**
	 * Creates a new column by copying an existing one.
	 *
	 * @param sourcecolumn
	 *            The view column to be copied.
	 * @return A ViewColumn object. The new column.
	 */
	@Override
	public ViewColumn copyColumn(final lotus.domino.ViewColumn sourceColumn);

	/**
	 * Creates a new column by copying an existing one. Positioning is 1-based. This differs from the vector returned by
	 * {@link #getColumns()}, which is 0-based.
	 *
	 * @param sourcecolumn
	 *            The view column to be copied.
	 * @param destinationindex
	 *            The position of the new column. Defaults to the last column.
	 * @return The new column.
	 */
	@Override
	public ViewColumn copyColumn(final lotus.domino.ViewColumn sourceColumn, final int destinationIndex);

	/**
	 * Creates a new column.
	 *
	 * @return The new column.
	 */
	@Override
	public ViewColumn createColumn();

	/**
	 * Creates a new column. Positioning is 1-based. This differs from the vector returned by {@link #getColumns()}, which is 0-based.
	 *
	 * @param position
	 *            The position of the new column. Defaults to the last column.
	 * @return The new column.
	 */
	@Override
	public ViewColumn createColumn(final int position);

	/**
	 * Creates a new column. Positioning is 1-based. This differs from the vector returned by {@link #getColumns()}, which is 0-based.
	 *
	 * @param position
	 *            The position of the new column. Defaults to the last column.
	 * @param columnTitle
	 *            The column title.
	 * @return The new column.
	 */
	@Override
	public ViewColumn createColumn(final int position, final String columnTitle);

	/**
	 * Creates a new column.
	 * <p>
	 * Positioning is 1-based. This differs from the vector returned by {@link #getColumns()}, which is 0-based.
	 * </p>
	 *
	 * @param position
	 *            The position of the new column. Defaults to the last column.
	 * @param columnTitle
	 *            The column title.
	 * @param formula
	 *            The column formula. "@DocNumber" is the default.
	 * @return The new column.
	 */
	@Override
	public ViewColumn createColumn(final int position, final String columnTitle, final String formula);

	/**
	 * Creates an empty view entry collection.
	 *
	 * @return An empty view entry collection.
	 */
	@Override
	public ViewEntryCollection createViewEntryCollection();

	/**
	 * Creates a view navigator for all entries in a view.
	 *
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNav();

	/**
	 * Creates a view navigator for all entries in a view.
	 *
	 * @param cacheSize
	 *            The size of the navigator cache in view entries. Legal values are 0 (no cache) through 128 (default). Applies only to
	 *            remote (IIOP) operations.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNav(final int cacheSize);

	/**
	 * Creates a view navigator for all entries in a view starting at a specified entry. The first entry in the navigator is the specified
	 * entry. The remaining entries are all the entries in the view that follow the first entry.
	 *
	 * <p>
	 * If the object is not found, the result is an empty navigator. All navigation methods return null.
	 * </p>
	 *
	 * @param entry
	 *            A {@link Document} or {@link ViewEntry} object representing the starting entry. Cannot be null.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFrom(final Object entry);

	/**
	 * Creates a view navigator for all entries in a view starting at a specified entry. The first entry in the navigator is the specified
	 * entry. The remaining entries are all the entries in the view that follow the first entry.
	 *
	 * <p>
	 * If the object is not found, the result is an empty navigator. All navigation methods return null.
	 * </p>
	 *
	 * <p>
	 * The cache enhances performance for iterative processing of entries using the navigation methods that do not take a parameter.
	 * </p>
	 *
	 * @param entry
	 *            A {@link Document} or {@link ViewEntry} object representing the starting entry. Cannot be null.
	 * @param cacheSize
	 *            The size of the navigator cache in view entries. Legal values are 0 (no cache) through 128 (default). Applies only to
	 *            remote (IIOP) operations.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFrom(final Object entry, final int cacheSize);

	/**
	 * Creates a view navigator for all unread entries in a view on behalf of the current user ID. The navigator contains all entries even
	 * if the view is filtered for a full-text search. If the database does not track unread marks, all documents are considered read.
	 *
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFromAllUnread();

	/**
	 * Creates a view navigator for all unread entries in a view. The navigator contains all entries even if the view is filtered for a
	 * full-text search. If the database does not track unread marks, all documents are considered read.
	 *
	 * @param userName
	 *            If present the method returns a {@link ViewNavigator} containing all unread documents on behalf of the given name.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFromAllUnread(final String userName);

	/**
	 * Creates a view navigator for all entries in a view under a specified category.
	 * <p>
	 * The entries in the navigator are all the entries in the view under the specified category. The category entry itself is excluded.
	 *
	 * If the category does not exist, the result is an empty navigator. All navigation methods return null.
	 *
	 * Subcategories can be specified using backslash notation (don't forget to escape the backslashes), for example, "Asia\\Korea" means
	 * the subcategory "Korea" under the main category "Asia."
	 * </p>
	 *
	 * @param categoryName
	 *            The name of a category in the view.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFromCategory(final String categoryName);

	/**
	 * Creates a view navigator for all entries in a view under a specified category.
	 * <p>
	 * The entries in the navigator are all the entries in the view under the specified category. The category entry itself is excluded.
	 *
	 * If the category does not exist, the result is an empty navigator. All navigation methods return null.
	 *
	 * Subcategories can be specified using backslash notation (don't forget to escape the backslashes), for example, "Asia\\Korea" means
	 * the subcategory "Korea" under the main category "Asia."
	 *
	 * </p>
	 * <p>
	 * The cache enhances performance for iterative processing of entries using the navigation methods that do not take a parameter.
	 * </p>
	 *
	 * @param categoryName
	 *            The name of a category in the view.
	 * @param cacheSize
	 *            The size of the navigator cache in view entries. Legal values are 0 (no cache) through 128 (default). Applies only to
	 *            remote (IIOP) operations.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFromCategory(final String categoryName, final int cacheSize);

	/**
	 * Creates a view navigator for the immediate children of a specified entry. The entries in the navigator are all the entries in the
	 * view that fall hierarchically at the next level under the specified entry. The parent entry itself is excluded.
	 *
	 * <p>
	 * If the entry has no children, the result is an empty navigator. All navigation methods return null.
	 * </p>
	 *
	 * @param entry
	 *            A {@link Document} or {@link ViewEntry} object representing the parent entry. Cannot be null.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFromChildren(final Object entry);

	/**
	 * Creates a view navigator for the immediate children of a specified entry.
	 * <p>
	 * The entries in the navigator are all the entries in the view that fall hierarchically at the next level under the specified entry.
	 * The parent entry itself is excluded.
	 *
	 * If the entry has no children, the result is an empty navigator. All navigation methods return null.
	 *
	 * </p>
	 * <p>
	 * The cache enhances performance for iterative processing of entries using the navigation methods that do not take a parameter.
	 * </p>
	 *
	 * @param entry
	 *            A {@link Document} or {@link ViewEntry} object representing the parent entry. Cannot be null.
	 * @param cacheSize
	 *            The size of the navigator cache in view entries. Legal values are 0 (no cache) through 128 (default). Applies only to
	 *            remote (IIOP) operations.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFromChildren(final Object entry, final int cacheSize);

	/**
	 * Creates a view navigator for all the descendants of a specified entry. The entries in the navigator are the entries in the view that
	 * fall hierarchically at all levels under the specified entry. The parent itself is excluded.
	 *
	 * If the entry has no children, the result is an empty navigator. All navigation methods return null.
	 *
	 * @param entry
	 *            A {@link Document} or {@link ViewEntry} object representing the parent entry. Cannot be null.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFromDescendants(final Object entry);

	/**
	 * Creates a view navigator for all the descendants of a specified entry.
	 * <p>
	 * The entries in the navigator are the entries in the view that fall hierarchically at all levels under the specified entry. The parent
	 * itself is excluded.
	 *
	 * If the entry has no children, the result is an empty navigator. All navigation methods return null.
	 * </p>
	 * <p>
	 * The cache enhances performance for iterative processing of entries using the navigation methods that do not take a parameter.
	 * </p>
	 *
	 * @param entry
	 *            A {@link Document} or {@link ViewEntry} object representing the parent entry. Cannot be null.
	 * @param cacheSize
	 *            The size of the navigator cache in view entries. Legal values are 0 (no cache) through 128 (default). Applies only to
	 *            remote (IIOP) operations.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavFromDescendants(final Object entry, final int cacheSize);

	/**
	 * Creates a view navigator for all entries in a view down to a specified level. The entries in the navigator are all the entries in the
	 * view at levels 0 through the specified level.
	 *
	 * <p>
	 * An empty view results in an empty navigator. All navigation methods return null.
	 * </p>
	 *
	 * @param level
	 *            The maximum level of navigation 0 (top level) through 30 (default).
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavMaxLevel(final int level);

	/**
	 * Creates a view navigator for all entries in a view down to a specified level.
	 * <p>
	 * The entries in the navigator are all the entries in the view at levels 0 through the specified level.
	 *
	 * An empty view results in an empty navigator. All navigation methods return null.
	 * </p>
	 * <p>
	 * The cache enhances performance for iterative processing of entries using the navigation methods that do not take a parameter.
	 * </p>
	 *
	 * @param level
	 *            The maximum level of navigation 0 (top level) through 30 (default).
	 * @param cacheSize
	 *            The size of the navigator cache in view entries. Legal values are 0 (no cache) through 128 (default). Applies only to
	 *            remote (IIOP) operations.
	 * @return The new view navigator.
	 */
	@Override
	public ViewNavigator createViewNavMaxLevel(final int level, final int cacheSize);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query. This method does not find word variants.
	 * <p>
	 * After calling FTSearch, you can use the regular View methods to navigate the result, which is a subset of the documents in the view.
	 * If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on.
	 * </p>
	 * <p>
	 * Use the clear method to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view.
	 * </p>
	 *
	 * <p>
	 * If the database is not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create
	 * an index on a local database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal.
	 *
	 * Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see "Refining a search query using operators" in
	 * Notes Help. Search for "query syntax" in the Domino Designer Eclipse help system or information center (for example,
	 * http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp), both of which include Notes.
	 * </p>
	 *
	 * @param query
	 *            The full-text query.
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@Override
	public int FTSearch(final String query);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query. This method does not find word variants.
	 * <p>
	 * After calling FTSearch, you can use the regular View methods to navigate the result, which is a subset of the documents in the view.
	 * If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on.
	 * </p>
	 * <p>
	 * Use the clear method to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view.
	 * </p>
	 *
	 * <p>
	 * If the database is not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create
	 * an index on a local database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal.
	 *
	 * Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see "Refining a search query using operators" in
	 * Notes Help. Search for "query syntax" in the Domino Designer Eclipse help system or information center (for example,
	 * http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp), both of which include Notes.
	 * </p>
	 *
	 * @param query
	 *            The full-text query.
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@Override
	public int FTSearch(final String query, final int maxDocs);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The NotesView methods now navigate to the full set of documents in the view. If the database
	 * is not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a
	 * local database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            The full-text query
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@Override
	public int FTSearchSorted(final String query);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            The full-text query
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            The full-text query or the intersection of multiple queries.
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            0-based index of a sorted column. A specification of View.VIEW_FTSS_RELEVANCE_ORDER (512) returns results in relevance
	 *            order while honoring the use of the extended flags for exact case, variants, and fuzzy search.
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final int column);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            The full-text query
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            0-based index of a sorted column. A specification of View.VIEW_FTSS_RELEVANCE_ORDER (512) returns results in relevance
	 *            order while honoring the use of the extended flags for exact case, variants, and fuzzy search.
	 * @param ascending
	 *            Sorts column data in ascending order if true, descending order if false. Ignored if View.VIEW_FTSS_RELEVANCE_ORDER is in
	 *            effect. The availability of a column to be sorted in ascending or descending order is determined by "Click on column
	 *            header to sort" on the Sorting tab of the column properties. The relevant options are Ascending, Descending, and Both.
	 *            Trying to sort a column in an unsupported direction throws an exception.
	 * @param exact
	 *            specify true to apply exact case to the search
	 * @param variants
	 *            specify true to return word variants in the search results
	 * @param fuzzy
	 *            specify true to return misspelled words in the search results
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final int column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            The full-text query
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            0-based index of a sorted column. A specification of View.VIEW_FTSS_RELEVANCE_ORDER (512) returns results in relevance
	 *            order while honoring the use of the extended flags for exact case, variants, and fuzzy search.
	 * @param ascending
	 *            Sorts column data in ascending order if true, descending order if false. Ignored if View.VIEW_FTSS_RELEVANCE_ORDER is in
	 *            effect. The availability of a column to be sorted in ascending or descending order is determined by "Click on column
	 *            header to sort" on the Sorting tab of the column properties. The relevant options are Ascending, Descending, and Both.
	 *            Trying to sort a column in an unsupported direction throws an exception.
	 * @param exact
	 *            specify true to apply exact case to the search
	 * @param variants
	 *            specify true to return word variants in the search results
	 * @param fuzzy
	 *            specify true to return misspelled words in the search results
	 * @param webQuerySyntax
	 *            specify true to use web query syntax instead of Notes full text search syntax
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final int column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy, final boolean webQuerySyntax);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            The full-text query or the intersection of multiple queries.
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            The name of a sorted column.
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final String column);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            The full-text query
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            The name of a sorted column.
	 * @param ascending
	 *            Sorts column data in ascending order if true, descending order if false. Ignored if View.VIEW_FTSS_RELEVANCE_ORDER is in
	 *            effect. The availability of a column to be sorted in ascending or descending order is determined by "Click on column
	 *            header to sort" on the Sorting tab of the column properties. The relevant options are Ascending, Descending, and Both.
	 *            Trying to sort a column in an unsupported direction throws an exception.
	 * @param exact
	 *            specify true to apply exact case to the search
	 * @param variants
	 *            specify true to return word variants in the search results
	 * @param fuzzy
	 *            specify true to return misspelled words in the search results
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final String column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            The full-text query
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            The name of a sorted column.
	 * @param ascending
	 *            Sorts column data in ascending order if true, descending order if false. Ignored if View.VIEW_FTSS_RELEVANCE_ORDER is in
	 *            effect. The availability of a column to be sorted in ascending or descending order is determined by "Click on column
	 *            header to sort" on the Sorting tab of the column properties. The relevant options are Ascending, Descending, and Both.
	 *            Trying to sort a column in an unsupported direction throws an exception.
	 * @param exact
	 *            specify true to apply exact case to the search
	 * @param variants
	 *            specify true to return word variants in the search results
	 * @param fuzzy
	 *            specify true to return misspelled words in the search results
	 * @param webQuerySyntax
	 *            specify true to use web query syntax instead of Notes full text search syntax
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 * @since Domino V10
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final String column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy, final boolean webQuerySyntax);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            intersection of multiple full-text queries
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            intersection of multiple full-text queries
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            intersection of multiple full-text queries
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            0-based index of a sorted column. A specification of View.VIEW_FTSS_RELEVANCE_ORDER (512) returns results in relevance
	 *            order while honoring the use of the extended flags for exact case, variants, and fuzzy search.
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final int column);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            intersection of multiple full-text queries
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            0-based index of a sorted column. A specification of View.VIEW_FTSS_RELEVANCE_ORDER (512) returns results in relevance
	 *            order while honoring the use of the extended flags for exact case, variants, and fuzzy search.
	 * @param ascending
	 *            Sorts column data in ascending order if true, descending order if false. Ignored if View.VIEW_FTSS_RELEVANCE_ORDER is in
	 *            effect. The availability of a column to be sorted in ascending or descending order is determined by "Click on column
	 *            header to sort" on the Sorting tab of the column properties. The relevant options are Ascending, Descending, and Both.
	 *            Trying to sort a column in an unsupported direction throws an exception.
	 * @param exact
	 *            specify true to apply exact case to the search
	 * @param variants
	 *            specify true to return word variants in the search results
	 * @param fuzzy
	 *            specify true to return misspelled words in the search results
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final int column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            intersection of multiple full-text queries
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            0-based index of a sorted column. A specification of View.VIEW_FTSS_RELEVANCE_ORDER (512) returns results in relevance
	 *            order while honoring the use of the extended flags for exact case, variants, and fuzzy search.
	 * @param ascending
	 *            Sorts column data in ascending order if true, descending order if false. Ignored if View.VIEW_FTSS_RELEVANCE_ORDER is in
	 *            effect. The availability of a column to be sorted in ascending or descending order is determined by "Click on column
	 *            header to sort" on the Sorting tab of the column properties. The relevant options are Ascending, Descending, and Both.
	 *            Trying to sort a column in an unsupported direction throws an exception.
	 * @param exact
	 *            specify true to apply exact case to the search
	 * @param variants
	 *            specify true to return word variants in the search results
	 * @param fuzzy
	 *            specify true to return misspelled words in the search results
	 * @param webQuerySyntax
	 *            specify true to use web query syntax instead of Notes full text search syntax
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 * @since Domino V10
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final int column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy, final boolean webQuerySyntax);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            intersection of multiple full-text queries
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            Name of a sorted column
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final String column);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            intersection of multiple full-text queries
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            name of a sorted column.
	 * @param ascending
	 *            Sorts column data in ascending order if true, descending order if false. Ignored if View.VIEW_FTSS_RELEVANCE_ORDER is in
	 *            effect. The availability of a column to be sorted in ascending or descending order is determined by "Click on column
	 *            header to sort" on the Sorting tab of the column properties. The relevant options are Ascending, Descending, and Both.
	 *            Trying to sort a column in an unsupported direction throws an exception.
	 * @param exact
	 *            specify true to apply exact case to the search
	 * @param variants
	 *            specify true to return word variants in the search results
	 * @param fuzzy
	 *            specify true to return misspelled words in the search results
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final String column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy);

	/**
	 * Conducts a full-text search on all documents in a view and filters the view so it represents only those documents that match the
	 * full-text query in sorted order.
	 * <p>
	 * After calling this method, you can use the regular View methods to navigate the result, which is a subset of the documents in the
	 * view. If the database is not full-text indexed, the documents in the subset are in the same order as they are in the original view.
	 * However, if the database is full-text indexed, the documents in the subset are sorted into descending order of relevance. The method
	 * getFirstDocument returns the first document in the subset, getLastDocument returns the last document, and so on. Use the clear method
	 * to clear the full-text search filtering. The View methods now navigate to the full set of documents in the view. If the database is
	 * not full-text indexed, this method works, but less efficiently. To test for an index, use isFTIndexed. To create an index on a local
	 * database, use updateFTIndex.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal. Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see
	 * "Refining a search query using operators" in Notes Help. You can also search for "query syntax" in the Domino Designer Eclipse help
	 * system.
	 * </p>
	 *
	 * @param query
	 *            intersection of multiple full-text queries
	 * @param maxDocs
	 *            The maximum number of documents you want returned from the search. If you want to receive all documents that match the
	 *            query, specify 0.
	 * @param column
	 *            name of a sorted column.
	 * @param ascending
	 *            Sorts column data in ascending order if true, descending order if false. Ignored if View.VIEW_FTSS_RELEVANCE_ORDER is in
	 *            effect. The availability of a column to be sorted in ascending or descending order is determined by "Click on column
	 *            header to sort" on the Sorting tab of the column properties. The relevant options are Ascending, Descending, and Both.
	 *            Trying to sort a column in an unsupported direction throws an exception.
	 * @param exact
	 *            specify true to apply exact case to the search
	 * @param variants
	 *            specify true to return word variants in the search results
	 * @param fuzzy
	 *            specify true to return misspelled words in the search results
	 * @param webQuerySyntax
	 *            specify true to use web query syntax instead of Notes full text search syntax
	 * @return The number of documents in the view after the search. Each of these documents matches the query.
	 * @since Domino V10
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final String column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy, final boolean webQuerySyntax);

	/**
	 * The aliases of a view. This property does not return the name of the view. Use {@link #getName()} to return the name.
	 *
	 * Old-style aliases that are in the name field, separated from the name by vertical bars, are returned as part of the name.
	 *
	 * @return The aliases of a view.
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getAliases();

	/**
	 * Finds documents based on their column values within a view. You create a key or vector of keys, where each key corresponds to a value
	 * in a sorted column in the view. The method returns all documents whose column values match the keys.
	 * <p>
	 * For the getAllDocumentsByKey method to work using a key, you must have at least one column sorted for every key in the vector.
	 *
	 * This method returns all the documents whose column values match the keys. To locate just the first document, use
	 * {@link #getDocumentByKey(Object)}.
	 *
	 * Documents returned by this method are in no particular order, and do not provide access to column values. Use
	 * {@link #getAllEntriesByKey(Object)} for these capabilities.
	 * <p>
	 * Matches are not case-sensitive. For example, "Turban" matches "turban." In an exact match, "cat" matches "cat" but does not match
	 * "category," while "20" matches "20" but does not match "201." In a partial match, "T" matches "Tim" or "turkey," but does not match
	 * "attic," while "cat" matches "catalog" or "category," but does not match "coat" or "bobcat."
	 * </p>
	 * <p>
	 * The use of partial matches with multiple keys may result in missed documents. If the first key is partial and the second column does
	 * not sort the same with the partial key as with the exact key, documents that fall out of sequence are missed.
	 * </p>
	 * <p>
	 * If any columns are formatted with both categories and subcategories in the same column (using the "\\" special character), the method
	 * will not find the documents.
	 * </p>
	 * <p>
	 * To get view entry information about the documents, use the {@link #getAllEntriesByKey(Object)} method.
	 * </p>
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @return All documents in the view whose column values match each of the keys. If no documents match, the collection is empty and the
	 *         count is zero.
	 */
	@Override
	public DocumentCollection getAllDocumentsByKey(final Object key);

	/**
	 * Finds documents based on their column values within a view. You create a key or vector of keys, where each key corresponds to a value
	 * in a sorted column in the view. The method returns all documents whose column values match the keys.
	 * <p>
	 * For the getAllDocumentsByKey method to work using a key, you must have at least one column sorted for every key in the vector.
	 *
	 * This method returns all the documents whose column values match the keys. To locate just the first document, use
	 * {@link #getDocumentByKey(Object)}.
	 *
	 * Documents returned by this method are in no particular order, and do not provide access to column values. Use
	 * {@link #getAllEntriesByKey(Object)} for these capabilities.
	 * <p>
	 * Matches are not case-sensitive. For example, "Turban" matches "turban." In an exact match, "cat" matches "cat" but does not match
	 * "category," while "20" matches "20" but does not match "201." In a partial match, "T" matches "Tim" or "turkey," but does not match
	 * "attic," while "cat" matches "catalog" or "category," but does not match "coat" or "bobcat."
	 * </p>
	 * <p>
	 * The use of partial matches with multiple keys may result in missed documents. If the first key is partial and the second column does
	 * not sort the same with the partial key as with the exact key, documents that fall out of sequence are missed.
	 * </p>
	 * <p>
	 * If any columns are formatted with both categories and subcategories in the same column (using the "\\" special character), the method
	 * will not find the documents.
	 * </p>
	 * <p>
	 * To get view entry information about the documents, use the {@link #getAllEntriesByKey(Object)} method.
	 * </p>
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @param exact
	 *            Specify true if you want to find an exact match. If you specify false a partial match succeeds.
	 * @return All documents in the view whose column values match each of the keys. If no documents match, the collection is empty and the
	 *         count is zero.
	 */
	@Override
	public DocumentCollection getAllDocumentsByKey(final Object key, final boolean exact);

	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getAllDocumentsByKey(Object)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(final Vector keys);

	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getAllDocumentsByKey(Object, boolean)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(final Vector keys, final boolean exact);

	/**
	 * All document entries in a view in view order. A view entry collection contains only document entries (no categories or totals).
	 *
	 * If a view is filtered by {@link #FTSearch(String)}, this property returns the entries in the filtered view.
	 *
	 * <p>
	 * Use {@link ViewNavigator} to get all view entries including categories and totals.
	 * </p>
	 */
	@Override
	public ViewEntryCollection getAllEntries();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getAllEntriesByKey(java.lang.Object)
	 */
	@Override
	public ViewEntryCollection getAllEntriesByKey(final Object key);

	/**
	 * Finds view entries of type document based on their column values within a view. You create a key or vector of keys, where each key
	 * corresponds to a value in a sorted column in the view. The method returns all entries whose column values match the keys. For the
	 * getAllEntriesByKey method to work using a key, you must have at least one column sorted for every key in the vector.
	 *
	 * <p>
	 * This method returns all the view entries of type document whose column values match the keys. To locate just the first entry, use
	 * {@link #getEntryByKey(Object)}.
	 * </p>
	 *
	 * <p>
	 * Entries returned by this method are in view order and provide access to column values.
	 * </p>
	 *
	 * <p>
	 * Matches are not case-sensitive. For example, "Turban" matches "turban." In an exact match, "cat" matches "cat" but does not match
	 * "category," while "20" matches "20" but does not match "201." In a partial match, "T" matches "Tim" or "turkey," but does not match
	 * "attic," while "cat" matches "catalog" or "category," but does not match "coat" or "bobcat."
	 * </p>
	 *
	 * <p>
	 * The use of partial matches with multiple keys may result in missed entries. If the first key is partial and the second column does
	 * not sort the same with the partial key as with the exact key, entries that fall out of sequence are missed.
	 * </p>
	 *
	 * <p>
	 * If any columns are formatted with both categories and subcategories in the same column (using the "\\" special character), the method
	 * will not find the entries.
	 * <p>
	 *
	 * <p>
	 * This method is similar to {@link #getAllDocumentsByKey(Object)}
	 * </p>
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @param exact
	 *            A String, Number, DateTime, or DateRange object that is compared to the first sorted column in the view.
	 * @return All entries of type document in the view whose column values match each of the keys. If no entries match, the collection is
	 *         empty and the count is zero.
	 */
	@Override
	public ViewEntryCollection getAllEntriesByKey(final Object key, final boolean exact);

	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getAllEntriesByKey(Object)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(final Vector keys);

	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getAllEntriesByKey(Object, boolean)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(final Vector keys, final boolean exact);

	/**
	 * The method returns all entries associated with documents that have been read for current user ID.
	 *
	 * @return All read entries. If no entries are read, the collection is empty and the count is zero. If the database does not track
	 *         unread marks, all entries are considered read.
	 */
	@Override
	public ViewEntryCollection getAllReadEntries();

	/**
	 * The method returns all entries associated with documents that have been read.
	 *
	 * @param userName
	 *            the returning ViewEntryCollection will contain all read entries on behalf of the given name.
	 * @return All read entries. If no entries are read, the collection is empty and the count is zero. If the database does not track
	 *         unread marks, all entries are considered read.
	 */
	@Override
	public ViewEntryCollection getAllReadEntries(final String userName);

	/**
	 * Returns all entries associated with documents that have not been read for current user ID.
	 *
	 * @return All unread entries. If no entries are unread, the collection is empty and the count is zero. If the database does not track
	 *         unread marks, all entries are considered read.
	 */
	@Override
	public ViewEntryCollection getAllUnreadEntries();

	/**
	 * Returns all entries associated with documents that have not been read for current user ID.
	 *
	 * @param userName
	 *            the returning ViewEntryCollection will contain all unread entries on behalf of the given name.
	 * @return All unread entries. If no entries are unread, the collection is empty and the count is zero. If the database does not track
	 *         unread marks, all entries are considered read.
	 */
	@Override
	public ViewEntryCollection getAllUnreadEntries(final String userName);

	/**
	 * The background color of a view.
	 *
	 * @return one of the Domino color index in the range 0-240
	 */
	@Override
	public int getBackgroundColor();

	/**
	 * Given a document in a view, returns the first response to the document. To find additional response documents, use
	 * {@link #getNextSibling(lotus.domino.Document)} The combination of <code>getChild</code> and <code>getNextSibling</code> allows you to
	 * access document responses, sorted in the same order that they appear in a view. To get all the immediate responses for a document
	 * unsorted, use {@link Document#getResponses()}.
	 *
	 * <p>
	 * If you've filtered the view with the {@link #FTSearch(String)} method, <code>getChild</code> returns the next document in the view,
	 * regardless of level.
	 * </p>
	 *
	 * @param doc
	 *            Any document in the view. Cannot be null.
	 * @return The first response document to the parameter you specify. Returns null if there are no responses to the document.
	 */
	@Override
	public Document getChild(final lotus.domino.Document doc);

	/**
	 * Returns a specified column in a view.
	 *
	 * @param columnNumber
	 *            A column number where 1 is the first column. Cannot be less than 1 or greater than the number of columns in the view.
	 * @return The specified column.
	 */
	@Override
	public ViewColumn getColumn(final int columnNumber);

	/**
	 * The number of columns in a view.
	 */
	@Override
	public int getColumnCount();

	/**
	 * The names of the columns in a view. The order of the column names in the vector corresponds to the order of the columns in the view,
	 * from left to right.
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getColumnNames();

	/**
	 * The columns in a view. The order of {@link ViewColumn} objects in the vector corresponds to the order of the columns in the view,
	 * from left to right.
	 *
	 * @see ViewColumn
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<ViewColumn> getColumns();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getColumnValues(int)
	 */
	@Override
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector<Object> getColumnValues(final int column);

	/**
	 * The date and time when a view was created.
	 */
	@Override
	public DateTime getCreated();

	/**
	 * Finds a document based on its column values within a view. You create a key or vector of keys, where each key corresponds to a value
	 * in a sorted column in the view. The method returns the first document with column values that match the keys.
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @return The first document in the view with column values that match the keys. Returns null if there are no matching documents.
	 * @deprecated Use {@link #getFirstDocumentByKey(Object)} instead.
	 */
	@Override
	@Deprecated
	public Document getDocumentByKey(final Object key);

	/**
	 * Finds a document based on its column values within a view. You create a key or vector of keys, where each key corresponds to a value
	 * in a sorted column in the view. The method returns the first document with column values that match the keys.
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @param exact
	 *            Specify true if you want to find an exact match. If you specify false, a partial match succeeds.
	 * @return The first document in the view with column values that match the keys. Returns null if there are no matching documents.
	 * @deprecated Use {@link #getFirstDocumentByKey(Object, boolean)} instead.
	 */
	@Override
	@Deprecated
	public Document getDocumentByKey(final Object key, final boolean exact);

	/**
	 * Finds a document based on its column values within a view. You create a key or vector of keys, where each key corresponds to a value
	 * in a sorted column in the view. The method returns the first document with column values that match the keys.
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @return The first document in the view with column values that match the keys. Returns null if there are no matching documents.
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getFirstDocumentByKey(Object)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy(Legacy.GENERICS_WARNING)
	public Document getDocumentByKey(final Vector keys);

	/**
	 * Finds a document based on its column values within a view. You create a key or vector of keys, where each key corresponds to a value
	 * in a sorted column in the view. The method returns the first document with column values that match the keys.
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @param exact
	 *            Specify true if you want to find an exact match. If you specify false, a partial match succeeds.
	 * @return The first document in the view with column values that match the keys. Returns null if there are no matching documents.
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getFirstDocumentByKey(Object, boolean)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy(Legacy.GENERICS_WARNING)
	public Document getDocumentByKey(final Vector keys, final boolean exact);

	/**
	 * Finds a view entry of type document based on its column values within a view. You create a key or vector of keys, where each key
	 * corresponds to a value in a sorted column in the view. The method returns the first entry with column values that match the keys.
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @return The first entry in the view with column values that match the keys. Returns null if there are no matching entries.
	 * @deprecated Use {@link #getFirstEntryByKey(Object)} instead.
	 */
	@Override
	@Deprecated
	public ViewEntry getEntryByKey(final Object key);

	/**
	 * Finds a view entry of type document based on its column values within a view. You create a key or vector of keys, where each key
	 * corresponds to a value in a sorted column in the view. The method returns the first entry with column values that match the keys.
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @param exact
	 *            Specify true if you want to find an exact match. If you specify false or omit this parameter, a partial match succeeds.
	 * @return The first entry in the view with column values that match the keys. Returns null if there are no matching entries.
	 * @deprecated Use {@link #getFirstEntryByKey(Object, boolean)} instead.
	 */
	@Override
	@Deprecated
	public ViewEntry getEntryByKey(final Object key, final boolean exact);

	/**
	 * Finds a document based on its column values within a view. You create a key or vector of keys, where each key corresponds to a value
	 * in a sorted column in the view. The method returns the first document with column values that match the keys.
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @return The first document in the view with column values that match the keys. Returns null if there are no matching documents.
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getFirstEntryByKey(Object)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy(Legacy.GENERICS_WARNING)
	public ViewEntry getEntryByKey(final Vector keys);

	/**
	 * Finds a document based on its column values within a view. You create a key or vector of keys, where each key corresponds to a value
	 * in a sorted column in the view. The method returns the first document with column values that match the keys.
	 *
	 * @param key
	 *            String, Number, DateTime, or DateRange objects that are compared to sorted columns in the view. The first element in the
	 *            vector is compared to the first sorted column in the view; the second element is compared to the second sorted column; and
	 *            so on.
	 * @param exact
	 *            Specify true if you want to find an exact match. If you specify false or omit this parameter, a partial match succeeds.
	 * @return The first document in the view with column values that match the keys. Returns null if there are no matching documents.
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getFirstEntryByKey(Object, boolean)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy(Legacy.GENERICS_WARNING)
	public ViewEntry getEntryByKey(final Vector keys, final boolean exact);

	/**
	 * The number of documents in a view.
	 */
	@Override
	public int getEntryCount();

	/**
	 * Returns the first document in a view. If the view is filtered by {@link #FTSearch(String)}, this method returns the first document in
	 * the filtered view.
	 *
	 * <p>
	 * The {@link ViewNavigator} and {@link ViewEntryCollection} classes provide more efficient methods for navigating views and accessing
	 * entries.
	 * </p>
	 *
	 * @return The first document in the view. Returns null if there are no documents in the view.
	 */
	@Override
	public Document getFirstDocument();

	/**
	 * The number of lines in the header of this view.
	 */
	@Override
	public int getHeaderLines();

	/**
	 * The Domino URL of a view when HTTP protocols are in effect. If HTTP protocols are not available, this property returns an empty
	 * string.
	 */
	@Override
	public String getHttpURL();

	/**
	 * Returns the last document in a view. If the view is filtered by {@link #FTSearch(String)}, this method returns the last document in
	 * the filtered view.
	 *
	 * The {@link ViewNavigator} and {@link ViewEntryCollection} classes provide more efficient methods for navigating views and accessing
	 * entries.
	 *
	 * @return The last document in the view. Returns null if there are no documents in the view.
	 */
	@Override
	public Document getLastDocument();

	/**
	 * The date that a view was last modified.
	 */
	@Override
	public DateTime getLastModified();

	/**
	 * The names of the holders of a lock. If the view is locked, the vector contains the names of the lock holders. The view can be locked
	 * by one or more users or groups.
	 *
	 * <p>
	 * If the view is not locked, the vector contains one element whose value is an empty string ("").
	 * </p>
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	@Deprecated
	public Vector<String> getLockHolders();

	/**
	 * The name of this view. Use {@link #getAliases()} to get and set the aliases.
	 *
	 * Old-style aliases that are in the name field, separated from the name by vertical bars, are returned as part of the name.
	 *
	 * @return The name of this view.
	 */
	@Override
	public String getName();

	/**
	 * Given a document in a view, returns the document immediately following it. This method returns the next document in the view
	 * regardless of what type of document it is (document, response, or response-to-response). If you want the next sibling document in the
	 * view, use {@link #getNextSibling(lotus.domino.Document)}.
	 *
	 * <p>
	 * If the view is filtered by {@link #FTSearch}, this method returns the next document in the filtered view.
	 * </p>
	 *
	 * <p>
	 * The {@link IndexType} and {@link ViewEntryCollection} classes provide more efficient methods for navigating views and accessing
	 * entries.
	 * </p>
	 *
	 * <p>
	 * When processing documents in a loop, do not delete the document, or modify it in a way that causes it to disappear from the view or
	 * for its position to change. In that situation, this method may not return a useful result because the document that was the next
	 * document, is now at a different position in the view. The most common symptom of this problem is that documents are skipped when you
	 * loop through them.
	 * </p>
	 *
	 * <p>
	 * To address this issue, change your logic so that you call this method before making any change to the document. Or, use
	 * {@link View#setAutoUpdate(boolean)} with false to prevent re-indexing of the view while you're using it.
	 * </p>
	 *
	 * @param doc
	 *            Any document in the view. Cannot be null.
	 * @return The document in the view following the specified parameter. Returns null if there are no more documents in the view.
	 */
	@Override
	public Document getNextDocument(final lotus.domino.Document doc);

	/**
	 * Given a document in a view, returns the document immediately following it at the same level. If you send the method a main document,
	 * the next main document in the view is returned. If you send a response document, the next response document with the same parent is
	 * returned.
	 * <p>
	 * You can use this method to:
	 * <ul>
	 * <li>Move from one main document to the next, skipping any response documents in between</li>
	 * <li>Visit the response documents of a particular parent document (use {@link #getChild(lotus.domino.Document)} to find the first
	 * response)</li>
	 * <li>Visit the response-to-response documents of a particular parent document (use {@link #getChild(lotus.domino.Document)} to find
	 * the first response-to-response) If you filtered the view with {@link #FTSearch(String)}, this method returns the next document in the
	 * view, regardless of level.</li>
	 * </ul>
	 * </p>
	 * <h5>Siblings</h5>Two documents are siblings if:
	 * <ul>
	 * <li>They are both main documents, or
	 * <li>They are both responses or response-to-responses and they share the same parent document</li>
	 * </ul>
	 * <h5>The last sibling</h5> This method returns null when the parameter is:
	 * <ul>
	 * <li>The last main document in a view</li>
	 * <li>The last response (or response-to-response) to a particular parent</li>
	 * </ul>
	 *
	 * @param doc
	 *            Any document in the view. Cannot be null.
	 * @return The document following the parameter, at the same level in the view. Returns null if there are no more siblings.
	 */
	@Override
	public Document getNextSibling(final lotus.domino.Document doc);

	/**
	 * The Domino URL of a view when Notes protocols are in effect. If Notes protocols are not available, this property returns an empty
	 * string.
	 */
	@Override
	public String getNotesURL();

	/**
	 * Returns the document at a specified position in the top level of a view. This method accesses only top-level (main) documents in a
	 * view; response documents are excluded.
	 *
	 * <p>
	 * In a categorized view, this method returns only the first document in each main category (not each subcategory).
	 * </p>
	 *
	 * <p>
	 * If you do not have reader access to the specified document, this method returns the next document to which you have reader access. If
	 * you increment n, the increment is relative to the actual value of n, not the document that was accessed. So you will access the same
	 * document repeatedly until that document actually is the nth document.
	 * </p>
	 *
	 * <p>
	 * Using this method to iterate through a loop is strongly discouraged for performance reasons. See
	 * {@link #getNextDocument(lotus.domino.Document)}, {@link #getNextSibling(lotus.domino.Document)},
	 * {@link #getPrevDocument(lotus.domino.Document)}, and {@link #getPrevSibling(lotus.domino.Document)} for the preferred loop
	 * structures.
	 * </p>
	 *
	 * @param n
	 *            A number indicating the document to return. Use 1 to indicate the first document in the view, 2 to indicate the second
	 *            document, and so on.
	 * @return The document in the nth position in the view. Returns null if there is no document at the specified position.
	 */
	@Override
	public Document getNthDocument(final int n);

	/**
	 * The database to which a view belongs.
	 */
	@Override
	public Database getParent();

	/**
	 * Given a response document in a view, returns its parent document. The document returnedmay be a main document, a response, or a
	 * response-to-response.
	 *
	 * @param doc
	 *            Any document in the view. Cannot be null.
	 * @return The parent of the parameter(the document to which the parameter is a response). If you have filtered the view using
	 *         {@link #FTSearch(String)}, this method returns the previous document in the view, regardless of level. Returns null for a
	 *         main document.
	 */
	@Override
	public Document getParentDocument(final lotus.domino.Document doc);

	/**
	 * Given a document in a view, returns the document immediately preceding it. If the view is filtered by {@link #FTSearch(String)}, this
	 * method returns the next document in the filtered view.
	 *
	 * The {@link ViewNavigator} and {@link ViewEntryCollection} classes provide more efficient methods for navigating views and accessing
	 * entries.
	 *
	 * @param doc
	 *            Any document in the view. Cannot be null.
	 * @return The document preceding the parameter. Returns null if there is no preceding document.
	 */
	@Override
	public Document getPrevDocument(final lotus.domino.Document doc);

	/**
	 * Given a document in a view, returns the document immediately preceding it at the same level. If you use this method on a main
	 * document, the preceding main document in the view is returned. If you use it on a response document, the preceding response document
	 * with the same parent is returned. You can use getPrevSibling to move from one main document to the next, skipping any response
	 * documents in between.
	 *
	 * <p>
	 * If you filtered the view using {@link #FTSearch(String)}, this method returns the previous document in the view, regardless of level.
	 * </p>
	 * <h5>Siblings</h5> Two documents are siblings if:
	 * <ul>
	 * <li>They are both main documents, or</li>
	 * <li>They are both responses or response-to-responses and share the same parent document.</li>
	 * </ul>
	 * <h5>The first sibling</h5> This method returns null when the parameter is:
	 * <ul>
	 * <li>The first main document in a view</li>
	 * <li>The first response (or response-to-response) to a particular parent</li>
	 * </ul>
	 *
	 * @param doc
	 *            Any document in the view. Cannot be null.
	 * @return The document preceding the parameter, at the same level. Returns null if there is no previous sibling in the view.
	 */
	@Override
	public Document getPrevSibling(final lotus.domino.Document doc);

	/**
	 * The contents of the $Readers field associated with the view.
	 *
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getReaders();

	/**
	 * The number of lines in each row of a view.
	 */
	@Override
	public int getRowLines();

	/**
	 * The selection formula of a view.
	 */
	@Override
	public String getSelectionFormula();

	/**
	 * The selection query of a DB2 query view.
	 */
	@Override
	public String getSelectionQuery();

	/**
	 * The spacing between rows of a view.
	 *
	 * @return One of the values:
	 *         <ul>
	 *         <li>View.SPACING_DOUBLE</li>
	 *         <li>View.SPACING_ONE_POINT_25</li>
	 *         <li>View.SPACING_ONE_POINT_50</li>
	 *         <li>View.SPACING_ONE_POINT_75</li>
	 *         <li>View.SPACING_SINGLE</li>
	 *         </ul>
	 */
	@Override
	public int getSpacing();

	/**
	 * The number of top-level entries in a view.
	 * <p>
	 * If the view is categorized, this count is the number of main categories.If the view has totals, this count includes the grand total.
	 * </p>
	 * <p>
	 * Where the count may exceed 32767, read this property into a Long and if it is negative add 65536. However, unless you are sure the
	 * number of top-level entries will always remain 65535 or less, use of this property is not advised. It does not throw an error if the
	 * number of entries exceeds 65535, so there is no way to tell if the result was correct.
	 * </p>
	 */
	@Override
	public int getTopLevelEntryCount();

	/**
	 * The universal ID of a view, which is a 32-character hexadecimal value that uniquely identifies a view across all replicas of a
	 * database.
	 */
	@Override
	public String getUniversalID();

	/**
	 * Returns the Domino URL for its parent object
	 */
	@Override
	public String getURL();

	/**
	 * The name of the view whose design a view inherits. If a view is a copy of another view, this property is the name of the base view.
	 * Otherwise, this property is an empty string.
	 *
	 * <p>
	 * If the design is customized after the view is copied, this property is an empty string.
	 * </p>
	 */
	@Override
	public String getViewInheritedName();

	/**
	 * Indicates whether a view is automatically refreshed when a navigation method touches an update (addition, deletion, or change) to the
	 * database that occurred after view creation or the last refresh. It is best to avoid automatically updating the view by explicitly
	 * setting this property to false especially if the view is a base for navigators or entry collections. Automatic updates degrade
	 * performance and may invalidate entries in child objects ("Entry not found in index").
	 *
	 * <p>
	 * If this property is false, you must call refresh to navigate to an update.
	 * </p>
	 *
	 * <p>
	 * Even if this property is true, you must call refresh if you are not navigating to an update and you want the view refreshed, for
	 * example, to get the top level entry count.
	 * </p>
	 *
	 * @return true if the view is automatically updated
	 */
	@Override
	public boolean isAutoUpdate();

	/**
	 * Indicates whether this view is a calendar view.
	 *
	 * @return true, if the view is a calendar view.
	 */
	@Override
	public boolean isCalendar();

	/**
	 * Indicates whether this view is categorized.
	 *
	 * @return true when the view is categorized
	 */
	@Override
	public boolean isCategorized();

	/**
	 * Indicates whether a view is enabled for conflict checking. This applies to calendar views only.
	 *
	 * @return true if the view is enabled for conflict checking.
	 */
	@Override
	public boolean isConflict();

	/**
	 * Indicates whether a view is the default view of the database.
	 */
	@Override
	public boolean isDefaultView();

	@Override
	public boolean isEnableNoteIDsForCategories();

	/**
	 * Indicates whether this object represents a folder.
	 *
	 * @return true, if this object is a folder, false if it is a view.
	 */
	@Override
	public boolean isFolder();

	/**
	 * Indicates whether a view shows response documents in a hierarchy.
	 */
	@Override
	public boolean isHierarchical();

	/**
	 * Indicates whether a view is modified.
	 * <p>
	 * This property can be used to determine if the current view snapshot on the Notes client corresponds to the real state of the view on
	 * the server. The view on the server can change as a result of multiple users accessing it and this property can be used to determine
	 * if the view needs refreshing.
	 * </p>
	 * <p>
	 * This property will always return true for programs accessing views remotely, and false when accessing views via agents or standalone
	 * applications.
	 * </p>
	 */
	@Override
	public boolean isModified();

	/**
	 * Indicates whether a view is private or shared on first use.
	 */
	@Override
	public boolean isPrivate();

	/**
	 * Indicates whether a design refresh or replace can overwrite a view. This property is the same as "Prohibit design refresh or replace
	 * to modify" under the Design tab in the View Design Properties box.
	 */
	@Override
	public boolean isProhibitDesignRefresh();

	/**
	 * Indicates whether $Readers items are protected from being overwritten by replication.
	 */
	@Override
	public boolean isProtectReaders();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#isQueryView()
	 */
	@Override
	public boolean isQueryView();

	/**
	 * Locks a view.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:<br/>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Throws an exception if the administration server is not available</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * </li>If the view is not locked, this method places the lock and returns true.</li>
	 * <li>If the view is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the view is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * If the view is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @return true if the lock is placed
	 */
	@Override
	public boolean lock();

	/**
	 * Locks a view.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:<br/>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Places a provisional lock if the administration server is not available and the parameter <code>provisionalOk</code>is true.</li>
	 * <li>Throws an exception if the administration server is not available</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * </li>If the view is not locked, this method places the lock and returns true.</li>
	 * <li>If the view is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the view is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * If the view is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param provisionalOk
	 *            true to permit the placement of a provisional lock, false to not permit a provisional lock
	 * @return true if the lock is placed
	 */
	@Override
	public boolean lock(final boolean provisionalOk);

	/**
	 * Locks a view.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:<br/>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Throws an exception if the administration server is not available</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * </li>If the view is not locked, this method places the lock and returns true.</li>
	 * <li>If the view is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the view is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * If the view is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The name of the lock holder (a user or a group). The empty string ("") is not permitted.
	 * @return true if the lock is placed
	 */
	@Override
	public boolean lock(final String name);

	/**
	 * Locks a view.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:<br/>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Places a provisional lock if the administration server is not available and the parameter <code>provisionalOk</code>is true.</li>
	 * <li>Throws an exception if the administration server is not available</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * </li>If the view is not locked, this method places the lock and returns true.</li>
	 * <li>If the view is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the view is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * If the view is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The name of the lock holder (a user or a group). The empty string ("") is not permitted.
	 * @param provisionalOk
	 *            true to permit the placement of a provisional lock, false to not permit a provisional lock
	 * @return true if the lock is placed
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk);

	/**
	 * Locks a view.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:<br/>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Throws an exception if the administration server is not available</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * </li>If the view is not locked, this method places the lock and returns true.</li>
	 * <li>If the view is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the view is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * If the view is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param names
	 *            names of the lock holders (a user or a group). The empty string ("") is not permitted.
	 * @return true if the lock is placed
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(final Vector names);

	/**
	 * Locks a view.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:<br/>
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Places a provisional lock if the administration server is not available and the parameter <code>provisionalOk</code>is true.</li>
	 * <li>Throws an exception if the administration server is not available</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * </li>If the view is not locked, this method places the lock and returns true.</li>
	 * <li>If the view is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the view is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * If the view is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param names
	 *            names of the lock holders (a user or a group). The empty string ("") is not permitted.
	 * @param provisionalOk
	 *            true to permit the placement of a provisional lock, false to not permit a provisional lock
	 * @return true if the lock is placed
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(final Vector names, final boolean provisionalOk);

	/**
	 * Locks a view provisionally.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * </li>If the view is not locked, this method places the lock and returns true.</li>
	 * <li>If the view is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the view is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * If the view is modified by another user before the lock can be placed, this method throws an exception.
	 *
	 * @return true if the lock is placed
	 */
	@Override
	public boolean lockProvisional();

	/**
	 * Locks a view provisionally.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * </li>If the view is not locked, this method places the lock and returns true.</li>
	 * <li>If the view is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the view is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * If the view is modified by another user before the lock can be placed, this method throws an exception.
	 *
	 * @param name
	 *            the name of the lock holder (a user or a group). The empty string ("") is not permitted.
	 * @return true if the lock is placed
	 */
	@Override
	public boolean lockProvisional(final String name);

	/**
	 * Locks a view provisionally.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * </li>If the view is not locked, this method places the lock and returns true.</li>
	 * <li>If the view is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the view is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * If the view is modified by another user before the lock can be placed, this method throws an exception.
	 *
	 * @param names
	 *            names of the lock holders (a user or a group). The empty string ("") is not permitted.
	 * @return true if the lock is placed
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lockProvisional(final Vector names);

	/**
	 * Marks all documents in the view as read on behalf of the current user ID. If the database does not track unread marks, all documents
	 * are considered read, and this method has no effect.
	 *
	 */
	@Override
	public void markAllRead();

	/**
	 * Marks all documents in the view as read on behalf of the given name. If the database does not track unread marks, all documents are
	 * considered read, and this method has no effect.
	 */
	@Override
	public void markAllRead(final String userName);

	/**
	 * Marks all documents in the view as unread on behalf of the current user ID.
	 */
	@Override
	public void markAllUnread();

	/**
	 * Marks all documents in the view as unread on behalf of the given user name.
	 */
	@Override
	public void markAllUnread(final String userName);

	/**
	 * Updates view contents to reflect any updates to the database since the View object was created, or since the last refresh. By
	 * default, refresh is automatic for local operations when view navigation touches an update.
	 *
	 * After a refresh, existing navigators and entries based on this object may contain invalid information. The typical error message is
	 * "Entry not found in index."
	 *
	 * @see #isAutoUpdate()
	 */
	@Override
	public void refresh();

	/**
	 * Permanently removes a view from a database. The removed view may still appear in the user interface until the user closes and opens
	 * the database.
	 *
	 * A subsequent call to a method of the view, or a method of a navigator based on the view, throws an exception.
	 */
	@Override
	public void remove();

	/**
	 * Removes the last column in this view.
	 */
	@Override
	public void removeColumn();

	/**
	 * Removes the given column from this view.
	 *
	 * @param column
	 *            The position of the column to be removed.
	 */
	@Override
	public void removeColumn(final int column);

	/**
	 * Removes the given column from this view.
	 *
	 * @param column
	 *            The title of the column to be removed.
	 */
	@Override
	public void removeColumn(final String column);

	/**
	 * Resorts this view on the first column ascending.
	 */
	@Override
	public void resortView();

	/**
	 * Resorts this view on the given column
	 *
	 * @param column
	 *            The name of the column on which to sort.
	 */
	@Override
	public void resortView(final String column);

	/**
	 * Resorts this view on the given column
	 *
	 * @param column
	 *            The name of the column on which to sort.
	 * @param ascending
	 *            True sorts columnName ascending. False sorts columnName descending.
	 */
	@Override
	public void resortView(final String column, final boolean ascending);

	/**
	 * Sets the aliases of this view. These aliases replace any existing aliases.
	 *
	 * @param alias
	 *            new alias, use the vertical bar to delineate multiple aliases. For example, "Alias1|Alias2" specifies two aliases.
	 */
	@Override
	public void setAliases(final String alias);

	/**
	 * Sets the aliases of this view. These aliases replace any existing aliases.
	 *
	 * @param aliases
	 *            new aliases
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setAliases(final Vector aliases);

	/**
	 * Sets whether a view is automatically refreshed when a navigation method touches an update (addition, deletion, or change) to the
	 * database that occurred after view creation or the last refresh.
	 *
	 * @param flag
	 *            true if the view is automatically updated
	 * @see #isAutoUpdate()
	 */
	@Override
	public void setAutoUpdate(final boolean flag);

	/**
	 * Sets the background color of a view.
	 *
	 * @param color
	 *            A Domino color index in the range 0-240
	 */
	@Override
	public void setBackgroundColor(final int color);

	/**
	 * Sets the view as the default view of the database.
	 *
	 * @param flag
	 *            MUST be true. To change the value of the current default view's IsDefaultView property to False, set another view's
	 *            IsDefaultView property to True, which will automatically change the current default view's value to False.
	 */
	@Override
	public void setDefaultView(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setEnableNoteIDsForCategories(boolean)
	 */
	@Override
	public void setEnableNoteIDsForCategories(final boolean flag);

	/**
	 * The name of a view. Use {@link #setAliases(String)} to set the aliases.
	 *
	 * Old-style aliases that are in the name field, separated from the name by vertical bars, are returned as part of the name.
	 */
	@Override
	public void setName(final String name);

	/**
	 * Sets whether a design refresh or replace can overwrite a view. This property is the same as "Prohibit design refresh or replace to
	 * modify" under the Design tab in the View Design Properties box.
	 *
	 * @param flag
	 *            true to indicate that the view cannot be refreshed
	 */
	@Override
	public void setProhibitDesignRefresh(final boolean flag);

	/**
	 * Sets the $Readers items as protected from being overwritten by replication.
	 *
	 * @param flag
	 *            true to protect $Readers items
	 */
	@Override
	public void setProtectReaders(final boolean flag);

	/**
	 * Sets $Readers field associated with the view. Setting this property replaces prior values.
	 *
	 * Set the property to null to remove all reader restrictions.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setReaders(final Vector readers);

	/**
	 * Sets the selection formula of a view.
	 * <p>
	 * This property must be a valid Domino formula that evaluates to True (includes the note) or False (excludes the note) for each note.
	 * </p>
	 *
	 * <p>
	 * This property is initially an empty string, which is equivalent to selecting all notes.
	 * </p>
	 *
	 * <p>
	 * This property is not a stand-alone specification. It intersects the other selection criteria.
	 * </p>
	 *
	 * <p>
	 * This method is sometimes incorrectly used to make a view display a new selection of documents for an end user.
	 * </p>
	 *
	 * <p>
	 * This method makes a modification to the design of the view and requires Designer access. If not, the view is private and the user is
	 * the owner of it.
	 * </p>
	 *
	 * <p>
	 * There are problems with using this method to make a view display a new selection of documents for an end user
	 * </p>
	 *
	 * <p>
	 * Do not give end-users Designer access to an application.
	 * </p>
	 *
	 * <p>
	 * If it is a shared view, users will interfere with each other's searches.
	 * </p>
	 *
	 * <p>
	 * The Notes client caches design information, and there's no way to tell it to update its cache (except for outlines). Exiting and
	 * re-entering the application usually works, but it's hard to programmatically ensure the user exited the application entirely.
	 * </p>
	 *
	 * <p>
	 * It is an inefficient way to search unless the result set is very large. Use a full-text search instead.
	 * </p>
	 * </p>
	 *
	 * @param formula
	 *            new selection formula for this view
	 */
	@Override
	public void setSelectionFormula(final String formula);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setSelectionQuery(java.lang.String)
	 */
	@Override
	public void setSelectionQuery(final String query);

	/**
	 * Sets the spacing between rows of a view.
	 *
	 * @param spacing
	 *            One of the values:
	 *            <ul>
	 *            <li>View.SPACING_DOUBLE</li>
	 *            <li>View.SPACING_ONE_POINT_25</li>
	 *            <li>View.SPACING_ONE_POINT_50</li>
	 *            <li>View.SPACING_ONE_POINT_75</li>
	 *            <li>View.SPACING_SINGLE</li>
	 *            </ul>
	 */
	@Override
	public void setSpacing(final int spacing);

	/**
	 * Unlocks a view. {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * <p>
	 * This method throws an exception if the current user is not one of the lock holders and does not have lock breaking authority.
	 * </p>
	 */
	@Override
	public void unlock();

	/* (non-Javadoc)
	 * @see lotus.domino.View#createViewNavFromKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ViewNavigator createViewNavFromKey(Vector arg0, boolean arg1);
}
