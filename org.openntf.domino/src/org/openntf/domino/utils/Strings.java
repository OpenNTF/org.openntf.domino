/**
 * 
 */
package org.openntf.domino.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * String Utilities
 * 
 * @author Devin S. Olsonm (dolson@czarnowski.com)
 * 
 */
public class Strings {
	public static final String CLASSNAME = "org.openntf.domino.utils.Strings";
	private static final Logger log_ = Logger.getLogger(Strings.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * Zero-Argument Constructor
	 */
	public Strings() {
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
		try {
			if (0 == length) {
				return "";
			}

			final int abslen = Math.abs(length);
			final StringBuilder sb = new StringBuilder(abslen);
			for (int i = 0; i < abslen; i++) {
				sb.append(c);
			}

			return sb.toString();

		} catch (final Exception e) {
			CzarCore.logException(Strings.CLASSNAME, e);
		}

		return "";
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
		try {
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

		} catch (final Exception e) {
			CzarCore.logException(Strings.CLASSNAME, e, "source:" + source.getClass().toString(), "delimiter:" + delimiter);
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
		try {
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
		} catch (final Exception e) {
			CzarCore.logException(Strings.CLASSNAME, e, "delimiter:" + delimiter);
		}

		return "";
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
		try {
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

		} catch (final Exception e) {
			CzarCore.logException(Strings.CLASSNAME, e, "source:" + source.getClass().toString(), "delimiter:" + delimiter);
		}

		return "";
	}

	public static StringBuilder removeBlankSpace(final StringBuilder sb) {
		try {
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

		} catch (final Exception e) {
			CzarCore.logException(Strings.CLASSNAME, e);
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
		try {
			return (null == source) ? "" : source.trim().replaceAll("[^A-Za-z0-9]", "");

		} catch (final Exception e) {
			CzarCore.logException(Strings.CLASSNAME, e, "Source: " + source);
		}

		return "";
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

}
