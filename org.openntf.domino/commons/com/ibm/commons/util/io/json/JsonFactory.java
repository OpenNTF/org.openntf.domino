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
 * JSON factory.
 * <p>
 * This factory links the JSON classes with the actual physical objects. It is used by
 * both the parser and the generator to access object properties and create new instance.
 * Any kind of object hierarchy can be handled through implementations of the factory. 
 * </p>
 * @ibm-api
 */
public interface JsonFactory {
	
	public static final int FEATURE_INLINEJAVASCRIPT	= 1;
	
	public boolean supportFeature(int feature) throws JsonException;

	/** @ibm-api */
	public Object createNull() throws JsonException;

	/** @ibm-api */
	public Object createUndefined() throws JsonException;
	
	/** @ibm-api */
	public Object createString(String value) throws JsonException;
	
	/** @ibm-api */
	public Object createNumber(double value) throws JsonException;
	
	/** @ibm-api */
	public Object createBoolean(boolean value) throws JsonException;
	
	/** @ibm-api */
	public Object createObject(Object parent, String propertyName) throws JsonException;
	
	/** @ibm-api */
	public Object createArray(Object parent, String propertyName, List<Object> values) throws JsonException;

	/** @ibm-api */
	public Object createJavaScriptCode(String code) throws JsonException;
	
	/** @ibm-api */
	public void setProperty(Object parent, String propertyName, Object value) throws JsonException;	

	
	
	// ====================================================================
	// Object introspectors used by the generator
	
	/** @ibm-api */
	public Object getProperty(Object parent, String propertyName) throws JsonException;

	/** @ibm-api */
	public boolean isValidValue(Object value) throws JsonException;
	
	/** @ibm-api */
	public boolean isNull(Object value) throws JsonException;

	/** @ibm-api */
	public boolean isUndefined(Object value) throws JsonException;
	
	/** @ibm-api */
	public boolean isString(Object value) throws JsonException;

	/** @ibm-api */
	public String getString(Object value) throws JsonException;

	/** @ibm-api */
	public boolean isJavaScriptCode(Object value) throws JsonException;

	/** @ibm-api */
	public String getJavaScriptCode(Object value) throws JsonException;
	
	/** @ibm-api */
	public boolean isNumber(Object value) throws JsonException;

	/** @ibm-api */
	public double getNumber(Object value) throws JsonException;
	
	/** @ibm-api */
	public boolean isBoolean(Object value) throws JsonException;

	/** @ibm-api */
	public boolean getBoolean(Object value) throws JsonException;
	
	/** @ibm-api */
	public boolean isObject(Object value) throws JsonException;

	/** @ibm-api */
	public Iterator<String> iterateObjectProperties(Object object) throws JsonException;
	
	/** @ibm-api */
	public boolean isArray(Object value) throws JsonException;

	/** @ibm-api */
	public int getArrayCount(Object value) throws JsonException;

	/** @ibm-api */
	public Iterator<Object> iterateArrayValues(Object array) throws JsonException;
	
	
    // ====================================================================
    // Temporary array
    /** @ibm-api */
    public List<Object> createTemporaryArray(Object parent) throws JsonException;
}
