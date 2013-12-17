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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * JSON factory wrapper.
 * <p>
 * This class wraps an existing JSON factory and delegates to it. This can be used for enhancing existing factory with new features without
 * having to inherit from that class.
 * </p>
 * 
 * @ibm-api
 */
public class JsonFactoryDelegator implements JsonFactory {

	private JsonFactory delegate;

	public JsonFactoryDelegator(final JsonFactory delegate) {
		this.delegate = delegate;
	}

	public boolean supportFeature(final int feature) throws JsonException {
		return delegate.supportFeature(feature);
	}

	public boolean isValidValue(final Object value) throws JsonException {
		return delegate.isValidValue(value);
	}

	public Object createArray(final Object parent, final String propertyName, final List<Object> values) throws JsonException {
		return delegate.createArray(parent, propertyName, values);
	}

	public Object createBoolean(final boolean value) throws JsonException {
		return delegate.createBoolean(value);
	}

	public Object createNull() throws JsonException {
		return delegate.createNull();
	}

	public Object createUndefined() throws JsonException {
		return delegate.createUndefined();
	}

	public Object createNumber(final double value) throws JsonException {
		return delegate.createNumber(value);
	}

	public Object createObject(final Object parent, final String propertyName) throws JsonException {
		return delegate.createObject(parent, propertyName);
	}

	public Object createString(final String value) throws JsonException {
		return delegate.createString(value);
	}

	public Object createJavaScriptCode(final String code) throws JsonException {
		return delegate.createJavaScriptCode(code);
	}

	public boolean getBoolean(final Object value) throws JsonException {
		return delegate.getBoolean(value);
	}

	public double getNumber(final Object value) throws JsonException {
		return delegate.getNumber(value);
	}

	public Object getProperty(final Object parent, final String propertyName) throws JsonException {
		return delegate.getProperty(parent, propertyName);
	}

	public String getString(final Object value) throws JsonException {
		return delegate.getString(value);
	}

	public Date getDate(final Object value) throws JsonException {
		return delegate.getDate(value);
	}

	public String getJavaScriptCode(final Object value) throws JsonException {
		return getJavaScriptCode(value);
	}

	public boolean isArray(final Object value) throws JsonException {
		return delegate.isArray(value);
	}

	public int getArrayCount(final Object value) throws JsonException {
		return delegate.getArrayCount(value);
	}

	public boolean isBoolean(final Object value) throws JsonException {
		return delegate.isBoolean(value);
	}

	public boolean isNull(final Object value) throws JsonException {
		return delegate.isNull(value);
	}

	public boolean isUndefined(final Object value) throws JsonException {
		return delegate.isUndefined(value);
	}

	public boolean isNumber(final Object value) throws JsonException {
		return delegate.isNumber(value);
	}

	public boolean isObject(final Object value) throws JsonException {
		return delegate.isObject(value);
	}

	public boolean isString(final Object value) throws JsonException {
		return delegate.isString(value);
	}

	public boolean isDate(final Object value) throws JsonException {
		return delegate.isDate(value);
	}

	public boolean isJavaScriptCode(final Object value) throws JsonException {
		return delegate.isJavaScriptCode(value);
	}

	public Iterator<Object> iterateArrayValues(final Object array) throws JsonException {
		return delegate.iterateArrayValues(array);
	}

	public Iterator<String> iterateObjectProperties(final Object object) throws JsonException {
		return delegate.iterateObjectProperties(object);
	}

	public void setProperty(final Object parent, final String propertyName, final Object value) throws JsonException {
		delegate.setProperty(parent, propertyName, value);
	}

	public List<Object> createTemporaryArray(final Object parent) throws JsonException {
		return delegate.createTemporaryArray(parent);
	}
}
