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

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.impl.View.DominoColumnInfo;
import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.TypeUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class ViewEntry.
 */
public class ViewEntry extends Base<org.openntf.domino.ViewEntry, lotus.domino.ViewEntry, View> implements org.openntf.domino.ViewEntry {
	private static final Logger log_ = Logger.getLogger(ViewEntry.class.getName());
	private Map<String, Object> columnValuesMap_;
	private Vector columnValues_;
	private static Method getParentViewMethod;

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					getParentViewMethod = lotus.domino.local.ViewEntry.class.getDeclaredMethod("getParentView", (Class<?>[]) null);
					getParentViewMethod.setAccessible(true);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			DominoUtils.handleException(e);
		}

	}

	/**
	 * Instantiates a new view entry.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public ViewEntry(final lotus.domino.ViewEntry delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, (View) parent);
		try {
			if (getAncestorSession().isFixEnabled(Fixes.FORCE_JAVA_DATES)) {
				delegate.setPreferJavaDates(true);
			}
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/**
	 * Instantiates a new outline.
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
	public ViewEntry(final lotus.domino.ViewEntry delegate, final View parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_VIEWENTRY);
		try {
			if (getAncestorSession().isFixEnabled(Fixes.FORCE_JAVA_DATES)) {
				delegate.setPreferJavaDates(true);
			}
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}

	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected View findParent(final lotus.domino.ViewEntry delegate) {
		return fromLotus(getParentView(delegate), View.SCHEMA, null);
	}

	/**
	 * Returns the session for a certain base object
	 * 
	 * @param base
	 * @return
	 */
	protected static lotus.domino.View getParentView(final lotus.domino.ViewEntry base) {
		if (base == null)
			return null;
		try {
			return ((lotus.domino.View) getParentViewMethod.invoke(base, (Object[]) null));
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getChildCount()
	 */
	@Override
	public int getChildCount() {
		try {
			return getDelegate().getChildCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getColumnIndentLevel()
	 */
	@Override
	public int getColumnIndentLevel() {
		try {
			return getDelegate().getColumnIndentLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getColumnValues()
	 */
	@Override
	public java.util.Vector<Object> getColumnValues() {
		return getColumnValues(getAncestorSession().isFixEnabled(Fixes.VIEWENTRY_RETURN_CONSTANT_VALUES));
	}

	/**
	 * Returns the columnValues of this entry.
	 * 
	 * @param returnConstants
	 *            this parameter controls if constant values should also be returned
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public java.util.Vector<Object> getColumnValues(final boolean returnConstants) {
		try {

			if (columnValues_ == null) {
				// cache the columnValues and rely that the caller will NOT modify the objects inside
				columnValues_ = Factory.wrapColumnValues(getDelegate().getColumnValues(), this.getAncestorSession());
			}

			if (returnConstants) {
				List<DominoColumnInfo> colInfos = ((org.openntf.domino.impl.View) getParentView()).getColumnInfos();
				if (colInfos.size() > columnValues_.size()) { // there were constant columns

					Vector<Object> ret = new Vector<Object>(colInfos.size());
					for (DominoColumnInfo colInfo : colInfos) {
						int idx = colInfo.getColumnValuesIndex();
						if (idx < 65535) {
							if (idx < columnValues_.size()) {
								ret.add(columnValues_.get(idx));
							} else {
								ret.add(null); // Categories!
							}
						} else {
							ret.add(colInfo.getConstantValue());
						}
					}
					return ret;
				}
			}
			return columnValues_;
		} catch (NotesException e) {
			if (e.id == 4432) {
				return new java.util.Vector();
			}
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getDescendantCount()
	 */
	@Override
	public int getDescendantCount() {
		try {
			return getDelegate().getDescendantCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getDocument()
	 */
	@Override
	public Document getDocument() {
		try {
			return fromLotus(getDelegate().getDocument(), Document.SCHEMA, getParentView().getParent());
		} catch (NotesException e) {
			if (e.id == 4432) {
				return null;
			}
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getFTSearchScore()
	 */
	@Override
	public int getFTSearchScore() {
		try {
			return getDelegate().getFTSearchScore();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getIndentLevel()
	 */
	@Override
	public int getIndentLevel() {
		try {
			return getDelegate().getIndentLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getNoteID()
	 */
	@Override
	public String getNoteID() {
		try {
			return getDelegate().getNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getNoteIDAsInt()
	 */
	@Override
	public int getNoteIDAsInt() {
		try {
			return getDelegate().getNoteIDAsInt();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public org.openntf.domino.Base<?> getParent() {
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.ViewEntry#getParentView()
	 */
	@Override
	public View getParentView() {
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getPosition(char)
	 */
	@Override
	public String getPosition(final char separator) {
		try {
			return getDelegate().getPosition(separator);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getRead()
	 */
	@Override
	public boolean getRead() {
		try {
			return getDelegate().getRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getRead(java.lang.String)
	 */
	@Override
	public boolean getRead(final String userName) {
		try {
			return getDelegate().getRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getSiblingCount()
	 */
	@Override
	public int getSiblingCount() {
		try {
			return getDelegate().getSiblingCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		try {
			return getDelegate().getUniversalID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isCategory()
	 */
	@Override
	public boolean isCategory() {
		try {
			return getDelegate().isCategory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isConflict()
	 */
	@Override
	public boolean isConflict() {
		try {
			return getDelegate().isConflict();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isDocument()
	 */
	@Override
	public boolean isDocument() {
		try {
			return getDelegate().isDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isPreferJavaDates()
	 */
	@Override
	public boolean isPreferJavaDates() {
		try {
			return getDelegate().isPreferJavaDates();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isTotal()
	 */
	@Override
	public boolean isTotal() {
		try {
			return getDelegate().isTotal();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#isValid()
	 */
	@Override
	public boolean isValid() {
		try {
			return getDelegate().isValid();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntry#setPreferJavaDates(boolean)
	 */
	@Override
	public void setPreferJavaDates(final boolean flag) {
		try {
			columnValues_ = null;
			columnValuesMap_ = null;
			getDelegate().setPreferJavaDates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return ((DatabaseDescendant) this.getParent()).getAncestorDatabase();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.ViewEntry#getColumnValue(java.lang.String)
	 */
	@Override
	public Object getColumnValue(final String columnName) {
		Map<String, DominoColumnInfo> colInfoMap = ((org.openntf.domino.impl.View) getParentView()).getColumnInfoMap();

		DominoColumnInfo colInfo = colInfoMap.get(columnName);
		if (colInfo != null) {
			int idx = colInfo.getColumnValuesIndex();
			if (idx == 65535) {
				return colInfo.getConstantValue();
			} else {
				Vector<Object> columnValues = getColumnValues(false);
				if (idx < columnValues.size())
					return (columnValues.get(idx));
			}
		}
		return null;
	}

	@Override
	public <T> T getColumnValue(final String columnName, final Class<?> T) {
		Object rawResult = getColumnValue(columnName);
		if (rawResult instanceof Vector) {
			return TypeUtils.vectorToClass((Vector) rawResult, T, this.getAncestorSession());
		} else {
			Vector v = new Vector();
			v.add(rawResult);
			return TypeUtils.vectorToClass(v, T, this.getAncestorSession());
		}
	}

	@Override
	public Map<String, Object> getColumnValuesMap() {
		if (columnValuesMap_ == null) {
			List<Object> columnValues = getColumnValues(true); // fetch the corrected column values
			List<DominoColumnInfo> columnInfos = ((org.openntf.domino.impl.View) getParentView()).getColumnInfos();
			columnValuesMap_ = new LinkedHashMap<String, Object>();

			for (int i = 0; i < columnInfos.size(); i++) {
				columnValuesMap_.put(columnInfos.get(i).getItemName(), columnValues.get(i));
			}
		}
		return columnValuesMap_;
	}

	@Override
	public Collection<Object> getColumnValuesEx() {
		//TODO - NTF not particularly happy with this. Should it be a List instead? Or should we rely on the caller to decide?
		return Collections.unmodifiableCollection(getColumnValues(true));
	}

	@Override
	public String getPosition() {
		char dot = '.';
		String pos = this.getPosition(dot); // e.g. 2.1
		return pos;
	}
}
