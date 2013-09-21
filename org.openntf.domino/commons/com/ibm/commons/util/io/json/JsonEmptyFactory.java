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
import java.util.Iterator;
import java.util.List;

import com.ibm.commons.util.EmptyIterator;

/**
 * This factory is used to test if a string is a valid JSON string.
 * <p>
 * No actual object is created by this factory.
 * </p>
 * 
 * @ibm-api
 */
public class JsonEmptyFactory implements JsonFactory {

	public static final JsonEmptyFactory instance = new JsonEmptyFactory();

	public boolean supportFeature(final int feature) throws JsonException {
		return true;
	}

	public boolean isValidValue(final Object value) throws JsonException {
		return true;
	}

	public Object createNull() {
		return null;
	}

	public Object createUndefined() {
		return null;
	}

	public Object createString(final String value) {
		return null;
	}

	public Object createJavaScriptCode(final String code) throws JsonException {
		return null;
	}

	public Object createNumber(final double value) {
		return null;
	}

	public Object createBoolean(final boolean value) {
		return null;
	}

	public Object createObject(final Object parent, final String propertyName) {
		return null;
	}

	public Object createArray(final Object parent, final String propertyName, final List<Object> values) {
		return null;
	}

	public void setProperty(final Object parent, final String propertyName, final Object value) {
	}

	public Object getProperty(final Object parent, final String propertyName) throws JsonException {
		return null;
	}

	// Not used...
	public boolean isNull(final Object value) throws JsonException {
		return false;
	}

	public boolean isUndefined(final Object value) throws JsonException {
		return false;
	}

	public boolean isString(final Object value) throws JsonException {
		return false;
	}

	public String getString(final Object value) throws JsonException {
		return null;
	}

	public boolean isDate(final Object value) throws JsonException {
		return false;
	}

	public Date getDate(final Object value) throws JsonException {
		return null;
	}

	public boolean isJavaScriptCode(final Object value) throws JsonException {
		return false;
	}

	public String getJavaScriptCode(final Object value) throws JsonException {
		return null;
	}

	public boolean isNumber(final Object value) throws JsonException {
		return false;
	}

	public double getNumber(final Object value) throws JsonException {
		return 0;
	}

	public boolean isBoolean(final Object value) throws JsonException {
		return false;
	}

	public boolean getBoolean(final Object value) throws JsonException {
		return false;
	}

	public boolean isObject(final Object value) throws JsonException {
		return false;
	}

	@SuppressWarnings("unchecked")//$NON-NLS-1$
	public Iterator<String> iterateObjectProperties(final Object object) throws JsonException {
		return EmptyIterator.getInstance();
	}

	public boolean isArray(final Object value) throws JsonException {
		return false;
	}

	public int getArrayCount(final Object value) throws JsonException {
		return 0;
	}

	@SuppressWarnings("unchecked")
	public Iterator<Object> iterateArrayValues(final Object array) throws JsonException {
		return EmptyIterator.getInstance();
	}

	public List<Object> createTemporaryArray(final Object parent) throws JsonException {
		return new ArrayList<Object>();
	}
}
