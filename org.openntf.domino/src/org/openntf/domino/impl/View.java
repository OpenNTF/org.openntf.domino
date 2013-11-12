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
package org.openntf.domino.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.exceptions.Notes9onlyException;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class View.
 */
@SuppressWarnings("deprecation")
public class View extends Base<org.openntf.domino.View, lotus.domino.View> implements org.openntf.domino.View {

	private List<DominoColumnInfo> columnInfo_;

	/**
	 * Instantiates a new view.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public View(final lotus.domino.View delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getParentDatabase(parent));
		initialize(delegate);
	}

	private String notesUrl_;
	private String name_;

	private void initialize(final lotus.domino.View delegate) {
		try {
			notesUrl_ = delegate.getNotesURL();
			name_ = delegate.getName();
		} catch (NotesException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearch(java.lang.String)
	 */
	@Override
	public int FTSearch(final String query) {
		try {
			return getDelegate().FTSearch(query);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearch(java.lang.String, int)
	 */
	@Override
	public int FTSearch(final String query, final int maxDocs) {
		try {
			return getDelegate().FTSearch(query, maxDocs);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.lang.String)
	 */
	@Override
	public int FTSearchSorted(final String query) {
		try {
			return getDelegate().FTSearchSorted(query);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.lang.String, int)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs) {
		try {
			return getDelegate().FTSearchSorted(query, maxDocs);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.lang.String, int, int)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final int column) {
		try {
			return getDelegate().FTSearchSorted(query, maxDocs, column);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.lang.String, int, int, boolean, boolean, boolean, boolean)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final int column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy) {
		try {
			return getDelegate().FTSearchSorted(query, maxDocs, column, ascending, exact, variants, fuzzy);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.lang.String, int, java.lang.String)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final String column) {
		try {
			return getDelegate().FTSearchSorted(query, maxDocs, column);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.lang.String, int, java.lang.String, boolean, boolean, boolean, boolean)
	 */
	@Override
	public int FTSearchSorted(final String query, final int maxDocs, final String column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy) {
		try {
			return getDelegate().FTSearchSorted(query, maxDocs, column, ascending, exact, variants, fuzzy);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int FTSearchSorted(final Vector query) {
		try {
			int result;
			java.util.Vector v = toDominoFriendly(query, this);
			result = getDelegate().FTSearchSorted(v);
			s_recycle(v);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs) {
		try {
			int result;
			java.util.Vector v = toDominoFriendly(query, this);
			result = getDelegate().FTSearchSorted(v, maxDocs);
			s_recycle(v);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs, final int column) {
		try {
			int result;
			java.util.Vector v = toDominoFriendly(query, this);
			result = getDelegate().FTSearchSorted(v, maxDocs, column);
			s_recycle(v);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int, int, boolean, boolean, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs, final int column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy) {
		try {
			int result;
			java.util.Vector v = toDominoFriendly(query, this);
			result = getDelegate().FTSearchSorted(v, maxDocs, column, ascending, exact, variants, fuzzy);
			s_recycle(v);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs, final String column) {
		try {
			return getDelegate().FTSearchSorted(toDominoFriendly(query, this), maxDocs, column);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int, java.lang.String, boolean, boolean, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs, final String column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy) {
		try {
			int result;
			java.util.Vector v = toDominoFriendly(query, this);
			result = getDelegate().FTSearchSorted(toDominoFriendly(query, this), maxDocs, column, ascending, exact, variants, fuzzy);
			s_recycle(v);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#clear()
	 */
	@Override
	public void clear() {
		try {
			getDelegate().clear();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#copyColumn(int)
	 */
	@Override
	public ViewColumn copyColumn(final int sourceColumn) {
		try {
			return Factory.fromLotus(getDelegate().copyColumn(sourceColumn), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#copyColumn(int, int)
	 */
	@Override
	public ViewColumn copyColumn(final int sourceColumn, final int destinationIndex) {
		try {
			return Factory.fromLotus(getDelegate().copyColumn(sourceColumn, destinationIndex), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#copyColumn(java.lang.String)
	 */
	@Override
	public ViewColumn copyColumn(final String sourceColumn) {
		try {
			return Factory.fromLotus(getDelegate().copyColumn(sourceColumn), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#copyColumn(java.lang.String, int)
	 */
	@Override
	public ViewColumn copyColumn(final String sourceColumn, final int destinationIndex) {
		try {
			return Factory.fromLotus(getDelegate().copyColumn(sourceColumn, destinationIndex), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#copyColumn(lotus.domino.ViewColumn)
	 */
	@Override
	public ViewColumn copyColumn(final lotus.domino.ViewColumn sourceColumn) {
		try {
			return Factory.fromLotus(getDelegate().copyColumn((lotus.domino.ViewColumn) toLotus(sourceColumn)), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#copyColumn(lotus.domino.ViewColumn, int)
	 */
	@Override
	public ViewColumn copyColumn(final lotus.domino.ViewColumn sourceColumn, final int destinationIndex) {
		try {
			return Factory.fromLotus(getDelegate().copyColumn((lotus.domino.ViewColumn) toLotus(sourceColumn), destinationIndex),
					ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createColumn()
	 */
	@Override
	public ViewColumn createColumn() {
		try {
			return Factory.fromLotus(getDelegate().createColumn(), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createColumn(int)
	 */
	@Override
	public ViewColumn createColumn(final int position) {
		try {
			return Factory.fromLotus(getDelegate().createColumn(position), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createColumn(int, java.lang.String)
	 */
	@Override
	public ViewColumn createColumn(final int position, final String columnTitle) {
		try {
			return Factory.fromLotus(getDelegate().createColumn(position, columnTitle), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createColumn(int, java.lang.String, java.lang.String)
	 */
	@Override
	public ViewColumn createColumn(final int position, final String columnTitle, final String formula) {
		try {
			return Factory.fromLotus(getDelegate().createColumn(position, columnTitle, formula), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewEntryCollection()
	 */
	@Override
	public ViewEntryCollection createViewEntryCollection() {
		try {
			return Factory.fromLotus(getDelegate().createViewEntryCollection(), ViewEntryCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNav()
	 */
	@Override
	public ViewNavigator createViewNav() {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNav(), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNav(int)
	 */
	@Override
	public ViewNavigator createViewNav(final int cacheSize) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNav(cacheSize), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFrom(java.lang.Object)
	 */
	@Override
	public ViewNavigator createViewNavFrom(final Object entry) {
		try {
			Object lotusObj = Base.toDominoFriendly(entry, this);
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFrom(lotusObj), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFrom(java.lang.Object, int)
	 */
	@Override
	public ViewNavigator createViewNavFrom(final Object entry, final int cacheSize) {
		try {
			Object lotusObj = toLotus(entry);
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFrom(lotusObj, cacheSize), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFromAllUnread()
	 */
	@Override
	public ViewNavigator createViewNavFromAllUnread() {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFromAllUnread(), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFromAllUnread(java.lang.String)
	 */
	@Override
	public ViewNavigator createViewNavFromAllUnread(final String userName) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFromAllUnread(userName), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFromCategory(java.lang.String)
	 */
	@Override
	public ViewNavigator createViewNavFromCategory(final String categoryName) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFromCategory(categoryName), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFromCategory(java.lang.String, int)
	 */
	@Override
	public ViewNavigator createViewNavFromCategory(final String categoryName, final int cacheSize) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFromCategory(categoryName, cacheSize), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFromChildren(java.lang.Object)
	 */
	@Override
	public ViewNavigator createViewNavFromChildren(final Object entry) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFromChildren(toLotus(entry)), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFromChildren(java.lang.Object, int)
	 */
	@Override
	public ViewNavigator createViewNavFromChildren(final Object entry, final int cacheSize) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFromChildren(toLotus(entry), cacheSize), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFromDescendants(java.lang.Object)
	 */
	@Override
	public ViewNavigator createViewNavFromDescendants(final Object entry) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFromDescendants(toLotus(entry)), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavFromDescendants(java.lang.Object, int)
	 */
	@Override
	public ViewNavigator createViewNavFromDescendants(final Object entry, final int cacheSize) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavFromDescendants(toLotus(entry), cacheSize), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavMaxLevel(int)
	 */
	@Override
	public ViewNavigator createViewNavMaxLevel(final int level) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavMaxLevel(level), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#createViewNavMaxLevel(int, int)
	 */
	@Override
	public ViewNavigator createViewNavMaxLevel(final int level, final int cacheSize) {
		try {
			getDelegate().setAutoUpdate(false);
			return Factory.fromLotus(getDelegate().createViewNavMaxLevel(level, cacheSize), ViewNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAliases()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getAliases() {
		try {
			return (Vector<String>) getDelegate().getAliases();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	public NoteCollection getNoteCollection() {
		NoteCollection nc = getAncestorDatabase().createNoteCollection(false);
		nc.setSelectDocuments(true);
		nc.setSelectionFormula(getSelectionFormula());
		nc.buildCollection();
		return nc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllDocuments()
	 */
	public DocumentCollection getAllDocuments() {
		// According to Tommy Valand's research, the fastest method is to build a NoteCollection with a matching selection formula
		// http://dontpanic82.blogspot.com/2013/06/benchmark-fetching-noteids-and.html
		Database db = getAncestorDatabase();
		DocumentCollection result = db.createDocumentCollection();
		NoteCollection nc = getNoteCollection();

		int[] nids = nc.getNoteIDs();

		// Arrays.sort(nids);
		// for (org.openntf.domino.Document doc : result) {
		// int nid = Integer.valueOf(doc.getNoteID(), 16);
		// if (!(Arrays.binarySearch(nids, nid) >= 0)) {
		// result.subtract(nid);
		// }
		// }

		// for (int nid : nids) {
		// result.intersect(nid);
		// }

		// TODO due to a bug in 9.0, this is being reverted to highly inefficient behavior...
		for (int nid : nids) {
			Document doc = db.getDocumentByID(Integer.toHexString(nid));
			result.add(doc);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllDocumentsByKey(java.lang.Object)
	 */
	@Override
	public DocumentCollection getAllDocumentsByKey(final Object key) {
		return getAllDocumentsByKey(key, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllDocumentsByKey(java.lang.Object, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public DocumentCollection getAllDocumentsByKey(final Object key, final boolean exact) {
		try {
			if (key instanceof java.util.Collection) {
				return Factory.fromLotus(getDelegate().getAllDocumentsByKey(toDominoFriendly((java.util.Collection) key, this), exact),
						DocumentCollection.class, this);
			}
			return Factory
					.fromLotus(getDelegate().getAllDocumentsByKey(toDominoFriendly(key, this), exact), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllDocumentsByKey(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DocumentCollection getAllDocumentsByKey(final Vector keys) {
		return getAllDocumentsByKey((Object) keys, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllDocumentsByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DocumentCollection getAllDocumentsByKey(final Vector keys, final boolean exact) {
		return getAllDocumentsByKey((Object) keys, exact);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllEntries()
	 */
	@Override
	public ViewEntryCollection getAllEntries() {
		try {
			return Factory.fromLotus(getDelegate().getAllEntries(), ViewEntryCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllEntriesByKey(java.lang.Object)
	 */
	@Override
	public ViewEntryCollection getAllEntriesByKey(final Object key) {
		return getAllEntriesByKey(key, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllEntriesByKey(java.lang.Object, boolean)
	 */
	@Override
	public ViewEntryCollection getAllEntriesByKey(final Object key, final boolean exact) {
		try {
			if (key instanceof java.util.Collection) {
				return Factory.fromLotus(getDelegate().getAllEntriesByKey(toDominoFriendly((java.util.Collection) key, this), exact),
						ViewEntryCollection.class, this);
			}
			return Factory.fromLotus(getDelegate().getAllEntriesByKey(toDominoFriendly(key, this), exact), ViewEntryCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllEntriesByKey(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ViewEntryCollection getAllEntriesByKey(final Vector keys) {
		return getAllEntriesByKey((Object) keys, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllEntriesByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ViewEntryCollection getAllEntriesByKey(final Vector keys, final boolean exact) {
		return getAllEntriesByKey((Object) keys, exact);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllReadEntries()
	 */
	@Override
	public ViewEntryCollection getAllReadEntries() {
		try {
			return Factory.fromLotus(getDelegate().getAllReadEntries(), ViewEntryCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllReadEntries(java.lang.String)
	 */
	@Override
	public ViewEntryCollection getAllReadEntries(final String userName) {
		try {
			return Factory.fromLotus(getDelegate().getAllReadEntries(userName), ViewEntryCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllUnreadEntries()
	 */
	@Override
	public ViewEntryCollection getAllUnreadEntries() {
		try {
			return Factory.fromLotus(getDelegate().getAllUnreadEntries(), ViewEntryCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getAllUnreadEntries(java.lang.String)
	 */
	@Override
	public ViewEntryCollection getAllUnreadEntries(final String userName) {
		try {
			return Factory.fromLotus(getDelegate().getAllUnreadEntries(userName), ViewEntryCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getBackgroundColor()
	 */
	@Override
	public int getBackgroundColor() {
		try {
			return getDelegate().getBackgroundColor();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getChild(lotus.domino.Document)
	 */
	@Override
	public Document getChild(final lotus.domino.Document doc) {
		try {
			return Factory.fromLotus(getDelegate().getChild((lotus.domino.Document) toLotus(doc)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getColumn(int)
	 */
	@Override
	public ViewColumn getColumn(final int columnNumber) {
		try {
			return Factory.fromLotus(getDelegate().getColumn(columnNumber), ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		try {
			return getDelegate().getColumnCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getColumnNames()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getColumnNames() {
		try {
			return (Vector<String>) getDelegate().getColumnNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getColumnValues(int)
	 */
	@Override
	public Vector<Object> getColumnValues(final int column) {
		try {
			return Factory.wrapColumnValues(getDelegate().getColumnValues(column), this.getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getColumns()
	 */
	@Override
	public Vector<org.openntf.domino.ViewColumn> getColumns() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getColumns(), org.openntf.domino.ViewColumn.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getCreated()
	 */
	@Override
	public DateTime getCreated() {
		try {
			return Factory.fromLotus(getDelegate().getCreated(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getDocumentByKey(java.lang.Object)
	 */
	@Override
	public Document getDocumentByKey(final Object key) {
		return getDocumentByKey(key, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getDocumentByKey(java.lang.Object, boolean)
	 */
	@Override
	public Document getDocumentByKey(final Object key, final boolean exact) {
		try {
			Object o = toDominoFriendly(key, this);
			if (o instanceof Vector) {
				return Factory.fromLotus(getDelegate().getDocumentByKey((Vector) o, exact), Document.class, this);
			} else {
				return Factory.fromLotus(getDelegate().getDocumentByKey(o, exact), Document.class, this);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getDocumentByKey(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Document getDocumentByKey(final Vector keys) {
		return getDocumentByKey((Object) keys, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getDocumentByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Document getDocumentByKey(final Vector keys, final boolean exact) {
		return getDocumentByKey((Object) keys, exact);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getEntryByKey(java.lang.Object)
	 */
	@Override
	public ViewEntry getEntryByKey(final Object key) {
		return getEntryByKey(key, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getEntryByKey(java.lang.Object, boolean)
	 */
	@Override
	public ViewEntry getEntryByKey(final Object key, final boolean exact) {
		try {
			return Factory.fromLotus(getDelegate().getEntryByKey(toDominoFriendly(key, this), exact), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getEntryByKey(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ViewEntry getEntryByKey(final Vector keys) {
		return getEntryByKey((Object) keys, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getEntryByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ViewEntry getEntryByKey(final Vector keys, final boolean exact) {
		return getEntryByKey((Object) keys, exact);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getEntryCount()
	 */
	@Override
	public int getEntryCount() {
		try {
			return getDelegate().getEntryCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getFirstDocument()
	 */
	@Override
	public Document getFirstDocument() {
		try {
			return Factory.fromLotus(getDelegate().getFirstDocument(), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getHeaderLines()
	 */
	@Override
	public int getHeaderLines() {
		try {
			return getDelegate().getHeaderLines();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getHttpURL()
	 */
	@Override
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getLastDocument()
	 */
	@Override
	public Document getLastDocument() {
		try {
			return Factory.fromLotus(getDelegate().getLastDocument(), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getLastModified()
	 */
	@Override
	public DateTime getLastModified() {
		try {
			return Factory.fromLotus(getDelegate().getLastModified(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getLockHolders()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getLockHolders() {
		try {
			return (Vector<String>) getDelegate().getLockHolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getName()
	 */
	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getNextDocument(lotus.domino.Document)
	 */
	@Override
	public Document getNextDocument(final lotus.domino.Document doc) {
		try {
			return Factory.fromLotus(getDelegate().getNextDocument((lotus.domino.Document) toLotus(doc)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getNextSibling(lotus.domino.Document)
	 */
	@Override
	public Document getNextSibling(final lotus.domino.Document doc) {
		try {
			return Factory.fromLotus(getDelegate().getNextSibling((lotus.domino.Document) toLotus(doc)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getNoteID()
	 */
	@Override
	public String getNoteID() {
		NoteCollection notes = this.getParent().createNoteCollection(false);
		notes.add(this);
		return notes.getFirstNoteID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getNotesURL()
	 */
	@Override
	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getNthDocument(int)
	 */
	@Override
	public Document getNthDocument(final int n) {
		try {
			return Factory.fromLotus(getDelegate().getNthDocument(n), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Database getParent() {
		return (Database) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getParentDocument(lotus.domino.Document)
	 */
	@Override
	public Document getParentDocument(final lotus.domino.Document doc) {
		try {
			return Factory.fromLotus(getDelegate().getParentDocument((lotus.domino.Document) toLotus(doc)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getPrevDocument(lotus.domino.Document)
	 */
	@Override
	public Document getPrevDocument(final lotus.domino.Document doc) {
		try {
			return Factory.fromLotus(getDelegate().getPrevDocument((lotus.domino.Document) toLotus(doc)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getPrevSibling(lotus.domino.Document)
	 */
	@Override
	public Document getPrevSibling(final lotus.domino.Document doc) {
		try {
			return Factory.fromLotus(getDelegate().getPrevSibling((lotus.domino.Document) toLotus(doc)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getReaders()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getReaders() {
		try {
			return (Vector<String>) getDelegate().getReaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getRowLines()
	 */
	@Override
	public int getRowLines() {
		try {
			return getDelegate().getRowLines();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getSelectionFormula()
	 */
	@Override
	public String getSelectionFormula() {
		try {
			return getDelegate().getSelectionFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getSelectionQuery()
	 */
	@Override
	public String getSelectionQuery() {
		try {
			return getDelegate().getSelectionQuery();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getSpacing()
	 */
	@Override
	public int getSpacing() {
		try {
			return getDelegate().getSpacing();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getTopLevelEntryCount()
	 */
	@Override
	public int getTopLevelEntryCount() {
		try {
			return getDelegate().getTopLevelEntryCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getURL()
	 */
	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		try {
			return getDelegate().getUniversalID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getViewInheritedName()
	 */
	@Override
	public String getViewInheritedName() {
		try {
			return getDelegate().getViewInheritedName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#getXPageAlt()
	 */
	@Override
	public String getXPageAlt() {
		return getDocument().getItemValueString("$XPageAlt");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isAutoUpdate()
	 */
	@Override
	public boolean isAutoUpdate() {
		try {
			return getDelegate().isAutoUpdate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isCalendar()
	 */
	@Override
	public boolean isCalendar() {
		try {
			return getDelegate().isCalendar();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isCategorized()
	 */
	@Override
	public boolean isCategorized() {
		try {
			return getDelegate().isCategorized();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isConflict()
	 */
	@Override
	public boolean isConflict() {
		try {
			return getDelegate().isConflict();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isDefaultView()
	 */
	@Override
	public boolean isDefaultView() {
		try {
			return getDelegate().isDefaultView();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isEnableNoteIDsForCategories()
	 */
	@Override
	public boolean isEnableNoteIDsForCategories() {
		throw new Notes9onlyException();
		/*
		try {
			return getDelegate().isEnableNoteIDsForCategories();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
		*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isFolder()
	 */
	@Override
	public boolean isFolder() {
		try {
			return getDelegate().isFolder();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isHierarchical()
	 */
	@Override
	public boolean isHierarchical() {
		try {
			return getDelegate().isHierarchical();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isModified()
	 */
	@Override
	public boolean isModified() {
		try {
			return getDelegate().isModified();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isPrivate()
	 */
	@Override
	public boolean isPrivate() {
		try {
			return getDelegate().isPrivate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isProhibitDesignRefresh()
	 */
	@Override
	public boolean isProhibitDesignRefresh() {
		try {
			return getDelegate().isProhibitDesignRefresh();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isProtectReaders()
	 */
	@Override
	public boolean isProtectReaders() {
		try {
			return getDelegate().isProtectReaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#isQueryView()
	 */
	@Override
	public boolean isQueryView() {
		try {
			return getDelegate().isQueryView();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#lock()
	 */
	@Override
	public boolean lock() {
		try {
			return getDelegate().lock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#lock(boolean)
	 */
	@Override
	public boolean lock(final boolean provisionalOk) {
		try {
			return getDelegate().lock(provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#lock(java.lang.String)
	 */
	@Override
	public boolean lock(final String name) {
		try {
			return getDelegate().lock(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk) {
		try {
			return getDelegate().lock(name, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#lock(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(final Vector names) {
		try {
			return getDelegate().lock(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(final Vector names, final boolean provisionalOk) {
		try {
			return getDelegate().lock(names, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#lockProvisional()
	 */
	@Override
	public boolean lockProvisional() {
		try {
			return getDelegate().lockProvisional();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#lockProvisional(java.lang.String)
	 */
	@Override
	public boolean lockProvisional(final String name) {
		try {
			return getDelegate().lockProvisional(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean lockProvisional(final Vector names) {
		try {
			return getDelegate().lockProvisional(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#markAllRead()
	 */
	@Override
	public void markAllRead() {
		try {
			getDelegate().markAllRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#markAllRead(java.lang.String)
	 */
	@Override
	public void markAllRead(final String userName) {
		try {
			getDelegate().markAllRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#markAllUnread()
	 */
	@Override
	public void markAllUnread() {
		try {
			getDelegate().markAllUnread();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#markAllUnread(java.lang.String)
	 */
	@Override
	public void markAllUnread(final String userName) {
		try {
			getDelegate().markAllUnread(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#refresh()
	 */
	@Override
	public void refresh() {
		try {
			getDelegate().refresh();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#removeColumn()
	 */
	@Override
	public void removeColumn() {
		try {
			getDelegate().removeColumn();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#removeColumn(int)
	 */
	@Override
	public void removeColumn(final int column) {
		try {
			getDelegate().removeColumn(column);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#removeColumn(java.lang.String)
	 */
	@Override
	public void removeColumn(final String column) {
		try {
			getDelegate().removeColumn(column);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#resortView()
	 */
	@Override
	public void resortView() {
		try {
			getDelegate().resortView();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#resortView(java.lang.String)
	 */
	@Override
	public void resortView(final String column) {
		try {
			getDelegate().resortView(column);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#resortView(java.lang.String, boolean)
	 */
	@Override
	public void resortView(final String column, final boolean ascending) {
		try {
			getDelegate().resortView(column, ascending);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setAliases(java.lang.String)
	 */
	@Override
	public void setAliases(final String alias) {
		try {
			getDelegate().setAliases(alias);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setAliases(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setAliases(final Vector aliases) {
		try {
			getDelegate().setAliases(aliases);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setAutoUpdate(boolean)
	 */
	@Override
	public void setAutoUpdate(final boolean flag) {
		try {
			getDelegate().setAutoUpdate(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setBackgroundColor(int)
	 */
	@Override
	public void setBackgroundColor(final int color) {
		try {
			getDelegate().setBackgroundColor(color);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setDefaultView(boolean)
	 */
	@Override
	public void setDefaultView(final boolean flag) {
		try {
			getDelegate().setDefaultView(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setEnableNoteIDsForCategories(boolean)
	 */
	@Override
	public void setEnableNoteIDsForCategories(final boolean flag) {
		throw new Notes9onlyException();
		/*
		try {
			getDelegate().setEnableNoteIDsForCategories(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		try {
			getDelegate().setName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setProhibitDesignRefresh(boolean)
	 */
	@Override
	public void setProhibitDesignRefresh(final boolean flag) {
		try {
			getDelegate().setProhibitDesignRefresh(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setProtectReaders(boolean)
	 */
	@Override
	public void setProtectReaders(final boolean flag) {
		try {
			getDelegate().setProtectReaders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setReaders(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setReaders(final Vector readers) {
		try {
			getDelegate().setReaders(readers);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setSelectionFormula(java.lang.String)
	 */
	@Override
	public void setSelectionFormula(final String formula) {
		try {
			getDelegate().setSelectionFormula(formula);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setSelectionQuery(java.lang.String)
	 */
	@Override
	public void setSelectionQuery(final String query) {
		try {
			getDelegate().setSelectionQuery(query);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#setSpacing(int)
	 */
	@Override
	public void setSpacing(final int spacing) {
		try {
			getDelegate().setSpacing(spacing);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#unlock()
	 */
	@Override
	public void unlock() {
		try {
			getDelegate().unlock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * New methods
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.View#getDocument()
	 */
	@Override
	public Document getDocument() {
		Database parent = this.getParent();
		return parent.getDocumentByUNID(this.getUniversalID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return this.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

	protected List<DominoColumnInfo> getColumnInfo() {
		if (columnInfo_ == null) {
			List<org.openntf.domino.ViewColumn> columns = getColumns();
			List<DominoColumnInfo> result = new ArrayList<DominoColumnInfo>(columns.size());
			for (org.openntf.domino.ViewColumn col : columns) {
				result.add(new DominoColumnInfo(col));
			}
			columnInfo_ = result;
		}
		return columnInfo_;
	}

	public static class DominoColumnInfo {
		private final String itemName_;
		private final int columnValuesIndex_;

		public DominoColumnInfo(final org.openntf.domino.ViewColumn column) {
			itemName_ = column.getItemName();
			columnValuesIndex_ = column.getColumnValuesIndex();
		}

		public String getItemName() {
			return itemName_;
		}

		public int getColumnValuesIndex() {
			return columnValuesIndex_;
		}
	}

	public boolean isIndexed() {
		return getDocument().hasItem("$Collation");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.View getDelegate() {
		lotus.domino.View view = super.getDelegate();
		try {
			view.getHttpURL();
		} catch (NotesException e) {
			resurrect();
		}
		return super.getDelegate();
	}

	public void resurrect() { // should only happen if the delegate has been destroyed somehow.
		Database db = getAncestorDatabase();
		try {
			lotus.domino.Database d = db.getDelegate();
			lotus.domino.View view = d.getView(name_);
			setDelegate(view);
			//			if (getAncestorSession().isFixEnabled(Fixes.VIEW_UPDATE_OFF)) {
			view.setAutoUpdate(false);
			//			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}
}
