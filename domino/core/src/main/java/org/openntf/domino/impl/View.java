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

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.ViewColumn;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.exceptions.BackendBridgeSanityCheckException;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

// TODO: Auto-generated Javadoc
/**
 * The Class View.
 */
public class View extends BaseThreadSafe<org.openntf.domino.View, lotus.domino.View, Database>implements org.openntf.domino.View {
	private static final Logger log_ = Logger.getLogger(View.class.getName());
	private transient List<DominoColumnInfo> columnInfo_;
	private transient Map<String, org.openntf.domino.ViewColumn> columnMap_;
	private transient Map<String, DominoColumnInfo> columnInfoMap_;
	private transient String indexOptions_;
	private transient String flags_;

	private String universalId_;
	private Vector<String> aliases_;
	private String name_;
	private ViewNavigator utilityNavigator_;

	private static Method iGetEntryByKeyMethod;

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					// There is no relation between method position in the two classes:
					//					Method m1[] = lotus.domino.local.View.class.getDeclaredMethods();
					//					for (int i = 0; i < m1.length; i++) {
					//						if (m1[i].getName().equals("iGetEntryByKey"))
					//							System.out.println("lotus View " + i); // returns 68
					//					}
					//					Method m2[] = org.openntf.domino.impl.View.class.getDeclaredMethods();
					//					for (int i = 0; i < m2.length; i++) {
					//						if (m2[i].getName().equals("iGetEntryByKey"))
					//							System.out.println("openntf View " + i); // returns 34
					//					}
					//					System.out.println(m1);
					//					System.out.println(m2);

					iGetEntryByKeyMethod = lotus.domino.local.View.class.getDeclaredMethod("iGetEntryByKey", Vector.class, boolean.class,
							int.class);
					iGetEntryByKeyMethod.setAccessible(true);

					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			DominoUtils.handleException(e);
		}

	}

	/**
	 * Instantiates a new view.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	protected View(final lotus.domino.View delegate, final Database parent) {
		super(delegate, parent, NOTES_VIEW);
		initialize(delegate);
	}

	@SuppressWarnings("unchecked")
	private void initialize(final lotus.domino.View delegate) {
		try {
			aliases_ = delegate.getAliases();
			name_ = delegate.getName();
			universalId_ = delegate.getUniversalID();
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
	@SuppressWarnings("rawtypes")
	@Override
	public int FTSearchSorted(final Vector query) {
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			return getDelegate().FTSearchSorted(toDominoFriendly(query, getAncestorSession(), recycleThis));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs) {
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			return getDelegate().FTSearchSorted(toDominoFriendly(query, getAncestorSession(), recycleThis), maxDocs);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int, int)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs, final int column) {
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			return getDelegate().FTSearchSorted(toDominoFriendly(query, getAncestorSession(), recycleThis), maxDocs, column);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int, int, boolean, boolean, boolean, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs, final int column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy) {
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			return getDelegate().FTSearchSorted(toDominoFriendly(query, getAncestorSession(), recycleThis), maxDocs, column, ascending,
					exact, variants, fuzzy);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int, java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs, final String column) {
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			return getDelegate().FTSearchSorted(toDominoFriendly(query, getAncestorSession(), recycleThis), maxDocs, column);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#FTSearchSorted(java.util.Vector, int, java.lang.String, boolean, boolean, boolean, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int FTSearchSorted(final Vector query, final int maxDocs, final String column, final boolean ascending, final boolean exact,
			final boolean variants, final boolean fuzzy) {
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			return getDelegate().FTSearchSorted(toDominoFriendly(query, getAncestorSession(), recycleThis), maxDocs, column, ascending,
					exact, variants, fuzzy);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
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
			ViewColumn result = fromLotus(getDelegate().copyColumn(sourceColumn), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			ViewColumn result = fromLotus(getDelegate().copyColumn(sourceColumn, destinationIndex), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			ViewColumn result = fromLotus(getDelegate().copyColumn(sourceColumn), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			ViewColumn result = fromLotus(getDelegate().copyColumn(sourceColumn, destinationIndex), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			ViewColumn result = fromLotus(getDelegate().copyColumn(toLotus(sourceColumn)), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			ViewColumn result = fromLotus(getDelegate().copyColumn(toLotus(sourceColumn), destinationIndex), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			ViewColumn result = fromLotus(getDelegate().createColumn(), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			ViewColumn result = fromLotus(getDelegate().createColumn(position), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			ViewColumn result = fromLotus(getDelegate().createColumn(position, columnTitle), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			ViewColumn result = fromLotus(getDelegate().createColumn(position, columnTitle, formula), ViewColumn.SCHEMA, this);
			flushCaches();
			return result;
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
			return fromLotus(getDelegate().createViewEntryCollection(), ViewEntryCollection.SCHEMA, this);
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNav(), ViewNavigator.SCHEMA, this);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.NONE);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNav(cacheSize), ViewNavigator.SCHEMA, this);
			result.setCacheGuidance(cacheSize);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.NONE);
			return result;
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
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			getDelegate().setAutoUpdate(false);
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFrom(toLotus(entry)), ViewNavigator.SCHEMA, this);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.FROM);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFrom(lotusObj, cacheSize), ViewNavigator.SCHEMA, this);
			result.setCacheSize(cacheSize);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.FROM);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFromAllUnread(), ViewNavigator.SCHEMA, this);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.UNREAD);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFromAllUnread(userName), ViewNavigator.SCHEMA, this);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.UNREAD);
			((org.openntf.domino.impl.ViewNavigator) result).setUnreadUser(userName);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFromCategory(categoryName), ViewNavigator.SCHEMA, this);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.CATEGORY);
			((org.openntf.domino.impl.ViewNavigator) result).setStartCategory(categoryName);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFromCategory(categoryName, cacheSize), ViewNavigator.SCHEMA, this);
			result.setCacheSize(cacheSize);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.CATEGORY);
			((org.openntf.domino.impl.ViewNavigator) result).setStartCategory(categoryName);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFromChildren(toLotus(entry)), ViewNavigator.SCHEMA, this);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.CHILDREN);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFromChildren(toLotus(entry), cacheSize), ViewNavigator.SCHEMA,
					this);
			result.setCacheSize(cacheSize);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.CHILDREN);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFromDescendants(toLotus(entry)), ViewNavigator.SCHEMA, this);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.DESCENDANTS);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavFromDescendants(toLotus(entry), cacheSize), ViewNavigator.SCHEMA,
					this);
			result.setCacheSize(cacheSize);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.DESCENDANTS);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavMaxLevel(level), ViewNavigator.SCHEMA, this);
			result.setMaxLevel(level);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.MAXLEVEL);
			return result;
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
			getDelegate().setEnableNoteIDsForCategories(true);
			ViewNavigator result = fromLotus(getDelegate().createViewNavMaxLevel(level, cacheSize), ViewNavigator.SCHEMA, this);
			result.setMaxLevel(level);
			result.setCacheSize(cacheSize);
			((org.openntf.domino.impl.ViewNavigator) result).setType(ViewNavigator.Types.MAXLEVEL);
			return result;
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
			return getDelegate().getAliases();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/**
	 * @deprecated RPr: This might be very slow, so I suggest not to use this
	 */
	@Override
	@Deprecated
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
	@Override
	public DocumentCollection getAllDocuments() {
		Database db = getAncestorDatabase();
		DocumentCollection result = db.createDocumentCollection();

		// When it's a folder, there's no selection formula, so do it the "dumb" way for now
		if (isFolder()) {
			// TODO See if there's a better way
			for (ViewEntry entry : getAllEntries()) {
				if (entry.isDocument()) {
					result.add(entry.getDocument());
				}
			}
		} else {
			// According to Tommy Valand's research, the fastest method is to build a NoteCollection with a matching selection formula
			// http://dontpanic82.blogspot.com/2013/06/benchmark-fetching-noteids-and.html
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
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			Object domKey = toDominoFriendly(key, getAncestorSession(), recycleThis);
			lotus.domino.DocumentCollection lotusColl;
			if (domKey instanceof java.util.Vector) {
				lotusColl = getDelegate().getAllDocumentsByKey((java.util.Vector) domKey, exact);
			} else {
				lotusColl = getDelegate().getAllDocumentsByKey(domKey, exact);
			}
			DocumentCollection dc = fromLotus(lotusColl, DocumentCollection.SCHEMA, getAncestorDatabase());
			dc.setParentView(this);
			return dc;

		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#getAllDocumentsByKey(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public DocumentCollection getAllDocumentsByKey(final java.util.Vector keys) {
		return getAllDocumentsByKey((Object) keys, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#getAllDocumentsByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public DocumentCollection getAllDocumentsByKey(final java.util.Vector keys, final boolean exact) {
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
			return fromLotus(getDelegate().getAllEntries(), ViewEntryCollection.SCHEMA, this);
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
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			Object domKey = null;
			if (key != null) {
				domKey = toDominoFriendly(key, getAncestorSession(), recycleThis);
			}
			lotus.domino.ViewEntryCollection rawColl;
			if (domKey instanceof java.util.Vector) {
				rawColl = getDelegate().getAllEntriesByKey((Vector<?>) domKey, exact);
			} else {
				rawColl = getDelegate().getAllEntriesByKey(domKey, exact);
			}
			return fromLotus(rawColl, ViewEntryCollection.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/**
	 * This method is neccessary to get some Backend-functions working.<br>
	 * <font color=red>Attention: The <b>name</b> of the function seems not to be important, but the <b>position</b>!</font> It seems that
	 * the backendbridge calls the n-th. method in this class. (didn't figure out, how n was computed. Method is at
	 * lotus.domino.local.View.class.getDeclaredMethods()[68], but 68 has no correlation to thisClass.getDeclaredMethods )<br/>
	 *
	 * To find the correct position, trace a call of<br> <code>DominoUtils.getViewEntryByKeyWithOptions(view, "key", 2243)</code><br>
	 * and hit "step into" until you are in one of the methods of this file. Move <b>this</b> method to the position you found with the
	 * debugger.
	 *
	 * @param paramVector
	 * @param paramBoolean
	 * @param paramInt
	 * @return
	 * @throws NotesException
	 */
	protected ViewEntry iGetEntryByKey(final Vector<?> paramVector, final boolean paramBoolean, final int paramInt) {
		if (paramVector == null && paramInt == 42) {
			throw new BackendBridgeSanityCheckException("It seems that the backend bridge has called the correct method :)");
		}
		try {
			lotus.domino.ViewEntry lotus = (lotus.domino.ViewEntry) iGetEntryByKeyMethod.invoke(getDelegate(), paramVector, paramBoolean,
					paramInt);
			return fromLotus(lotus, ViewEntry.SCHEMA, this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#getAllEntriesByKey(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ViewEntryCollection getAllEntriesByKey(final Vector keys) {
		return getAllEntriesByKey((Object) keys, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#getAllEntriesByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
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
			return fromLotus(getDelegate().getAllReadEntries(), ViewEntryCollection.SCHEMA, this);
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
			return fromLotus(getDelegate().getAllReadEntries(userName), ViewEntryCollection.SCHEMA, this);
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
			return fromLotus(getDelegate().getAllUnreadEntries(), ViewEntryCollection.SCHEMA, this);
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
			return fromLotus(getDelegate().getAllUnreadEntries(userName), ViewEntryCollection.SCHEMA, this);
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
			return fromLotus(getDelegate().getChild(toLotus(doc)), Document.SCHEMA, getAncestorDatabase());
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
			return fromLotus(getDelegate().getColumn(columnNumber), ViewColumn.SCHEMA, this);
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
	public java.util.Vector<String> getColumnNames() {
		try {
			return getDelegate().getColumnNames();
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
	public java.util.Vector<Object> getColumnValues(final int column) {
		try {
			return wrapColumnValues(getDelegate().getColumnValues(column), this.getAncestorSession());
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
			try {
				lotus.domino.View raw = getDelegate();
				Vector rawColumns = raw.getColumns();
				return fromLotusAsVector(rawColumns, org.openntf.domino.ViewColumn.SCHEMA, this);
			} catch (NullPointerException e) {
				throw new RuntimeException(
						"Unable to get columns for a view called " + getName() + " in database " + getAncestorDatabase().getApiPath(), e);
			}
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
			return fromLotus(getDelegate().getCreated(), DateTime.SCHEMA, getAncestorSession());
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
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();

		try {
			Object domKey = toDominoFriendly(key, getAncestorSession(), recycleThis);
			if (domKey instanceof java.util.Vector) {
				return fromLotus(getDelegate().getDocumentByKey((java.util.Vector<?>) domKey, exact), Document.SCHEMA,
						getAncestorDatabase());
			} else {
				return fromLotus(getDelegate().getDocumentByKey(domKey, exact), Document.SCHEMA, getAncestorDatabase());
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#getDocumentByKey(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Document getDocumentByKey(final java.util.Vector keys) {
		return getDocumentByKey((Object) keys, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#getDocumentByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
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
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			Object domKey = toDominoFriendly(key, getAncestorSession(), recycleThis);
			if (domKey instanceof java.util.Vector) {
				return fromLotus(getDelegate().getEntryByKey((Vector<?>) domKey, exact), ViewEntry.SCHEMA, this);
			} else {
				return fromLotus(getDelegate().getEntryByKey(domKey, exact), ViewEntry.SCHEMA, this);

			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#getEntryByKey(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ViewEntry getEntryByKey(final Vector keys) {
		return getEntryByKey((Object) keys, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#getEntryByKey(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
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
			return fromLotus(getDelegate().getFirstDocument(), Document.SCHEMA, getAncestorDatabase());
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
			return fromLotus(getDelegate().getLastDocument(), Document.SCHEMA, getAncestorDatabase());
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
			return fromLotus(getDelegate().getLastModified(), DateTime.SCHEMA, getAncestorSession());
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
			return getDelegate().getLockHolders();
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
			return fromLotus(getDelegate().getNextDocument(toLotus(doc)), Document.SCHEMA, getAncestorDatabase());
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
			return fromLotus(getDelegate().getNextSibling(toLotus(doc)), Document.SCHEMA, getAncestorDatabase());
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
		NoteCollection notes = parent.createNoteCollection(false);
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
			return fromLotus(getDelegate().getNthDocument(n), Document.SCHEMA, getAncestorDatabase());
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
	public final Database getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#getParentDocument(lotus.domino.Document)
	 */
	@Override
	public Document getParentDocument(final lotus.domino.Document doc) {
		try {
			return fromLotus(getDelegate().getParentDocument(toLotus(doc)), Document.SCHEMA, getAncestorDatabase());
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
			return fromLotus(getDelegate().getPrevDocument(toLotus(doc)), Document.SCHEMA, getAncestorDatabase());
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
			return fromLotus(getDelegate().getPrevSibling(toLotus(doc)), Document.SCHEMA, getAncestorDatabase());
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
			return getDelegate().getReaders();
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
		return getFlags().contains("c");
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
		if (!isCalendar()) {
			return false;//NTF conflict checking only applies to calendar views
		}
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
		try {
			return getDelegate().isEnableNoteIDsForCategories();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#isFolder()
	 */
	@Override
	public boolean isFolder() {
		return getFlags().contains("F");
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
		IndexType type = getIndexType();
		if (type == IndexType.PRIVATE) {
			return true;
		}
		if (type == IndexType.SHAREDPRIVATEONDESKTOP) {
			return true;
		}
		if (type == IndexType.SHAREDPRIVATEONSERVER) {
			return true;
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
	@SuppressWarnings("rawtypes")
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
	@SuppressWarnings("rawtypes")
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
	@SuppressWarnings("rawtypes")
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
			flushCaches();
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
			flushCaches();
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
			flushCaches();
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
			initialize(getDelegate());// name and alias may be affected
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.View#setAliases(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setAliases(final Vector aliases) {
		try {
			getDelegate().setAliases(aliases);
			initialize(getDelegate());// name and alias may be affected
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
			flags_ = null;
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
		try {
			getDelegate().setEnableNoteIDsForCategories(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
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
			initialize(getDelegate());// name and alias may be affected
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
			flags_ = null;
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
	@SuppressWarnings("rawtypes")
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
		return parent.getDocumentByUNID(this.getUniversalID());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		Database db = this.getAncestorDatabase();
		if (db == null) {
			return Factory.getSession(SessionType.CURRENT);
		} else {
			return db.getAncestorSession();
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#getColumnMap()
	 */
	@Override
	public Map<String, org.openntf.domino.ViewColumn> getColumnMap() {
		if (columnMap_ == null) {
			columnMap_ = new LinkedHashMap<String, org.openntf.domino.ViewColumn>();
			Vector<ViewColumn> columns = getColumns();
			if (columns != null && !columns.isEmpty()) {
				for (ViewColumn column : columns) {
					columnMap_.put(column.getItemName(), column);
				}
			}
		}
		return columnMap_;
	}

	/**
	 * Used by the viewEntry to determine the correct columnValue
	 *
	 * @return
	 */
	protected List<DominoColumnInfo> getColumnInfos() {
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

	/**
	 * Used by the viewEntry to determine the correct columnValue
	 *
	 * @return
	 */
	protected Map<String, DominoColumnInfo> getColumnInfoMap() {
		if (columnInfoMap_ == null) {
			columnInfoMap_ = new LinkedHashMap<String, DominoColumnInfo>();
			for (DominoColumnInfo columnInfo : getColumnInfos()) {
				columnInfoMap_.put(columnInfo.getItemName(), columnInfo);
			}

		}
		return columnInfoMap_;
	}

	/**
	 * Metadata about a ViewColumn, comprising the programmatic column name and the column index
	 *
	 * @since org.openntf.domino 3.0.0
	 */
	public static class DominoColumnInfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private final String itemName_;
		private final int columnValuesIndex_;
		private final Object constantValue_;

		/**
		 * Constructor, passing the ViewColumn object
		 *
		 * @param column
		 *            ViewColumn from which to extract the metadata
		 * @since org.openntf.domino 3.0.0
		 */
		public DominoColumnInfo(final org.openntf.domino.ViewColumn column) {
			itemName_ = column.getItemName();
			columnValuesIndex_ = column.getColumnValuesIndex(false);

			if (columnValuesIndex_ == 65535) {
				// resolve the constant values (with the openntf session, to get proper dateTime values!)
				Vector<?> v = column.getAncestorSession().evaluate(column.getFormula());
				constantValue_ = v.get(0);
			} else {
				constantValue_ = null;
			}

		}

		/**
		 * Gets the programmatic name of the column, from the Advanced tab (beanie image) of the Column
		 *
		 * @return String programmatic column name
		 * @since org.openntf.domino 3.0.0
		 */
		public String getItemName() {
			return itemName_;
		}

		/**
		 * Gets the index for the column in the view, beginning at 0
		 *
		 * @return int index of the column
		 * @since org.openntf.domino 3.0.0
		 */
		public int getColumnValuesIndex() {
			return columnValuesIndex_;
		}

		/**
		 * If this is a constant
		 *
		 * @return the constant value of this column
		 */
		public Object getConstantValue() {
			return constantValue_;
		}
	}

	@Override
	public boolean isIndexed() {
		return getDocument().hasItem("$Collection");
	}

	private lotus.domino.View recreateView() throws NotesException {
		lotus.domino.Database d = toLotus(getAncestorDatabase());
		lotus.domino.View ret = d.getView(name_);
		if (ret == null) {
			throw new IllegalStateException("View '" + name_ + "' not found in " + getAncestorDatabase());
		}
		if (!universalId_.equals(ret.getUniversalID())) {

			lotus.domino.View candidate = null;
			for (String alias : aliases_) {
				lotus.domino.View vw = d.getView(alias);
				if (candidate == null && vw != null && universalId_.equals(vw.getUniversalID())) {
					candidate = vw;
					break;
				} else if (!ret.equals(vw)) {
					// wrap every view, so that it gets recycled
					getFactory().fromLotus(vw, View.SCHEMA, getAncestorDatabase());
				}
			}
			if (candidate != null) {
				log_.log(Level.WARNING, "The view name '" + name_ + "' is not unique in " + getAncestorDatabase() + ". View1: "
						+ candidate.getAliases() + ", View2:" + ret.getAliases());
				// recycle our first view by adding a wrapper (a recycle call will probably hard recycle the delegate)
				fromLotus(ret, View.SCHEMA, getAncestorDatabase());
				ret = candidate;
			}

		}
		return ret;
	}

	@Override
	protected void resurrect() {// should only happen if the delegate has been destroyed somehow.
		try {
			lotus.domino.View view = recreateView();
			setDelegate(view, true);
			/* No special logging, since by now View is a BaseThreadSafe */
			view.setAutoUpdate(false);
			//			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#checkUnique(java.lang.Object, org.openntf.domino.Document)
	 */
	@Override
	public boolean checkUnique(final Object key, final Document srcDoc) {
		boolean retVal_ = false;
		try {
			DocumentCollection dc = this.getAllDocumentsByKey(key, true);
			for (Document checkDoc : dc) {
				if (null == srcDoc) {
					return false;
				} else {
					if (!checkDoc.getUniversalID().equals(srcDoc.getUniversalID())) {
						return retVal_;
					}
				}
			}
			retVal_ = true;
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return retVal_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#isTimeSensitive()
	 */
	@Override
	public boolean isTimeSensitive() {
		Document doc = getDocument();
		return doc.hasItem("$FormulaTV");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#getIndexType()
	 */
	@Override
	public IndexType getIndexType() {
		IndexType result = IndexType.SHARED;
		String flags = getFlags();
		if (flags.contains("P") && flags.contains("Y")) {
			if (flags.contains("p")) {
				result = IndexType.SHAREDPRIVATEONSERVER;
				if (flags.contains("o")) {
					result = IndexType.SHAREDPRIVATEONDESKTOP;
				}
			} else if (flags.contains("V")) {
				result = IndexType.PRIVATE;
			} else if (flags.contains("l")) {
				result = IndexType.SHAREDINCLUDESDELETES;
			} else if (flags.contains("a")) {
				result = IndexType.SHAREDNOTINFOLDERS;
			}
		}
		return result;
	}

	protected String getFlags() {
		if (flags_ == null) {
			flags_ = getDocument().getItemValueString("$Flags");
		}
		return flags_;
	}

	protected String getIndexOptions() {
		if (indexOptions_ == null) {
			indexOptions_ = getDocument().getItemValueString("$Index");
		}
		return indexOptions_;
	}

	/*
	'/P=' + the number of hours until discarding of the view index.
	'/T' Discard view index after each use.
	'/M' Manual refresh.
	'/O' Automatic refresh.
	'/R=' + the number of seconds between automatically refresh of view.
	'/C' Don't show empty categories
	'/L' Disable auto-update
	 */
	protected static Pattern R_MATCH = Pattern.compile("^.*\\bR=(\\d+).*$", Pattern.CASE_INSENSITIVE);
	protected static Pattern P_MATCH = Pattern.compile("^.*\\bP=(\\d+).*$", Pattern.CASE_INSENSITIVE);

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#isDisableAutoUpdate()
	 */
	@Override
	public boolean isDisableAutoUpdate() {
		String index = getIndexOptions();
		return index.contains("/L");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#isHideEmptyCategories()
	 */
	@Override
	public boolean isHideEmptyCategories() {
		String index = getIndexOptions();
		return index.contains("/C");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#isDiscardIndex()
	 */
	@Override
	public boolean isDiscardIndex() {
		String index = getIndexOptions();
		return index.contains("/T");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#isManualRefresh()
	 */
	@Override
	public boolean isManualRefresh() {
		String index = getIndexOptions();
		return index.contains("/M");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#isAutoAfterFirstUse()
	 */
	@Override
	public boolean isAutoRefreshAfterFirstUse() {
		String index = getIndexOptions();
		if (index.contains("/M") || index.contains("/O") || index.contains("/R")) {
			return false;
		} else {
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#isAutomaticRefresh()
	 */
	@Override
	public boolean isAutomaticRefresh() {
		String index = getIndexOptions();
		return index.contains("/O");
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.View#getAutoRefreshSeconds()
	 */
	@Override
	public int getAutoRefreshSeconds() {
		int result = 0;
		String index = getIndexOptions();
		if (index.contains("/R")) {
			Matcher matcher = R_MATCH.matcher(index);
			if (matcher.matches()) {
				result = Integer.parseInt(matcher.group(1));
			}
		}
		return result;
	}

	@Override
	public int getDiscardHours() {
		int result = 0;
		String index = getIndexOptions();
		if (index.contains("/P")) {
			Matcher matcher = P_MATCH.matcher(index);
			if (matcher.matches()) {
				result = Integer.parseInt(matcher.group(1));
			}
		}
		return result;
	}

	protected void flushCaches() {
		columnInfo_ = null;
		columnInfoMap_ = null;
		columnMap_ = null;
	}

	@Override
	public Document getFirstDocumentByKey(final Object key) {
		return this.getDocumentByKey(key);
	}

	@Override
	public Document getFirstDocumentByKey(final Object key, final boolean exact) {
		return this.getDocumentByKey(key, exact);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Document getFirstDocumentByKey(final Vector keys) {
		return this.getDocumentByKey(keys);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Document getFirstDocumentByKey(final Vector keys, final boolean exact) {
		return this.getDocumentByKey(keys, exact);
	}

	@Override
	public ViewEntry getFirstEntryByKey(final Object key) {
		return this.getEntryByKey(key);
	}

	@Override
	public ViewEntry getFirstEntryByKey(final Object key, final boolean exact) {
		return this.getEntryByKey(key, exact);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ViewEntry getFirstEntryByKey(final Vector keys) {
		return this.getEntryByKey(keys);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ViewEntry getFirstEntryByKey(final Vector keys, final boolean exact) {
		return this.getEntryByKey(keys, exact);
	}

	//-------------- Externalize  stuff ------------------
	private static final int EXTERNALVERSIONUID = 20141205;

	/**
	 * @deprecated needed for {@link Externalizable} - do not use!
	 */
	@Deprecated
	public View() {
		super(NOTES_VIEW);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(EXTERNALVERSIONUID);// data version

		out.writeObject(name_);
		out.writeObject(universalId_);
		out.writeObject(aliases_);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);

		int version = in.readInt();
		if (version != EXTERNALVERSIONUID) {
			throw new InvalidClassException("Cannot read dataversion " + version);
		}

		name_ = (String) in.readObject();
		universalId_ = (String) in.readObject();
		aliases_ = (Vector<String>) in.readObject();
	}

	protected Object readResolve() {
		// TODO: The View name may not be acourate enough, if the view is not unique
		try {
			return getFactory().fromLotus(recreateView(), View.SCHEMA, getAncestorDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return this;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
		result = prime * result + ((universalId_ == null) ? 0 : universalId_.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof View)) {
			return false;
		}
		View other = (View) obj;
		if (name_ == null) {
			if (other.name_ != null) {
				return false;
			}
		} else if (!name_.equals(other.name_)) {
			return false;
		}
		if (universalId_ == null) {
			if (other.universalId_ != null) {
				return false;
			}
		} else if (!universalId_.equals(other.universalId_)) {
			return false;
		}
		return true;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

	@Override
	public boolean containsKey(final Object arg0) {
		return getColumnInfoMap().containsKey(arg0);
	}

	@Override
	public boolean containsValue(final Object arg0) {
		if (arg0 instanceof Document) {
			return containsDocument((Document) arg0);
		} else if (arg0 instanceof ViewEntry) {
			return containsEntry((ViewEntry) arg0);
		}
		return getColumnInfoMap().containsValue(arg0);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (String key : getColumnInfoMap().keySet()) {
			result.put(key, getColumnInfoMap().get(key));
		}
		return result.entrySet();
	}

	@Override
	public Object get(final Object key) {
		return getColumnInfoMap().get(key);
	}

	@Override
	public boolean isEmpty() {
		return getColumnInfoMap().isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return getColumnInfoMap().keySet();
	}

	@Override
	public Object put(final String key, final Object value) {
		throw new UnsupportedOperationException("View Map may not be modified");
	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException("View Map may not be modified");
	}

	@Override
	public Object remove(final Object key) {
		throw new UnsupportedOperationException("View Map may not be modified");
	}

	@Override
	public int size() {
		return getColumnInfoMap().size();
	}

	@Override
	public Collection<Object> values() {
		Collection<Object> result = new ArrayList<Object>();
		result.addAll(getColumnInfoMap().values());
		return result;
	}

	@Override
	public ViewEntry getEntryAtPosition(final String position, final char separator) {
		return getUtilityNavigator().getPos(position, separator);
	}

	@Override
	public ViewEntry getEntryAtPosition(final String position) {
		return getUtilityNavigator().getPos(position, ViewNavigator.DEFAULT_SEPARATOR);
	}

	@Override
	public boolean containsDocument(final Document doc) {
		boolean result = getUtilityNavigator().gotoEntry(doc);
		getUtilityNavigator().gotoFirst();
		return result;
	}

	@Override
	public boolean containsEntry(final ViewEntry entry) {
		boolean result = getUtilityNavigator().gotoEntry(entry);
		getUtilityNavigator().gotoFirst();
		return result;
	}

	private ViewNavigator getUtilityNavigator() {
		if (utilityNavigator_ != null) {
			try {
				utilityNavigator_.getBufferMaxEntries();
			} catch (Exception e) {
				utilityNavigator_ = null;
			}
		}
		if (utilityNavigator_ == null) {
			try {
				getDelegate().setAutoUpdate(false);
				getDelegate().setEnableNoteIDsForCategories(true);
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			utilityNavigator_ = this.createViewNav(128);
			utilityNavigator_.setCacheGuidance(128, lotus.domino.ViewNavigator.VN_CACHEGUIDANCE_READSELECTIVE);
			utilityNavigator_.setEntryOptions(lotus.domino.ViewNavigator.VN_ENTRYOPT_NOCOLUMNVALUES);
		}
		return utilityNavigator_;
	}

	@Override
	public String getMetaversalID() {
		return getAncestorDatabase().getReplicaID() + getUniversalID();
	}
}
