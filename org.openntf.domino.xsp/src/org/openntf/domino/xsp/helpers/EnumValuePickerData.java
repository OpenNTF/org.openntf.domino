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
 */
@SuppressWarnings("rawtypes")
public class EnumValuePickerData extends MapValuePickerData {
	private static final Logger log_ = Logger.getLogger(EnumValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;
	private String enumName;
	private Boolean sorted;

	public EnumValuePickerData() {

	}

	@SuppressWarnings("unchecked")
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

	public void setEnumName(final String enumName) {
		this.enumName = enumName;
	}

	@Override
	public Map<String, String> getOptions() {
		if (options != null) {
			return options;
		}
		setOptions();
		return options;
	}

	public boolean isSorted() {
		if (sorted != null) {
			return sorted;
		}
		ValueBinding vb = getValueBinding("sorted"); // $NON-NLS-1$
		if (vb != null) {
			Boolean b = (Boolean) vb.getValue(getFacesContext());
			if (b != null) {
				return b;
			}
		}
		return false;
	}

	public void setSorted(final boolean sorted) {
		this.sorted = sorted;
	}

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

	@Override
	public void restoreState(final FacesContext _context, final Object _state) {
		Object _values[] = (Object[]) _state;
		super.restoreState(_context, _values[0]);
		options = (Map<String, String>) _values[1];
		searchType = (String) _values[2];
		caseInsensitive = (Boolean) _values[3];
		enumName = (String) _values[4];
		sorted = (Boolean) _values[5];
	}

	@Override
	public Object saveState(final FacesContext _context) {
		Object _values[] = new Object[5];
		_values[0] = super.saveState(_context);
		_values[1] = options;
		_values[2] = searchType;
		_values[3] = caseInsensitive;
		_values[4] = enumName;
		_values[5] = sorted;
		return _values;
	}

}
