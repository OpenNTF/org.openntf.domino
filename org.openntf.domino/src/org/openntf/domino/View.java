/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.Design;

/**
 * The Interface View.
 */
public interface View extends lotus.domino.View, org.openntf.domino.ext.View, Base<lotus.domino.View>, Design, DatabaseDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#clear()
	 */
	@Override
	public void clear();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#copyColumn(int)
	 */
	@Override
	public ViewColumn copyColumn(int sourceColumn);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#copyColumn(int, int)
	 */
	@Override
	public ViewColumn copyColumn(int sourceColumn, int destinationIndex);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#copyColumn(java.lang.String)
	 */
	@Override
	public ViewColumn copyColumn(String sourceColumn);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#copyColumn(java.lang.String, int)
	 */
	@Override
	public ViewColumn copyColumn(String sourceColumn, int destinationIndex);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#copyColumn(lotus.domino.ViewColumn)
	 */
	@Override
	public ViewColumn copyColumn(lotus.domino.ViewColumn sourceColumn);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#copyColumn(lotus.domino.ViewColumn, int)
	 */
	@Override
	public ViewColumn copyColumn(lotus.domino.ViewColumn sourceColumn, int destinationIndex);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createColumn()
	 */
	@Override
	public ViewColumn createColumn();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createColumn(int)
	 */
	@Override
	public ViewColumn createColumn(int position);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createColumn(int, java.lang.String)
	 */
	@Override
	public ViewColumn createColumn(int position, String columnTitle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createColumn(int, java.lang.String, java.lang.String)
	 */
	@Override
	public ViewColumn createColumn(int position, String columnTitle, String formula);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewEntryCollection()
	 */
	@Override
	public ViewEntryCollection createViewEntryCollection();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNav()
	 */
	@Override
	public ViewNavigator createViewNav();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNav(int)
	 */
	@Override
	public ViewNavigator createViewNav(int cacheSize);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFrom(java.lang.Object)
	 */
	@Override
	public ViewNavigator createViewNavFrom(Object entry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFrom(java.lang.Object, int)
	 */
	@Override
	public ViewNavigator createViewNavFrom(Object entry, int cacheSize);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFromAllUnread()
	 */
	@Override
	public ViewNavigator createViewNavFromAllUnread();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFromAllUnread(java.lang.String)
	 */
	@Override
	public ViewNavigator createViewNavFromAllUnread(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFromCategory(java.lang.String)
	 */
	@Override
	public ViewNavigator createViewNavFromCategory(String categoryName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFromCategory(java.lang.String, int)
	 */
	@Override
	public ViewNavigator createViewNavFromCategory(String categoryName, int cacheSize);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFromChildren(java.lang.Object)
	 */
	@Override
	public ViewNavigator createViewNavFromChildren(Object entry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFromChildren(java.lang.Object, int)
	 */
	@Override
	public ViewNavigator createViewNavFromChildren(Object entry, int cacheSize);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFromDescendants(java.lang.Object)
	 */
	@Override
	public ViewNavigator createViewNavFromDescendants(Object entry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavFromDescendants(java.lang.Object, int)
	 */
	@Override
	public ViewNavigator createViewNavFromDescendants(Object entry, int cacheSize);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavMaxLevel(int)
	 */
	@Override
	public ViewNavigator createViewNavMaxLevel(int level);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#createViewNavMaxLevel(int, int)
	 */
	@Override
	public ViewNavigator createViewNavMaxLevel(int level, int cacheSize);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearch(java.lang.String)
	 */
	@Override
	public int FTSearch(String query);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearch(java.lang.String, int)
	 */
	@Override
	public int FTSearch(String query, int maxDocs);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String)
	 */
	@Override
	public int FTSearchSorted(String query);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int)
	 */
	@Override
	public int FTSearchSorted(String query, int maxDocs);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int, int)
	 */
	@Override
	public int FTSearchSorted(String query, int maxDocs, int column);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int, int, boolean, boolean, boolean, boolean)
	 */
	@Override
	public int FTSearchSorted(String query, int maxDocs, int column, boolean ascending, boolean exact, boolean variants, boolean fuzzy);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int, java.lang.String)
	 */
	@Override
	public int FTSearchSorted(String query, int maxDocs, String column);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int, java.lang.String, boolean, boolean, boolean, boolean)
	 */
	@Override
	public int FTSearchSorted(String query, int maxDocs, String column, boolean ascending, boolean exact, boolean variants, boolean fuzzy);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs, int column);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int, int, boolean, boolean, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs, int column, boolean ascending, boolean exact, boolean variants, boolean fuzzy);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs, String column);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int, java.lang.String, boolean, boolean, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(Vector query, int maxDocs, String column, boolean ascending, boolean exact, boolean variants, boolean fuzzy);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAliases()
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getAliases();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllDocumentsByKey(java.lang.Object)
	 */
	@Override
	public DocumentCollection getAllDocumentsByKey(Object key);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllDocumentsByKey(java.lang.Object, boolean)
	 */
	@Override
	public DocumentCollection getAllDocumentsByKey(Object key, boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllDocumentsByKey(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(Vector keys);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllDocumentsByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(Vector keys, boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllEntries()
	 */
	@Override
	public ViewEntryCollection getAllEntries();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllEntriesByKey(java.lang.Object)
	 */
	@Override
	public ViewEntryCollection getAllEntriesByKey(Object key);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllEntriesByKey(java.lang.Object, boolean)
	 */
	@Override
	public ViewEntryCollection getAllEntriesByKey(Object key, boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllEntriesByKey(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(Vector keys);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllEntriesByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(Vector keys, boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllReadEntries()
	 */
	@Override
	public ViewEntryCollection getAllReadEntries();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllReadEntries(java.lang.String)
	 */
	@Override
	public ViewEntryCollection getAllReadEntries(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllUnreadEntries()
	 */
	@Override
	public ViewEntryCollection getAllUnreadEntries();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getAllUnreadEntries(java.lang.String)
	 */
	@Override
	public ViewEntryCollection getAllUnreadEntries(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getBackgroundColor()
	 */
	@Override
	public int getBackgroundColor();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getChild(lotus.domino.Document)
	 */
	@Override
	public Document getChild(lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getColumn(int)
	 */
	@Override
	public ViewColumn getColumn(int columnNumber);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getColumnCount()
	 */
	@Override
	public int getColumnCount();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getColumnNames()
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getColumnNames();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getColumns()
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
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector<Object> getColumnValues(int column);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getCreated()
	 */
	@Override
	public DateTime getCreated();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getDocumentByKey(java.lang.Object)
	 */
	@Override
	public Document getDocumentByKey(Object key);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getDocumentByKey(java.lang.Object, boolean)
	 */
	@Override
	public Document getDocumentByKey(Object key, boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getDocumentByKey(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Document getDocumentByKey(Vector keys);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getDocumentByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Document getDocumentByKey(Vector keys, boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getEntryByKey(java.lang.Object)
	 */
	@Override
	public ViewEntry getEntryByKey(Object key);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getEntryByKey(java.lang.Object, boolean)
	 */
	@Override
	public ViewEntry getEntryByKey(Object key, boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getEntryByKey(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntry getEntryByKey(Vector keys);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getEntryByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntry getEntryByKey(Vector keys, boolean exact);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getEntryCount()
	 */
	@Override
	public int getEntryCount();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getFirstDocument()
	 */
	@Override
	public Document getFirstDocument();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getHeaderLines()
	 */
	@Override
	public int getHeaderLines();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getHttpURL()
	 */
	@Override
	public String getHttpURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getLastDocument()
	 */
	@Override
	public Document getLastDocument();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getLastModified()
	 */
	@Override
	public DateTime getLastModified();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getLockHolders()
	 */
	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	@Deprecated
	public Vector<String> getLockHolders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getName()
	 */
	@Override
	public String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getNextDocument(lotus.domino.Document)
	 */
	@Override
	public Document getNextDocument(lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getNextSibling(lotus.domino.Document)
	 */
	@Override
	public Document getNextSibling(lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getNotesURL()
	 */
	@Override
	public String getNotesURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getNthDocument(int)
	 */
	@Override
	public Document getNthDocument(int n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getParent()
	 */
	@Override
	public Database getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getParentDocument(lotus.domino.Document)
	 */
	@Override
	public Document getParentDocument(lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getPrevDocument(lotus.domino.Document)
	 */
	@Override
	public Document getPrevDocument(lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getPrevSibling(lotus.domino.Document)
	 */
	@Override
	public Document getPrevSibling(lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getReaders()
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<String> getReaders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getRowLines()
	 */
	@Override
	public int getRowLines();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getSelectionFormula()
	 */
	@Override
	public String getSelectionFormula();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getSelectionQuery()
	 */
	@Override
	public String getSelectionQuery();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getSpacing()
	 */
	@Override
	public int getSpacing();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getTopLevelEntryCount()
	 */
	@Override
	public int getTopLevelEntryCount();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getUniversalID()
	 */
	@Override
	public String getUniversalID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getURL()
	 */
	@Override
	public String getURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#getViewInheritedName()
	 */
	@Override
	public String getViewInheritedName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isAutoUpdate()
	 */
	@Override
	public boolean isAutoUpdate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isCalendar()
	 */
	@Override
	public boolean isCalendar();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isCategorized()
	 */
	@Override
	public boolean isCategorized();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isConflict()
	 */
	@Override
	public boolean isConflict();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isDefaultView()
	 */
	@Override
	public boolean isDefaultView();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isEnableNoteIDsForCategories()
	 */
	@Override
	public boolean isEnableNoteIDsForCategories();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isFolder()
	 */
	@Override
	public boolean isFolder();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isHierarchical()
	 */
	@Override
	public boolean isHierarchical();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isModified()
	 */
	@Override
	public boolean isModified();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isPrivate()
	 */
	@Override
	public boolean isPrivate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isProhibitDesignRefresh()
	 */
	@Override
	public boolean isProhibitDesignRefresh();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#isProtectReaders()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#lock()
	 */
	@Override
	public boolean lock();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#lock(boolean)
	 */
	@Override
	public boolean lock(boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#lock(java.lang.String)
	 */
	@Override
	public boolean lock(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(String name, boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#lock(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(Vector names, boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#lockProvisional()
	 */
	@Override
	public boolean lockProvisional();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#lockProvisional(java.lang.String)
	 */
	@Override
	public boolean lockProvisional(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lockProvisional(Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#markAllRead()
	 */
	@Override
	public void markAllRead();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#markAllRead(java.lang.String)
	 */
	@Override
	public void markAllRead(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#markAllUnread()
	 */
	@Override
	public void markAllUnread();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#markAllUnread(java.lang.String)
	 */
	@Override
	public void markAllUnread(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#refresh()
	 */
	@Override
	public void refresh();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#removeColumn()
	 */
	@Override
	public void removeColumn();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#removeColumn(int)
	 */
	@Override
	public void removeColumn(int column);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#removeColumn(java.lang.String)
	 */
	@Override
	public void removeColumn(String column);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#resortView()
	 */
	@Override
	public void resortView();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#resortView(java.lang.String)
	 */
	@Override
	public void resortView(String column);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#resortView(java.lang.String, boolean)
	 */
	@Override
	public void resortView(String column, boolean ascending);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setAliases(java.lang.String)
	 */
	@Override
	public void setAliases(String alias);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setAliases(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setAliases(Vector aliases);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setAutoUpdate(boolean)
	 */
	@Override
	public void setAutoUpdate(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setBackgroundColor(int)
	 */
	@Override
	public void setBackgroundColor(int color);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setDefaultView(boolean)
	 */
	@Override
	public void setDefaultView(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setEnableNoteIDsForCategories(boolean)
	 */
	@Override
	public void setEnableNoteIDsForCategories(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setName(java.lang.String)
	 */
	@Override
	public void setName(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setProhibitDesignRefresh(boolean)
	 */
	@Override
	public void setProhibitDesignRefresh(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setProtectReaders(boolean)
	 */
	@Override
	public void setProtectReaders(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setReaders(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setReaders(Vector readers);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setSelectionFormula(java.lang.String)
	 */
	@Override
	public void setSelectionFormula(String formula);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setSelectionQuery(java.lang.String)
	 */
	@Override
	public void setSelectionQuery(String query);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#setSpacing(int)
	 */
	@Override
	public void setSpacing(int spacing);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.View#unlock()
	 */
	@Override
	public void unlock();
}
