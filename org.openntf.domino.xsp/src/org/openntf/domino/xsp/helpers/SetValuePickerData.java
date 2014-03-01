/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.activation.UnsupportedDataTypeException;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Nathan T. Freeman
 * 
 */
@SuppressWarnings("rawtypes")
public class SetValuePickerData extends MapValuePickerData {
	private static final Logger log_ = Logger.getLogger(SetValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;
	public Set set;

	public SetValuePickerData() {

	}

	public SetValuePickerData(final Set<String> source) {
		Map<String, String> opts;
		opts = new LinkedHashMap<String, String>();
		for (String e : source) {
			opts.put(e, e);
		}
		setOptions(opts);
	}

	@SuppressWarnings("unchecked")
	public Set<String> getSet() {
		if (set != null) {
			return set;
		}
		ValueBinding vb = getValueBinding("set"); //$NON-NLS-1$
		if (vb != null) {
			Object vbVal = vb.getValue(getFacesContext());
			if (vbVal instanceof Set) {
				return (Set<String>) vbVal;
			} else {
				try {
					throw new UnsupportedDataTypeException("Value is not a Set");
				} catch (UnsupportedDataTypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public void setList(final Set<String> set) {
		this.set = set;
	}

	@Override
	public Map<String, String> getOptions() {
		if (options != null) {
			return options;
		}
		setOptions();
		return options;
	}

	public void setOptions() {
		Map<String, String> opts;
		opts = new LinkedHashMap<String, String>();
		for (String e : getSet()) {
			opts.put(e, e);
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
		set = (Set) _values[4];
	}

	@Override
	public Object saveState(final FacesContext _context) {
		Object _values[] = new Object[5];
		_values[0] = super.saveState(_context);
		_values[1] = options;
		_values[2] = searchType;
		_values[3] = caseInsensitive;
		_values[4] = set;
		return _values;
	}

}
