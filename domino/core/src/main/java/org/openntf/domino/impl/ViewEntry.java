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
package org.openntf.domino.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.regex.Pattern;

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
import org.openntf.domino.utils.TypeUtils;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.util.JsonWriter;

// TODO: Auto-generated Javadoc

/**
 * The Class ViewEntry.
 */
public class ViewEntry extends BaseThreadSafe<org.openntf.domino.ViewEntry, lotus.domino.ViewEntry, View>
		implements org.openntf.domino.ViewEntry {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ViewEntry.class.getName());
	private Map<String, Object> columnValuesMap_;
	private Vector<Object> columnValues_;

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
	protected ViewEntry(final lotus.domino.ViewEntry delegate, final View parent) {
		super(delegate, parent, NOTES_VIEWENTRY);
		try {
			if (getAncestorSession().isFixEnabled(Fixes.FORCE_JAVA_DATES)) {
				delegate.setPreferJavaDates(true);
			}
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
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
	 */
	protected java.util.Vector<Object> getColumnValues(final boolean returnConstants) {
		try {

			if (columnValues_ == null) {
				// cache the columnValues and rely that the caller will NOT modify the objects inside
				Vector<Object> raw = getDelegate().getColumnValues();
				columnValues_ = wrapColumnValues(raw, this.getAncestorSession());
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
				return new Vector<Object>();
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
			return fromLotus(getDelegate().getDocument(), Document.SCHEMA, getParentView().getAncestorDatabase());
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

	@Override
	public final View getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.ViewEntry#getParentView()
	 */
	@Override
	public final View getParentView() {
		return parent;
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
	public final Database getAncestorDatabase() {
		return ((DatabaseDescendant) parent).getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
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
				if (idx < columnValues.size()) {
					return (columnValues.get(idx));
				}
			}
		}
		return null;
	}

	@Override
	public <T> T getColumnValue(final String columnName, final Class<T> type) {
		Object rawResult = getColumnValue(columnName);
		if (rawResult instanceof Vector) {
			return TypeUtils.collectionToClass((Vector<?>) rawResult, type, this.getAncestorSession());
		} else {
			Vector<Object> v = new Vector<Object>();
			v.add(rawResult);
			return TypeUtils.collectionToClass(v, type, this.getAncestorSession());
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

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

	/**
	 * Returns the session for a certain base object
	 *
	 * @param base
	 * @return
	 */
	protected static lotus.domino.View getParentView(final lotus.domino.ViewEntry base) {
		if (base == null) {
			return null;
		}
		try {
			return ((lotus.domino.View) getParentViewMethod.invoke(base, (Object[]) null));
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("ViewEntry Map is unmodifiable.");
	}

	@Override
	public boolean containsKey(final Object key) {
		return getColumnValuesMap().containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return getColumnValuesMap().containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return getColumnValuesMap().entrySet();
	}

	@Override
	public Object get(final Object key) {
		return getColumnValuesMap().get(key);
	}

	@Override
	public boolean isEmpty() {
		return getColumnValuesMap().isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return getColumnValuesMap().keySet();
	}

	@Override
	public Object put(final String key, final Object value) {
		throw new UnsupportedOperationException("ViewEntry Map is unmodifiable.");

	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException("ViewEntry Map is unmodifiable.");

	}

	@Override
	public Object remove(final Object key) {
		throw new UnsupportedOperationException("ViewEntry Map is unmodifiable.");

	}

	@Override
	public int size() {
		return getColumnValuesMap().size();
	}

	@Override
	public Collection<Object> values() {
		return getColumnValuesMap().values();
	}

	private transient String metaversalid_;

	@Override
	public String getMetaversalID() {
		if (null == metaversalid_) {
			if (isDocument()) {
				metaversalid_ = getAncestorDatabase().getReplicaID() + getUniversalID();
			} else {
				metaversalid_ = getParentView().getMetaversalID() + getNoteID();
			}
		}
		return metaversalid_;
	}

	private static Pattern posSplit = Pattern.compile("\\.");

	@Override
	public Object getCategoryValue() {
		if (isCategory()) {
			Object result = null;
			try {
				Vector<Object> values = getColumnValues(true);
				//				String position = getPosition();
				//				String[] posArray = posSplit.split(position);
				int level = getIndentLevel();
				org.openntf.domino.impl.View parent = (org.openntf.domino.impl.View) getParentView();
				int[] catColumns = parent.getCategoryColumnPositions();
				int catColumn = catColumns[level];
				result = values.get(catColumn);
			} catch (Throwable t) {
				t.printStackTrace();
				DominoUtils.handleException(t);
				throw new RuntimeException(t);
			}
			return result;
		} else {
			return null;
		}
	}

	public String toJson(boolean compact) {
		StringWriter sw = new StringWriter();
		JsonWriter jw = new JsonWriter(sw, compact);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		try {
			jw.startObject();
			jw.outStringProperty("@unid", getUniversalID());
			jw.outStringProperty("@noteid", getNoteID());
			jw.outStringProperty("@replicaid", getAncestorDatabase().getReplicaID());
			jw.outStringProperty("@metaversalid", getMetaversalID());

			boolean resetPreferDates = false;
			if (!isPreferJavaDates()) {
				setPreferJavaDates(true);
				resetPreferDates = true;
			}
			Map<String, Object> colVals = getColumnValuesMap();
			Set<String> keys = colVals.keySet();
			for (String key : keys) {
				Object colVal = colVals.get(key);
				if (colVal instanceof java.util.Date) {
					jw.outProperty(key, sdf.format((java.util.Date) colVal));
				} else {
					jw.outProperty(key, colVal.toString());
				}
			}
			
			if (resetPreferDates) {
				setPreferJavaDates(false);
			}

			jw.endObject();
			jw.flush();
		} catch (IOException e) {
			DominoUtils.handleException(e, this.getDocument());
			return null;
		} catch (JsonException e) {
			DominoUtils.handleException(e, this.getDocument());
			return null;
		}
		return sw.toString();
	}

}
