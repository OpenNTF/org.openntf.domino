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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openntf.domino.DateTime;
import org.openntf.domino.Item;

/**
 * Date and Time handling utilities
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
 * 
 */
public enum Dates {
	DATEONLY(Strings.TIMESTAMP_DATEONLY, Strings.REGEX_DATEONLY),

	TIMEONLY(Strings.TIMESTAMP_TIMEONLY, Strings.REGEX_TIMEONLY),

	DAYMONTH_NAMES(Strings.TIMESTAMP_DAYMONTH_NAMES, Strings.REGEX_DAYMONTH_NAMES),

	DEFAULT(Strings.TIMESTAMP_DEFAULT, Strings.REGEX_DEFAULT),

	MILITARY(Strings.TIMESTAMP_MILITARY, Strings.REGEX_MILITARY),

	SIMPLETIME(Strings.TIMESTAMP_SIMPLETIME, Strings.REGEX_SIMPLETIME);

	private final String _timestampFormat;
	private final String _regex;
	private Pattern _pattern;
	private SimpleDateFormat _sdf;

	/**
	 * Instance Constructor
	 * 
	 * @param format
	 *            Format for the instance
	 * @param regex
	 *            String used to generate the pattern
	 */
	private Dates(final String timestampFormat, final String regex) {
		this._timestampFormat = timestampFormat;
		this._regex = Strings.REGEX_BEGIN_NOCASE + regex + Strings.REGEX_END;
		this._pattern = Pattern.compile(Strings.REGEX_BEGIN_NOCASE + regex + Strings.REGEX_END);
	}

	@Override
	public String toString() {
		return Dates.class.getName() + " " + this.name() + "{\"" + this.getInstanceTimestampFormat() + "\", " + this.getPattern() + "}";
	}

	/**
	 * Gets the format
	 * 
	 * @return the format
	 */
	public String getInstanceTimestampFormat() {
		return this._timestampFormat;
	}

	/**
	 * Gets the regex
	 * 
	 * @return the regex
	 */
	public String getRegex() {
		return this._regex;
	}

	/**
	 * Gets the Pattern object
	 * 
	 * @return the Pattern
	 */
	public Pattern getPattern() {
		if (null == this._pattern) {
			this._pattern = Pattern.compile(this.getRegex());
		}
		return this._pattern;
	}

	/**
	 * Gets the SimpleDateFormat object
	 * 
	 * @return the SimpleDateFormat
	 */
	public SimpleDateFormat getSimpleDateFormat() {
		if (null == this._sdf) {
			this._sdf = Dates.getSimpleDateFormat(this.getInstanceTimestampFormat());
			this._sdf.setLenient(true);
		}

		return this._sdf;
	}

	/**
	 * Gets the timestamp for the current moment.
	 * 
	 * @return Timestamp for the current moment.
	 */
	public String getInstanceTimestamp() {
		return this.getInstanceTimestamp(null);
	}

	/**
	 * Gets the timestamp for the specified date.
	 * 
	 * @param date
	 *            moment for which to get the Timestamp. If null the current moment will be used.
	 * 
	 * @return Timestamp for the specified date.
	 */
	public String getInstanceTimestamp(final Date date) {
		return this.getSimpleDateFormat().format((null == date) ? Dates.getDate() : date);
	}

	/**
	 * Gets the timestamp for the specified date.
	 * 
	 * @param object
	 *            Object from which a date can be determined.
	 * 
	 * @return Timestamp for the specified date.
	 */
	public String getInstanceTimestamp(final Object object) {
		return this.getInstanceTimestamp(Dates.getDate(object));
	}

	/**
	 * Determines if a given source string matches the Pattern.
	 * 
	 * @param source
	 *            String for which to test for match against Pattern.
	 * 
	 * @return Flag indicating if the source string matches the Pattern.
	 */
	public boolean matches(final String source) {
		try {
			final Matcher m = this.getPattern().matcher(source);
			return m.matches();
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/*
	 * **************************************************************************
	 * **************************************************************************
	 * 
	 * PUBLIC STATIC methods
	 * 
	 * **************************************************************************
	 * **************************************************************************
	 */
	/**
	 * Generates a timecode.
	 * 
	 * A timecode is a 6 character long alphanumeric (base36) String representing the current date and time.
	 * 
	 * @return base36 String representing the current date and time
	 * 
	 */
	public static String getTimeCode() {
		try {
			Long ntime = Math.abs(System.nanoTime());
			if (ntime.equals(Long.MIN_VALUE)) {
				ntime = Long.MAX_VALUE;
			}

			final String result = Long.toString(ntime, 36).toUpperCase();
			final int length = result.length();
			if (length > 6) {
				return result.substring(length - 6);
			} else if (length < 6) {
				return Strings.getFilledString(6 - length, '0') + result;
			}

			return result;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Gets the timestamp for a date.
	 * 
	 * @param date
	 *            the date from which to get the timestamp
	 * 
	 * @param format
	 *            the format for the timestamp.
	 * 
	 * @return the timestamp for the date.
	 */
	public static String getTimestamp(final Object object, final String format) {
		try {
			if (Strings.isBlankString(format)) {
				return Dates.DEFAULT.getInstanceTimestamp(object);
			}

			Dates tf = Dates.get(format);
			if (null != tf) {
				return tf.getInstanceTimestamp(object);
			}

			final Date date = (Dates.getDate(object));
			final SimpleDateFormat sdf = Dates.getSimpleDateFormat(format);
			return sdf.format((null == date) ? Dates.getDate() : date);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Gets the timestamp.
	 * 
	 * @param date
	 *            the date
	 * @return the timestamp
	 */
	public static String getTimestamp(final Date date) {
		return Dates.DEFAULT.getInstanceTimestamp(date);
	}

	/**
	 * Gets the timestamp.
	 * 
	 * @return the timestamp
	 */
	public static String getTimestamp() {
		return Dates.DEFAULT.getInstanceTimestamp();
	}

	/**
	 * Compares two Objects as Dates. Returns true if the first is after the second.
	 * 
	 * Arguments are first compared by existence, then by value.
	 * 
	 * @param object0
	 *            First Object to compare.
	 * 
	 * @param object1
	 *            Second Object to compare.
	 * 
	 * @return Flag indicating if the first object is after the second object.
	 * 
	 */
	public static boolean isAfter(final Object object0, final Object object1) {
		try {
			return (Dates.compareDates(object0, object1, false) > DominoUtils.EQUAL);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Compares two Objects as Dates. Returns true if the first is before the second.
	 * 
	 * Arguments are first compared by existence, then by value.
	 * 
	 * @param object0
	 *            First Object to compare.
	 * 
	 * @param object1
	 *            Second Object to compare.
	 * 
	 * @return Flag indicating if the first object is before the second object.
	 * 
	 */
	public static boolean isBefore(final Object object0, final Object object1) {
		try {
			return (Dates.compareDates(object0, object1, false) < DominoUtils.EQUAL);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Determines if two Dates represent the same day.
	 * 
	 * Spawns Calendar objects from the arguments, and returns true if the year, month, and day for the objects are equal.
	 * 
	 * @param object0
	 *            First object from which a Calendar object can be constructed.
	 * 
	 * @param object1
	 *            Second object from which a Calendar object can be constructed.
	 * 
	 * 
	 * @return Flag indicating of the two Dates represent the same day.
	 * 
	 */
	public static boolean isSameDay(final Object object0, final Object object1) {
		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			final Calendar cal0 = Dates.getCalendar(object0);
			final Calendar cal1 = Dates.getCalendar(object1);

			return ((cal0.get(Calendar.YEAR) == cal1.get(Calendar.YEAR)) && (cal0.get(Calendar.DAY_OF_YEAR) == cal1
					.get(Calendar.DAY_OF_YEAR)));

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Determines if a date is within a specified range.
	 * 
	 * @param source
	 *            Date to check.
	 * 
	 * @param begin
	 *            Begin date range.
	 * 
	 * @param end
	 *            End date range. (must be after begin)
	 * 
	 * @param inclusive
	 *            Flag indicating to include begin and end as valid dates for checking.
	 * 
	 * @return Flag indicating if source is (after begin) AND (before end); OR (if inclusive is true) equal to begin or equal to end.
	 */
	public static boolean isInRange(final Object source, final Object begin, final Object end, final boolean inclusive) {
		final Calendar calSource = Dates.getCalendar(source);
		final Calendar calBegin = Dates.getCalendar(begin);
		final Calendar calEnd = Dates.getCalendar(end);

		return (Dates.isAfter(calSource, calBegin) && Dates.isBefore(calSource, calEnd))
				|| (inclusive && (calSource.equals(calBegin) || calSource.equals(calEnd)));
	}

	/**
	 * Gets the days between two Dates.
	 * 
	 * Spawns Calendar objects from the arguments, and returns the number of days between them. Returns a negative value if the first
	 * argument is after the second argument.
	 * 
	 * @param object0
	 *            First object from which a Calendar object can be constructed.
	 * 
	 * @param object1
	 *            Second object from which a Calendar object can be constructed.
	 * 
	 * @return Days between the two objects. Maximum days is 2,147,483,647, a range of nearly 5.9 million years.
	 * 
	 */
	public static int getDaysBetween(final Object object0, final Object object1) {
		int result = 0;
		// Maximum days is 2,147,483,647, a range of nearly 5.9 million years.

		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			final Calendar cal0 = Dates.getCalendar(object0);
			final Calendar cal1 = Dates.getCalendar(object1);

			if ((cal0.get(Calendar.YEAR) == cal1.get(Calendar.YEAR)) && (cal0.get(Calendar.DAY_OF_YEAR) == cal1.get(Calendar.DAY_OF_YEAR))) {
				return 0;
			}

			if (cal1.before(cal0)) {
				return -Dates.getDaysBetween(cal1, cal0);
			}

			final Calendar temp = (Calendar) cal0.clone();
			while (temp.before(cal1)) {
				temp.add(Calendar.DAY_OF_MONTH, 1);
				result++;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return result;
	}

	/**
	 * Gets the days between two Dates, excluding Saturdays, Sundays, and (optionally) Holidays.
	 * 
	 * Spawns Calendar objects from the arguments, and returns the number of days between them. Returns a negative value if the first
	 * argument is after the second argument.
	 * 
	 * @param object0
	 *            First object from which a Calendar object can be constructed.
	 * 
	 * @param object1
	 *            Second object from which a Calendar object can be constructed.
	 * 
	 * @param holidays
	 *            Holiday days (as Calendary.DAY_OF_YEAR) to exclude
	 * 
	 * @return Business Days between the two objects. Maximum days is 2,147,483,647, a range of nearly 5.9 million years.
	 * 
	 */
	public static int getBusinessDaysBetween(final Object object0, final Object object1, final List<Integer> holidays) {
		int result = 0;
		// Maximum days is 2,147,483,647, a range of nearly 5.9 million years.

		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			final Calendar cal0 = Dates.getCalendar(object0);
			final Calendar cal1 = Dates.getCalendar(object1);

			if ((cal0.get(Calendar.YEAR) == cal1.get(Calendar.YEAR)) && (cal0.get(Calendar.DAY_OF_YEAR) == cal1.get(Calendar.DAY_OF_YEAR))) {
				return 0;
			}

			if (cal1.before(cal0)) {
				return -Dates.getDaysBetween(cal1, cal0);
			}

			final Calendar temp = (Calendar) cal0.clone();
			if (null == holidays) {
				while (temp.before(cal1)) {
					temp.add(Calendar.DAY_OF_MONTH, 1);
					if ((Calendar.SATURDAY != temp.get(Calendar.DAY_OF_WEEK)) && (Calendar.SUNDAY != temp.get(Calendar.DAY_OF_WEEK))) {
						result++;
					}
				}

			} else {
				while (temp.before(cal1)) {
					temp.add(Calendar.DAY_OF_MONTH, 1);
					if ((Calendar.SATURDAY != temp.get(Calendar.DAY_OF_WEEK)) && (Calendar.SUNDAY != temp.get(Calendar.DAY_OF_WEEK))
							&& !holidays.contains(temp.get(Calendar.DAY_OF_YEAR))) {
						result++;
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return result;
	}

	/**
	 * Gets the hours between two Dates.
	 * 
	 * Spawns Calendar objects from the arguments, and returns the number of days between them. Returns a negative value if the first
	 * argument is after the second argument.
	 * 
	 * @param object0
	 *            First object from which a Calendar object can be constructed.
	 * 
	 * @param object1
	 *            Second object from which a Calendar object can be constructed.
	 * 
	 * @return whole number of hours between two values. Assumes object0 < object1, returns negative value otherwise. Maximum hours
	 *         converted from 9,223,372,036,854,775,807 milliseconds, a range of just over 293 million years
	 * 
	 */
	public static long getHoursBetween(final Object object0, final Object object1) {
		// whole number of hours between two values. Assumes object0 < object1,
		// returns negative otherwise
		// long maximum hours converted from 9,223,372,036,854,775,807
		// milliseconds,
		// a range of just over 293 million years

		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			return Dates.getHours(object1) - Dates.getHours(object0);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	/**
	 * Gets the minutes between two Dates.
	 * 
	 * Spawns Calendar objects from the arguments, and returns the number of days between them. Returns a negative value if the first
	 * argument is after the second argument.
	 * 
	 * @param object0
	 *            First object from which a Calendar object can be constructed.
	 * 
	 * @param object1
	 *            Second object from which a Calendar object can be constructed.
	 * 
	 * @return whole number of minutes between two values. Assumes object0 < object1, returns negative value otherwise. Maximum minutes
	 *         converted from 9,223,372,036,854,775,807 milliseconds, a range of just over 293 million years
	 * 
	 */
	public static long getMinutesBetween(final Object object0, final Object object1) {
		// whole number of minutes between two values. Assumes object0 < object1,
		// returns negative otherwise
		// long maximum minutes converted from 9,223,372,036,854,775,807
		// milliseconds,
		// a range of just over 293 million years

		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			return Dates.getMinutes(object1) - Dates.getMinutes(object0);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	/**
	 * Gets the seconds between two Dates.
	 * 
	 * Spawns Calendar objects from the arguments, and returns the number of days between them. Returns a negative value if the first
	 * argument is after the second argument.
	 * 
	 * @param object0
	 *            First object from which a Calendar object can be constructed.
	 * 
	 * @param object1
	 *            Second object from which a Calendar object can be constructed.
	 * 
	 * @return whole number of seconds between two values. Assumes object0 < object1, returns negative value otherwise. Maximum seconds
	 *         converted from 9,223,372,036,854,775,807 milliseconds, a range of just over 293 million years
	 * 
	 */
	public static long getSecondsBetween(final Object object0, final Object object1) {
		// whole number of seconds between two values. Assumes object0 < object1,
		// returns negative otherwise
		// long maximum seconds converted from 9,223,372,036,854,775,807
		// milliseconds,
		// a range of just over 293 million years

		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			return Dates.getSeconds(object1) - Dates.getSeconds(object0);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	/**
	 * Gets the milliseconds between two Dates.
	 * 
	 * Spawns Calendar objects from the arguments, and returns the number of days between them. Returns a negative value if the first
	 * argument is after the second argument.
	 * 
	 * @param object0
	 *            First object from which a Calendar object can be constructed.
	 * 
	 * @param object1
	 *            Second object from which a Calendar object can be constructed.
	 * 
	 * @return whole number of milliseconds between two values. Assumes object0 < object1, returns negative value otherwise. Maximum
	 *         milliseconds is 9,223,372,036,854,775,807, a range of just over 293 million years
	 * 
	 */
	public static long getMillisecondsBetween(final Object object0, final Object object1) {
		// whole number of milliseconds between two values. Assumes object0 < object1,
		// returns negative otherwise
		// long maximum milliseconds is converted from 9,223,372,036,854,775,807, a range of just over 293 million years
		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			return Dates.getTime(object1) - Dates.getTime(object0);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	/**
	 * Gets the number of hours between the Epoch January 1, 1970, 00:00:00 GMT and the Date value for the passed in object.
	 * 
	 * @param object
	 *            Object from which a Calendar object can be constructed.
	 * 
	 * @return Hours between the Epoch and the date value of the object.
	 */
	public static long getHours(final Object object) {
		// whole number of hours from the Epoch January 1, 1970, 00:00:00 GMT
		try {
			return TimeUnit.HOURS.convert(Dates.getTime(object), TimeUnit.MILLISECONDS);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	/**
	 * Gets the number of minutes between the Epoch January 1, 1970, 00:00:00 GMT and the Date value for the passed in object.
	 * 
	 * @param object
	 *            Object from which a Calendar object can be constructed.
	 * 
	 * @return Minutes between the Epoch and the date value of the object.
	 */
	public static long getMinutes(final Object object) {
		// whole number of minutes from the Epoch January 1, 1970, 00:00:00 GMT
		try {
			return TimeUnit.MINUTES.convert(Dates.getTime(object), TimeUnit.MILLISECONDS);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	/**
	 * Gets the number of seconds between the Epoch January 1, 1970, 00:00:00 GMT and the Date value for the passed in object.
	 * 
	 * @param object
	 *            Object from which a Calendar object can be constructed.
	 * 
	 * @return Seconds between the Epoch and the date value of the object.
	 */
	public static long getSeconds(final Object object) {
		// whole number of seconds from the Epoch January 1, 1970, 00:00:00 GMT
		try {
			return TimeUnit.SECONDS.convert(Dates.getTime(object), TimeUnit.MILLISECONDS);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	/**
	 * Gets the number of milliseconds between the Epoch January 1, 1970, 00:00:00 GMT and the Date value for the passed in object.
	 * 
	 * @param object
	 *            Object from which a Calendar object can be constructed.
	 * 
	 * @return Milliseconds between the Epoch and the date value of the object.
	 */
	public static long getTime(final Object object) {
		// whole number of milliseconds from Epoch January 1, 1970, 00:00:00 GMT
		try {
			return Dates.getDate(object).getTime();
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	/**
	 * Gets the TimeFormatter for the associated key.
	 * 
	 * Searches all TimeFormatters to find one which has a name or format equal to the key. If none found then searches all TimeFormatters
	 * for one that successfully matches() the key. Returns the first one found.
	 * 
	 * @param key
	 *            Key value to check for equality against the the TimeFormatter or a match to the TimeFormatter.
	 * 
	 * @return First found TimeFormatter. Null if no name or format equality or match found.
	 */
	public static Dates get(final String key) {
		if (!Strings.isBlankString(key)) {
			for (final Dates result : Dates.values()) {
				if (result.name().equalsIgnoreCase(key) || result.getInstanceTimestampFormat().equalsIgnoreCase(key)) {
					return result;
				}
			}
			for (final Dates result : Dates.values()) {
				if (result.matches(key)) {
					return result;
				}
			}
		}

		return null;
	}

	/**
	 * Gets a Calendar object.
	 * 
	 * Creates or casts a Calendar from the passed in object.
	 * 
	 * @param object
	 *            Source from which to create or cast a Calendar object.
	 * 
	 * @return Newly created or casted Calendar from the source. Null on exception.
	 */
	public static Calendar getCalendar(final Object object) {

		String classname = "";
		lotus.domino.DateTime datetime = null;
		lotus.domino.Item item = null;

		try {
			if (null == object) {
				throw new IllegalArgumentException("object is null");
			}

			classname = object.getClass().getName();
			if (object instanceof Calendar) {
				return (Calendar) object;
			}
			if ((object instanceof String) || (classname.equalsIgnoreCase("java.lang.String"))) {
				return Dates.getCalendar(Dates.parse((String) object));
			}

			Calendar result = null;
			if (object instanceof Date) {
				result = Calendar.getInstance();
				final Date date = (Date) object;
				result.setTime(date);
				return result;
			}

			if (object instanceof DateTime) {
				result = Calendar.getInstance();
				final DateTime obj = (DateTime) object;
				final Date date = obj.toJavaDate();
				result.setTime(date);
				return result;
			}

			if (object instanceof lotus.domino.DateTime) {
				result = Calendar.getInstance();
				datetime = (lotus.domino.DateTime) object;
				final Date date = datetime.toJavaDate();
				result.setTime(date);
				return result;
			}

			if (object instanceof Item) {
				return Dates.getCalendar(Dates.getDate((Item) object));
			}

			if (object instanceof lotus.domino.Item) {
				item = (lotus.domino.Item) object;
				return Dates.getCalendar(Dates.getDate(item));
			}

			throw new IllegalArgumentException("Unsupported Class:" + classname);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(datetime, item);
		}

		return null;
	}

	/**
	 * Gets a Calendar object.
	 * 
	 * Creates a new Calendar object
	 * 
	 * @return New Calendar object
	 */
	public static Calendar getCalendar() {
		return Dates.getCalendar(Dates.getDate());
	}

	/**
	 * Gets a Date object.
	 * 
	 * Creates or casts a Date from the passed in object.
	 * 
	 * @param object
	 *            Source from which to create or cast a Date object.
	 * 
	 * @return Newly created or casted Date from the source. Null on exception
	 * 
	 */
	public static Date getDate(final Object object) {

		String classname = "";
		lotus.domino.DateTime datetime = null;
		lotus.domino.Item item = null;

		try {
			if (null == object) {
				throw new IllegalArgumentException("object is null");
			}

			classname = object.getClass().getName();
			if (object instanceof Date) {
				return (Date) object;
			}

			if (object instanceof Calendar) {
				return ((Calendar) object).getTime();
			}

			if (object instanceof String) {
				return Dates.parse((String) object);
			}

			if (object instanceof DateTime) {
				return ((DateTime) object).toJavaDate();
			}

			if (object instanceof lotus.domino.DateTime) {
				datetime = (DateTime) object;
				return datetime.toJavaDate();
			}

			if (object instanceof Item) {
				return Dates.getDate(((Item) object).getDateTimeValue());
			}

			if (object instanceof lotus.domino.Item) {
				item = (lotus.domino.Item) object;
				return Dates.getDate(item.getDateTimeValue());
			}

			throw new IllegalArgumentException("Unsupported Class:" + classname);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(datetime, item);
		}

		return null;
	}

	/**
	 * Gets a Date object.
	 * 
	 * Creates a New Date object from the passed in arguments.
	 * 
	 * @param date
	 *            Source from which to get the DATE portion of the new Date object. If null then the value from time will be used.
	 * 
	 * @param time
	 *            Source from which to get the TIME portion of the new Date object. If null then the value from date will be used.
	 * 
	 * @return Newly created or cast Date from the source.
	 * 
	 * @throws IllegalArgumentException
	 *             if both date nor time arguments are null.
	 * @throws IllegalArgumentException
	 *             If both arguments cannot be processed as a date.
	 */
	public static Date getDate(final Object date, final Object time) throws NullPointerException, IllegalArgumentException {
		if ((null == date) && (null == time)) {
			throw new NullPointerException("Both DATE and TIME arguments are null");
		}

		try {
			final Date dDate = (null == date) ? Dates.getDate(time) : Dates.getDate(date);
			if (null == dDate) {
				throw new RuntimeException("dDate is null");
			}

			final Date dTime = (null == time) ? Dates.getDate(date) : Dates.getDate(time);
			if (null == dTime) {
				throw new RuntimeException("dTime is null");
			}

			final Calendar cDate = Dates.getCalendar(dDate);
			final Calendar cTime = Dates.getCalendar(dTime);

			cDate.set(Calendar.HOUR_OF_DAY, cTime.get(Calendar.HOUR_OF_DAY));
			cDate.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
			cDate.set(Calendar.SECOND, cTime.get(Calendar.SECOND));

			return cDate.getTime();

		} catch (final Exception e) {
			DominoUtils.handleException(e);
			throw new IllegalArgumentException("Neither DATE nor TIME argument could be processed as a Date object.");
		}
	}

	/**
	 * Gets a Date object.
	 * 
	 * Creates a new Date object
	 * 
	 * @return New Date object
	 */
	public static Date getDate() {
		return new Date();
	}

	/**
	 * Gets a SimpleDateFormat object for the specified format.
	 * 
	 * @param format
	 *            Format for which to generate a new SimpleDateFormat object.
	 * 
	 * @return SimpleDateFormat object for the specified format
	 */
	public static SimpleDateFormat getSimpleDateFormat(final String format) {
		final SimpleDateFormat result = (Strings.isBlankString(format)) ? new SimpleDateFormat() : new SimpleDateFormat(format);
		result.setLenient(true);
		return result;
	}

	/**
	 * Attempts to spawn a new Date by parsing a string.
	 * 
	 * @param string
	 *            Value from which to attempt to construct a new Date object.
	 * 
	 * @return new Date object constructed from the string. Null on error.
	 */
	public static Date parse(final String string) {
		if (!Strings.isBlankString(string)) {
			try {
				final Dates tf = Dates.get(string);
				final SimpleDateFormat sdf = (null == tf) ? new SimpleDateFormat() : tf.getSimpleDateFormat();
				return sdf.parse(string);

			} catch (final Exception e) {
				DominoUtils.handleException(e);
			}
		}

		return null;
	}

	/**
	 * Attempts to spawn a new Date by parsing a string.
	 * 
	 * @param dateText
	 *            Value from which to attempt to construct a new Date object.
	 * 
	 * @param format
	 *            Format used to construct the SimpleDateFormatter object.
	 * 
	 * @return new Date object constructed from the string. Null on error.
	 */
	public static Date parse(final String dateText, final String format) {
		if (null != dateText) {
			if (Strings.isBlankString(format)) {
				return Dates.parse(dateText);
			}

			try {
				return Dates.getSimpleDateFormat(format).parse(dateText);

			} catch (final Exception e) {
				DominoUtils.handleException(e);
			}
		}

		return null;
	}

	/**
	 * Compares two Objects as Dates
	 * 
	 * Arguments are first compared by existence, then by value.
	 * 
	 * @param object0
	 *            First date to compare.
	 * 
	 * @param object1
	 *            Second date to compare.
	 * 
	 * @param descending
	 *            flags indicating comparison order. true = descending, false = ascending.
	 * 
	 * @return a negative integer, zero, or a positive integer indicating if the first object is less than, equal to, or greater than the
	 *         second object.
	 * 
	 * @throws RuntimeException
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see DominoUtils#LESS_THAN
	 * @see DominoUtils#EQUAL
	 * @see DominoUtils#GREATER_THAN
	 */
	@SuppressWarnings("javadoc")
	public static int compareDaysBetween(final Object object0, final Object object1, final boolean descending) {
		try {
			if (null == object0) {
				return (null == object1) ? DominoUtils.EQUAL : (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
			} else if (null == object1) {
				return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
			}

			final Date date0 = Dates.getDate(object0);
			final Date date1 = Dates.getDate(object1);

			if (null == date0) {
				return (null == date1) ? DominoUtils.EQUAL : (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
			} else if (null == date1) {
				return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
			}

			if (date0.equals(date1)) {
				return DominoUtils.EQUAL;
			} else {
				final int daysbetween = Dates.getDaysBetween(date0, date1);
				if (0 == daysbetween) {
					return DominoUtils.EQUAL;
				}

				final int temp = (daysbetween > 0) ? DominoUtils.LESS_THAN : (daysbetween < 0) ? DominoUtils.GREATER_THAN
						: DominoUtils.EQUAL;
				return (descending) ? -1 * temp : temp;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
			throw new RuntimeException("EXCEPTION in Dates.compareDaysBetween()");
		}
	}

	/**
	 * Compares two Objects as Dates
	 * 
	 * Arguments are first compared by existence, then by value.
	 * 
	 * @param object0
	 *            First date to compare.
	 * 
	 * @param object1
	 *            Second date to compare.
	 * 
	 * @return a negative integer, zero, or a positive integer indicating if the first object is less than, equal to, or greater than the
	 *         second object.
	 * 
	 * @throws RuntimeException
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see DominoUtils#LESS_THAN
	 * @see DominoUtils#EQUAL
	 * @see DominoUtils#GREATER_THAN
	 */
	@SuppressWarnings("javadoc")
	public static int compareDaysBetween(final Object object0, final Object object1) {
		return (Dates.compareDaysBetween(object0, object1, false));
	}

	/**
	 * Compares the ELAPSED days of two Date objects.
	 * 
	 * Arguments are first compared by existence, then by value.
	 * 
	 * @param object0
	 *            First date to compare.
	 * 
	 * @param object1
	 *            Second date to compare.
	 * 
	 * @param descending
	 *            flags indicating comparison order. true = descending, false = ascending.
	 * 
	 * @return a negative integer, zero, or a positive integer indicating if the number of days elapsed since the first date is less than,
	 *         equal to, or greater than the number of days elapsed since the second date.
	 * 
	 * @throws RuntimeException
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see DominoUtils#LESS_THAN
	 * @see DominoUtils#EQUAL
	 * @see DominoUtils#GREATER_THAN
	 */
	@SuppressWarnings("javadoc")
	public static int compareElapsedDays(final Object object0, final Object object1, final boolean descending) {
		try {
			if (null == object0) {
				return (null == object1) ? DominoUtils.EQUAL : (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
			} else if (null == object1) {
				return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
			}

			final Date date0 = Dates.getDate(object0);
			final Date date1 = Dates.getDate(object1);

			if (null == date0) {
				return (null == date1) ? DominoUtils.EQUAL : (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
			} else if (null == date1) {
				return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
			}

			if (date0.equals(date1)) {
				return DominoUtils.EQUAL;
			} else {
				final Date now = Dates.getDate();
				final int elapsed0 = Dates.getDaysBetween(date0, now);
				final int elapsed1 = Dates.getDaysBetween(date1, now);
				if (elapsed0 == elapsed1) {
					return DominoUtils.EQUAL;
				}

				final int temp = (elapsed0 < elapsed1) ? DominoUtils.GREATER_THAN : (elapsed0 > elapsed1) ? DominoUtils.LESS_THAN
						: DominoUtils.EQUAL;
				return (descending) ? -1 * temp : temp;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
			throw new RuntimeException("EXCEPTION in Dates.compareElapsedDays()");
		}
	}

	/**
	 * Compares two Objects as Dates
	 * 
	 * Arguments are first compared by existence, then by value.
	 * 
	 * @param object0
	 *            First date to compare.
	 * 
	 * @param object1
	 *            Second date to compare.
	 * 
	 * @return a negative integer, zero, or a positive integer indicating if the first object is less than, equal to, or greater than the
	 *         second object.
	 * 
	 * @throws RuntimeException
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see DominoUtils#LESS_THAN
	 * @see DominoUtils#EQUAL
	 * @see DominoUtils#GREATER_THAN
	 */
	@SuppressWarnings("javadoc")
	public static int compareElapsedDays(final Object object0, final Object object1) {
		return (Dates.compareElapsedDays(object0, object1, false));
	}

	/**
	 * Compares two Objects as Dates
	 * 
	 * Arguments are first compared by existence, then by value.
	 * 
	 * @param object0
	 *            First date to compare.
	 * 
	 * @param object1
	 *            Second date to compare.
	 * 
	 * @param descending
	 *            flags indicating comparison order. true = descending, false = ascending.
	 * 
	 * @return a negative integer, zero, or a positive integer indicating if the first object is less than, equal to, or greater than the
	 *         second object.
	 * 
	 * @throws RuntimeException
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see DominoUtils#LESS_THAN
	 * @see DominoUtils#EQUAL
	 * @see DominoUtils#GREATER_THAN
	 */
	@SuppressWarnings("javadoc")
	public static int compareDates(final Object object0, final Object object1, final boolean descending) {
		try {
			if (null == object0) {
				return (null == object1) ? DominoUtils.EQUAL : (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
			} else if (null == object1) {
				return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
			}

			final Date date0 = Dates.getDate(object0);
			final Date date1 = Dates.getDate(object1);

			if (null == date0) {
				return (null == date1) ? DominoUtils.EQUAL : (descending) ? DominoUtils.GREATER_THAN : DominoUtils.LESS_THAN;
			} else if (null == date1) {
				return (descending) ? DominoUtils.LESS_THAN : DominoUtils.GREATER_THAN;
			}

			return (date0.equals(date1)) ? DominoUtils.EQUAL : (descending) ? date1.compareTo(date0) : date0.compareTo(date1);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
			throw new RuntimeException("EXCEPTION in Dates.compareDates()");
		}
	}
}
