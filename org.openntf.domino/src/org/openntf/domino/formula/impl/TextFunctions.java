package org.openntf.domino.formula.impl;

import com.ibm.commons.util.NotImplementedException;

public enum TextFunctions {
	;
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 2 })
	public static String atText(final Object... args) {
		if (args.length != 1)
			throw new NotImplementedException();
		return args[0].toString();
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Left, @LeftBack, @Right, @RightBack
	 */
	/*----------------------------------------------------------------------------*/
	private static String leftRight(final String whose, final Object what, final boolean left, final boolean back) {
		int lh = whose.length();
		int howMany;
		if (what instanceof Number) {
			long nWhat = ((Number) what).longValue();
			if (nWhat < 0)
				throw new IllegalArgumentException("Got negative Number: " + what.toString());
			howMany = (nWhat > lh) ? lh : (int) nWhat;
			if (back)
				howMany = lh - howMany;
		} else {
			if (!(what instanceof String))
				throw new IllegalArgumentException("Expected Number or String, got " + what.getClass());
			String sWhat = (String) what;
			if (back)
				howMany = whose.lastIndexOf(sWhat);
			else
				howMany = whose.indexOf(sWhat);
			if (howMany < 0)
				return "";
			if (!left)
				howMany = lh - howMany - sWhat.length();
		}
		return left ? whose.substring(0, howMany) : whose.substring(lh - howMany);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static String atLeft(final String whose, final Object what) {
		return leftRight(whose, what, true, false);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static String atLeftBack(final String whose, final Object what) {
		return leftRight(whose, what, true, true);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static String atRight(final String whose, final Object what) {
		return leftRight(whose, what, false, false);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static String atRightBack(final String whose, final Object what) {
		return leftRight(whose, what, false, true);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Begins, @Ends, @Contains
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static int atBegins(final String what, final String how) {
		return what.startsWith(how) ? 1 : 0;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static int atEnds(final String what, final String how) {
		return what.endsWith(how) ? 1 : 0;
	}
	/*----------------------------------------------------------------------------*/
}
