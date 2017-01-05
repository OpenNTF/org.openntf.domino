/*
 * Copyright 2013
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

import java.io.Externalizable;
import java.util.Map;
import java.util.Vector;

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.Design;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;

// TODO: Auto-generated Javadoc
/**
 * The Interface View.
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
	public ViewColumn copyColumn(final int sourceColumn);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#copyColumn(int, int)
	 */
	@Override
	public ViewColumn copyColumn(final int sourceColumn, final int destinationIndex);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#copyColumn(java.lang.String)
	 */
	@Override
	public ViewColumn copyColumn(final String sourceColumn);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#copyColumn(java.lang.String, int)
	 */
	@Override
	public ViewColumn copyColumn(final String sourceColumn, final int destinationIndex);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#copyColumn(lotus.domino.ViewColumn)
	 */
	@Override
	public ViewColumn copyColumn(final lotus.domino.ViewColumn sourceColumn);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#copyColumn(lotus.domino.ViewColumn, int)
	 */
	@Override
	public ViewColumn copyColumn(final lotus.domino.ViewColumn sourceColumn, final int destinationIndex);

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
	public ViewColumn createColumn(final int position);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createColumn(int, java.lang.String)
	 */
	@Override
	public ViewColumn createColumn(final int position, final String columnTitle);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createColumn(int, java.lang.String, java.lang.String)
	 */
	@Override
	public ViewColumn createColumn(final int position, final String columnTitle, final String formula);

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
	public ViewNavigator createViewNav(final int cacheSize);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavFrom(java.lang.Object)
	 */
	@Override
	public ViewNavigator createViewNavFrom(final Object entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavFrom(java.lang.Object, int)
	 */
	@Override
	public ViewNavigator createViewNavFrom(final Object entry, final int cacheSize);

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
	public ViewNavigator createViewNavFromAllUnread(final String userName);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavFromCategory(java.lang.String)
	 */
	@Override
	public ViewNavigator createViewNavFromCategory(final String categoryName);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavFromCategory(java.lang.String, int)
	 */
	@Override
	public ViewNavigator createViewNavFromCategory(final String categoryName, final int cacheSize);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavFromChildren(java.lang.Object)
	 */
	@Override
	public ViewNavigator createViewNavFromChildren(final Object entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavFromChildren(java.lang.Object, int)
	 */
	@Override
	public ViewNavigator createViewNavFromChildren(final Object entry, final int cacheSize);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavFromDescendants(java.lang.Object)
	 */
	@Override
	public ViewNavigator createViewNavFromDescendants(final Object entry);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavFromDescendants(java.lang.Object, int)
	 */
	@Override
	public ViewNavigator createViewNavFromDescendants(final Object entry, final int cacheSize);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavMaxLevel(int)
	 */
	@Override
	public ViewNavigator createViewNavMaxLevel(final int level);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#createViewNavMaxLevel(int, int)
	 */
	@Override
	public ViewNavigator createViewNavMaxLevel(final int level, final int cacheSize);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearch(java.lang.String)
	 */
	@Override
	public int FTSearch(final String query);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearch(java.lang.String, int)
	 */
	@Override
	public int FTSearch(final String query, final int maxDocs);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String)
	 */
	@Override
	public int FTSearchSorted(final String query);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int, int)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final int column);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int, int, boolean, boolean, boolean, boolean)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final int column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int, java.lang.String)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final String column);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.lang.String, int, java.lang.String, boolean, boolean, boolean, boolean)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final String column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int, int)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final int column);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int, int, boolean, boolean, boolean, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final int column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int, java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final String column);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#FTSearchSorted(java.util.Vector, int, java.lang.String, boolean, boolean, boolean, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public int FTSearchSorted(final Vector query, final int maxDocs, final String column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy);

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
	public DocumentCollection getAllDocumentsByKey(final Object key);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getAllDocumentsByKey(java.lang.Object, boolean)
	 */
	@Override
	public DocumentCollection getAllDocumentsByKey(final Object key, final boolean exact);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getAllDocumentsByKey(java.util.Vector)
	 */
	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getAllDocumentsByKey(Object)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(final Vector keys);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getAllDocumentsByKey(java.util.Vector, boolean)
	 */
	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getAllDocumentsByKey(Object, boolean)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public DocumentCollection getAllDocumentsByKey(final Vector keys, final boolean exact);

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
	public ViewEntryCollection getAllEntriesByKey(final Object key);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getAllEntriesByKey(java.lang.Object, boolean)
	 */
	@Override
	public ViewEntryCollection getAllEntriesByKey(final Object key, final boolean exact);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getAllEntriesByKey(java.util.Vector)
	 */
	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getAllEntriesByKey(Object)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(final Vector keys);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getAllEntriesByKey(java.util.Vector, boolean)
	 */
	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getAllEntriesByKey(Object, boolean)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public ViewEntryCollection getAllEntriesByKey(final Vector keys, final boolean exact);

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
	public ViewEntryCollection getAllReadEntries(final String userName);

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
	public ViewEntryCollection getAllUnreadEntries(final String userName);

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
	public Document getChild(final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getColumn(int)
	 */
	@Override
	public ViewColumn getColumn(final int columnNumber);

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
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector<Object> getColumnValues(final int column);

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
	/**
	 * @deprecated Use {@link #getFirstDocumentByKey(Object)} instead.
	 */
	@Override
	@Deprecated
	public Document getDocumentByKey(final Object key);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getDocumentByKey(java.lang.Object, boolean)
	 */
	/**
	 * @deprecated Use {@link #getFirstDocumentByKey(Object, boolean)} instead.
	 */
	@Override
	@Deprecated
	public Document getDocumentByKey(final Object key, final boolean exact);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getDocumentByKey(java.util.Vector)
	 */
	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getFirstDocumentByKey(Object)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy(Legacy.GENERICS_WARNING)
	public Document getDocumentByKey(final Vector keys);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getDocumentByKey(java.util.Vector, boolean)
	 */
	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getFirstDocumentByKey(Object, boolean)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy(Legacy.GENERICS_WARNING)
	public Document getDocumentByKey(final Vector keys, final boolean exact);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getEntryByKey(java.lang.Object)
	 */
	/**
	 * @deprecated Use {@link #getFirstEntryByKey(Object)} instead.
	 */
	@Override
	@Deprecated
	public ViewEntry getEntryByKey(final Object key);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getEntryByKey(java.lang.Object, boolean)
	 */
	/**
	 * @deprecated Use {@link #getFirstEntryByKey(Object, boolean)} instead.
	 */
	@Override
	@Deprecated
	public ViewEntry getEntryByKey(final Object key, final boolean exact);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getEntryByKey(java.util.Vector)
	 */
	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getFirstEntryByKey(Object)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy(Legacy.GENERICS_WARNING)
	public ViewEntry getEntryByKey(final Vector keys);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getEntryByKey(java.util.Vector, boolean)
	 */
	/**
	 * @deprecated Pass generic {@link java.util.Collection}s to {@link #getFirstEntryByKey(Object, boolean)} instead.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy(Legacy.GENERICS_WARNING)
	public ViewEntry getEntryByKey(final Vector keys, final boolean exact);

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
	public Document getNextDocument(final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getNextSibling(lotus.domino.Document)
	 */
	@Override
	public Document getNextSibling(final lotus.domino.Document doc);

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
	public Document getNthDocument(final int n);

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
	public Document getParentDocument(final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getPrevDocument(lotus.domino.Document)
	 */
	@Override
	public Document getPrevDocument(final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#getPrevSibling(lotus.domino.Document)
	 */
	@Override
	public Document getPrevSibling(final lotus.domino.Document doc);

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
	public boolean lock(final boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#lock(java.lang.String)
	 */
	@Override
	public boolean lock(final String name);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#lock(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(final Vector names);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lock(final Vector names, final boolean provisionalOk);

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
	public boolean lockProvisional(final String name);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public boolean lockProvisional(final Vector names);

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
	public void markAllRead(final String userName);

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
	public void markAllUnread(final String userName);

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
	public void removeColumn(final int column);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#removeColumn(java.lang.String)
	 */
	@Override
	public void removeColumn(final String column);

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
	public void resortView(final String column);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#resortView(java.lang.String, boolean)
	 */
	@Override
	public void resortView(final String column, final boolean ascending);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setAliases(java.lang.String)
	 */
	@Override
	public void setAliases(final String alias);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setAliases(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setAliases(final Vector aliases);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setAutoUpdate(boolean)
	 */
	@Override
	public void setAutoUpdate(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setBackgroundColor(int)
	 */
	@Override
	public void setBackgroundColor(final int color);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setDefaultView(boolean)
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

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setProhibitDesignRefresh(boolean)
	 */
	@Override
	public void setProhibitDesignRefresh(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setProtectReaders(boolean)
	 */
	@Override
	public void setProtectReaders(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setReaders(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public void setReaders(final Vector readers);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setSelectionFormula(java.lang.String)
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

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#setSpacing(int)
	 */
	@Override
	public void setSpacing(final int spacing);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.View#unlock()
	 */
	@Override
	public void unlock();

	/* (non-Javadoc)
	 * @see lotus.domino.View#createViewNavFromKey(java.util.Vector, boolean)
	 */
	@Override
	public ViewNavigator createViewNavFromKey(Vector arg0, boolean arg1);
}
