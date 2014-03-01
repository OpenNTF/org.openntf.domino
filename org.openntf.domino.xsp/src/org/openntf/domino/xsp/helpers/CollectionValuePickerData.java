/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.activation.UnsupportedDataTypeException;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Nathan T. Freeman
 * 
 */
@SuppressWarnings("rawtypes")
public class CollectionValuePickerData extends MapValuePickerData {
	private static final Logger log_ = Logger.getLogger(CollectionValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;
	public Collection<String> collection;

	public CollectionValuePickerData() {

	}

	@SuppressWarnings("unchecked")
	public Collection<String> getCollection() {
		if (collection != null) {
			return collection;
		}
		ValueBinding vb = getValueBinding("collection"); //$NON-NLS-1$
		if (vb != null) {
			Object vbVal = vb.getValue(getFacesContext());
			if (vbVal instanceof Collection) {
				return (Collection<String>) vbVal;
			} else {
				try {
					throw new UnsupportedDataTypeException("Value is not a Collection");
				} catch (UnsupportedDataTypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public void setCollection(final Collection<String> collection) {
		this.collection = collection;
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
		for (String e : getCollection()) {
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
		searchStyle = (String) _values[4];
		collection = (Collection) _values[5];
	}

	@Override
	public Object saveState(final FacesContext _context) {
		Object _values[] = new Object[6];
		_values[0] = super.saveState(_context);
		_values[1] = options;
		_values[2] = searchType;
		_values[3] = caseInsensitive;
		_values[4] = searchStyle;
		_values[5] = collection;
		return _values;
	}

}
