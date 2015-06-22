package org.openntf.domino.rest.service;

import java.util.EnumMap;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public enum Parameters {
	ID, TYPE, EDGES, FILTER, LABEL, DIRECTION, START, COUNT, ORDERBY, PROPS, INPROPS, OUTPROPS, COMMAND, SWITCH;

	public static ParamMap toParamMap(UriInfo uriInfo) {
		ParamMap result = new ParamMap();
		MultivaluedMap<String, String> mm = uriInfo.getQueryParameters(true);
		for (String key : mm.keySet()) {
			List<String> values = mm.get(key);
			if (values != null) {
				try {
					Parameters param = Parameters.valueOf(key.toUpperCase());
					result.put(param, values);
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

	}
}
