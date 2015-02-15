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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;

import org.openntf.domino.Document;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * String Utilities
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
 * 
 */
public enum Strings {
	;

	/*
	 * **************************************************************************
	 * **************************************************************************
	 * 
	 * PUBLIC STATIC properties
	 * 
	 * **************************************************************************
	 * **************************************************************************
	 */
	public static final String CHARSET_NAME = "UTF-8";

	// <ALT> + 0216 on numeric keypad
	public static final String DEFAULT_DELIMITER = "~";

	public static final String MESSAGE_DIGEST_ALGORYTHM = "MD5";
	public static final String MESSAGE_FORMULA_INVALID = "The Formula syntax is invalid.  ";

	public static final String REGEX_PIPE = "\\|";
	public static final Pattern PATTERN_PIPE = Pattern.compile(REGEX_PIPE);
	public static final String REGEX_NEWLINE = "\\r?\\n";
	public static final Pattern PATTERN_NEWLINE = Pattern.compile(REGEX_NEWLINE);
	public static final String REGEX_BEGIN_NOCASE = "(?i)^";
	public static final Pattern PATTERN_BEGIN_NOCASE = Pattern.compile(REGEX_BEGIN_NOCASE);
	public static final String REGEX_MONTH = "(?:Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May?|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?)";
	public static final String REGEX_DAYOFWEEK = "(?:Sun(?:day)?|Mon(?:day)?|Tue(?:sday)?|Wed(?:nesday)?|Thu(?:rsday)?|Fri(?:day)?|Sat(?:urday)?)";
	public static final String REGEX_9_1 = "\\d{1}";
	public static final String REGEX_9_2 = "\\d{2}";
	public static final String REGEX_9_3 = "\\d{3}";
	public static final String REGEX_9_4 = "\\d{4}";
	public static final String REGEX_9_5 = "\\d{5}";
	public static final String REGEX_9_6 = "\\d{6}";
	public static final String REGEX_9_7 = "\\d{7}";
	public static final String REGEX_9_8 = "\\d{8}";
	public static final String REGEX_9_9 = "\\d{9}";
	public static final String REGEX_ampm = "[ap]m";
	public static final String REGEX_TIMEZONE = "[a-z]{3}";
	public static final String REGEX_HHmm = Strings.join(":", Strings.REGEX_9_2, Strings.REGEX_9_2);
	public static final String REGEX_HHmmss = Strings.join(":", Strings.REGEX_9_2, Strings.REGEX_9_2, Strings.REGEX_9_2);
	public static final String REGEX_END = "$";
	public static final String REGEX_DATEONLY = Strings.join(" ", Strings.REGEX_9_2, Strings.REGEX_MONTH, Strings.REGEX_9_4);
	public static final String REGEX_TIMEONLY = Strings.join(" ", Strings.REGEX_HHmm, Strings.REGEX_ampm);
	public static final String REGEX_DEFAULT = Strings.join(" ", Strings.REGEX_DATEONLY, Strings.REGEX_TIMEONLY, Strings.REGEX_TIMEZONE);

	public static final String REGEX_DAYMONTH_NAMES = Strings.join(" ", Strings.REGEX_DAYOFWEEK, Strings.REGEX_DATEONLY,
			Strings.REGEX_HHmmss, Strings.REGEX_ampm, Strings.REGEX_TIMEZONE);

	public static final String REGEX_MILITARY = Strings.join(" ", Strings.REGEX_9_8, Strings.REGEX_HHmmss + ",", Strings.REGEX_TIMEZONE);

	public static final String REGEX_MEDDATE = "\\d+/\\d+/\\d{4}";
	public static final String REGEX_SHORTDATE = "\\d+/\\d+/\\d+";
	public static final String REGEX_SIMPLETIME = Strings.REGEX_9_4 + Strings.REGEX_ampm;

	public static final String TIMESTAMP_DATEONLY = "dd MMM yyyy";
	public static final String TIMESTAMP_TIMEONLY = "HH:mm aa";
	public static final String TIMESTAMP_DEFAULT = "dd MMM yyyy hh:mm aa zzz";
	public static final String TIMESTAMP_DAYMONTH_NAMES = "EEE, dd MMM yyyy HH:mm:ss aa zzz";
	public static final String TIMESTAMP_MEDDATE = "MM/dd/yyyy";
	public static final String TIMESTAMP_MILITARY = "yyyyMMdd HHmm:ss, zzz";
	public static final String TIMESTAMP_SHORTDATE = "MM/dd/yy";
	public static final String TIMESTAMP_SIMPLETIME = "HHmmaa";

	public static enum IDTYPE {
		BLANK, REPLICA, NOTE, UNIVERSAL;

		@Override
		public String toString() {
			return this.name();
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

	/*
	 * ************************************************************************
	 * ************************************************************************
	 * 
	 * PUBLIC Methods
	 * 
	 * ************************************************************************
	 * ************************************************************************
	 */

	/**
	 * Gets or generates a Vector of Strings from an Object
	 * 
	 * @param object
	 *            Object from which to get or generate the result. Attempts to retrieve the string values from the object.
	 * 
	 * @return Vector of Strings retrieved or generated from the input. Returns null on error.
	 * 
	 */
	public static Vector<String> getVectorizedStrings(final Object object) {
		final List<String> al = CollectionUtils.getListStrings(object);
		return ((null == al) || (al.size() < 1)) ? null : new Vector<String>(al);
	}

	/**
	 * Generates a properly formatted String which can be used as an Environment Variable name.
	 * 
	 * Returns trimmed identifier converted to all uppercase with spaces converted to underscores.
	 * 
	 * @param identifier
	 *            String from which to generate an Environment Variable name.
	 * 
	 * @return Environment Variable name generated from identifier. Empty string "" if blank.
	 */
	public static String getEnvarName(final String identifier) {
		return (Strings.isBlankString(identifier)) ? "" : identifier.trim().toUpperCase().replaceAll(" ", "_");
	}

	/**
	 * Determines if a specified id is a potential id based upon the specified idType.
	 * 
	 * @param id
	 *            String to check if potential ID for type.
	 * @param idType
	 *            Type of id for which to check id.
	 * @return Flag indicating if the specified id is an allowed potential id for the specified type.
	 */
	public static boolean isPotentialID(final String id, final IDTYPE idType) {
		try {
			if (null == id) {
				return IDTYPE.BLANK.equals(idType);
			}

			if (IDTYPE.UNIVERSAL.equals(idType)) {
				return ((32 == id.length()) && Strings.isHexadecimalString(id));
			}

			if (IDTYPE.NOTE.equals(idType)) {
				boolean result = ((id.length() > 0) && (id.length() <= 8));
				if (result) {
					return Strings.isHexadecimalString(id);
				}

				if ((id.length() > 2) && ("NT".equalsIgnoreCase(id.substring(0, 1)))) {
					result = Strings.isPotentialID(id.substring(2), idType);
				}

				return result;
			}

			if (IDTYPE.REPLICA.equals(idType)) {
				return ((16 == id.length()) && Strings.isHexadecimalString(id));
			}

			if (IDTYPE.BLANK.equals(idType)) {
				return (Strings.isBlankString(id));
			}

			throw new IllegalArgumentException("Unsupported IDTYPE");

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Generates a UniversalID from a String
	 * 
	 * wrapper method for {@link #getHash(String)}
	 * 
	 * @param string
	 *            Source from which to generate the UniversalID.
	 * 
	 * @return UniversalID (MD5 Message Digest Hash)
	 * 
	 * @see #getHash(String)
	 */
	public static String generateUniversalID(final String string) {
		return Strings.getHash(string);
	}

	/**
	 * Generates a RecordID from a Name
	 * 
	 * Format: Name.idPrefix (4 Alpha characters) + "-" + Dates.TimeCode (6 character base36 value representing the time).
	 * 
	 * wrapper method for {@link #getSpawnedRecordID(Name)}
	 * 
	 * @param name
	 *            Name for which to generate a RecordID
	 * 
	 * @return new RecordID
	 * 
	 * @see #generateRecordID(Name)
	 */
	public static String generateRecordID(final Name name) {
		return Strings.getSpawnedRecordID(name);
	}

	/**
	 * Generates a RecordID from a Name
	 * 
	 * Format: Name.idPrefix (4 Alpha characters) + "-" + Dates.TimeCode (6 character base36 value representing the time).
	 * 
	 * wrapper method for {@link #getSpawnedRecordID(Session)}
	 * 
	 * @return new RecordID
	 * 
	 * @see #generateRecordID()
	 */
	public static String generateRecordID() {
		return Strings.getSpawnedRecordID(Factory.getSession(SessionType.CURRENT));
	}

	/**
	 * Generates a Hash from a string.
	 * 
	 * Uses the MD5 Message Digest Algorythm.
	 * 
	 * @param string
	 *            Source from which to generate the hash.
	 * @return MD5 Message Digest Hash
	 * 
	 * @see #md5Hex(String)
	 */
	public static String getHash(final String string) {
		return (Strings.isBlankString(string)) ? "" : Strings.md5Hex(string);
	}

	/**
	 * Generates a String filled with a specific character.
	 * 
	 * @param length
	 *            The length of the String to return. Negative values will be treated as positive.
	 * 
	 * @param c
	 *            Character with which to populate the result.
	 * 
	 * @return String consisting c characters repeated length times.
	 */
	public static String getFilledString(final int length, final char c) {
		if (0 == length) {
			return "";
		}

		final int abslen = Math.abs(length);
		final StringBuilder sb = new StringBuilder(abslen);
		for (int i = 0; i < abslen; i++) {
			sb.append(c);
		}

		return sb.toString();
	}

	/**
	 * Generates a Proper Case (first character capitalized, all other characters unchanged) string from an input string.
	 * 
	 * @param string
	 *            Source string for which to generate a Proper Case string.
	 * 
	 * @return Proper Case string from the specified source string.
	 */
	public static String toProperCase(final String string) {
		return isBlankString(string) ? "" : string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}

	/**
	 * Determines whether a CharSequence consists only of "White Spaces" (or is null or empty)
	 * 
	 * @param cs
	 *            The CharSequence to test
	 * 
	 * @return true if it does
	 */
	public static boolean isBlankString(final CharSequence cs) {
		if (cs == null)
			return true;
		for (int i = cs.length() - 1; i >= 0; i--)
			if (!Character.isWhitespace(cs.charAt(i)))
				return false;
		return true;
	}

	/**
	 * Determines if a string is null or blank
	 * 
	 * @param o
	 *            Source string to check for null or blank value.
	 * 
	 * @return Flag indicating if the source string is null or blank.
	 */
	public static boolean isBlankString(final Object o) {
		if (o == null)
			return true;
		if (o instanceof CharSequence)
			return isBlankString((CharSequence) o);
		throw new RuntimeException("Cannot check for blankness on a non-null object of type " + o.getClass().getName());
	}

	/**
	 * Determines if a specified object is a Hexadecimal string (comprised of characters 0-9 or A-F, case insensitive)
	 * 
	 * @param o
	 *            Object to check.
	 * @return true if it is
	 */
	public static boolean isHexadecimalString(final Object o) {
		if (o == null)
			return false;
		if (o instanceof CharSequence)
			return isHexadecimalString((CharSequence) o);
		throw new RuntimeException("Cannot check for hexidecimal on a non-null object of type " + o.getClass().getName());
	}

	private static final Pattern PatternHexadecimal = Pattern.compile("^[A-Fa-f0-9]+$");

	/**
	 * Determines if a specified string is a Hexadecimal string (comprised of characters 0-9 or A-F, case insensitive)
	 * 
	 * @param cs
	 *            Source string to check.
	 * @return Flag indicating if string is comprised only of Hexadecimal characters.
	 */
	public static boolean isHexadecimalString(final CharSequence cs) {
		return Strings.isBlankString(cs) ? false : PatternHexadecimal.matcher(cs).matches();
	}

	/**
	 * Joins elements into a String using a specified delimiter.
	 * 
	 * Concatenates the string values of elements from collection using a specified delimiter
	 * 
	 * @param source
	 *            Collection to join
	 * 
	 * @param delimiter
	 *            String used to delimit elements in source
	 * 
	 * @return String values of all elements in source concatenated by delimiter
	 */
	@SuppressWarnings({ "rawtypes", "cast" })
	public static String join(final Collection source, final String delimiter) {
		if ((null != source) && (source.size() > 0)) {
			final StringBuilder stringbuilder = new StringBuilder();
			if (source.iterator().next() instanceof Object) {
				// treat as an object
				for (final Object o : source) {
					stringbuilder.append(o.toString() + delimiter);
				}
			} else {
				// treat as a primitive
				final Iterator it = source.iterator();
				while (it.hasNext()) {
					stringbuilder.append(String.valueOf(it.next()) + delimiter);
				}
			}

			return stringbuilder.substring(0, stringbuilder.lastIndexOf(delimiter));
		}

		return "";
	}

	public static String join(final String[] source, final String delimiter) {
		if ((null != source) && (source.length > 0)) {
			final StringBuilder stringbuilder = new StringBuilder();

			for (int i = 0; i < source.length; i++) {
				stringbuilder.append(source[i]);
				if (i < source.length) {
					stringbuilder.append(delimiter);
				}
			}

			return stringbuilder.toString();
		}
		return "";
	}

	/**
	 * Joins elements into a String using a specified delimiter.
	 * 
	 * Concatenates the string values of elements from collection using a specified delimiter
	 * 
	 * @param delimiter
	 *            String used to delimit elements in source
	 * 
	 * @param objects
	 *            Objects to join
	 * 
	 * @return String values of all elements in source concatenated by delimiter
	 */
	public static String join(final String delimiter, final Object... objects) {
		final StringBuilder stringbuilder = new StringBuilder();
		if (Strings.isBlankString(delimiter)) {
			for (final Object o : objects) {
				stringbuilder.append(o.toString());
			}

			return stringbuilder.toString();

		} else {
			for (final Object o : objects) {
				stringbuilder.append(o.toString() + delimiter);
			}

			return stringbuilder.substring(0, stringbuilder.lastIndexOf(delimiter));
		}
	}

	/**
	 * Joins elements into a String using a specified delimiter.
	 * 
	 * Concatenates the string values of elements from an array or collection using a specified delimiter
	 * 
	 * @param source
	 *            Array or Collection to join
	 * 
	 * @param delimiter
	 *            String used to delimit elements
	 * 
	 * @return String values of all elements in source concatenated by delimiter
	 */
	@SuppressWarnings("rawtypes")
	public static String join(final Object source, final String delimiter) {
		if (null != source) {
			if (source instanceof Collection) {
				return Strings.join((Collection) source, delimiter);
			}

			final String classname = source.getClass().getName();
			if (classname.equalsIgnoreCase("java.lang.String[]") || classname.equalsIgnoreCase("[Ljava.lang.String;")) {
				final StringBuilder stringbuilder = new StringBuilder();
				if (Strings.isBlankString(delimiter)) {
					for (final String s : (String[]) source) {
						stringbuilder.append(s + delimiter);
					}
					return stringbuilder.toString();

				} else {

					for (final String s : (String[]) source) {
						stringbuilder.append(s + delimiter);
					}
					return stringbuilder.substring(0, stringbuilder.lastIndexOf(delimiter));
				} // if (Strings.isBlankString(delimiter))

			} else {
				return Strings.join(CollectionUtils.getStringArray(source), delimiter);
			}
		}

		return "";
	}

	/**
	 * Removes all blank spaces from a specified StringBuilder object.
	 * 
	 * @param sb
	 *            StringBuilder object from which to remove all blank spaces.
	 * @return StringBuilder object with all blank spaces removed.
	 */
	public static StringBuilder removeBlankSpace(final StringBuilder sb) {
		if (null != sb) {
			int currentEnd = -1;
			for (int i = sb.length() - 1; i >= 0; i--) {
				if (Character.isWhitespace(sb.charAt(i))) {
					if (currentEnd == -1) {
						currentEnd = i + 1;
					}
				} else {
					// Moved from whitespace to non-whitespace
					if (currentEnd != -1) {
						sb.delete(i + 1, currentEnd);
						currentEnd = -1;
					}
				}
			}
			// All leading whitespace
			if (currentEnd != -1) {
				sb.delete(0, currentEnd);
			}
		}

		return sb;
	}

	/**
	 * Returns the substring to the left of the specified substring in the specified String, starting from the left.
	 * 
	 * @param source
	 *            String within which to search for searchFor
	 * 
	 * @param searchFor
	 *            String to search for within source
	 * 
	 * @return Substring to the left of the specified substring in the specified String, starting from the left.
	 */
	public static String left(final String source, final String searchFor) {
		final int idx = (Strings.isBlankString(source) || Strings.isBlankString(searchFor)) ? -1 : source.indexOf(searchFor);
		return (idx > -1) ? source.substring(0, idx) : "";
	}

	/**
	 * Returns the substring to the left of the specified substring in the specified String, starting from the right.
	 * 
	 * @param source
	 *            String within which to search for searchFor
	 * 
	 * @param searchFor
	 *            String to search for within source
	 * 
	 * @return Substring to the left of the specified substring in the specified String, starting from the right.
	 */
	public static String leftBack(final String source, final String searchFor) {
		final int idx = (Strings.isBlankString(source) || Strings.isBlankString(searchFor)) ? -1 : source.lastIndexOf(searchFor);
		return (idx > -1) ? source.substring(0, idx) : "";
	}

	/**
	 * Returns the substring to the right of the specified substring in the specified String, starting from the left.
	 * 
	 * @param source
	 *            String within which to search for searchFor
	 * 
	 * @param searchFor
	 *            String to search for within source
	 * 
	 * @return Substring to the right of the specified substring in the specified String, starting from the left.
	 */
	public static String right(final String source, final String searchFor) {
		final int idx = (Strings.isBlankString(source) || Strings.isBlankString(searchFor)) ? -1 : source.indexOf(searchFor);
		return (idx > -1) ? source.substring(idx + 1) : "";
	}

	/**
	 * Returns the substring to the right of the specified substring in the specified String, starting from the right.
	 * 
	 * @param source
	 *            String within which to search for searchFor
	 * 
	 * @param searchFor
	 *            String to search for within source
	 * 
	 * @return Substring to the right of the specified substring in the specified String, starting from the right.
	 */
	public static String rightBack(final String source, final String searchFor) {
		final int idx = (Strings.isBlankString(source) || Strings.isBlankString(searchFor)) ? -1 : source.lastIndexOf(searchFor);
		return (idx > -1) ? source.substring(idx + 1) : "";
	}

	/**
	 * Returns the substring to the left of the specified substring in the specified String, starting from the left.
	 * 
	 * @param source
	 *            String within which to search for searchFor
	 * 
	 * @param searchFor
	 *            String to search for within source
	 * 
	 * @return Substring to the left of the specified substring in the specified String, starting from the left.
	 */
	public static String left(final String source, final char searchFor) {
		final int idx = (Strings.isBlankString(source)) ? -1 : source.indexOf(searchFor);
		return (idx > -1) ? source.substring(0, idx) : "";
	}

	/**
	 * Returns the substring to the left of the specified substring in the specified String, starting from the right.
	 * 
	 * @param source
	 *            String within which to search for searchFor
	 * 
	 * @param searchFor
	 *            String to search for within source
	 * 
	 * @return Substring to the left of the specified substring in the specified String, starting from the right.
	 */
	public static String leftBack(final String source, final char searchFor) {
		final int idx = (Strings.isBlankString(source)) ? -1 : source.lastIndexOf(searchFor);
		return (idx > -1) ? source.substring(0, idx) : "";
	}

	/**
	 * Returns the substring to the right of the specified substring in the specified String, starting from the left.
	 * 
	 * @param source
	 *            String within which to search for searchFor
	 * 
	 * @param searchFor
	 *            String to search for within source
	 * 
	 * @return Substring to the right of the specified substring in the specified String, starting from the left.
	 */
	public static String right(final String source, final char searchFor) {
		final int idx = (Strings.isBlankString(source)) ? -1 : source.indexOf(searchFor);
		return (idx > -1) ? source.substring(idx + 1) : "";
	}

	/**
	 * Returns the substring to the right of the specified substring in the specified String, starting from the right.
	 * 
	 * @param source
	 *            String within which to search for searchFor
	 * 
	 * @param searchFor
	 *            String to search for within source
	 * 
	 * @return Substring to the right of the specified substring in the specified String, starting from the right.
	 */
	public static String rightBack(final String source, final char searchFor) {
		final int idx = (Strings.isBlankString(source)) ? -1 : source.lastIndexOf(searchFor);
		return (idx > -1) ? source.substring(idx + 1) : "";
	}

	/**
	 * Strips all non-alphanumeric characters from a source string.
	 * 
	 * Alphanumeric is defined as all characters from A-Z, a-z, and 0-9
	 * 
	 * @param source
	 *            String from which to strip the non-alphanumeric characters.
	 * @return source with all non-alphanumeric characters removed.
	 */
	public static String getAlphanumericOnly(final String source) {
		return (null == source) ? "" : source.trim().replaceAll("[^A-Za-z0-9]", "");
	}

	/**
	 * Checks a String to determine if it begins with a prefix.
	 * 
	 * Performs a Case-INSENSITIVE check.
	 * 
	 * <strong>Special Behavior</strong>: Returns false if source or prefix are null.
	 * 
	 * @param source
	 *            CharSequence to check if begins with prefix.
	 * 
	 * @param prefix
	 *            CharSequence to match against the beginning of source.
	 * 
	 * @return Flag indicating if source begins with prefix.
	 */
	public static boolean startsWithIgnoreCase(final CharSequence source, final CharSequence prefix) {
		// return source.toLowerCase().startsWith(prefix.toLowerCase());
		// Of source, this works for Strings, but the variant below is cheaper
		if (null == source || null == prefix)
			return false;
		int sz = prefix.length();
		if (sz > source.length())
			return false;
		for (int i = 0; i < sz; i++)
			if (Character.toLowerCase(source.charAt(i)) != Character.toLowerCase(prefix.charAt(i)))
				return false;
		return true;
	}

	/**
	 * Generates a RecordID from a Name
	 * 
	 * Format: Name.idPrefix (4 Alpha characters) + "-" + Dates.TimeCode (6 character base36 value representing the time).
	 * 
	 * @param name
	 *            Name for which to generate a RecordID
	 * 
	 * @return new RecordID
	 * 
	 * @see Name#getIDprefix()
	 * @see Dates#getTimeCode()
	 * 
	 */
	public static String getSpawnedRecordID(final Name name) {
		try {
			if (null == name) {
				throw new IllegalArgumentException("NameHandle is null");
			}

			String result = name.getIDprefix() + "-" + Dates.getTimeCode();
			// RPr: Dates.getTimeCode() does not produce duplicates any more
			//			try {
			//				// avoid potential duplicate consecutive results by sleeping for 1/4 second 
			//				Thread.sleep(5); // 5 millis
			//			} catch (InterruptedException ex) {
			//				Thread.currentThread().interrupt();
			//				DominoUtils.handleException(ex);
			//			}

			return result;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Generates a RecordID from a Name
	 * 
	 * Format: Name.idPrefix (4 Alpha characters) + "-" + Dates.TimeCode (6 character base36 value representing the time).
	 * 
	 * @param name
	 *            Name for which to generate a RecordID
	 * 
	 * @return new RecordID
	 * 
	 * @see Name#getIDprefix()
	 * @see Dates#getTimeCode()
	 * 
	 */
	public static String getSpawnedRecordID(final lotus.domino.Name name) {
		WrapperFactory wf = Factory.getWrapperFactory();
		return Strings.getSpawnedRecordID(wf.fromLotus(name, Name.SCHEMA, null));
	}

	/**
	 * Generates a RecordID from a Name
	 * 
	 * Format: Name.idPrefix (4 Alpha characters) + "-" + Dates.TimeCode (6 character base36 value representing the time).
	 * 
	 * @param session
	 *            Session from which to get the current effective user name.
	 * 
	 * @return new RecordID
	 * 
	 * @see Name#getIDprefix()
	 * @see Dates#getTimeCode()
	 * 
	 */
	public static String getSpawnedRecordID(final Session session) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			return Strings.getSpawnedRecordID(session.createName(session.getEffectiveUserName()));

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Generates a List of Strings from a source String.
	 * 
	 * Breaks the source string at all br and at the ends of div and p elements.
	 * 
	 * @param source
	 *            String from which to generate the list.
	 * 
	 * @return List constructed of source segments.
	 */
	public static List<String> getTextLinesRemoveHTMLtags(final String source) {
		final List<String> result = new ArrayList<String>();
		try {
			if (!Strings.isBlankString(source)) {
				final String stripped = source.replaceAll("<br></div><div>", "<br>").replaceAll("</p></div><div>", "</p>");
				final String searchfor = "</div><div>|<br>|</p>";
				final String[] chunks = stripped.split(searchfor);
				if ((null == chunks) || (chunks.length < 1)) {
					return CollectionUtils.getListStrings(Strings.stripHTMLtags(source));
				} else {
					for (final String s : chunks) {
						result.add(Strings.stripHTMLtags(s));
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return result;
	}

	/**
	 * Wraps String elements of a collection with a prefix and suffix string
	 * 
	 * @param collection
	 *            AbstractCollection from which to get or generate the result.
	 * @param prefix
	 *            String to prepend each returned element.
	 * @param suffix
	 *            String to append each returned element.
	 * 
	 * @return List of Strings retrieved or generated from the input. Returns null on error.
	 */
	@SuppressWarnings({ "rawtypes", "cast" })
	public static List<String> wrapStringElements(final AbstractCollection collection, final String prefix, final String suffix) {
		try {
			final String prepend = Strings.isBlankString(prefix) ? "" : prefix;
			final String append = Strings.isBlankString(suffix) ? "" : suffix;

			if ((null != collection) && (collection.size() > 0)) {
				final List<String> result = new ArrayList<String>();
				if (collection.iterator().next() instanceof Object) {
					// treat as an object
					for (final Object o : collection) {
						result.add(prepend + o.toString() + append);
					}
				} else {
					// treat as a primitive
					final Iterator it = collection.iterator();
					while (it.hasNext()) {
						result.add(prepend + String.valueOf(it.next()) + append);
					}
				}

				return result;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Strips all HTML tags from a source string.
	 * 
	 * @param source
	 *            String from which to strip HTML tags.
	 * 
	 * @return source with all HTML tags removed.
	 */
	public static String stripHTMLtags(final String source) {
		return (Strings.isBlankString(source)) ? "" : source.replaceAll("\\<.*?>", "");
	}

	/**
	 * Gets an HTML Unordered List
	 * 
	 * Converts TreeSet elements to HTML list item elements.
	 * 
	 * @param source
	 *            TreeSet containing elements to be wrapped as HTML List Items
	 * 
	 * @return Concatenated HTML Unordered List
	 */
	public static String getHTMLunorderedList(final TreeSet<String> source) {

		try {
			if (null == source) {
				throw new IllegalArgumentException("Source is null");
			}

			if (source.size() > 0) {
				final StringBuilder sb = new StringBuilder("<ul>");
				for (final String element : Strings.wrapStringElements(source, "<li>", "</li>")) {
					sb.append(element);
				}
				sb.append("</ul>");
				return sb.toString();
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Generates an XML Node
	 * 
	 * @param node
	 *            Node name
	 * @param content
	 *            Node content
	 * 
	 * @return XML Node constructed from name and content
	 */
	public static String getXMLnode(String node, final String content) {
		String result = "";
		node = (null == node) ? "" : node.trim().toLowerCase();
		if (0 == node.length()) {
			return result;
		}

		// TODO: Fail if "xml" is in node

		if (Strings.isBlankString(content)) {
			result = "<" + node + " />";
		} else {
			result = "<" + node + ">" + content + "</" + node + ">";
		}

		return result;
	}

	/**
	 * Generates an XML Node
	 * 
	 * @param node
	 *            Node name
	 * @param content
	 *            Node content
	 * @param attributes
	 *            Attributes for the node.
	 * 
	 * @return XML Node constructed from name, attributes and content
	 */
	public static String getXMLnode(String node, final String content, final Map<String, String> attribs) {
		node = (null == node) ? "" : node.trim().toLowerCase();
		if (0 == node.length()) {
			return "";
		}

		// TODO: Fail if "xml" is in node

		if (null == attribs) {
			return Strings.getXMLnode(node, content);
		}

		String result = "<" + node;
		for (String key : attribs.keySet()) {
			if (key.trim().length() > 0) {
				result += " " + key + "=\"" + attribs.get(key) + "\"";
			}
		} // for (String key : attribs.keySet())

		if (Strings.isBlankString(content)) {
			result += " />";
		} else {
			result += ">" + content + "</" + node + ">";
		}

		return result;
	}

	/*
	 * ************************************************************************
	 * ************************************************************************
	 * 
	 * PROTECTED Methods
	 * 
	 * ************************************************************************
	 * ************************************************************************
	 */

	/**
	 * Generates a Hexidecimal string from an array of bytes
	 * 
	 * @param array
	 *            bytes from which to generate the string
	 * 
	 * @return Hexidecimal String generated from the input array.
	 */
	protected static String hex(final byte[] array) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
		}

		return sb.toString().toUpperCase();
	}

	/**
	 * Generates an MD5 MessageDigest String from a source string
	 * 
	 * @param string
	 *            Source from which to generate the MessageDigest
	 * 
	 * @return Generated Message Digest
	 * 
	 * @see java.security.MessageDigest#digest(byte[])
	 */
	protected static String md5Hex(final String arg0) {
		if (Strings.isBlankString(arg0)) {
			return "";
		}
		try {
			final MessageDigest md = MessageDigest.getInstance(Strings.MESSAGE_DIGEST_ALGORYTHM);
			return Strings.hex(md.digest(arg0.getBytes(Strings.CHARSET_NAME)));
		} catch (final NoSuchAlgorithmException e) {
			DominoUtils.handleException(e);
		} catch (final UnsupportedEncodingException e) {
			DominoUtils.handleException(e);
		}
		return "";
	}

	@SuppressWarnings("rawtypes")
	public static Vector safeEvaluate(final Session session, final String formula) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			return Strings.safeEvaluate(session, formula, null);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
			//			Logger.slogException(Core.CLASSNAME, e, "formula:" + formula);
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	public static Vector safeEvaluate(final Session session, final String formula, final Document context) {
		String evalformula = "";
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			if (Strings.isBlankString(formula)) {
				throw new IllegalArgumentException("Parameter is blank or null");
			}
			final String unicodeReplacement = "�";
			evalformula = formula.replaceAll(unicodeReplacement, "\"").replaceAll(unicodeReplacement, "\"");
			if (Strings.checkFormulaSyntax(session, evalformula)) {
				return (null == context) ? session.evaluate(evalformula) : session.evaluate(evalformula, context);
			}
		} catch (final Exception e) {
			DominoUtils.handleException(e);
			//					Logger.slogException(Core.CLASSNAME, e, "formula:" + formula);
		}

		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean checkFormulaSyntax(final Session session, final String formula) {
		String syntaxformula = "";
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (Strings.isBlankString(formula)) {
				throw new IllegalArgumentException("Formula is blank or null");
			}

			syntaxformula = "@CheckFormulaSyntax({" + formula + "})";
			final Vector syntax = session.evaluate(syntaxformula);
			if (syntax.elementAt(0).toString().trim().equalsIgnoreCase("1")) {
				return true;
			}

			String msg = Strings.MESSAGE_FORMULA_INVALID;
			msg += "\n syntax.elementAt(0): \"" + syntax.elementAt(0).toString() + "\"";
			final String[] info = (String[]) syntax.toArray(new String[syntax.size()]);
			for (int i = 0; i < info.length; i++) {
				msg += "\n  syntax.elementAt(" + i + "):" + info[i];
			}

			throw new IllegalArgumentException(msg);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
			//					Logger.slogException(Core.CLASSNAME, e, "formula:" + formula, "syntaxformula:" + syntaxformula);
		}

		return false;
	}

}
