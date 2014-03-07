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

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.component.picker.data.IPickerEntry;
import com.ibm.xsp.extlib.component.picker.data.IPickerOptions;
import com.ibm.xsp.extlib.component.picker.data.IPickerResult;
import com.ibm.xsp.extlib.component.picker.data.IValuePickerData;
import com.ibm.xsp.extlib.component.picker.data.SimplePickerResult;

/**
 * @author Nathan T. Freeman
 * 
 */
public class MapPickerData implements IValuePickerData, Serializable {
	private static final Logger log_ = Logger.getLogger(MapPickerData.class.getName());
	private static final long serialVersionUID = 1L;

	protected Map<String, String> options_;

	public MapPickerData() {

	}

	// Mapo should be label / value
	public MapPickerData(final Map<String, String> options) {
		options_ = options;
	}

	public String[] getSourceLabels() {
		return null;
	}

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

	private LinkedHashMap<String, String> filteredOptions(final String key, final String startKey, final int start, final int searchIndex) {
		LinkedHashMap<String, String> retVal = new LinkedHashMap<String, String>();
		if (StringUtil.isNotEmpty(key)) {
			// We've got a typeahead key passed in, filter to entries beginning with that key
			Iterator<String> it = options_.keySet().iterator();
			while (it.hasNext()) {
				String mapKey = it.next();
				if (mapKey.toLowerCase().contains(key.toLowerCase())/* StringUtil.startsWithIgnoreCase(mapKey, key) */) {
					retVal.put(mapKey, options_.get(mapKey));
				}
			}
		} else if (StringUtil.isNotEmpty(startKey)) {
			// We've got a search key passed in, jump to that entry and add all remaining entries
			Iterator<String> it = options_.keySet().iterator();
			boolean found = false;
			while (it.hasNext()) {
				String mapKey = it.next();
				if (found || StringUtil.startsWithIgnoreCase(mapKey, startKey)) {
					retVal.put(mapKey, options_.get(mapKey));
					found = true;
				}
			}
		} else {
			retVal.putAll(options_);
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
	public List<IPickerEntry> loadEntries(final Object[] values, final String[] attributes) {
		List<IPickerEntry> entries = new ArrayList<IPickerEntry>();
		if (null != values) {
			for (int i = 0; i < values.length; i++) {
				String checkStr = values[i].toString();
				if (StringUtil.isNotEmpty(checkStr)) {
					Iterator<String> it = options_.keySet().iterator();
					while (it.hasNext()) {
						String mapKey = it.next();
						if (StringUtil.equals(checkStr, options_.get(mapKey))) {
							entries.add(new SimplePickerResult.Entry(options_.get(mapKey), mapKey));
							break;
						}
					}
				}
			}
		}
		return entries;
	}

}
