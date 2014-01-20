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
import java.util.TreeSet;
import java.util.Vector;

import org.openntf.arpa.ISO;
import org.openntf.domino.Session;
import org.openntf.domino.impl.Name;

/**
 * String Utilities
 * 
 * @author Devin S. Olsonm (dolson@czarnowski.com)
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
	public static final String CHARSET_NAME = "CP1252";

	// <ALT> + 0216 on numeric keypad
	public static final String DEFAULT_DELIMITER = "Ø";

	public static final String MESSAGE_DIGEST_ALGORYTHM = "MD5";
	public static final String MESSAGE_FORMULA_INVALID = "The Formula syntax is invalid.  ";

	public static final String REGEX_NEWLINE = "\\r?\\n";
	public static final String REGEX_BEGIN_NOCASE = "(?i)^";
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

	public static final String REGEX_SIMPLETIME = Strings.REGEX_9_4 + Strings.REGEX_ampm;

	public static final String TIMESTAMP_DATEONLY = "dd MMM yyyy";
	public static final String TIMESTAMP_TIMEONLY = "HH:mm aa";
	public static final String TIMESTAMP_DEFAULT = "dd MMM yyyy hh:mm aa zzz";
	public static final String TIMESTAMP_DAYMONTH_NAMES = "EEE, dd MMM yyyy HH:mm:ss aa zzz";
	public static final String TIMESTAMP_MILITARY = "yyyyMMdd HHmm:ss, zzz";
	public static final String TIMESTAMP_SIMPLETIME = "HHmmaa";

	/*
	 * ************************************************************************
	 * ************************************************************************
	 * 
	 * PUBLIC Methods
	 * 
	 * ************************************************************************
	 * ************************************************************************
	 */
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
		final List<String> al = Congeries.getListStrings(object);
		return ((null == al) || (al.size() < 1)) ? null : new Vector<String>(al);
	}

	public static String getEnvarName(final String identifier) {
		return (null == identifier) ? "" : identifier.trim().toUpperCase().replaceAll(" ", "_");
	}

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
	 * @see # #getSpawnedRecordID(Name)
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
	 * @see # #getSpawnedRecordID(Session)
	 */
	public static String generateRecordID() {
		return Strings.getSpawnedRecordID(Factory.getSession());
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

	public static String toProperCase(final String string) {
		return ISO.toProperCase(string);
	}

	/**
	 * Determines if a string is null or blank
	 * 
	 * @param string
	 *            Source string to check for null or blank value.
	 * 
	 * @return Flag indicating if the source string is null or blank.
	 */
	public static boolean isBlankString(final String string) {
		return ISO.isBlankString(string);
	}

	public static boolean isHexadecimalString(final String string) {
		return (null != string) && (string.matches("^[A-Fa-f0-9]+$"));
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
	public static String join(final Object source, final String delimiter) {
		if (null != source) {
			if (source instanceof Collection) {
				return Strings.join(source, delimiter);
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
				return Strings.join(Congeries.getStringArray(source), delimiter);
			}
		}

		return "";
	}

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
	 *            String to check if begins with prefix.
	 * 
	 * @param prefix
	 *            String to match agains the beginning of source.
	 * 
	 * @return Flag indicating if source begins with prefix.
	 */
	public static boolean startsWithIgnoreCase(final String source, final String prefix) {
		return ((null == source) || (null == prefix)) ? false : source.toLowerCase().startsWith(prefix.toLowerCase());
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

			if (name instanceof org.openntf.domino.impl.Name) {
				String result = name.getIDprefix() + "-" + Dates.getTimeCode();
				try {
					// avoid potential duplicate consecutive results by sleeping for 1/4 second 
					Thread.sleep(250); // 250 milliseconds = 1/4 second
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
					DominoUtils.handleException(ex);
				}

				return result;
			}

			return Strings.getSpawnedRecordID(Names.createName(name));

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
		return Strings.getSpawnedRecordID(Names.createName(name));
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

			return Strings.getSpawnedRecordID(new org.openntf.domino.impl.Name(session));

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
				final String regex = "</div><div>|<br>|</p>";
				final String[] chunks = stripped.split(regex);
				if ((null == chunks) || (chunks.length < 1)) {
					return Congeries.getListStrings(Strings.stripHTMLtags(source));
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

}
