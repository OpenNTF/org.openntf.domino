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
package org.openntf.domino.xsp.helpers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.activation.UnsupportedDataTypeException;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.openntf.domino.utils.DominoUtils;

/**
 * @author Nathan T. Freeman
 * 
 *         EnumValuePickerData for use with ValuePicker control<br/>
 *         NOTE: This has not been fully tested
 */
// TODO: Remove before 3.0 - all functionality introduced in ExtLib 14
@SuppressWarnings("rawtypes")
public class EnumValuePickerData extends MapValuePickerData {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(EnumValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;
	private String enumName;
	private Boolean sorted;

	/**
	 * Constructor
	 */
	public EnumValuePickerData() {

	}

	/**
	 * Gets the name of the enum to use from the "enumName" property
	 * 
	 * @return String enum name
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public String getEnumName() {
		if (enumName != null) {
			return enumName;
		}
		ValueBinding vb = getValueBinding("enumName"); //$NON-NLS-1$
		if (vb != null) {
			String vbVal = (String) vb.getValue(getFacesContext());
			return vbVal;
		}

		return null;
	}

	/**
	 * Sets the enum name
	 * 
	 * @param enumName
	 *            String
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void setEnumName(final String enumName) {
		this.enumName = enumName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.xsp.helpers.MapValuePickerData#getOptions()
	 */
	@Override
	public Map<String, String> getOptions() {
		if (options != null) {
			return options;
		}
		setOptions();
		return options;
	}

	/**
	 * Gets whether or not the options should be sorted alphabetically, from the "sorted" property
	 * 
	 * @return boolean, whether the options should be sorted
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public boolean isSorted() {
		if (sorted != null) {
			return sorted;
		}
		ValueBinding vb = getValueBinding("sorted");// $NON-NLS-1$
		if (vb != null) {
			Boolean b = (Boolean) vb.getValue(getFacesContext());
			if (b != null) {
				return b;
			}
		}
		return false;
	}

	/**
	 * Sets whether the options should be sorted
	 * 
	 * @param sorted
	 *            boolean
	 * @since org.openntf.domino.xsp 5.0.0
	 */
	public void setSorted(final boolean sorted) {
		this.sorted = sorted;
	}

	/**
	 * Loads the options, creating a LinkedHashMap where the key is the enum name and the value is the enum class + " " + the enum name
	 * 
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	@SuppressWarnings("unchecked")
	public void setOptions() {
		Map<String, String> opts;
		if (sorted) {
			opts = new TreeMap<String, String>();
		} else {
			opts = new LinkedHashMap<String, String>();
		}
		try {
			Class<Enum> enumClass = (Class<Enum>) Class.forName(getEnumName());
			if (enumClass.isEnum()) {
				Enum[] enums = enumClass.getEnumConstants();
				for (Enum e : enums) {
					opts.put(e.name(), enumClass.getName() + " " + e.name());
				}
			} else {
				throw new UnsupportedDataTypeException("Value is not an Enum");
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		super.setOptions(opts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.xsp.helpers.MapValuePickerData#restoreState(javax.faces.context.FacesContext, java.lang.Object)
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
		enumName = (String) _values[5];
		sorted = (Boolean) _values[6];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.xsp.helpers.MapValuePickerData#saveState(javax.faces.context.FacesContext)
	 */
	@Override
	public Object saveState(final FacesContext _context) {
		Object _values[] = new Object[7];
		_values[0] = super.saveState(_context);
		_values[1] = options;
		_values[2] = searchType;
		_values[3] = searchStyle;
		_values[4] = caseInsensitive;
		_values[5] = enumName;
		_values[6] = sorted;
		return _values;
	}

}
