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


/**
 *
 */
public interface JsonArray extends Iterable<Object> {

	public int length() throws JsonException;

	public Object get(int index) throws JsonException;
	public String getString(int index) throws JsonException;
	public double getNumber(int index) throws JsonException;
	public boolean getBoolean(int index) throws JsonException;
	public JsonObject getObject(int index) throws JsonException;
	public JsonArray getArray(int index) throws JsonException;

	public void put(int index, Object value) throws JsonException;
	public void putString(int index, String value) throws JsonException;
	public void putNumber(int index, double value) throws JsonException;
	public void putBoolean(int index, boolean value) throws JsonException;
	public void putObject(int index, JsonObject value) throws JsonException;
	public void putArray(int index, JsonArray value) throws JsonException;

	public Object remove(int index) throws JsonException;
	
	public boolean add(Object value) throws JsonException;
}
