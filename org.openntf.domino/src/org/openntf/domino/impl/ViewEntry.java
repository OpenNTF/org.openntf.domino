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
		try {
			return Factory.wrapColumnValues(getDelegate().getColumnValues(), this.getAncestorSession());
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
		return (Database) ((DatabaseDescendant) this.getParent()).getAncestorDatabase();
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
		return getColumnValuesMap().get(columnName);
	}

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

	public Map<String, Object> getColumnValuesMap() {
		if (columnValuesMap_ == null) {
			List<Object> columnValues = getColumnValues();
			columnValuesMap_ = new LinkedHashMap<String, Object>();
			// TODO RPr: Review this
			for (org.openntf.domino.impl.View.DominoColumnInfo info : ((org.openntf.domino.impl.View) getParentView()).getColumnInfo()) {
				if (info.getColumnValuesIndex() < 65535) {
					int vindex = info.getColumnValuesIndex();
					if (columnValues.size() > vindex) {
						columnValuesMap_.put(info.getItemName(), columnValues.get(vindex));
					} else {
						columnValuesMap_.put(info.getItemName(), null);
					}
				} else {
					columnValuesMap_.put(info.getItemName(), null);
				}
			}
		}
		return columnValuesMap_;
	}

	public Collection<Object> getColumnValuesEx() {
		//TODO - NTF not particularly happy with this. Should it be a List instead? Or should we rely on the caller to decide?
		return Collections.unmodifiableCollection(getColumnValuesMap().values());
	}
}
