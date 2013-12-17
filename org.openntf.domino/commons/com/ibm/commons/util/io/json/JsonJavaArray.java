/*
 * © Copyright IBM Corp. 2012-2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.commons.util.io.json;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class JsonJavaArray extends ArrayList<Object> implements JsonArray {
	private static final Logger log_ = Logger.getLogger(JsonArray.class.getName());
	private static final long serialVersionUID = 1L;

	public JsonJavaArray() {
	}

	public JsonJavaArray(final int initialCapacity) {
		super(initialCapacity);
	}

	public JsonJavaArray(final List<Object> list) {
		if (list != null) {
			addAll(list);
		}
	}

	// Convert to a string
	@Override
	public String toString() {
		try {
			return JsonGenerator.toJson(JsonJavaFactory.instanceEx, this);
		} catch (Exception ex) {
			log_.log(Level.WARNING, ex.getMessage(), ex);
			return "";
		}
	}

	/** @ibm-api */
	public int length() {
		return size();
	}

	/** @ibm-api */
	public String getString(final int index) {
		Object o = get(index);
		if (o != null) {
			return (String) o;
		}
		return null;
	}

	/** @ibm-api */
	public double getNumber(final int index) {
		Object o = get(index);
		if (o != null) {
			return ((Number) o).doubleValue();
		}
		return 0.0;
	}

	/** @ibm-api */
	public boolean getBoolean(final int index) {
		Object o = get(index);
		if (o != null) {
			return (Boolean) o;
		}
		return false;
	}

	/** @ibm-api */
	public JsonObject getObject(final int index) {
		Object o = get(index);
		if (o != null) {
			return (JsonObject) o;
		}
		return null;
	}

	/** @ibm-api */
	public JsonArray getArray(final int index) {
		Object o = get(index);
		if (o != null) {
			return (JsonArray) o;
		}
		return null;
	}

	/** @ibm-api */
	public void put(final int index, final Object value) {
		// Emulate JavaScript with put: add the missing elements, if needed
		while (size() <= index) {
			add((Object) null);
		}
		set(index, value);
	}

	/** @ibm-api */
	public void putString(final int index, final String value) {
		put(index, value);
	}

	/** @ibm-api */
	public void putNumber(final int index, final double value) {
		put(index, value);
	}

	/** @ibm-api */
	public void putBoolean(final int index, final boolean value) {
		put(index, value);
	}

	/** @ibm-api */
	public void putObject(final int index, final JsonObject value) {
		put(index, value);
	}

	/** @ibm-api */
	public void putArray(final int index, final JsonArray value) {
		put(index, value);
	}

	/** @ibm-api */
	public void add(final Object[] values) {
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				add(values[i]);
			}
		}
	}

	//
	// Typed helpers
	//

	/** @ibm-api */
	public String getAsString(final int index) {
		Object v = get(index);
		if (v != null) {
			return v.toString();
		}
		return null;
	}

	/** @ibm-api */
	public double getAsDouble(final int index) {
		Object v = get(index);
		if (v instanceof Number) {
			return ((Number) v).doubleValue();
		}
		if (v instanceof String) {
			return Double.parseDouble((String) v);
		}
		if (v instanceof Boolean) {
			return (Boolean) v ? 1 : 0;
		}
		return 0;
	}

	/** @ibm-api */
	public int getAsInt(final int index) {
		Object v = get(index);
		if (v instanceof Number) {
			return ((Number) v).intValue();
		}
		if (v instanceof String) {
			return Integer.parseInt((String) v);
		}
		if (v instanceof Boolean) {
			return (Boolean) v ? 1 : 0;
		}
		return 0;
	}

	/** @ibm-api */
	public long getAsLong(final JsonObject o, final int index) {
		Object v = get(index);
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		if (v instanceof String) {
			return Long.parseLong((String) v);
		}
		if (v instanceof Boolean) {
			return (Boolean) v ? 1 : 0;
		}
		return 0;
	}

	/** @ibm-api */
	public boolean getAsBoolean(final JsonObject o, final int index) {
		Object v = get(index);
		if (v instanceof Boolean) {
			return ((Boolean) v).booleanValue();
		}
		if (v instanceof String) {
			String s = (String) v;
			if (s.equalsIgnoreCase("false") || s.equals("0")) {
				return false;
			}
			return true;
		}
		if (v instanceof Number) {
			return ((Number) o).doubleValue() != 0.0;
		}
		return v != null;
	}

	/** @ibm-api */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonJavaObject getAsObject(final int index) {
		Object v = get(index);
		if (v instanceof JsonJavaObject) {
			return (JsonJavaObject) v;
		}
		if (v instanceof Map) {
			return new JsonJavaObject((Map) v);
		}
		return null;
	}

	/** @ibm-api */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getAsMap(final int index) {
		Object v = get(index);
		if (v instanceof Map) {
			return (Map) v;
		}
		return null;
	}

	/** @ibm-api */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonJavaArray getAsArray(final int index) {
		Object v = get(index);
		if (v instanceof JsonJavaArray) {
			return (JsonJavaArray) v;
		}
		if (v instanceof List) {
			return new JsonJavaArray((List) v);
		}
		return null;
	}

	/** @ibm-api */
	@SuppressWarnings("unchecked")
	public List<Object> getAsList(final int index) {
		Object v = get(index);
		if (v instanceof List) {
			return (List<Object>) v;
		}
		return null;
	}

	//
	// Extensions
	//
	/** @ibm-api */
	public Date getJavaDate(final int index) throws IOException, ParseException {
		String s = getString(index);
		if (s != null) {
			return JsonGenerator.stringToDate(s);
		}
		return null;
	}

	/** @ibm-api */
	public void putJavaDate(final int index, final Date value) throws IOException {
		if (value != null) {
			String dt = JsonGenerator.dateToString(value);
			put(index, dt);
		}
	}

}
