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

import java.io.Reader;
import java.util.List;

import com.ibm.commons.util.io.json.parser.Json;


/**
 * JSON parser.
 * <p>
 * This class provides some methods for generating parsing JSON text and create
 * objects out of it. It uses a factory to deal with the actual object classes.
 * </p>
 * @ibm-api
 */
public class JsonParser {
	
	/**
	 * Callback when a stream of json entries is parsed
	 */
	public static interface ParseCallback {
		public void jsonEntry(Object o) throws JsonException;
	}

	/**
	 * Check if a string a a valid JSON text.
	 * @param json the JSON text to check
	 * @return true if the string is a JSON object
	 * @throws JsonException
	 * @ibm-api
	 */
	public static boolean isJson(String json) {
		try {
			Json parser = getParser(JsonEmptyFactory.instance,new java.io.StringReader(json));
			parser.parseJson();
			return true;
		} catch(Throwable ex) {
			return false;
		}
	}
	/**
	 * Parse a JSON text and return an object.
	 * @param factory the object factory
	 * @param json the JSON text
	 * @return the object created from the JSON text
	 * @throws JsonException
	 * @ibm-api
	 */
	public static Object fromJson(JsonFactory factory, String json) throws JsonException {
		try {
			Json parser = getParser(factory,new java.io.StringReader(json));
			return parser.parseJson();
        } catch(Throwable ex) {
		    throw new JsonException(ex,"Error when parsing JSON string"); // $NLS-JsonParser.ErrorwhenparsingJSONstring-1$
		}
	}

	/**
	 * Parse a JSON stream and return an object.
	 * @param factory the object factory
	 * @param reader the JSON stream
	 * @return the object created from the JSON text
	 * @throws JsonException
	 * @ibm-api
	 */
	public static Object fromJson(JsonFactory factory, Reader reader) throws JsonException {
		try {
			Json parser = getParser(factory,reader);
			return parser.parseJson();
        } catch(Throwable ex) {
		    throw new JsonException(ex,"Error when parsing JSON stream"); // $NLS-JsonParser.ErrorwhenparsingJSONstream-1$
		}
	}
	
	/**
	 * Parse a JSON stream and add all the entries to a list.
	 * @param factory the object factory
	 * @param reader the JSON stream
	 * @param cb the cllback to call for each entry
	 * @return the object created from the JSON text
	 * @throws JsonException
	 * @ibm-api
	 */
	public static void fromJson(JsonFactory factory, Reader reader, List<Object> list) throws JsonException {
		try {
			Json parser = getParser(factory,reader);
			parser.parseJsonList(list);
        } catch(Throwable ex) {
		    throw new JsonException(ex,"Error when parsing JSON list"); 
		}
	}
	
	/**
	 * Parse a JSON stream and call a function for each entry.
	 * @param factory the object factory
	 * @param reader the JSON stream
	 * @param cb the cllback to call for each entry
	 * @return the object created from the JSON text
	 * @throws JsonException
	 * @ibm-api
	 */
	public static void fromJson(JsonFactory factory, Reader reader, ParseCallback cb) throws JsonException {
		try {
			Json parser = getParser(factory,reader);
			parser.parseJsonCallback(cb);
        } catch(Throwable ex) {
		    throw new JsonException(ex,"Error when parsing JSON stream"); 
		}
	}

	
    private static Json getParser(JsonFactory factory, Reader r) {
        Json parser = new Json(r);
        parser.factory = factory;
        return parser;
    }
}
