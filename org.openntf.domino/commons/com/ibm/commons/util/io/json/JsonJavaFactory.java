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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibm.commons.util.ArrayIterator;
import com.ibm.commons.util.StringUtil;

/**
 * This factory is used manipulate Java object.
 * <p>
 * The values are mapped by Java objects (String, Number, List<?>, Map<?,?>...). Note that the collections can either be Map or
 * JsonJavaObject.
 * </p>
 * 
 * @ibm-api
 */
public class JsonJavaFactory implements JsonFactory {

	/**
	 * Singleton instance that uses java.util.Map for collection.
	 * 
	 * @ibm-api
	 */
	public static final JsonJavaFactory instance = new JsonJavaFactory();

	/**
	 * Singleton instance that uses JsonJavaObject for collection.
	 * 
	 * @ibm-api
	 */
	public static final JsonJavaFactory instanceEx = new JsonJavaFactory() {
		@Override
		public Object createObject(final Object parent, final String propertyName) {
			return new JsonJavaObject();
		}
	};
	public static final JsonJavaFactory instanceEx2 = new JsonJavaFactory() {
		@Override
		public Object createObject(final Object parent, final String propertyName) {
			return new JsonJavaObject();
		}

		@Override
		public List<Object> createTemporaryArray(final Object parent) throws JsonException {
			return new JsonJavaArray();
		}

		@Override
		public Object createArray(final Object parent, final String propertyName, final List<Object> values) {
			if (values instanceof JsonJavaArray) {
				return values;
			}
			return new JsonJavaArray(values);
		}
	};

	public boolean supportFeature(final int feature) {
		if (feature == FEATURE_INLINEJAVASCRIPT) {
			return true;
		}
		return false;
	}

	public boolean isValidValue(final Object value) throws JsonException {
		return isNull(value) || isUndefined(value) || isBoolean(value) || isNumber(value) || isString(value) || isObject(value)
				|| isArray(value) || isJavaScriptCode(value);
	}

	public Object createNull() throws JsonException {
		return null;
	}

	public Object createUndefined() throws JsonException {
		throw new JsonException(null, "Undefined does not exist in Java");
	}

	public Object createString(final String value) throws JsonException {
		return value;
	}

	public Object createNumber(final double value) throws JsonException {
		return Double.valueOf(value);
	}

	public Object createBoolean(final boolean value) throws JsonException {
		return Boolean.valueOf(value);
	}

	public Object createObject(final Object parent, final String propertyName) throws JsonException {
		return new HashMap<String, Object>();
	}

	public Object createArray(final Object parent, final String propertyName, final List<Object> values) throws JsonException {
		return values;
	}

	public Object createJavaScriptCode(final String code) throws JsonException {
		return new JsonReference(code);
	}

	@SuppressWarnings("unchecked")//$NON-NLS-1$
	public void setProperty(final Object parent, final String propertyName, final Object value) throws JsonException {
		if (parent instanceof Map) {
			((Map<String, Object>) parent).put(propertyName, value);
		} else if (parent instanceof JsonObject) {
			((JsonObject) parent).putJsonProperty(propertyName, value);
		} else {
			throw new IllegalArgumentException(StringUtil.format("Invalid Json object, class: {0}", parent != null ? parent.getClass()
					.toString() : "null")); // $NLS-JsonJavaFactory.InvalidJsonobjectclass0-1$ $NON-NLS-2$
		}
	}

	@SuppressWarnings("unchecked")//$NON-NLS-1$
	public Object getProperty(final Object parent, final String propertyName) throws JsonException {
		if (parent instanceof Map) {
			return ((Map<String, Object>) parent).get(propertyName);
		} else if (parent instanceof JsonObject) {
			return ((JsonObject) parent).getJsonProperty(propertyName);
		} else {
			throw new IllegalArgumentException(StringUtil.format("Invalid Json object, class: {0}", parent != null ? parent.getClass()
					.toString() : "null")); // $NLS-JsonJavaFactory.InvalidJsonobjectclass0.1-1$ $NON-NLS-2$
		}
	}

	public boolean isNull(final Object value) throws JsonException {
		return value == null;
	}

	public boolean isUndefined(final Object value) {
		return false; // Doesn't exist in Java
	}

	public boolean isString(final Object value) throws JsonException {
		return value instanceof String;
	}

	public String getString(final Object value) throws JsonException {
		return (String) value;
	}

	public boolean isDate(final Object value) throws JsonException {
		return value instanceof Date;
	}

	public Date getDate(final Object value) throws JsonException {
		return (Date) value;
	}

	public boolean isNumber(final Object value) throws JsonException {
		return value instanceof Number;
	}

	public double getNumber(final Object value) throws JsonException {
		return ((Number) value).doubleValue();
	}

	public boolean isBoolean(final Object value) throws JsonException {
		return value instanceof Boolean;
	}

	public boolean getBoolean(final Object value) throws JsonException {
		return ((Boolean) value).booleanValue();
	}

	public boolean isObject(final Object value) throws JsonException {
		return (value instanceof Map) || (value instanceof JsonObject);
	}

	public boolean isJavaScriptCode(final Object value) throws JsonException {
		if (supportFeature(FEATURE_INLINEJAVASCRIPT)) {
			return value instanceof JsonReference;
		}
		return false;
	}

	public String getJavaScriptCode(final Object value) throws JsonException {
		if (supportFeature(FEATURE_INLINEJAVASCRIPT)) {
			return ((JsonReference) value).getRef();
		}
		return null;
	}

	@SuppressWarnings("unchecked")//$NON-NLS-1$
	public Iterator<String> iterateObjectProperties(final Object object) throws JsonException {
		if (object instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) object;
			return map.keySet().iterator();
		} else if (object instanceof JsonObject) {
			return ((JsonObject) object).getJsonProperties();
		} else {
			throw new IllegalArgumentException(StringUtil.format("Invalid Json object, class: {0}", object != null ? object.getClass()
					.toString() : "null")); // $NLS-JsonJavaFactory.InvalidJsonobjectclass0.2-1$ $NON-NLS-2$
		}
	}

	public boolean isArray(final Object value) throws JsonException {
		if (value == null) {
			return false;
		}
		if (value instanceof JsonArray) {
			return true;
		}
		if (value instanceof List) {
			return true;
		}
		if (value.getClass().isArray()) {
			return true;
		}
		return false;
	}

	public int getArrayCount(final Object value) throws JsonException {
		if (value instanceof JsonArray) {
			return ((JsonArray) value).length();
		}
		if (value instanceof List) {
			return ((List<?>) value).size();
		}
		return ((Object[]) value).length;
	}

	public Object getArrayItem(final Object value, final int index) throws JsonException {
		if (value instanceof JsonArray) {
			return ((JsonArray) value).get(index);
		}
		if (value instanceof List) {
			return ((List<?>) value).get(index);
		}
		return ((Object[]) value)[index];
	}

	@SuppressWarnings("unchecked")//$NON-NLS-1$
	public Iterator<Object> iterateArrayValues(final Object array) throws JsonException {
		if (array instanceof JsonArray) {
			return ((JsonArray) array).iterator();
		}
		if (array instanceof List) {
			List<Object> list = (List<Object>) array;
			return list.iterator();
		}
		return new ArrayIterator<Object>(array);
	}

	public List<Object> createTemporaryArray(final Object parent) throws JsonException {
		return new ArrayList<Object>();
	}
}