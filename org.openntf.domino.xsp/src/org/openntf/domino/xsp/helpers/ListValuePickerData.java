/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
@SuppressWarnings("rawtypes")
public class ListValuePickerData extends MapValuePickerData {
	private static final Logger log_ = Logger.getLogger(ListValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;

	public ListValuePickerData() {

	}

	public ListValuePickerData(final List<String> source) {
		Map<String, String> opts;
		opts = new LinkedHashMap<String, String>();
		for (String e : source) {
			opts.put(e, e);
		}
		setOptions(opts);
	}

}
