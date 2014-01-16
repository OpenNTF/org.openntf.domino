/*
 * Copyright 2013
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
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
 *
 */
package org.openntf.domino.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

import org.openntf.domino.Name;

/**
 * Base object carrier for map of Serializable objects.
 * 
 * Map Keys are Strings: MappedSerializables
 * 
 * @author Devin Olson dolson@czarnowski.com
 * 
 * @updated 01/2014
 */
public class StringMappedSerializables extends MappedSerializables {

	private static final long serialVersionUID = 31L;

	/**
	 * Zero-Argument Constructor
	 */
	public StringMappedSerializables() {
		super();
	}

	/**
	 * Optional Constructor
	 * 
	 * @param content
	 *            Content to use for the object
	 */
	public StringMappedSerializables(final HashMap<Serializable, Serializable> content) {
		super(content);
		this.clean();
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * Public Methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/**
	 * Clones the object
	 * 
	 * @return a Clone of the object
	 */
	@Override
	public StringMappedSerializables clone() {
		return (this.isEmpty()) ? new StringMappedSerializables() : new StringMappedSerializables(this.getContent());
	}

	/**
	 * Indicates if the internal Map contains a specified key.
	 * 
	 * @param key
	 *            Key to check for existence in the internal Map.
	 * 
	 * @return internal Map's containsKey() result.
	 */
	public boolean containsKey(final String key) {
		return super.containsKey(key);
	}

	/**
	 * Gets a value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public Serializable get(final String key) {
		return (this.checkKey(key)) ? super.get(key) : null;
	}

	/**
	 * Puts a value into the inernal internal Map.
	 * 
	 * @param key
	 *            Key for which the new value should be associated.
	 * 
	 * @param value
	 *            Value to add to the internal Map.
	 * 
	 * @return Previous value for the key, if it existed in the internal Map.
	 */
	@Override
	public Serializable put(final Serializable key, final Serializable value) {
		return (key instanceof String) ? super.put((String) key, value) : null;
	}

	/**
	 * Puts a value into the inernal internal Map.
	 * 
	 * @param key
	 *            Key for which the new value should be associated.
	 * 
	 * @param value
	 *            Value to add to the internal Map.
	 * 
	 * @return Previous value for the key, if it existed in the internal Map.
	 */
	public Serializable put(final String key, final String value) {
		return (this.checkKey(key)) ? super.put(key, value) : null;
	}

	/**
	 * Removes a value from the inernal internal Map.
	 * 
	 * @param key
	 *            Key whose value should be removed.
	 * 
	 * @return Previous value for the key, if it existed in the internal Map.
	 */
	public Serializable remove(final String key) {
		return (this.checkKey(key)) ? super.remove(key) : null;
	}

	/**
	 * Gets a Boolean value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public Boolean getBoolean(final String key) {
		return (this.checkKey(key)) ? super.getBoolean(key) : null;
	}

	/**
	 * Associates the Boolean value with the specified key in the internal Map. If the Map previously contained a mapping for the key, the
	 * old value is replaced.
	 * 
	 * @param key
	 *            key with which the Boolean value is to be associated.
	 * @param value
	 *            Boolean to be associated with the specified key.
	 * 
	 * @return previous value associated with the key, IF AND ONLY IF the previous value was a Boolean object. False otherwise.
	 */
	public Boolean putBoolean(final String key, final Boolean value) {
		return (this.checkKey(key)) ? super.putBoolean(key, value) : null;
	}

	/**
	 * Gets an Integer value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public Integer getInteger(final String key) {
		return (this.checkKey(key)) ? super.getInteger(key) : null;
	}

	/**
	 * Associates the Integer value with the specified key in the internal Map. If the Map previously contained a mapping for the key, the
	 * old value is replaced.
	 * 
	 * @param key
	 *            key with which the Integer value is to be associated.
	 * @param value
	 *            Integer to be associated with the specified key.
	 * 
	 * @return previous value associated with the key, IF AND ONLY IF the previous value was a Integer object. Zero otherwise.
	 */
	public Integer putInteger(final String key, final Integer value) {
		return super.putInteger(key, value);
	}

	/**
	 * Gets a Date value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public Date getDate(final String key) {
		return (this.checkKey(key)) ? super.getDate(key) : null;
	}

	/**
	 * Associates the Date value with the specified key in the internal Map. If the Map previously contained a mapping for the key, the old
	 * value is replaced.
	 * 
	 * @param key
	 *            key with which the Date value is to be associated.
	 * @param value
	 *            Date to be associated with the specified key.
	 * 
	 * @return previous value associated with the key, IF AND ONLY IF the previous value was a Date object. Null otherwise.
	 */
	public Date putDate(final String key, final Date value) {
		return (this.checkKey(key)) ? super.putDate(key, value) : null;
	}

	/**
	 * Sets the time portion of an existing Date object found in the internal map.
	 * 
	 * If no mapping is found for the specified key, a new Date object will be created and set to the passed in Date value.
	 * 
	 * @param key
	 *            key with which the Date value is to be associated.
	 * @param time
	 *            time to set the existing mapped Date object.
	 * @return previous value associated with the key, IF AND ONLY IF the previous value was a Date object. Null otherwise.
	 */
	public Date setTime(final String key, final Date time) {
		return (this.checkKey(key)) ? super.setTime(key, time) : null;
	}

	/**
	 * Gets a Timestamp value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @param format
	 *            Format to use when constructing the timestamp
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public String getTimestamp(final String key, final String format) {
		return (this.checkKey(key)) ? super.getTimestamp(key, format) : null;
	}

	/**
	 * Gets a Timestamp value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public String getTimestamp(final String key) {
		return (this.checkKey(key)) ? super.getTimestamp(key) : null;
	}

	/**
	 * Gets a String value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public String getString(final String key) {
		return (this.checkKey(key)) ? super.getString(key) : null;
	}

	/**
	 * Associates the String value with the specified key in the internal Map. If the Map previously contained a mapping for the key, the
	 * old value is replaced.
	 * 
	 * @param key
	 *            key with which the String value is to be associated.
	 * @param value
	 *            String to be associated with the specified key.
	 * 
	 * @return previous value associated with the key, IF AND ONLY IF the previous value was a String object. Otherwise will attempt to
	 *         return the result of previous value's toString() method. Empty string "" on error or no previous value exists.
	 */
	public String putString(final String key, final String value) {
		return (this.checkKey(key)) ? super.putString(key, value) : null;
	}

	/**
	 * Returns true if the object contains a non-null Name for the specified key
	 * 
	 * @param key
	 *            The key whose presence is to be tested
	 * 
	 * @return Flag indicating if the Names contains a non-null mapping for the key
	 */
	public boolean containsName(final String key) {
		return (this.checkKey(key)) ? super.getBoolean(key) : null;
	}

	/**
	 * Gets a Name value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public Name getName(final String key) {
		return (this.checkKey(key)) ? super.getName(key) : null;
	}

	/**
	 * Associates the Name value with the specified key in the internal Map. If the Map previously contained a mapping for the key, the old
	 * value is replaced.
	 * 
	 * @param key
	 *            key with which the Name value is to be associated.
	 * @param value
	 *            Name to be associated with the specified key.
	 * 
	 * @return previous value associated with the key, IF AND ONLY IF the previous value was a Name object. Null otherwise.
	 */
	public Name putName(final String key, final Name value) {
		return (this.checkKey(key)) ? super.putName(key, value) : null;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * private methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	private boolean checkKey(final String key) {
		return (Strings.isBlankString(key)) ? false : true;
	}

	private void clean() {
		if (!this.isEmpty()) {
			TreeSet<Serializable> removekeys = new TreeSet<Serializable>();
			for (Serializable key : this.getContent().keySet()) {
				if (!(key instanceof String)) {
					removekeys.add(key);
				}
			}

			for (Serializable key : removekeys) {
				this.remove(key);
			}
		}
	}

}
