/**
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
	;

	public static final String TIMESTAMP_DATEONLY = "dd MMM yyyy";
	public static final String TIMESTAMP_TIMEONLY = "HH:mm aa";
	public static final String TIMESTAMP_DEFAULT = "dd MMM yyyy hh:mm aa zzz";
	public static final String TIMESTAMP_DAYMONTH_NAMES = "EEE, dd MMM yyyy HH:mm:ss aa zzz";
	public static final String TIMESTAMP_MILITARY = "yyyyMMdd HHmm:ss, zzz";
	public static final String TIMESTAMP_SIMPLETIME = "HHmmaa";

	private static final String REGEX_BEGIN_NOCASE = "(?i)^";
	private static final String REGEX_MONTH = "(?:Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May?|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?)";
	private static final String REGEX_DAYOFWEEK = "(?:Sun(?:day)?|Mon(?:day)?|Tue(?:sday)?|Wed(?:nesday)?|Thu(?:rsday)?|Fri(?:day)?|Sat(?:urday)?)";
	private static final String REGEX_9_1 = "\\d{1}";
	private static final String REGEX_9_2 = "\\d{2}";
	private static final String REGEX_9_3 = "\\d{3}";
	private static final String REGEX_9_4 = "\\d{4}";
	private static final String REGEX_9_5 = "\\d{5}";
	private static final String REGEX_9_6 = "\\d{6}";
	private static final String REGEX_9_7 = "\\d{7}";
	private static final String REGEX_9_8 = "\\d{8}";
	private static final String REGEX_9_9 = "\\d{9}";
	private static final String REGEX_ampm = "[ap]m";
	private static final String REGEX_TIMEZONE = "[a-z]{3}";
	private static final String REGEX_HHmm = Strings.join(":", Dates.REGEX_9_2, Dates.REGEX_9_2);
	private static final String REGEX_HHmmss = Strings.join(":", Dates.REGEX_9_2, Dates.REGEX_9_2, Dates.REGEX_9_2);
	private static final String REGEX_END = "$";

	private static final String REGEX_DATEONLY = Strings.join(" ", Dates.REGEX_9_2, Dates.REGEX_MONTH, Dates.REGEX_9_4);
	private static final String REGEX_TIMEONLY = Strings.join(" ", Dates.REGEX_HHmm, Dates.REGEX_ampm);
	private static final String REGEX_DEFAULT = Strings.join(" ", Dates.REGEX_DATEONLY, Dates.REGEX_TIMEONLY, Dates.REGEX_TIMEZONE);

	private static final String REGEX_DAYMONTH_NAMES = Strings.join(" ", Dates.REGEX_DAYOFWEEK, Dates.REGEX_DATEONLY, Dates.REGEX_HHmmss,
			Dates.REGEX_ampm, Dates.REGEX_TIMEZONE);

	private static final String REGEX_MILITARY = Strings.join(" ", Dates.REGEX_9_8, Dates.REGEX_HHmmss + ",", Dates.REGEX_TIMEZONE);

	private static final String REGEX_SIMPLETIME = Dates.REGEX_9_4 + Dates.REGEX_ampm;

	public static enum TimeFormatter {
		DATEONLY(Dates.TIMESTAMP_DATEONLY, Dates.REGEX_DATEONLY),

		TIMEONLY(Dates.TIMESTAMP_TIMEONLY, Dates.REGEX_TIMEONLY),

		DAYMONTH_NAMES(Dates.TIMESTAMP_DAYMONTH_NAMES, Dates.REGEX_DAYMONTH_NAMES),

		DEFAULT(Dates.TIMESTAMP_DEFAULT, Dates.REGEX_DEFAULT),

		MILITARY(Dates.TIMESTAMP_MILITARY, Dates.REGEX_MILITARY),

		SIMPLETIME(Dates.TIMESTAMP_SIMPLETIME, Dates.REGEX_SIMPLETIME);

		private final String format;
		private final Pattern pattern;
		private SimpleDateFormat sdf;

		private TimeFormatter(final String format, final String regex) {
			this.format = format;
			this.pattern = Pattern.compile(Dates.REGEX_BEGIN_NOCASE + regex + Dates.REGEX_END);
		}

		public String extendedName() {
			return this.getDeclaringClass() + ".TimeStamp." + this.name();
		}

		public String getFormat() {
			return this.format;
		}

		public Pattern getPattern() {
			return this.pattern;
		}

		public SimpleDateFormat getSimpleDateFormat() {
			if (null == this.sdf) {
				this.sdf = Dates.getSimpleDateFormat(this.getFormat());
				this.sdf.setLenient(true);
			}

			return this.sdf;
		}

		public String getTimestamp() {
			return this.getTimestamp(null);
		}

		public String getTimestamp(final Date date) {
			return this.getSimpleDateFormat().format((null == date) ? Dates.getDate() : date);
		}

		public String getTimestamp(final Object object) {
			return this.getTimestamp(Dates.getDate(object));
		}

		public boolean matches(final String source) {
			try {
				final Matcher m = this.getPattern().matcher(source);
				return m.matches();
			} catch (final Exception e) {
				DominoUtils.handleException(e);
			}

			return false;
		}
	};

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
				return Dates.TimeFormatter.DEFAULT.getTimestamp(object);
			}

			for (final Dates.TimeFormatter tf : Dates.TimeFormatter.values()) {
				if (format.equals(tf.getFormat())) {
					return tf.getTimestamp(object);
				}
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
		return Dates.getTimestamp(date, null);
	}

	/**
	 * Gets the timestamp.
	 * 
	 * @return the timestamp
	 */
	public static String getTimestamp() {
		return Dates.TimeFormatter.DEFAULT.getTimestamp();
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
	 *            First object from which a Calendar object can be constructred.
	 * @param object1
	 *            Second object from which a Calendar object can be constructred.
	 * 
	 * @return Flag indicating of the two Dates represent the same day.
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

	// public static boolean isInRange(final Object source, final Object begin,
	// final Object end, final boolean inclusive) {}

	/**
	 * Gets the days between two Dates.
	 * 
	 * Spawns Calendar objects from the arguments, and returns the number of days between them. Returns a negative value if the first
	 * argument is after the second argument.
	 * 
	 * @param object0
	 *            First object from which a Calendar object can be constructred.
	 * @param object1
	 *            Second object from which a Calendar object can be constructred.
	 * @return Days between the two objects.
	 */
	public static int getDaysBetween(final Object object0, final Object object1) {
		int result = 0;
		// int maximum days to 2,147,483,647
		// a range of nearly 5.9 million years.

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
	 *            First object from which a Calendar object can be constructred.
	 * @param object1
	 *            Second object from which a Calendar object can be constructred.
	 * @param holidays
	 *            Holiday days (as Calendary.DAY_OF_YEAR) to exclude
	 * @return Business Days between the two objects.
	 */
	public static int getBusinessDaysBetween(final Object object0, final Object object1, final List<Integer> holidays) {
		int result = 0;
		// int maximum days to 2,147,483,647
		// a range of nearly 5.9 million years.

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

			return Dates.getHours(object0) - Dates.getHours(object1);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	public static long getMinutesBetween(final Object object0, final Object object1) {
		// whole number of minutes between two values. Assumes object0 <
		// object1, returns negative otherwise
		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			return Dates.getMinutes(object0) - Dates.getMinutes(object1);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	public static long getSecondsBetween(final Object object0, final Object object1) {
		// whole number of seconds between two values. Assumes object0 <
		// object1, returns negative otherwise
		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			return Dates.getSeconds(object0) - Dates.getSeconds(object1);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	public static long getMillisecondsBetween(final Object object0, final Object object1) {
		// whole number of milliseconds between two values. Assumes object0 <
		// object1, returns negative otherwise
		try {
			if (null == object0) {
				throw new IllegalArgumentException("object0 is null");
			}

			if (null == object1) {
				throw new IllegalArgumentException("object1 is null");
			}

			return Dates.getTime(object0) - Dates.getTime(object1);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	public static long getHours(final Object object) {
		// whole number of hours from the Epoch January 1, 1970, 00:00:00 GMT
		try {
			return TimeUnit.HOURS.convert(Dates.getTime(object), TimeUnit.MILLISECONDS);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	public static long getMinutes(final Object object) {
		// whole number of seconds from the Epoch January 1, 1970, 00:00:00 GMT
		try {
			return TimeUnit.MINUTES.convert(Dates.getTime(object), TimeUnit.MILLISECONDS);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

	public static long getSeconds(final Object object) {
		// whole number of seconds from the Epoch January 1, 1970, 00:00:00 GMT
		try {
			return TimeUnit.SECONDS.convert(Dates.getTime(object), TimeUnit.MILLISECONDS);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return 0;
	}

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
	 * for one successfully matches() the key. Returns the first one found.
	 * 
	 * @param key
	 *            String value to either equal the TimeFormatter's name or format or successfully match the TimeFormatter.
	 * 
	 * @return First found TimeFormatter. Null if no name or format equality or match found.
	 */
	public static Dates.TimeFormatter getTimeFormatter(final String key) {
		if (!Strings.isBlankString(key)) {
			for (final Dates.TimeFormatter result : Dates.TimeFormatter.values()) {
				if (result.name().equalsIgnoreCase(key) || result.getFormat().equalsIgnoreCase(key)) {
					return result;
				}
			}
			for (final Dates.TimeFormatter result : Dates.TimeFormatter.values()) {
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
	 * @return Newly created or casted Date from the source. Null on exception.
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
	 *            Source from which to get the DATE portion of the new Date object.
	 * 
	 * @param time
	 *            Source from which to get the TIME portion of the new Date object.
	 * 
	 * @return Newly created or cast Date from the source. Null on exception.
	 */
	public Date getDate(final Object date, final Object time) {
		try {
			if ((null == date) && (null == time)) {
				return Dates.getDate();
			}

			final Date dDate = (null == date) ? Dates.getDate(time) : Dates.getDate(date);
			final Date dTime = (null == time) ? Dates.getDate() : Dates.getDate(time);
			final Calendar cDate = Dates.getCalendar(dDate);
			final Calendar cTime = Dates.getCalendar(dTime);

			cDate.set(Calendar.HOUR_OF_DAY, cTime.get(Calendar.HOUR_OF_DAY));
			cDate.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
			cDate.set(Calendar.SECOND, cTime.get(Calendar.SECOND));

			return cDate.getTime();

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
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

	public static SimpleDateFormat getSimpleDateFormat(final String format) {
		final SimpleDateFormat result = (Strings.isBlankString(format)) ? new SimpleDateFormat() : new SimpleDateFormat(format);
		result.setLenient(true);
		return result;
	}

	public static SimpleDateFormat getSimpleDateFormat() {
		return Dates.getSimpleDateFormat(null);
	}

	public static Date parse(final String string) {
		if (!Strings.isBlankString(string)) {
			try {
				final Dates.TimeFormatter tf = Dates.getTimeFormatter(string);
				final SimpleDateFormat sdf = (null == tf) ? Dates.getSimpleDateFormat() : tf.getSimpleDateFormat();
				return sdf.parse(string);

			} catch (final Exception e) {
				DominoUtils.handleException(e);
			}
		}

		return null;
	}

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