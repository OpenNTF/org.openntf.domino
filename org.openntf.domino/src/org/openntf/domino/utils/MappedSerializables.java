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

	private HashMap<String, Serializable> _content;
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
	public MappedSerializables(final HashMap<String, Serializable> content) {
		this.setContent(content);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * Serializable Methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	public HashMap<String, Serializable> getContent() {
		if (null == this._content) {
			this._content = new HashMap<String, Serializable>();
		}
		return this._content;
	}

	@SuppressWarnings("unchecked")
	public void setContent(final HashMap<String, Serializable> content) {
		this._content = (null == content) ? null : (HashMap<String, Serializable>) content.clone();
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

	@Override
	public MappedSerializables clone() {
		final MappedSerializables result = new MappedSerializables();
		result.setContent(this.getContent());
		return result;
	}

	/**
	 * Clears all initernal information from the object.
	 */
	public void clear() {
		this.getContent().clear();
	}

	public int size() {
		return (null == this._content) ? 0 : this._content.size();
	}

	public boolean isempty() {
		return (null == this._content) ? true : this._content.isEmpty();
	}

	public boolean containsKey(final String key) {
		return this.getContent().containsKey(key);
	}

	public Serializable get(final String key) {
		try {
			if (Strings.isBlankString(key)) {
				throw new IllegalArgumentException("Key is blank or null");
			}
			return this.getContent().get(key);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	public Serializable put(final String key, final Serializable value) {
		return (Strings.isBlankString(key)) ? null : this.getContent().put(key, value);
	}

	public Serializable remove(final String key) {
		return (Strings.isBlankString(key)) ? null : this.getContent().remove(key);
	}

	public Boolean getBoolean(final String key) {
		final Serializable temp = this.get(key);
		return (null == temp) ? false : (temp instanceof Boolean) ? (Boolean) temp : Boolean.parseBoolean(temp.toString());
	}

	public HashMap<String, Boolean> getBooleans() {
		if (!this.isempty()) {
			final HashMap<String, Boolean> result = new HashMap<String, Boolean>();
			final Iterator<Map.Entry<String, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<String, Serializable> entry = it.next();
				if (entry.getValue() instanceof Boolean) {
					result.put(entry.getKey(), (Boolean) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
	}

	public Boolean putBoolean(final String key, final Boolean value) {
		final Serializable temp = this.put(key, value);
		return (temp instanceof Boolean) ? (Boolean) temp : false;
	}

	public Integer getInteger(final String key) {
		final Serializable temp = this.get(key);
		return (null == temp) ? 0 : (temp instanceof Integer) ? (Integer) temp : Integer.parseInt(temp.toString());
	}

	public HashMap<String, Integer> getIntegers() {
		if (!this.isempty()) {
			final HashMap<String, Integer> result = new HashMap<String, Integer>();
			final Iterator<Map.Entry<String, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<String, Serializable> entry = it.next();
				if (entry.getValue() instanceof Integer) {
					result.put(entry.getKey(), (Integer) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
	}

	public Integer putInteger(final String key, final Integer value) {
		final Serializable temp = this.put(key, value);
		return (temp instanceof Integer) ? (Integer) temp : 0;
	}

	public Date getDate(final String key) {
		final Serializable temp = this.get(key);
		return (null == temp) ? null : (temp instanceof Date) ? (Date) temp : Dates.getDate(temp);
	}

	public HashMap<String, Date> getDates() {
		if (!this.isempty()) {
			final HashMap<String, Date> result = new HashMap<String, Date>();
			final Iterator<Map.Entry<String, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<String, Serializable> entry = it.next();
				if (entry.getValue() instanceof Date) {
					result.put(entry.getKey(), (Date) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
	}

	public Date putDate(final String key, final Date value) {
		final Serializable temp = this.put(key, value);
		return (temp instanceof Date) ? (Date) temp : null;
	}

	public Date setTime(final String key, final Date time) {
		if (Strings.isBlankString(key)) {
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

	public String getTimestamp(final String key, final String format) {
		final Date temp = this.getDate(key);
		return (null == temp) ? "" : Dates.getTimestamp(temp, format);
	}

	public String getTimestamp(final String key) {
		final Date temp = this.getDate(key);
		return (null == temp) ? "" : Dates.getTimestamp(temp);
	}

	public String getString(final String key) {
		final Serializable temp = this.get(key);
		return (null == temp) ? "" : (temp instanceof String) ? (String) temp : temp.toString();
	}

	public HashMap<String, String> getStrings() {
		if (!this.isempty()) {
			final HashMap<String, String> result = new HashMap<String, String>();
			final Iterator<Map.Entry<String, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<String, Serializable> entry = it.next();
				if (entry.getValue() instanceof String) {
					result.put(entry.getKey(), (String) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
	}

	public String putString(final String key, final String value) {
		final Serializable temp = this.put(key, value);
		return (temp instanceof String) ? (String) temp : "";
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
		final Serializable temp = this.get(key);
		return ((null != temp) && (temp instanceof Name));
	}

	public Name getName(final String key) {
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

	public HashMap<String, Name> getNames() {
		if (!this.isempty()) {
			final HashMap<String, Name> result = new HashMap<String, Name>();
			final Iterator<Map.Entry<String, Serializable>> it = this.getContent().entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry<String, Serializable> entry = it.next();
				if (entry.getValue() instanceof Name) {
					result.put(entry.getKey(), (Name) entry.getValue());
				}
			}

			return (result.size() > 0) ? result : null;
		}

		return null;
	}

	public Name putName(final String key, final Name value) {
		final Serializable temp = this.put(key, value);
		return (temp instanceof Name) ? (Name) temp : null;
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
