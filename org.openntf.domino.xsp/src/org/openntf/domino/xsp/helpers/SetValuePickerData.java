/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
@SuppressWarnings("rawtypes")
public class SetValuePickerData extends MapValuePickerData {
	private static final Logger log_ = Logger.getLogger(SetValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;

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

}
