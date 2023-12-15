/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.xsp.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.complex.ValueBindingObjectImpl;
import com.ibm.xsp.extlib.component.picker.data.IPickerEntry;
import com.ibm.xsp.extlib.component.picker.data.IPickerOptions;
import com.ibm.xsp.extlib.component.picker.data.IPickerResult;
import com.ibm.xsp.extlib.component.picker.data.IValuePickerData;
import com.ibm.xsp.extlib.component.picker.data.SimplePickerResult;

/**
 * @author Nathan T. Freeman
 * 
 *         MapValuePickerData, for use with the ValuePicker control
 */
// TODO: Remove before 3.0 - all functionality introduced in ExtLib 14
@SuppressWarnings("nls")
public class MapValuePickerData extends ValueBindingObjectImpl implements IValuePickerData, Serializable {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(MapValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;
	public String searchType;
	public String searchStyle;
	public Boolean caseInsensitive;
	public Map<String, String> options;

	/**
	 * Enum for easy and consistent access to search type options
	 * 
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	private static enum SearchType {
		SEARCH_STARTFROM("startFrom"), SEARCH_MATCH("match"), SEARCH_FTSEARCH("ftSearch"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		private final String value_;

		private SearchType(final String value) {
			value_ = value;
		}

		public String getValue() {
			return value_;
		}
	}

	/**
	 * Enum for easy access to the search styles - jumpTo and restrictToSearch
	 * 
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	private static enum SearchStyle {
		SEARCH_JUMPTO("jumpTo"), SEARCH_RESTRICTTOSEARCH("restrictToSearch"); //$NON-NLS-1$ //$NON-NLS-2$

		private final String value_;

		private SearchStyle(final String value) {
			value_ = value;
		}

		public String getValue() {
			return value_;
		}
	}

	public MapValuePickerData() {

	}

	/**
	 * Gets the options for the Value Picker, from the "options" property
	 * 
	 * @return Map<String, String> of values
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getOptions() {
		if (options != null) {
			return options;
		}
		ValueBinding vb = getValueBinding("options"); //$NON-NLS-1$
		if (vb != null) {
			Object vbVal = vb.getValue(getFacesContext());
			if (vbVal instanceof Map) {
				return (Map<String, String>) vbVal;
			} else {
				throw new UnsupportedOperationException("Value is not a map");
			}
		}

		return null;
	}

	/**
	 * Loads the options for the Value Picker
	 * 
	 * @param options
	 *            Map<String, String>
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void setOptions(final Map<String, String> options) {
		this.options = options;
	}

	/**
	 * Gets the search type for the picker, from the "searchType" property
	 * 
	 * @return String search type
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public String getSearchType() {
		if (searchType != null) {
			return searchType;
		}
		ValueBinding vb = getValueBinding("searchType"); //$NON-NLS-1$
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		}

		return null;
	}

	/**
	 * Loads the search type
	 * 
	 * @param searchType
	 *            String search type
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public void setSearchType(final String searchType) {
		this.searchType = searchType;
	}

	/**
	 * Gets the search style, from the "searchStyle" property
	 * 
	 * @return String search style
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public String getSearchStyle() {
		if (searchStyle != null) {
			return searchStyle;
		}
		ValueBinding vb = getValueBinding("searchStyle"); //$NON-NLS-1$
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		} else {
			return SearchStyle.SEARCH_JUMPTO.getValue();
		}

	}

	/**
	 * Loads the search style
	 * 
	 * @param searchStyle
	 *            String search style
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public void setSearchStyle(final String searchStyle) {
		this.searchStyle = searchStyle;
	}

	/**
	 * Whether the options should be searched case insensitive or not
	 * 
	 * @return boolean whether case insensitive
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public boolean isCaseInsensitive() {
		if (caseInsensitive != null) {
			return caseInsensitive;
		}
		ValueBinding vb = getValueBinding("caseInsensitive");//$NON-NLS-1$
		if (vb != null) {
			Boolean b = (Boolean) vb.getValue(getFacesContext());
			if (b != null) {
				return b;
			}
		}
		return false;
	}

	/**
	 * Loads whether the search should be done case inszensitive
	 * 
	 * @param caseInsensitive
	 *            boolean
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public void setCaseInsensitive(final boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#getSourceLabels()
	 */
	@Override
	public String[] getSourceLabels() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#hasCapability(int)
	 */
	@Override
	public boolean hasCapability(final int capability) {
		if (capability == IValuePickerData.CAPABILITY_LABEL || capability == IValuePickerData.CAPABILITY_SEARCHBYKEY
				|| capability == IValuePickerData.CAPABILITY_SEARCHLIST)
			return true;
		return false;
	}

	/*
	 * This method appears to be the one that gets the entries for the picker
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#readEntries(com.ibm.xsp.extlib.component.picker.data.IPickerOptions)
	 */
	@Override
	public IPickerResult readEntries(final IPickerOptions options) {
		String startKey = options.getStartKey();
		String key = options.getKey();
		int start = options.getStart();
		int count = options.getCount();
		int searchIndex = 0;
		LinkedHashMap<String, String> opts = filteredOptions(key, startKey, start, searchIndex);
		List<IPickerEntry> entries = new ArrayList<IPickerEntry>();
		Iterator<String> it = opts.keySet().iterator();
		while (it.hasNext()) {
			String mapKey = it.next();
			entries.add(new SimplePickerResult.Entry(opts.get(mapKey), mapKey));
		}
		return new SimplePickerResult(entries, count);
	}

	/**
	 * Returns the filtered options, a subset of the options for the MapValuePickerData
	 * 
	 * @param key
	 *            String typeahead key
	 * @param startKey
	 *            String search option
	 * @param start
	 *            int not used
	 * @param searchIndex
	 *            int not used
	 * @return LinkedHashMap<String, String> of options filtered from the total options
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	private LinkedHashMap<String, String> filteredOptions(final String key, final String startKey, final int start, final int searchIndex) {
		LinkedHashMap<String, String> retVal = new LinkedHashMap<String, String>();
		String searchType = getSearchType();

		if (StringUtil.isNotEmpty(key)) {
			// We've got a typeahead key passed in, filter to entries beginning with that key
			String tmpKey = key;
			if (isCaseInsensitive()) {
				tmpKey = tmpKey.toLowerCase();
			}

			Iterator<String> it = getOptions().keySet().iterator();
			while (it.hasNext()) {
				String mapKey = it.next();
				String tmpMapKey = mapKey;
				if (isCaseInsensitive()) {
					tmpMapKey = tmpMapKey.toLowerCase();
				}
				if (tmpMapKey.startsWith(tmpKey)) {
					retVal.put(mapKey, getOptions().get(mapKey));
				}
			}
		} else if (StringUtil.isNotEmpty(startKey)) {
			// startKey is actually a search key
			// We've got a search key passed in, so search and add all remaining entries
			String tmpSearchKey = startKey;
			if (isCaseInsensitive()) {
				tmpSearchKey = tmpSearchKey.toLowerCase();
			}

			Iterator<String> it = getOptions().keySet().iterator();
			boolean found = false;
			while (it.hasNext()) {
				String mapKey = it.next();
				String tmpMapKey = mapKey;
				if (isCaseInsensitive()) {
					tmpMapKey = tmpMapKey.toLowerCase();
				}
				if (found) {
					retVal.put(mapKey, getOptions().get(mapKey));
					found = true;
				} else {
					if (SearchType.SEARCH_MATCH.getValue().equals(searchType)) {
						if (StringUtil.equals(tmpMapKey, tmpSearchKey)) {
							retVal.put(mapKey, getOptions().get(mapKey));
							if (SearchStyle.SEARCH_JUMPTO.getValue().equals(getSearchStyle())) {
								found = true;
							}
						}
					} else if (SearchType.SEARCH_FTSEARCH.getValue().equals(searchType)) {
						if (tmpMapKey.contains(tmpSearchKey)) {
							retVal.put(mapKey, getOptions().get(mapKey));
							if (SearchStyle.SEARCH_JUMPTO.getValue().equals(getSearchStyle())) {
								found = true;
							}
						}
					} else {
						if (tmpMapKey.startsWith(tmpSearchKey)) {
							retVal.put(mapKey, getOptions().get(mapKey));
							if (SearchStyle.SEARCH_JUMPTO.getValue().equals(getSearchStyle())) {
								found = true;
							}
						}
					}
				}
			}
		} else {
			retVal.putAll(getOptions());
		}
		return retVal;
	}

	/*
	 * This method appears to be the one that is used for validation, to get an entry based on a value or values in the relevant component.
	 * The ArrayList only has values, so check values passed in and return those that exist in the options
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.extlib.component.picker.data.IPickerData#loadEntries(java.lang.Object[], java.lang.String[])
	 */
	@Override
	public List<IPickerEntry> loadEntries(final Object[] values, final String[] attributes) {
		List<IPickerEntry> entries = new ArrayList<IPickerEntry>();
		if (null != values) {
			for (int i = 0; i < values.length; i++) {
				String checkStr = values[i].toString();
				if (StringUtil.isNotEmpty(checkStr)) {
					Iterator<String> it = getOptions().keySet().iterator();
					while (it.hasNext()) {
						String mapKey = it.next();
						if (StringUtil.equals(checkStr, getOptions().get(mapKey))) {
							entries.add(new SimplePickerResult.Entry(getOptions().get(mapKey), mapKey));
							break;
						}
					}
				}
			}
		}
		return entries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.complex.ValueBindingObjectImpl#restoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void restoreState(final FacesContext _context, final Object _state) {
		Object _values[] = (Object[]) _state;
		super.restoreState(_context, _values[0]);
		options = (Map<String, String>) _values[1];
		searchType = (String) _values[2];
		caseInsensitive = (Boolean) _values[3];
		searchStyle = (String) _values[4];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.complex.ValueBindingObjectImpl#saveState(javax.faces.context.FacesContext)
	 */
	@Override
	public Object saveState(final FacesContext _context) {
		Object _values[] = new Object[5];
		_values[0] = super.saveState(_context);
		_values[1] = options;
		_values[2] = searchType;
		_values[3] = caseInsensitive;
		_values[4] = searchStyle;
		return _values;
	}

}
