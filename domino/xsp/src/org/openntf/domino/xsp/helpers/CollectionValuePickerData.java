/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Nathan T. Freeman
 * 
 *         CollectionValuePickerData, for use with ValuePicker control
 */
@SuppressWarnings("rawtypes")
public class CollectionValuePickerData extends MapValuePickerData {
	//private static final Logger log_ = Logger.getLogger(CollectionValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;
	public Collection<String> collection;

	/**
	 * Constructor
	 */
	public CollectionValuePickerData() {

	}

	/**
	 * Gets the Collection from the "collection" property, throwing an error if it is not a valid Collection
	 * 
	 * @return Collection<String> of values to use in the picker
	 * @since org.openntf.domino.xsp 4.5.0
	 */
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

	/**
	 * Loads a Collection into the class instance
	 * 
	 * @param collection
	 *            Collection<String> of values to use in the picker
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void setCollection(final Collection<String> collection) {
		this.collection = collection;
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
	 * Loads the options, converting the Collection to a LinkedHashMap, where the key and value are the same
	 * 
	 * @since org.openntf.domino.xsp 4.5.0
	 */
	public void setOptions() {
		Map<String, String> opts;
		opts = new LinkedHashMap<String, String>();
		for (String e : getCollection()) {
			opts.put(e, e);
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
		collection = (Collection) _values[5];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.xsp.helpers.MapValuePickerData#saveState(javax.faces.context.FacesContext)
	 */
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
