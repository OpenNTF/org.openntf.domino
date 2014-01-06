package org.openntf.domino.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * String Utilities
 * 
 * @author Devin S. Olsonm (dolson@czarnowski.com)
 * 
 */
public enum Strings {
	;

	public static final String MESSAGE_DIGEST_ALGORYTHM = "MD5";
	public static final String MESSAGE_FORMULA_INVALID = "The Formula syntax is invalid.  ";
	public static final String REGEX_NEWLINE = "\\r?\\n";

	public static final String CHARSET_NAME = "CP1252";

	// <ALT> + 0216 on numeric keypad
	public static final String DEFAULT_DELIMITER = "Ø";

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
		final List<String> al = CollectionUtils.getListStrings(object);
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

	public static String toProperCase(final String source) {
		return (Strings.isBlankString(source)) ? "" : source.substring(0, 1).toUpperCase() + source.substring(1);
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
		return ((null == string) || (string.trim().length() < 1));
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
				return Strings.join(CollectionUtils.getStringArray(source), delimiter);
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
