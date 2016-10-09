package org.openntf.domino.rest.service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.apache.wink.common.internal.utils.StringUtils;
import org.openntf.domino.types.CaseInsensitiveString;

public enum Parameters {
	ID, KEY, TYPE, EDGES, VERTICES, COUNTS, DESC, FILTERKEY, FILTERVALUE, LABEL, DIRECTION, START, COUNT, ORDERBY, PROPS, HIDEPROPS, INPROPS, OUTPROPS, COMMAND, SWITCH, PARTIALKEY, PARTIALVALUE, STARTSKEY, STARTSVALUE, ADD, REMOVE;

	public static ParamMap toParamMap(UriInfo uriInfo) {
		ParamMap result = new ParamMap();
		MultivaluedMap<String, String> mm = uriInfo.getQueryParameters(true);
		for (String key : mm.keySet()) {
			List<String> values = mm.get(key);
			if (values != null) {
				List<String> newValues = new ArrayList<String>();
				try {
					for (String value : values) {
						if (value != null) {
							if (value.contains(",")) {
								String[] sepstrs = StringUtils.fastSplit(value, ",");
								for (String sepstr : sepstrs) {
									newValues.add(sepstr);
								}
							} else {
								newValues.add(value);
							}
						}
					}
					Parameters param = Parameters.valueOf(key.toUpperCase());
					result.put(param, newValues);
				} catch (IllegalArgumentException iae) {
					// don't sweat it. Unused parameters are ignored by the
					// engine
				}
			}
		}
		return result;
	}

	public static class ParamMap extends EnumMap<Parameters, List<String>> {
		private static final long serialVersionUID = 1L;

		public ParamMap() {
			super(Parameters.class);
		}

		public List<CharSequence> getTypes() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.TYPE));
		}

		public List<CharSequence> getKeys() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.KEY));
		}

		public List<CharSequence> getProperties() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.PROPS));
		}

		public List<CharSequence> getHideProperties() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.HIDEPROPS));
		}

		public List<CharSequence> getFilterKeys() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.FILTERKEY));
		}

		public List<CharSequence> getFilterValues() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.FILTERVALUE));
		}

		public List<CharSequence> getPartialKeys() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.PARTIALKEY));
		}

		public List<CharSequence> getPartialValues() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.PARTIALVALUE));
		}

		public List<CharSequence> getStartsKeys() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.STARTSKEY));
		}

		public List<CharSequence> getStartsValues() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.STARTSVALUE));
		}

		public List<CharSequence> getOrderBys() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.ORDERBY));
		}

		public int getStart() {
			List<String> raw = get(Parameters.START);
			if (raw == null)
				return 0;
			String intStr = raw.get(0);
			if (intStr == null || intStr.length() == 0) {
				return 0;
			}
			return Integer.valueOf(intStr);
		}

		public int getCount() {
			List<String> raw = get(Parameters.COUNT);
			if (raw == null)
				return 0;
			String intStr = raw.get(0);
			if (intStr == null || intStr.length() == 0) {
				return 0;
			}
			return Integer.valueOf(intStr);
		}

		public List<CharSequence> getInProperties() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.INPROPS));
		}

		public List<CharSequence> getOutProperties() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.OUTPROPS));
		}

		public List<CharSequence> getLabels() {
			return CaseInsensitiveString.toCaseInsensitive(get(Parameters.LABEL));
		}

		public boolean getIncludeVertices() {
			return get(Parameters.VERTICES) != null;
		}

		public boolean getIncludeEdges() {
			return get(Parameters.EDGES) != null;
		}

		public boolean getIncludeCounts() {
			return get(Parameters.COUNTS) != null;
		}

		public boolean getDescending() {
			return get(Parameters.DESC) != null;
		}

	}
}
