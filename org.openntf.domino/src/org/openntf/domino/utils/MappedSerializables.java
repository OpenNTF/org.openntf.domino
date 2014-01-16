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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openntf.domino.Name;

/**
 * Base object carrier for map of Serializable objects.
 * 
 * @author Devin Olson dolson@czarnowski.com
 * 
 * @updated 01/2014
 */
public class MappedSerializables implements Serializable {

	private HashMap<Serializable, Serializable> _content;
	private static final long serialVersionUID = 31L;

	/**
	 * Zero-Argument Constructor
	 */
	public MappedSerializables() {
	}

	/**
	 * Optional Constructor
	 * 
	 * @param content
	 *            Content to use for the object
	 */
	public MappedSerializables(final HashMap<Serializable, Serializable> content) {
		this.setContent(content);
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
	public MappedSerializables clone() {
		return (null == this._content) ? new MappedSerializables() : new MappedSerializables(
				(HashMap<Serializable, Serializable>) this._content.clone());
	}

	/**
	 * Clears all internal information from the object.
	 */
	public void clear() {
		if (null != this._content) {
			this._content.clear();
		}
	}

	/**
	 * Gets the number of elements in the internal Map.
	 * 
	 * @return internal Map's size property
	 */
	public int size() {
		return (null == this._content) ? 0 : this._content.size();
	}

	/**
	 * Flag indicating if the internal Map is empty.
	 * 
	 * @return internal Map's isEmpty property
	 */
	public boolean isEmpty() {
		return (null == this._content) ? true : this._content.isEmpty();
	}

	/**
	 * Indicates if the internal Map contains a specified key.
	 * 
	 * @param key
	 *            Key to check for existence in the internal Map.
	 * 
	 * @return internal Map's containsKey() result.
	 */
	public boolean containsKey(final Serializable key) {
		return this.getContent().containsKey(key);
	}

	/**
	 * Gets a value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if no mapping exists.
	 */
	public Serializable get(final Serializable key) {
		return this.getContent().get(key);
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
	public Serializable put(final Serializable key, final Serializable value) {
		return (null == key) ? null : this.getContent().put(key, value);
	}

	/**
	 * Removes a value from the inernal internal Map.
	 * 
	 * @param key
	 *            Key whose value should be removed.
	 * 
	 * @return Previous value for the key, if it existed in the internal Map.
	 */
	public Serializable remove(final Serializable key) {
		return (null == key) ? null : this.getContent().remove(key);
	}

	/**
	 * Gets a Boolean value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public Boolean getBoolean(final Serializable key) {
		final Serializable temp = this.get(key);
		return (null == temp) ? false : (temp instanceof Boolean) ? (Boolean) temp : Boolean.parseBoolean(temp.toString());
	}

	/**
	 * Gets a Map of all Boolean values from the internal Map.
	 * 
	 * @return Map of Boolean values. Null if no Booleans exist in the internal Map.
	 */
	public HashMap<Serializable, Boolean> getBooleans() {
		if (!this.isEmpty()) {
			final HashMap<Serializable, Boolean> result = new HashMap<Serializable, Boolean>();
			final Iterator<Map.Entry<Serializable, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<Serializable, Serializable> entry = it.next();
				if (entry.getValue() instanceof Boolean) {
					result.put(entry.getKey(), (Boolean) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
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
	public Boolean putBoolean(final Serializable key, final Boolean value) {
		final Serializable temp = this.put(key, value);
		return (temp instanceof Boolean) ? (Boolean) temp : false;
	}

	/**
	 * Gets an Integer value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public Integer getInteger(final Serializable key) {
		final Serializable temp = this.get(key);
		return (null == temp) ? 0 : (temp instanceof Integer) ? (Integer) temp : Integer.parseInt(temp.toString());
	}

	/**
	 * Gets a Map of all Integer values from the internal Map.
	 * 
	 * @return Map of Integer values. Null if no Integers exist in the internal Map.
	 */
	public HashMap<Serializable, Integer> getIntegers() {
		if (!this.isEmpty()) {
			final HashMap<Serializable, Integer> result = new HashMap<Serializable, Integer>();
			final Iterator<Map.Entry<Serializable, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<Serializable, Serializable> entry = it.next();
				if (entry.getValue() instanceof Integer) {
					result.put(entry.getKey(), (Integer) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
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
	public Integer putInteger(final Serializable key, final Integer value) {
		final Serializable temp = this.put(key, value);
		return (temp instanceof Integer) ? (Integer) temp : 0;
	}

	/**
	 * Gets a Date value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public Date getDate(final Serializable key) {
		final Serializable temp = this.get(key);
		return (null == temp) ? null : (temp instanceof Date) ? (Date) temp : Dates.getDate(temp);
	}

	/**
	 * Gets a Map of all Date values from the internal Map.
	 * 
	 * @return Map of Date values. Null if no Dates exist in the internal Map.
	 */
	public HashMap<Serializable, Date> getDates() {
		if (!this.isEmpty()) {
			final HashMap<Serializable, Date> result = new HashMap<Serializable, Date>();
			final Iterator<Map.Entry<Serializable, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<Serializable, Serializable> entry = it.next();
				if (entry.getValue() instanceof Date) {
					result.put(entry.getKey(), (Date) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
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
	public Date putDate(final Serializable key, final Date value) {
		final Serializable temp = this.put(key, value);
		return (temp instanceof Date) ? (Date) temp : null;
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
	public Date setTime(final Serializable key, final Date time) {
		if (null == key) {
			throw new IllegalArgumentException("Key is null");
		}
		if (null == time) {
			return null;
		}

		final Date date = this.getDate(key);
		if (null == date) {
			return this.putDate(key, time);
		} else {
			final Calendar caldate = Calendar.getInstance();
			final Calendar caltime = Calendar.getInstance();
			caldate.setTime(date);
			caltime.setTime(time);
			caldate.set(Calendar.HOUR_OF_DAY, caltime.get(Calendar.HOUR_OF_DAY));
			caldate.set(Calendar.MINUTE, caltime.get(Calendar.MINUTE));
			caldate.set(Calendar.SECOND, caltime.get(Calendar.SECOND));
			return this.putDate(key, caldate.getTime());
		}
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
	public String getTimestamp(final Serializable key, final String format) {
		final Date temp = this.getDate(key);
		return (null == temp) ? "" : Dates.getTimestamp(temp, format);
	}

	/**
	 * Gets a Timestamp value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public String getTimestamp(final Serializable key) {
		final Date date = this.getDate(key);
		return (null == date) ? "" : Dates.getDefaultTimestamp(date);
	}

	/**
	 * Gets a String value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public String getString(final Serializable key) {
		final Serializable temp = this.get(key);
		return (null == temp) ? "" : (temp instanceof String) ? (String) temp : temp.toString();
	}

	/**
	 * Gets a Map of all String values from the internal Map.
	 * 
	 * @return Map of String values. Null if no Strings exist in the internal Map.
	 */
	public HashMap<Serializable, String> getStrings() {
		if (!this.isEmpty()) {
			final HashMap<Serializable, String> result = new HashMap<Serializable, String>();
			final Iterator<Map.Entry<Serializable, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<Serializable, Serializable> entry = it.next();
				if (entry.getValue() instanceof String) {
					result.put(entry.getKey(), (String) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
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
	public String putString(final Serializable key, final String value) {
		final Serializable temp = this.put(key, value);
		try {
			return (temp instanceof String) ? (String) temp : temp.toString();
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Returns true if the object contains a non-null Name for the specified key
	 * 
	 * @param key
	 *            The key whose presence is to be tested
	 * 
	 * @return Flag indicating if the Names contains a non-null mapping for the key
	 */
	public boolean containsName(final Serializable key) {
		final Serializable temp = this.get(key);
		return ((null != temp) && (temp instanceof Name));
	}

	/**
	 * Gets a Name value from the internal Map for the specified key, if it exists.
	 * 
	 * @param key
	 *            Key for which to retrieve the mapped value.
	 * 
	 * @return Value associated with the key. Null if not found or exception occurs.
	 */
	public Name getName(final Serializable key) {
		try {
			final Serializable temp = this.get(key);
			if (temp instanceof Name) {
				return (Name) temp;
			} else if (null == temp) {
				final Name result = new org.openntf.domino.impl.Name();
				this.put(key, result);
				return result;
			}

			throw new RuntimeException("Mapped Object not instance of Name.  Mapped Object Class: " + temp.getClass());

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Gets a Map of all Name values from the internal Map.
	 * 
	 * @return Map of Name values. Null if no Names exist in the internal Map.
	 */
	public HashMap<Serializable, Name> getNames() {
		if (!this.isEmpty()) {
			final HashMap<Serializable, Name> result = new HashMap<Serializable, Name>();
			final Iterator<Map.Entry<Serializable, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<Serializable, Serializable> entry = it.next();
				if (entry.getValue() instanceof Name) {
					result.put(entry.getKey(), (Name) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
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
	public Name putName(final Serializable key, final Name value) {
		final Serializable temp = this.put(key, value);
		return (temp instanceof Name) ? (Name) temp : null;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * protected methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/**
	 * Gets the internal Map.
	 * 
	 * @return internal Map.
	 */
	protected HashMap<Serializable, Serializable> getContent() {
		if (null == this._content) {
			this._content = new HashMap<Serializable, Serializable>();
		}
		return this._content;
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

	/**
	 * Sets the internal Map.
	 * 
	 * @param content
	 *            internal Map.
	 */
	@SuppressWarnings("unchecked")
	private void setContent(final HashMap<Serializable, Serializable> content) {
		this._content = (null == content) ? null : (HashMap<Serializable, Serializable>) content.clone();
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * hashcode and equals methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((null == this._content) ? 0 : this._content.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof MappedSerializables)) {
			return false;
		}
		final MappedSerializables other = (MappedSerializables) obj;
		if (null == this._content) {
			if (null != other._content) {
				return false;
			}
		} else if (!this._content.equals(other._content)) {
			return false;
		}

		return true;
	}

}
