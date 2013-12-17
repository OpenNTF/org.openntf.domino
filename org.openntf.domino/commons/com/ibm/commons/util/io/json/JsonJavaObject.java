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
import java.io.Serializable;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//import com.ibm.commons.Platform;

/**
 * Default JSON implementation for a JsonObject.
 * <p>
 * It can be used instead of Maps as it proposes some very convenient methods for accessing the properties.<br>
 * This implementation internal uses a Map for storing the properties.
 * </p>
 * 
 * @ibm-api
 */
public class JsonJavaObject extends HashMap<String, Object> implements JsonObject, Serializable {
	private static final Logger log_ = Logger.getLogger(JsonJavaObject.class.getName());
	private static final long serialVersionUID = 1L;

	public JsonJavaObject() {
	}

	@SuppressWarnings("unchecked")
	public JsonJavaObject(final Map<String, Object> props) {
		super(props != null ? props : Collections.EMPTY_MAP);
	}

	public JsonJavaObject(final Object... properties) {
		if (properties != null) {
			for (int i = 0; i < properties.length; i += 2) {
				String name = (String) properties[i];
				Object value = properties[i + 1];
				put(name, value);
			}
		}
	}

	// Convert to a string
	@Override
	public String toString() {
		try {
			return JsonGenerator.toJson(JsonJavaFactory.instanceEx, this);
		} catch (Exception ex) {
			log_.log(Level.WARNING, ex.getMessage(), ex);
			//			Platform.getInstance().log(ex);
			return "";
		}
	}

	// Interface implementation
	public final Iterator<String> getJsonProperties() {
		return getProperties();
	}

	public final Object getJsonProperty(final String property) {
		return get(property);
	}

	public final void putJsonProperty(final String property, final Object value) {
		put(property, value);
	}

	// Json Object implementation
	/** @ibm-api */
	@Override
	public boolean isEmpty() {
		return super.isEmpty();
	}

	/** @ibm-api */
	public Iterator<String> getProperties() {
		return keySet().iterator();
	}

	/** @ibm-api */
	public Object get(final String property) {
		return super.get(property);
	}

	/** @ibm-api */
	public String getString(final String property) {
		Object o = get(property);
		if (o != null) {
			return o.toString();
		}
		return null;
	}

	/** @ibm-api */
	public int getInt(final String property) {
		Object o = get(property);
		if (o != null) {
			return ((Number) o).intValue();
		}
		return 0;
	}

	/** @ibm-api */
	public long getLong(final String property) {
		Object o = get(property);
		if (o != null) {
			return ((Number) o).longValue();
		}
		return 0L;
	}

	/** @ibm-api */
	public double getDouble(final String property) {
		Object o = get(property);
		if (o != null) {
			return ((Number) o).doubleValue();
		}
		return 0.0;
	}

	/** @ibm-api */
	public boolean getBoolean(final String property) {
		Object o = get(property);
		if (o != null) {
			return ((Boolean) o).booleanValue();
		}
		return false;
	}

	/** @ibm-api */
	public JsonJavaObject getJsonObject(final String property) {
		Object o = get(property);
		return (JsonJavaObject) o;
	}

	/** @ibm-api */
	public void remove(final String property) {
		super.remove(property);
	}

	/** @ibm-api */
	public void removeAll() {
		super.clear();
	}

	/** @ibm-api */
	@Override
	public Object put(final String property, final Object value) {
		return super.put(property, value);
	}

	/** @ibm-api */
	public void putString(final String property, final Object value) {
		putString(property, value != null ? value.toString() : null);
	}

	/** @ibm-api */
	public void putString(final String property, final String value) {
		put(property, value);
	}

	/** @ibm-api */
	public void putInt(final String property, final int value) {
		put(property, value);
	}

	/** @ibm-api */
	public void putLong(final String property, final long value) {
		put(property, value);
	}

	/** @ibm-api */
	public void putDouble(final String property, final double value) {
		put(property, value);
	}

	/** @ibm-api */
	public void putBoolean(final String property, final boolean value) {
		put(property, Boolean.valueOf(value));
	}

	/** @ibm-api */
	public void putObject(final String property, final Object value) {
		put(property, value);
	}

	/** @ibm-api */
	public void putMap(final String property, final Map<String, Object> value) {
		put(property, value);
	}

	/** @ibm-api */
	public void putArray(final String property, final JsonJavaArray value) {
		put(property, value);
	}

	/** @ibm-api */
	public void putList(final String property, final List<Object> value) {
		put(property, value);
	}

	//
	// Typed helpers
	//

	/** @ibm-api */
	public String getAsString(final String property) {
		Object v = get(property);
		if (v != null) {
			return v.toString();
		}
		return null;
	}

	/** @ibm-api */
	public double getAsDouble(final String property) {
		Object v = get(property);
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
	public int getAsInt(final String property) {
		Object v = get(property);
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
	public long getAsLong(final JsonObject o, final String property) {
		Object v = get(property);
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
	public boolean getAsBoolean(final JsonObject o, final String property) {
		Object v = get(property);
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
	public JsonJavaObject getAsObject(final String property) {
		Object v = get(property);
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
	public Map<String, Object> getAsMap(final String property) {
		Object v = get(property);
		if (v instanceof Map) {
			return (Map) v;
		}
		return null;
	}

	/** @ibm-api */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonJavaArray getAsArray(final String property) {
		Object v = get(property);
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
	public List<Object> getAsList(final String property) {
		Object v = get(property);
		if (v instanceof List) {
			return (List<Object>) v;
		}
		return null;
	}

	//
	// Extensions
	//
	/** @ibm-api */
	public Date getJavaDate(final String property) throws IOException, ParseException {
		String s = getString(property);
		if (s != null) {
			return JsonGenerator.stringToDate(s);
		}
		return null;
	}

	/** @ibm-api */
	public void putJavaDate(final String property, final Date value) throws IOException {
		if (value != null) {
			String dt = JsonGenerator.dateToString(value);
			put(property, dt);
		}
	}
}
