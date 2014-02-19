package org.openntf.domino.utils;

import java.util.List;
import java.util.Vector;

import org.openntf.domino.exceptions.UnimplementedException;

public enum Formulas {
	;

	/*
	 * ******BEGIN STRING METHODS******
	 */
	public static List<String> atLeft(final List<Object> strings, final int length) {
		List<String> result = new Vector<String>(strings.size());	//TODO change to ArrayList?
		for (Object o : strings) {
			if (o instanceof String) {
				result.add(atLeft((String) o, length));
			}
		}
		return result;
	}

	public static String atLeft(final String string, final int length) {
		throw new UnimplementedException("Not yet implemented");
	}

	public static List<String> atLeftBack(final List<Object> strings, final int length) {
		List<String> result = new Vector<String>(strings.size());	//TODO change to ArrayList?
		for (Object o : strings) {
			if (o instanceof String) {
				result.add(atLeftBack((String) o, length));
			}
		}
		return result;
	}

	public static String atLeftBack(final String string, final int length) {
		throw new UnimplementedException("Not yet implemented");
	}

	public static List<String> atRight(final List<Object> strings, final int length) {
		List<String> result = new Vector<String>(strings.size());	//TODO change to ArrayList?
		for (Object o : strings) {
			if (o instanceof String) {
				result.add(atRight((String) o, length));
			}
		}
		return result;
	}

	public static String atRight(final String string, final int length) {
		throw new UnimplementedException("Not yet implemented");
	}

	public static List<String> atRightBack(final List<Object> strings, final int length) {
		List<String> result = new Vector<String>(strings.size());	//TODO change to ArrayList?
		for (Object o : strings) {
			if (o instanceof String) {
				result.add(atRightBack((String) o, length));
			}
		}
		return result;
	}

	public static String atRightBack(final String string, final int length) {
		throw new UnimplementedException("Not yet implemented");
	}

	/*
	 * ******END STRING METHODS******
	 */

	/*
	 * ******BEGIN NUMBER METHODS******
	 */

	/*
	 * ******END NUMBER METHODS******
	 */

	/*
	 * ******BEGIN DATE METHODS******
	 */

	/*
	 * ******END DATE METHODS******
	 */

	/*
	 * ******BEGIN DOCUMENT METHODS******
	 */

	/*
	 * ******END DOCUMENT METHODS******
	 */

}
