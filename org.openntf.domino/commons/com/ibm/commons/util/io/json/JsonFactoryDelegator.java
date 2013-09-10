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

import java.util.Iterator;
import java.util.List;

/**
 * JSON factory wrapper.
 * <p> 
 * This class wraps an existing JSON factory and delegates to it. This can be
 * used for enhancing existing factory with new features without having to
 * inherit from that class.
 * </p>
 * @ibm-api
 */
public class JsonFactoryDelegator implements JsonFactory {

	private JsonFactory delegate;

	public JsonFactoryDelegator(JsonFactory delegate) {
		this.delegate = delegate;
	}

	public boolean supportFeature(int feature) throws JsonException {
		return delegate.supportFeature(feature);
	}

	public boolean isValidValue(Object value) throws JsonException {
		return delegate.isValidValue(value);
	}

	public Object createArray(Object parent, String propertyName,
			List<Object> values) throws JsonException {
		return delegate.createArray(parent, propertyName, values);
	}

	public Object createBoolean(boolean value) throws JsonException {
		return delegate.createBoolean(value);
	}

	public Object createNull() throws JsonException {
		return delegate.createNull();
	}

	public Object createUndefined() throws JsonException {
		return delegate.createUndefined();
	}

	public Object createNumber(double value) throws JsonException {
		return delegate.createNumber(value);
	}

	public Object createObject(Object parent, String propertyName)
			throws JsonException {
		return delegate.createObject(parent, propertyName);
	}

	public Object createString(String value) throws JsonException {
		return delegate.createString(value);
	}

	public Object createJavaScriptCode(String code) throws JsonException {
		return delegate.createJavaScriptCode(code);
	}

	public boolean getBoolean(Object value) throws JsonException {
		return delegate.getBoolean(value);
	}

	public double getNumber(Object value) throws JsonException {
		return delegate.getNumber(value);
	}

	public Object getProperty(Object parent, String propertyName)
			throws JsonException {
		return delegate.getProperty(parent, propertyName);
	}

	public String getString(Object value) throws JsonException {
		return delegate.getString(value);
	}

	public String getJavaScriptCode(Object value) throws JsonException {
		return getJavaScriptCode(value);
	}

	public boolean isArray(Object value) throws JsonException {
		return delegate.isArray(value);
	}

	public int getArrayCount(Object value) throws JsonException {
		return delegate.getArrayCount(value);
	}

	public boolean isBoolean(Object value) throws JsonException {
		return delegate.isBoolean(value);
	}

	public boolean isNull(Object value) throws JsonException {
		return delegate.isNull(value);
	}

	public boolean isUndefined(Object value) throws JsonException {
		return delegate.isUndefined(value);
	}

	public boolean isNumber(Object value) throws JsonException {
		return delegate.isNumber(value);
	}

	public boolean isObject(Object value) throws JsonException {
		return delegate.isObject(value);
	}

	public boolean isString(Object value) throws JsonException {
		return delegate.isString(value);
	}

	public boolean isJavaScriptCode(Object value) throws JsonException {
		return delegate.isJavaScriptCode(value);
	}

	public Iterator<Object> iterateArrayValues(Object array)
			throws JsonException {
		return delegate.iterateArrayValues(array);
	}

	public Iterator<String> iterateObjectProperties(Object object)
			throws JsonException {
		return delegate.iterateObjectProperties(object);
	}

	public void setProperty(Object parent, String propertyName, Object value)
			throws JsonException {
		delegate.setProperty(parent, propertyName, value);
	}

    public List<Object> createTemporaryArray(Object parent) throws JsonException {
        return delegate.createTemporaryArray(parent);
    }
}
