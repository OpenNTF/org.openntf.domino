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
import java.util.Iterator;
import java.util.List;

import com.ibm.commons.util.EmptyIterator;



/**
 * This factory is used to test if a string is a valid JSON string.
 * <p>No actual object is created by this factory.</p>
 * 
 * @ibm-api
 */
public class JsonEmptyFactory implements JsonFactory {
	
	public static final JsonEmptyFactory instance = new JsonEmptyFactory();
	
	public boolean supportFeature(int feature) throws JsonException {
		return true;
	}

	public boolean isValidValue(Object value) throws JsonException {
		return true;
	}

	public Object createNull() {
		return null;
	}
	
	public Object createUndefined() {
		return null;
	}

	public Object createString(String value) {
		return null;
	}
	
	public Object createJavaScriptCode(String code) throws JsonException {
		return null;
	}

	public Object createNumber(double value) {
		return null;
	}
	
	public Object createBoolean(boolean value) {
		return null;
	}

	public Object createObject(Object parent, String propertyName) {
		return null;
	}
	
	public Object createArray(Object parent, String propertyName, List<Object> values) {
		return null;
	}
	
	public void setProperty(Object parent, String propertyName, Object value) {
	}
	
	public Object getProperty(Object parent, String propertyName) throws JsonException {
		return null;
	}
	
	// Not used...
	public boolean isNull(Object value) throws JsonException {
		return false;
	}	
	public boolean isUndefined(Object value) throws JsonException {
		return false;
	}
	public boolean isString(Object value) throws JsonException {
		return false;
	}
	public String getString(Object value) throws JsonException {
		return null;
	}

	public boolean isJavaScriptCode(Object value) throws JsonException {
		return false;
	}

	public String getJavaScriptCode(Object value) throws JsonException {
		return null;
	}

	public boolean isNumber(Object value) throws JsonException {
		return false;
	}
	public double getNumber(Object value) throws JsonException {
		return 0;
	}
	public boolean isBoolean(Object value) throws JsonException {
		return false;
	}
	public boolean getBoolean(Object value) throws JsonException {
		return false;
	}
	public boolean isObject(Object value) throws JsonException {
		return false;
	}
	@SuppressWarnings("unchecked") //$NON-NLS-1$
	public Iterator<String> iterateObjectProperties(Object object) throws JsonException {
		return EmptyIterator.getInstance();
	}
	public boolean isArray(Object value) throws JsonException {
		return false;
	}
	public int getArrayCount(Object value) throws JsonException {
		return 0;
	}
	@SuppressWarnings("unchecked")
	public Iterator<Object> iterateArrayValues(Object array) throws JsonException {
		return EmptyIterator.getInstance();
	}
	
    public List<Object> createTemporaryArray(Object parent) throws JsonException {
        return new ArrayList<Object>();
    }
}
