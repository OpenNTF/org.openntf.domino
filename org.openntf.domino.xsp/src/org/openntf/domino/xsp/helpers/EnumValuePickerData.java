/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
@SuppressWarnings("rawtypes")
public class EnumValuePickerData extends MapValuePickerData {
	private static final Logger log_ = Logger.getLogger(EnumValuePickerData.class.getName());
	private static final long serialVersionUID = 1L;
	private Class<Enum> source_;

	public EnumValuePickerData() {

	}

	public EnumValuePickerData(final Class<Enum> source, final boolean sorted) {
		source_ = source;
		Map<String, String> opts;
		if (sorted) {
			opts = new TreeMap<String, String>();
		} else {
			opts = new LinkedHashMap<String, String>();
		}
		if (source.isEnum()) {
			Enum[] enums = source.getEnumConstants();
			for (Enum e : enums) {
				opts.put(e.name(), source.getName() + " " + e.name());
			}
		}
		setOptions(opts);
	}

}
