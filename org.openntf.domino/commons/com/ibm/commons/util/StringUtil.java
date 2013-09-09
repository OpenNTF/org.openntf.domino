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

package com.ibm.commons.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <P>
 * This class contains helpers for string manipulation.
 * </P>
 * 
 * @ibm-api
 */
public class StringUtil {
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	public static final String SPACE = " ";
	public static final String EMPTY_STRING = "";

	/**
	 * Unit test.
	 * 
	 * @param args
	 */
	//    public static void main(String args[]) {
	//    	testExpandProperties(args);
	//    	testMnemonics(args);
	//    }
	//    public static void testMnemonics(String args[]) {
	//        String a = "hi&"; // $NON-NLS-1$
	//        String b= "&&hello"; // $NON-NLS-1$
	//        String c = "&";
	//        String d = "whats up (&&):"; // $NON-NLS-1$
	//        String f = "(&a)"; // $NON-NLS-1$
	//        String e = "(&b"; // $NON-NLS-1$
	//        String g = "&d"; // $NON-NLS-1$
	//        String h = "hi &there"; // $NON-NLS-1$
	//        
	//        testMnemonics(a, "hi&"); // $NON-NLS-1$
	//        testMnemonics(b, "&hello"); // $NON-NLS-1$
	//        testMnemonics(c, "&");
	//        testMnemonics(d, "whats up :"); // $NON-NLS-1$
	//        testMnemonics(f, "");
	//        testMnemonics(e, "(b"); // $NON-NLS-1$
	//        testMnemonics(g, "d");
	//        testMnemonics(h, "hi there"); // $NON-NLS-1$
	//    }
	//    private static void testMnemonics(String test, String correct) {
	//        String removed = removeMnemonics(test);
	//        if (!StringUtil.equals(removed, correct)) {
	//            System.out.println("Failed:" + test + " removed was:" + removed + " instead of:" + correct); // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
	//        }
	//    }
	//    public static void testExpandProperties(String args[]) {
	//    	testExpandProperties(""); // $NON-NLS-1$
	//    	testExpandProperties("a"); // $NON-NLS-1$
	//    	testExpandProperties("aaa"); // $NON-NLS-1$
	//    	testExpandProperties("${java.home}"); // $NON-NLS-1$
	//    	testExpandProperties("a${java.home}"); // $NON-NLS-1$
	//    	testExpandProperties("${java.home}a"); // $NON-NLS-1$
	//    	testExpandProperties("aa${java.home}aa"); // $NON-NLS-1$
	//    	testExpandProperties("${java.home}${java.home}"); // $NON-NLS-1$
	//    	testExpandProperties("aa${java.home}${java.home}aa"); // $NON-NLS-1$
	//    	testExpandProperties("aa${java.home}aa${java.home}aa"); // $NON-NLS-1$
	//    	testExpandProperties("${java.home}aa${java.home}"); // $NON-NLS-1$
	//    }
	//    public static void testExpandProperties(String exp) {
	//    	String s = expandProperties(exp);
	//        System.out.println(exp+"="+s);
	//    }

	/**
	 * Test the string equality, ignoring the case. The 2 parameters can be null as far they are assimiled as empty.
	 * 
	 * @param s1
	 *            the first string to compare
	 * @param s2
	 *            the first string to compare
	 * @return true if the 2 strings are equals, false otherwise
	 * @ibm-api
	 */
	public static final boolean equalsIgnoreCase(final String s1, final String s2) {
		if (s1 == null || s2 == null) {
			return isEmpty(s1) == isEmpty(s2);
		}
		return s1.equalsIgnoreCase(s2);
	}

	/**
	 * Concat a list of strings into a single string.
	 * 
	 * @param strings
	 *            the array of strings used
	 * @param sep
	 *            the separator between strings
	 * @param trim
	 *            indicate if the strings must be trimmed
	 * @return the concatened string
	 * @ibm-api
	 */
	public static String concatStrings(final String[] strings, final char sep, final boolean trim) {
		int count = strings.length;
		if (count == 0) {
			return ""; //$NON-NLS-1$
		}
		if (count == 1) {
			return trim ? trim(strings[0]) : strings[0];
		}

		StringBuilder b = new StringBuilder();

		for (int i = 0; i < count; i++) {
			if (i > 0) {
				b.append(sep);
			}
			b.append(trim ? trim(strings[i]) : strings[i]);
		}
		return b.toString();
	}

	/**
	 * Trims the space characters from both ends of a string. All characters that have codes less than or equal to <code>'&#92;u0020'</code>
	 * (the space character) are considered to be white space.
	 * 
	 * @param s
	 *            the string to edit
	 * @return the trimmed string
	 * @ibm-api
	 */
	public static final String trim(final String s) {
		// Call the String method which more efficient
		if (s != null) {
			return s.trim();
		} else {
			return null;
		}
	}

	/**
	 * Trims the spaces characters from both ends of of the character array.
	 * 
	 * @param array
	 *            the char array to trim
	 * @return the trimmed string
	 * @ibm-api
	 */
	public static final String trim(final char[] array) {
		int count = array.length;
		int last = count;
		int st = 0;

		while ((st < last) && isSpace(array[st])) {
			st++;
		}
		while ((st < last) && isSpace(array[last - 1])) {
			last--;
		}
		if (last > st) {
			return new String(array, st, last - st);
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Compares two strings. The comparison is based on the Unicode value of each character in the strings.
	 * 
	 * @param s1
	 *            the first string
	 * @param s2
	 *            the second string
	 * @return the value <code>0</code> if s1 is equal to s2. A value less than <code>0</code> if s1<s2 and a value greater than
	 *         <code>0</code> if s1>s2.
	 * @ibm-api
	 */
	public static int compareTo(final String s1, final String s2) {
		boolean e1 = isEmpty(s1);
		boolean e2 = isEmpty(s2);
		if (e1 || e2) {
			if (e1 && !e2)
				return -1;
			if (!e1 && e2)
				return 1;
			return 0;
		}
		return s1.compareTo(s2);
	}

	/**
	 * Compares two strings ignoring the case. The comparison is based on the Unicode value of each character in the strings. When sun will
	 * provide such a native String function, it will be more optimized.
	 * 
	 * @param s1
	 *            the first string
	 * @param s2
	 *            the second string
	 * @return the value <code>0</code> if s1 is equal to s2. A value less than <code>0</code> if s1<s2 and a value greater than
	 *         <code>0</code> if s1>s2.
	 * @ibm-api
	 */
	public static int compareToIgnoreCase(final String s1, final String s2) {
		boolean e1 = isEmpty(s1);
		boolean e2 = isEmpty(s2);
		if (e1 || e2) {
			if (e1 && !e2)
				return -1;
			if (!e1 && e2)
				return 1;
			return 0;
		}
		return s1.compareToIgnoreCase(s2);
	}

	/**
	 * Test the string equality. The 2 parameters can be null as far they are assimiled as empty.
	 * 
	 * @param s1
	 *            the first string to compare
	 * @param s2
	 *            the first string to compare
	 * @return true if the 2 strings are equals, false otherwise
	 * @ibm-api
	 */
	public static final boolean equals(final String s1, final String s2) {
		if (s1 == null || s2 == null) {
			return isEmpty(s1) == isEmpty(s2);
		}
		return s1.equals(s2);
	}

	/**
	 * Test if the specified objects are equal. This method acts as Object.equals except that it checks for nullity.
	 * 
	 * @param str1
	 *            the first object to test
	 * @param str2
	 *            the second object to test
	 * @return true if the 2 objects are equals, false otherwise
	 * @ibm-api
	 */
	static public boolean equals(final Object str1, final Object str2) {
		if (str1 == null && str2 == null)
			return true;
		if (str1 != null)
			return str1.equals(str2);
		if (str2 != null)
			return str2.equals(str1);
		return false;
	}

	/**
	 * Test if a string is empty. A string is considered empty if it is either null or has a length of 0 characters.
	 * 
	 * @param s
	 *            the string to check
	 * @return true if the string is empty
	 * @ibm-api
	 */
	public static boolean isEmpty(final String s) {
		return s == null || s.length() == 0;
	}

	/**
	 * Test if a string is not empty. A string is considered empty if it is either null or has a length of 0 characters.
	 * 
	 * @param s
	 *            the string to check
	 * @return true if the string is not empty
	 * @ibm-api
	 */
	public static boolean isNotEmpty(final String s) {
		return s != null && s.length() > 0;
	}

	/**
	 * Return a non-null string if this string is null
	 * 
	 * @ibm-api
	 */
	public static String getNonNullString(final String string) {
		return string == null ? "" : string;//$NON-NLS-1$
	}

	/**
	 * Test if a string is only made of spaces. By spaces, we mean blank, tab, CR and LF.
	 * 
	 * @param s
	 *            the string to check
	 * @return true if the string is only made of spaces (or if it is empty)
	 * @ibm-api
	 */
	public static final boolean isSpace(final String s) {
		if (s != null) {
			int len = s.length();
			for (int i = 0; i < len; i++) {
				char c = s.charAt(i);
				if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Format a string. Format a string using a formt string and parameters. Each entry like {n} in the format string is replaced by the
	 * actual parameter value converted to a string.
	 * 
	 * @param fmt
	 *            the format string
	 * @param p1
	 *            the first message parameter
	 * @param p2
	 *            the second message parameter
	 * @param p3
	 *            the third message parameter
	 * @param p4
	 *            the fourth message parameter
	 * @param p5
	 *            the fifth message parameter
	 * @param p6
	 *            the sixth message parameter
	 * @return the formatted string
	 * @ibm-api
	 */
	public static final String format(final String fmt, final Object... parameters) {
		if (fmt != null) {
			FastStringBuffer buffer = new FastStringBuffer();
			return buffer.appendFormat(fmt, parameters).toString();
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Convert an object to a string. The resulting string is limited to 32 characters.
	 * 
	 * @param o
	 *            the object to convert
	 * @return the resulting string
	 * @ibm-api
	 */
	public static String toString(final Object o) {
		return toString(o, 32);
	}

	/**
	 * Convert a character to a string.
	 * 
	 * @param ch
	 *            the character to convert
	 * @return the resulting string
	 * @ibm-api
	 */
	public static String toString(final char ch) {
		// Optimize that for 1.4?
		char[] array = new char[] { ch };
		return new String(array);
	}

	/**
	 * Convert a byte to a string.
	 * 
	 * @param value
	 *            the value to convert
	 * @return the resulting string
	 * @ibm-api
	 */
	public static String toString(final byte value) {
		return Byte.toString(value);
	}

	/**
	 * Convert a short to a string.
	 * 
	 * @param value
	 *            the value to convert
	 * @return the resulting string
	 * @ibm-api
	 */
	public static String toString(final short value) {
		return Short.toString(value);
	}

	/**
	 * Convert an integer to a string.
	 * 
	 * @param value
	 *            the value to convert
	 * @return the resulting string
	 * @ibm-api
	 */
	public static String toString(final int value) {
		return Integer.toString(value);
	}

	/**
	 * Convert a long to a string.
	 * 
	 * @param value
	 *            the value to convert
	 * @return the resulting string
	 * @ibm-api
	 */
	public static String toString(final long value) {
		return Long.toString(value);
	}

	/**
	 * Convert a float to a string.
	 * 
	 * @param value
	 *            the value to convert
	 * @return the resulting string
	 * @ibm-api
	 */
	public static String toString(final float value) {
		return Float.toString(value);
	}

	/**
	 * Convert a double to a string.
	 * 
	 * @param value
	 *            the value to convert
	 * @return the resulting string
	 * @ibm-api
	 */
	public static String toString(final double value) {
		return Double.toString(value);
	}

	/**
	 * Convert a boolean to a string.
	 * 
	 * @param value
	 *            the value to convert
	 * @return the resulting string
	 * @ibm-api
	 */
	public static String toString(final boolean value) {
		return value ? "true" : "false"; // $NON-NLS-1$ $NON-NLS-2$
	}

	/**
	 * Convert a object to its string representation. This function takes care of null objects.
	 * 
	 * @param value
	 *            the value to be converted
	 * @return the string representing the object
	 * @ibm-api
	 */
	public static final String toString(final Object value, final int max) {
		if (value != null) {
			if (value.getClass().isArray()) {
				return toStringArray(value, -1, max);
			} else {
				// Simply convert the object
				return value.toString();
			}
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Convert a integer to its string representation and fill with characters on left to match the number of digits required. If the number
	 * cannot fit in the specified digits, then the return string is only composed of '*'.
	 * 
	 * @param value
	 *            the value to be converted
	 * @param digits
	 *            the number of desired digits
	 * @param fill
	 *            the character used to fill
	 * @return the string representing the integer
	 * @ibm-api
	 */
	public static final String toString(final int value, final int digits, final char fill) {
		FastStringBuffer b = new FastStringBuffer();
		String s = Integer.toString(value);
		if (s.length() <= digits) {
			b.repeat(fill, digits - s.length());
			b.append(s);
		} else {
			b.repeat('*', digits);
		}
		return b.toString();
	}

	/**
	 * Converts a collection full of Strings to a string array.
	 * 
	 * @ibm-api
	 */
	public static String[] toStringArray(final Collection strings) {
		if (null == strings || strings.size() == 0)
			return StringUtil.EMPTY_STRING_ARRAY;
		return (String[]) strings.toArray(new String[strings.size()]);
	}

	/**
	 * @ibm-not-published
	 */
	public static final String toStringArray(final Object value, final int used, final int max) {
		int nItem = -1;
		boolean[] za = null;
		char[] ca = null;
		byte[] ba = null;
		short[] sa = null;
		int[] ia = null;
		long[] la = null;
		float[] fa = null;
		double[] da = null;
		Object[] oa = null;

		// Find the good array type
		if (value instanceof boolean[]) {
			za = (boolean[]) value;
			nItem = za.length;
		} else if (value instanceof char[]) {
			ca = (char[]) value;
			nItem = ca.length;
		} else if (value instanceof byte[]) {
			ba = (byte[]) value;
			nItem = ba.length;
		} else if (value instanceof short[]) {
			sa = (short[]) value;
			nItem = sa.length;
		} else if (value instanceof int[]) {
			ia = (int[]) value;
			nItem = ia.length;
		} else if (value instanceof long[]) {
			la = (long[]) value;
			nItem = la.length;
		} else if (value instanceof float[]) {
			fa = (float[]) value;
			nItem = fa.length;
		} else if (value instanceof double[]) {
			da = (double[]) value;
			nItem = da.length;
		} else if (value instanceof Object[]) {
			oa = (Object[]) value;
			nItem = oa.length;
		}

		if (used >= 0) {
			nItem = used;
		}

		// And browse the array
		if (nItem >= 0) {
			StringBuilder buf = new StringBuilder(512);
			buf.append(Integer.toString(nItem));
			buf.append(" {"); //$NON-NLS-1$
			int count = max < nItem ? max : nItem;
			for (int i = 0; i < count; i++) {
				if (i != 0)
					buf.append(", "); //$NON-NLS-1$
				if (za != null) {
					buf.append(toString(za[i]));
				} else if (ca != null) {
					buf.append('\'');
					buf.append(ca[i]);
					buf.append('\'');
				} else if (ba != null) {
					buf.append(toString(ba[i]));
				} else if (sa != null) {
					buf.append(toString(sa[i]));
				} else if (ia != null) {
					buf.append(toString(ia[i]));
				} else if (la != null) {
					buf.append(toString(la[i]));
				} else if (fa != null) {
					buf.append(toString(fa[i]));
				} else if (da != null) {
					buf.append(toString(da[i]));
				} else if (oa != null) {
					buf.append(toString(oa[i]));
				}
			}
			if (max < nItem)
				buf.append(", ..."); //$NON-NLS-1$
			buf.append("}"); //$NON-NLS-1$
			return buf.toString();
		} else {
			return format("Unknown java array type {0}", value.getClass().toString()); // $NON-NLS-1$
		}
	}

	/**
	 * Create a new string by repeating a character.
	 * 
	 * @param toRepeat
	 *            the character to repeat
	 * @param count
	 *            the number of repetition
	 * @return the resulting string
	 * @ibm-api
	 */
	public static final String repeat(final char toRepeat, final int count) {
		StringBuilder b = new StringBuilder(count);
		for (int i = 0; i < count; i++) {
			b.append(toRepeat);
		}
		return b.toString();
	}

	/**
	 * Create a new string by repeating a string.
	 * 
	 * @param toRepeat
	 *            the string to repeat
	 * @param count
	 *            the number of repetition
	 * @return the resulting string
	 * @ibm-api
	 */
	public static final String repeat(final String toRepeat, final int count) {
		StringBuilder b = new StringBuilder(count * toRepeat.length());
		for (int i = 0; i < count; i++) {
			b.append(toRepeat);
		}
		return b.toString();
	}

	/**
	 * Replace the first occurrence of a string within a source string.
	 * 
	 * @param source
	 *            the source string
	 * @param value
	 *            the string to search for
	 * @param replace
	 *            the string replacement
	 * @return the resulting string
	 * @ibm-api
	 */
	public static final String replaceFirst(final String source, final String value, String replace) {
		if (StringUtil.isEmpty(source) || StringUtil.isEmpty(value)) {
			return ""; //$NON-NLS-1$
		}
		if (replace == null) {
			replace = ""; //$NON-NLS-1$
		}
		int idx = source.indexOf(value);
		if (idx >= 0) {
			// Initialize the buffer with the begining of the string
			FastStringBuffer buffer = new FastStringBuffer();
			buffer.append(source, 0, idx);
			// Append the change to the str
			buffer.append(replace);
			int next = idx + value.length();
			// Append the string up to the next occurence of the value
			buffer.append(source, next, source.length());

			return buffer.toString();
		}

		// Nothing changed in the string
		return source;
	}

	/**
	 * Replace all the occurrences of a string within a source string.
	 * 
	 * @param source
	 *            the source string
	 * @param value
	 *            the string to search for
	 * @param replace
	 *            the string replacement
	 * @return the resulting string
	 * @ibm-api
	 */
	public static final String replace(final String source, final String value, String replace) {
		if (isEmpty(source) || isEmpty(value)) {
			return ""; //$NON-NLS-1$
		}
		if (replace == null) {
			replace = ""; //$NON-NLS-1$
		}
		int idx = source.indexOf(value);
		if (idx >= 0) {
			// Initialize the buffer with the begining of the string
			FastStringBuffer buffer = new FastStringBuffer();
			buffer.append(source, 0, idx);
			int next;
			do {
				// Append the change to the str
				buffer.append(replace);
				next = idx + value.length();

				// And search for the next occurence
				idx = source.indexOf(value, next);

				// Append the string up to the next occurence of the value
				buffer.append(source, next, idx >= 0 ? idx : source.length());
			} while (idx >= 0);

			return buffer.toString();
		}

		// Nothing changed in the string
		return source;
	}

	/**
	 * Replace all the occurrences of a character within a source string.
	 * 
	 * @param source
	 *            the source string
	 * @param value
	 *            the character to search for
	 * @param replace
	 *            the character replacement
	 * @return the resulting string
	 * @ibm-api
	 */
	public static final String replace(final String string, final char value, final char replace) {
		int idx = string.indexOf(value);
		if (idx >= 0) {
			// Initialize the buffer with the begining of the string
			FastStringBuffer buffer = new FastStringBuffer();
			buffer.append(string, 0, idx);

			int next;
			do {
				// Append the change to the str
				if (replace != 0) {
					buffer.append(replace);
				}
				next = idx + 1;

				// And search for the next occurence
				idx = string.indexOf(value, next);

				// Append the string up to the next occurence of the value
				buffer.append(string, next, idx >= 0 ? idx : string.length());
			} while (idx >= 0);

			return buffer.toString();
		}

		// Nothing change in the string
		return string;
	}

	/**
	 * Convert an integer to an Hexa string and pad the result with '0'
	 * 
	 * @param value
	 *            the int to convert
	 * @param nChars
	 *            the number of characters of the result
	 * @return the resulting string
	 * @ibm-api
	 * @throws NumberFormatException
	 *             if nChars is less that the actual number of characters needed
	 */
	public static final String toUnsignedHex(final int value, final int nChars) {
		FastStringBuffer b = new FastStringBuffer();
		//String s = Integer.toHexString(value);
		for (int i = 7; i >= 0; i--) {
			int v = (value >>> (i * 4)) & 0x0F;
			if (b.length() > 0 || v != 0 || i < nChars || i == 0) {
				b.append(hexChar(v));
			}
		}
		if (nChars > 0 && b.length() > nChars) {
			throw new NumberFormatException(StringUtil.format("Hexadecimal number {0} too big to fit in '{1}' characters", //$NLS-StringUtil.StringUtil.HexNumTooBig.Exception-1$
					StringUtil.toString(value), StringUtil.toString(nChars)));
		}
		return b.toString();
	}

	public static final char hexChar(final int v) {
		return (char) ((v >= 10) ? (v - 10 + 'A') : (v + '0'));
	}

	public static final int hexValue(final char c) {
		if (c >= '0' && c <= '9') {
			return c - '0';
		}
		if (c >= 'A' && c <= 'F') {
			return c - 'A' + 10;
		}
		if (c >= 'a' && c <= 'f') {
			return c - 'a' + 10;
		}
		// Do not throw an exception...
		return -1;
	}

	/**
	 * Breaks a string into an array of substrings, with the delimeter removed. The delimeter character passed as aparanmeter. Note that
	 * <CODE>java.util.StringTokenizer</CODE> offers similar functionality. This method differs by the fact that it returns all of the
	 * tokens at once, using a String array, and does not require the explicit creation of a tokenizer object. This function also returns
	 * empty strings when delimiters are concatened, which is not the case of <CODE>java.util.StringTokenizer</CODE>. Finally, the delimiter
	 * is unique and passed as a char, which makes the method much faster.<BR>
	 * 
	 * @param s
	 *            the string to split
	 * @param c
	 *            the delimeter
	 * @param trim
	 *            indicates if the strings returned should be trimmed
	 * @return the substrings of s
	 * @ibm-api
	 */
	public static String[] splitString(final String s, final char sep, final boolean trim) {
		if (s == null) {
			return EMPTY_STRING_ARRAY;
		}
		return splitString(null, 0, s, 0, sep, trim);
	}

	private static String[] splitString(String[] result, final int count, final String s, final int pos, final char sep, final boolean trim) {
		int newPos = s.indexOf(sep, pos);
		if (newPos >= 0) {
			result = splitString(null, count + 1, s, newPos + 1, sep, trim);
			result[count] = s.substring(pos, newPos);
		} else {
			result = new String[count + 1];
			result[count] = s.substring(pos);
		}
		if (trim) {
			result[count] = result[count].trim();
		}
		return result;
	}

	public static String[] splitString(final String s, final String sep, final boolean trim) {
		if (s == null) {
			return EMPTY_STRING_ARRAY;
		}
		if (isEmpty(sep)) {
			// Handling this like the JavaScript split method, 
			// if the separator is omitted, the entire string is returned.
			// Different to the Java String split method, which returns 
			// an array full with entries for each character.
			return new String[] { s };
		}
		return splitString(null, 0, s, 0, sep, trim);
	}

	private static String[] splitString(String[] result, final int count, final String s, final int pos, final String sep,
			final boolean trim) {
		int newPos = s.indexOf(sep, pos);
		if (newPos >= 0) {
			result = splitString(null, count + 1, s, newPos + sep.length(), sep, trim);
			result[count] = s.substring(pos, newPos);
		} else {
			result = new String[count + 1];
			result[count] = s.substring(pos);
		}
		if (trim) {
			result[count] = result[count].trim();
		}
		return result;
	}

	/**
	 * Breaks a string into an array of substrings, with the delimeter removed. The delimeter character passed as aparanmeter. Note that
	 * <CODE>java.util.StringTokenizer</CODE> offers similar functionality. This method differs by the fact that it returns all of the
	 * tokens at once, using a String array, and does not require the explicit creation of a tokenizer object. This function also returns
	 * empty strings when delimiters are concatened, which is not the case of <CODE>java.util.StringTokenizer</CODE>. Finally, the delimiter
	 * is unique and passed as a char, which makes the method much faster.<BR>
	 * The returned strings are *not* trimmed.
	 * 
	 * @param s
	 *            the string to split
	 * @param c
	 *            the delimeter
	 * @return the substrings of s
	 * @ibm-api
	 */
	public static final String[] splitString(final String s, final char sep) {
		return splitString(s, sep, false);
	}

	/**
	 * Trims the space characters from the beginning of a string. For example, the call <CODE>ltrim ("  Tennessee")</CODE> returns the
	 * string "Tennessee".<BR>
	 * 
	 * @param s
	 *            the string to edit
	 * @return the trimmed string
	 * @ibm-api
	 */
	public static final String ltrim(final String s) {
		int count = s.length();
		int st = 0;
		while ((st < count) && isSpace(s.charAt(st))) {
			st++;
		}
		return st > 0 ? s.substring(st, count) : s;
	}

	private static boolean isSpace(final char c) {
		return c <= ' ';
	}

	/**
	 * Trims the space characters from the end of a string. For example, the call <CODE>rtrim ("Tennessee  ")</CODE> returns the string
	 * "Tenness".<BR>
	 * All characters that have codes less than or equal to <code>'&#92;u0020'</code> (the space character) are considered to be white
	 * space.
	 * 
	 * @param s
	 *            the string to edit
	 * @return the trimmed string
	 * @ibm-api
	 */
	public static final String rtrim(final String s) {
		int count = s.length();
		int len = count;
		while ((0 < len) && isSpace(s.charAt(len - 1))) {
			len--;
		}
		return (len < count) ? s.substring(0, len) : s;
	}

	/**
	 * Returns the index within this string of the first occurrence of the specified string. The search is done ignore the character case.
	 * 
	 * @param source
	 *            the string to search
	 * @param str
	 *            the string to search for
	 * @param fromIndex
	 *            the index to start from
	 * @return the index of the first occurrence of the string in the character sequence represented by this object, or <code>-1</code> if
	 *         the string does not occur.
	 * @ibm-api
	 */
	public static int indexOfIgnoreCase(final String source, final String str, int fromIndex) {
		int count = source.length();
		int strCount = str.length();
		int max = count - strCount;
		if (fromIndex >= count) {
			if (count == 0 && fromIndex == 0 && strCount == 0) {
				/* There is an empty string at index 0 in an empty string. */
				return 0;
			}
			/* Note: fromIndex might be near -1>>>1 */
			return -1;
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (str.length() == 0) {
			return fromIndex;
		}

		char first = Character.toLowerCase(str.charAt(0));
		int i = fromIndex;

		startSearchForFirstChar: while (true) {
			/* Look for first character. */
			while (i <= max && Character.toLowerCase(source.charAt(i)) != first) {
				i++;
			}
			if (i > max) {
				return -1;
			}

			/* Found first character, now look at the rest of v2 */
			int j = i + 1;
			int end = j + strCount - 1;
			int k = 1;
			while (j < end) {
				if (Character.toLowerCase(source.charAt(j++)) != Character.toLowerCase(str.charAt(k++))) {
					i++;
					/* Look for str's first char again. */
					continue startSearchForFirstChar;
				}
			}
			return i; /* Found whole string. */
		}
	}

	/**
	 * Returns the index within this string of the first occurrence of the specified string. The search is done ignore the character case.
	 * 
	 * @param source
	 *            the string to search
	 * @param str
	 *            the string to search for
	 * @return the index of the first occurrence of the string in the character sequence represented by this object, or <code>-1</code> if
	 *         the string does not occur.
	 * @ibm-api
	 */
	public static int indexOfIgnoreCase(final String source, final String str) {
		return indexOfIgnoreCase(source, str, 0);
	}

	/**
	 * Returns the index within this string of the last occurrence of the specified string. The search is done ignore the character case.
	 * 
	 * @param source
	 *            the string to search
	 * @param str
	 *            the string to search for
	 * @param fromIndex
	 *            the index to start from
	 * @return the index of the first occurrence of the string in the character sequence represented by this object, or <code>-1</code> if
	 *         the string does not occur.
	 * @ibm-api
	 */
	public static int lastIndexOfIgnoreCase(final String source, final String str, int fromIndex) {
		int count = source.length();
		int strCount = str.length();

		/*
		 * Check arguments; return immediately where possible. For
		 * consistency, don't check for null str.
		 */
		int rightIndex = count - strCount;
		if (fromIndex < 0) {
			return -1;
		}
		if (fromIndex > rightIndex) {
			fromIndex = rightIndex;
		}
		/* Empty string always matches. */
		if (strCount == 0) {
			return fromIndex;
		}

		int strLastIndex = strCount - 1;
		char strLastChar = Character.toLowerCase(str.charAt(strLastIndex));
		int min = strCount - 1;
		int i = min + fromIndex;

		startSearchForLastChar: while (true) {
			/* Look for the last character */
			while (i >= min && Character.toLowerCase(source.charAt(i)) != strLastChar) {
				i--;
			}
			if (i < min) {
				return -1;
			}

			/* Found last character, now look at the rest of v2. */
			int j = i - 1;
			int start = j - (strCount - 1);
			int k = strLastIndex - 1;

			while (j > start) {
				if (Character.toLowerCase(source.charAt(j--)) != Character.toLowerCase(str.charAt(k--))) {
					i--;
					/* Look for str's last char again. */
					continue startSearchForLastChar;
				}
			}

			return start + 1; /* Found whole string. */
		}
	}

	/**
	 * Returns the index within this string of the last occurrence of the specified string. The search is done ignore the character case.
	 * 
	 * @param source
	 *            the string to search
	 * @param str
	 *            the string to search for
	 * @return the index of the first occurrence of the string in the character sequence represented by this object, or <code>-1</code> if
	 *         the string does not occur.
	 * @ibm-api
	 */
	public static int lastIndexOfIgnoreCase(final String source, final String str) {
		return lastIndexOfIgnoreCase(source, str, source.length());
	}

	/**
	 * Identical to String.startsWith but case insensitive.
	 * 
	 * @ibm-api
	 */
	public static boolean startsWithIgnoreCase(final String s, final String prefix) {
		return startsWithIgnoreCase(s, prefix, 0);
	}

	/**
	 * Identical to String.startsWith but case insensitive.
	 * 
	 * @ibm-api
	 */
	public static boolean startsWithIgnoreCase(final String s, final String prefix, final int start) {
		return s.regionMatches(true, start, prefix, 0, prefix.length());
	}

	/**
	 * Identical to String.endsWith but case insensitive.
	 * 
	 * @ibm-api
	 */
	public static boolean endsWithIgnoreCase(final String s, final String suffix) {
		return s.regionMatches(true, s.length() - suffix.length(), suffix, 0, suffix.length());
	}

	//========================================================================================
	// Basic password encryption
	//========================================================================================

	private static int MINPWDLENGTH = 16;
	private static int[] ENCRYPTION = new int[] { 187, 89, 189, 17, 45, 3, 0, 98, 79, 232, 65, 98, 75, 3, 224, 177 };

	/**
	 * @ibm-not-published
	 * @deprecated
	 */
	@Deprecated
	public static boolean isEncrypted(final String pwd) {
		if (pwd == null) {
			return false;
		}
		return pwd.startsWith("[") && pwd.endsWith("]"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @ibm-not-published
	 * @deprecated
	 */
	@Deprecated
	public static String encrypt(final String pwd) {
		return encrypt(pwd, null);
	}

	/**
	 * @ibm-not-published
	 * @deprecated
	 */
	@Deprecated
	public static String encrypt(final String pwd, final String ctx) {
		if (pwd == null) {
			return null;
		}
		int offset;
		if (StringUtil.isNotEmpty(ctx)) {
			int acc = Math.abs((int) (ctx.hashCode() % ENCRYPTION.length));
			offset = Math.min(ENCRYPTION.length, acc);
		} else {
			offset = Math.min(ENCRYPTION.length, (int) (Math.random() * ENCRYPTION.length));
		}

		FastStringBuffer b = new FastStringBuffer();
		b.append('[');
		// Write the offset/count
		encryptChar(b, 0, (char) offset);
		encryptChar(b, offset++, (char) (pwd.length()));
		for (int i = 0; i < Math.max(MINPWDLENGTH, pwd.length()); i++) {
			char c;
			if (i < pwd.length()) {
				c = pwd.charAt(i);
			} else {
				if (StringUtil.isNotEmpty(ctx)) {
					int pwdlen = pwd.length();
					c = ctx.charAt((i - pwdlen) % ctx.length());
				} else {
					c = (char) (Math.random() * 64 + 32);
				}
			}
			encryptChar(b, offset++, c);
		}
		b.append(']');
		return b.toString();
	}

	/**
	 * @ibm-not-published
	 * @deprecated
	 */
	@Deprecated
	public static String decrypt(final String pwd) {
		if (isEncrypted(pwd)) {
			FastStringBuffer b = new FastStringBuffer();
			// Read the offset/count;
			int offset = decryptChar(pwd, 0, 1);
			int pwdlength = decryptChar(pwd, offset++, 3);
			for (int i = 0; i < pwdlength; i++) {
				b.append(decryptChar(pwd, offset++, 5 + i * 2));
			}
			return b.toString();
		}
		return pwd;
	}

	private static void encryptChar(final FastStringBuffer b, final int offset, final char c) {
		int xor = ENCRYPTION[offset % ENCRYPTION.length];
		int val = ((int) c) ^ xor;
		char c1 = (char) ((val & 0x00F0) / 16 + (int) 'A');
		char c2 = (char) ((val & 0x000F) + (int) 'A');
		b.append(c1);
		b.append(c2);
	}

	private static char decryptChar(final String pwd, final int offset, final int pos) {
		int xor = ENCRYPTION[offset % ENCRYPTION.length];
		char c1 = pwd.charAt(pos);
		char c2 = pwd.charAt(pos + 1);
		char c = (char) (((c1 - (int) 'A') * 16 + (c2 - (int) 'A')) ^ xor);
		return c;
	}

	/**
	 * Converts an HTML string to a Java one, converting the numeric entities (&#1234;) sent by the browser. this does not handle HTML
	 * strings, just XML strings. i.e. &#160; is handled but &nbsp; is not.
	 * 
	 * @param s
	 *            the HTML string
	 * @return the converted string
	 * @ibm-api
	 */
	public static String fromXmlInputString(final String s) {
		if (isEmpty(s)) {
			return s;
		}
		int l = s.length();
		FastStringBuffer b = null;
		try {
			int start = 0;
			int firstChar;
			while ((firstChar = s.indexOf("&#", start)) != -1) { //$NON-NLS-1$
				boolean ok = false;
				int lastChar = firstChar + 2;
				for (;;) {
					if (lastChar >= l) {
						break;
					}
					char c = s.charAt(lastChar);
					if (c == ';') {
						ok = true;
						break;
					}
					if (c < '0' || c > '9') {
						break;
					}
					lastChar++;
				}
				if (ok) {
					int n = Integer.parseInt(s.substring(firstChar + 2, lastChar));
					if (b == null) {
						b = new FastStringBuffer();
						b.append(s, 0, firstChar);
					} else {
						b.append(s, start, firstChar);
					}
					b.append((char) n);
					start = lastChar + 1;
				} else {
					start = firstChar + 2;
				}
			}
			if (b != null && start < l) {
				b.append(s, start, l);
			}
			return b != null ? b.toString() : s;
		} finally {
			b = null;
		}
	}

	private static final String TRUE_STRING = "true"; //$NON-NLS-1$
	private static final String FALSE_STRING = "false"; //$NON-NLS-1$

	public static boolean isTrueValue(final String stringToTest) {
		return TRUE_STRING.equalsIgnoreCase(stringToTest);
	}

	public static boolean isFalseValue(final String stringToTest) {
		return FALSE_STRING.equalsIgnoreCase(stringToTest);
	}

	/**
	 * Returns a string whose first letter is a Capital letter, and a space is inserted anywhere an upper case letter exists in the existing
	 * string. For instance, if the user passes in thisIsATest - the following will be returned: This Is A Test
	 * 
	 * @param sample
	 *            : String - The string that you want to convert to proper case.
	 * @return String
	 * @ibm-api
	 */
	public static String getProperCaseString(final String sample) {
		if (sample == null) {
			return null;
		}
		char[] chars = sample.toCharArray();
		StringBuffer buffer = new StringBuffer(sample.length());

		if (chars != null && chars.length > 0) {
			buffer.append(Character.toUpperCase(chars[0]));
			for (int i = 1; i < chars.length; i++) {
				if (Character.isUpperCase(chars[i])) {
					buffer.append(SPACE);
					chars[i] = Character.toLowerCase(chars[i]);
				}
				buffer.append(chars[i]);
			}
		}
		return buffer.toString();
	}

	private static String[] lineSeparators;

	@SuppressWarnings("unchecked")
	// $NON-NLS-1$
	public static String[] getLineSeparators() {
		//TODO come up with alternative to Eclipse runtime dependency
		//        Map separators = Platform.knownPlatformLineSeparators();
		//        List sepList = new ArrayList(separators.values());

		List sepList = new ArrayList();

		sepList.add(System.getProperty("line.separator"));

		// make sure the biggest separators are first, this means
		// we wont end up with too many spaces.
		Collections.sort(sepList, new Comparator() {
			public int compare(final Object o1, final Object o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				int s1L = s1.length();
				int s2L = s2.length();

				if (s1L > s2L) {
					return -1;
				} else if (s1L < s2L) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		return (String[]) sepList.toArray(new String[sepList.size()]);
	}

	/**
	 * Remove line breaks.
	 */
	public static String removeLineBreaks(String s) {
		if (lineSeparators == null) {
			lineSeparators = getLineSeparators();
		}
		for (int i = 0; i < lineSeparators.length; i++) {
			s = replace(s, lineSeparators[i], SPACE);
		}
		return s;
	}

	/**
	 * Strip extra spaces and html tags.
	 */
	public static String parseHtml(final String s) {
		// Quickly strip the extra spaces
		if (StringUtil.isNotEmpty(s)) {
			StringBuilder b = new StringBuilder();
			boolean space = true;
			int len = s.length();
			for (int i = 0; i < len; i++) {
				char c = s.charAt(i);
				if (c == ' ' || c == '\t') {
					if (!space) {
						b.append(' ');
						space = true;
					}
				} else if (c == '<') {
					// Skip the HTML tags for now
					while (i < len && c != '>') {
						i++;
					}
				} else {
					b.append(c);
					if (c == '\n' || c == '\r') {
						space = true;
					} else {
						space = false;
					}
				}
			}
			return b.toString();
		}
		return s;
	}

	public static String getAutoGenNameFromJavaName(final String javaName) {
		if (javaName == null) {
			return null;
		}
		int len = javaName.length();
		StringBuffer returnString = new StringBuffer(len);

		char[] chars = javaName.toCharArray();
		char c;
		//We do not want the name to start with a digit or contain characters outside the range
		//a-z A-Z 0-9 and '_'
		boolean leadingDigit = false;
		for (int i = 0; i < len; i++) {
			c = chars[i];
			if ((c >= 'A' && c <= 'z') || (c >= '0' && c <= '9') || c == '_') {
				if (i == 0) {
					if (Character.isLetter(c)) {
						returnString.append(Character.toUpperCase(c));
					} else {
						leadingDigit = true;
					}
				} else {
					if (leadingDigit) {
						if (Character.isLetter(c)) {
							leadingDigit = false;
							returnString.append(Character.toUpperCase(c));
						}
					} else {
						returnString.append(c);
					}
				}
			} else {
				leadingDigit = i == 0;
			}

		}

		return returnString.toString();
	}

	public static char getMnemonicCharacter(final String text) {
		char ret = (char) -1;

		if (text != null) {
			int index = text.indexOf("&");

			if (index != -1 && (index < (text.length() - 1))) {
				ret = text.charAt(index + 1);
			}
		}

		return ret;
	}

	/**
	 * Create a name with the _copy_x appended, where X is the next available number.
	 */
	public static String generateCopyName(final String sourceName, final String[] listOfNames) {
		// create a new name with same name + copy_n
		StringBuffer buffer = new StringBuffer(sourceName);
		String copyString = "copy"; // $NLS-StringUtil.copy-1$
		String separator = "_";

		StringBuffer copySeparator = new StringBuffer(separator);
		copySeparator.append(copyString);
		copySeparator.append(separator);

		String copySep = copySeparator.toString();

		int index = sourceName.indexOf(copySep);

		long num = 1;

		if (index != -1) {
			String substring = sourceName.substring(index + copySep.length());
			if (StringUtil.isNotEmpty(substring)) {
				try {
					num = Long.parseLong(substring);
					buffer.delete(index + copySep.length(), buffer.length());
				} catch (NumberFormatException nfe) {
					buffer.append(copySep);
				}
			}
		} else {
			buffer.append(copySep);
		}

		return getNextUniqueValue(buffer.toString(), num, listOfNames);
	}

	/**
	 * Given the current name, append a long until a unique is found, starting at the given starting num.
	 */
	public static String getNextUniqueValue(final String name, long startingNum, final String[] listOfNames) {
		boolean valid = true;

		for (int i = 0; i < Long.MAX_VALUE; i++) {
			StringBuffer newBuffer = new StringBuffer(name);
			newBuffer.append(startingNum);

			String test = newBuffer.toString();

			if (listOfNames != null) {
				for (int x = 0; x < listOfNames.length; x++) {
					String currName = listOfNames[x];
					if (StringUtil.equals(currName, test)) {
						valid = false;
					}
				}
			}

			if (!valid) {
				valid = true;
				startingNum++;
			} else {
				return test;
			}
		}

		return name;
	}

	/**
	 * Remove the mnemonics from a string.
	 * 
	 * @param string
	 * @return If no mnemonics are found, the string is returned as is. If mnemonics are found, a new string without the mnemonics is
	 *         returned.
	 */
	public static String removeMnemonics(final String string) {
		if (string == null || string.indexOf('&') == -1) {
			return string;
		}
		int strLen = string.length();
		StringBuffer sb = new StringBuffer(strLen);
		int lastIndex = 0;
		int iIndex = string.indexOf('&');
		while (iIndex != -1) {
			if (iIndex == strLen - 1) {
				break;
			}

			int modifier = 1;

			// whitney - fix korean, japanese, etc. when you have some characters and (&C):
			if ((iIndex - 1) >= 0 && (iIndex + 2) < string.length() && string.charAt(iIndex - 1) == '(' && string.charAt(iIndex + 2) == ')') {
				--iIndex;
				modifier = 4;
			} else if (string.charAt(iIndex + 1) == '&') {
				++iIndex;
			}

			sb.append(string.substring(lastIndex, iIndex));
			iIndex += modifier;
			lastIndex = iIndex;
			iIndex = string.indexOf('&', iIndex);
		}
		if (lastIndex < strLen) {
			sb.append(string.substring(lastIndex, strLen));
		}
		return sb.toString();
	}

	/**
	 * A Helper function for conditionally calling removeMnemonics. Used in the case where the mnemonic is conditionally removed based on if
	 * the control is in a dialog or not.
	 * 
	 * @param string
	 * @param remove
	 *            - if true remove any mnemonics in the string, if false just return the string.
	 * @return
	 */
	public static String removeMnemonics(final String string, final boolean remove) {
		return remove ? removeMnemonics(string) : string;
	}

	/**
	 * Transforms a String into a fixed-length digest. This is a one-way transformation, but with consistent return value.
	 * 
	 * 
	 * @param str
	 *            the String to transform
	 * @return the digest in hexadecimal form
	 */
	public static String getDigest(final String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
			md.update(str.getBytes("UTF-8")); //$NON-NLS-1$
			byte[] digest = md.digest();
			return toHexValue(digest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts a byte array to a hexadecimal String equivalent.
	 */
	public static String toHexValue(final byte[] barr) {
		FastStringBuffer sb = new FastStringBuffer(barr.length * 2);
		for (int i = 0; i < barr.length; i++) {
			int unsigned = barr[i] & 0xFF;
			sb.append(getUpperDigitAsHex(unsigned));
			sb.append(getLowerDigitAsHex(unsigned));
		}
		return sb.toString();
	}

	private static char getUpperDigitAsHex(final int b) {
		int val = b / 16;
		return toHex(val);
	}

	private static char getLowerDigitAsHex(final int b) {
		int val = b % 16;
		return toHex(val);
	}

	private static final char A = 'a' - 10;

	private static char toHex(final int n) {
		if ((0 <= n) && (n <= 9)) {
			return (char) ('0' + n);
		} else if ((10 <= n) && (n <= 15)) {
			return (char) (A + n);
		} else {
			throw new IllegalArgumentException("" + n);
		}
	}

	public static boolean contains(final Object[] objects, final Object toTest) {
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				if (equals(objects[i], toTest)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isLineSeparator(final String s) {
		if (isNotEmpty(s)) {
			String[] separators = getLineSeparators();
			return isContainedWithin(s, separators);
		}
		return false;
	}

	/**
	 * Checks if an array of string is empty.
	 * <p>
	 * An array is empty if it is nul, or if it contains no elements.
	 * </p>
	 * 
	 * @param arr
	 * @return
	 */
	public static boolean isEmpty(final String[] arr) {
		return arr == null || arr.length < 1;
	}

	/**
	 * Checks if an array of string is not empty.
	 * <p>
	 * An array is empty if it is nul, or if it contains no elements.
	 * </p>
	 * 
	 * @param arr
	 * @return
	 */
	public static boolean isNotEmpty(final String[] arr) {
		return arr != null && arr.length > 0;
	}

	public static boolean isContainedWithin(final String s, final String[] container) {
		if (isNotEmpty(s) && isNotEmpty(container)) {
			for (int i = 0; i < container.length; i++) {
				if (equals(s, container[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Property expander This function replaces all the occurences of ${propname} by the actual property value
	 * 
	 * @param s
	 * @param flag
	 * @return
	 */
	public static String expandProperties(final String s) {
		if (isEmpty(s)) {
			return s;
		}

		int index = s.indexOf("${", 0);
		if (index < 0) {
			return s;
		}

		StringBuilder b = new StringBuilder();
		if (index > 0) {
			b.append(s, 0, index);
		}
		do {
			int end = s.indexOf('}', index);
			if (end < 0) {
				// Error: just ignore the macro...
				return b.toString();
			}

			// replace the value
			String propName = s.substring(index + 2, end);
			String propValue = System.getProperty(propName);
			if (isNotEmpty(propValue)) {
				b.append(propValue);
			}
			index = end + 1;

			// And find the next occurence
			int pos = s.indexOf("${", index);
			if (pos < 0) {
				b.append(s, index, s.length());
				return b.toString();
			}
			if (pos > index) {
				b.append(s, index, pos);
			}
			index = pos;
		} while (true);
	}
}