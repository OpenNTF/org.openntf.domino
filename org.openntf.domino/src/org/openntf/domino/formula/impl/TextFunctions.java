package org.openntf.domino.formula.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

import lotus.domino.DateTime;

import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

import com.ibm.commons.util.NotImplementedException;

public enum TextFunctions {
	;
	/*----------------------------------------------------------------------------*/
	/*
	 * Generals
	 */
	/*----------------------------------------------------------------------------*/
	private static ValueHolder boolValueHolder(final FormulaContext ctx, final boolean b) {
		return new ValueHolder(b ? ctx.TRUE : ctx.FALSE);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Text
	 */
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
	private static Integer numOrStringTest(final Object what, final boolean mustBeNonNeg) {
		if (what instanceof Number) {
			int nWhat = ((Number) what).intValue();
			if (nWhat < 0 && mustBeNonNeg)
				throw new IllegalArgumentException("Got negative Number: " + what.toString());
			return (nWhat);
		}
		if (what instanceof String)
			return (null);
		throw new IllegalArgumentException("Expected Number or String, got " + what.getClass());
	}

	/*----------------------------------------------------------------------------*/
	private static String leftRight(final String whose, final Object what, final boolean left, final boolean back) {
		int lh = whose.length();
		int howMany;
		Integer i = numOrStringTest(what, true);
		if (i != null) {
			if ((howMany = i) > lh)
				howMany = lh;
			if (back)
				howMany = lh - howMany;
		} else {
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
	 * @Middle, @MiddleBack
	 */
	/*----------------------------------------------------------------------------*/
	private static String middle(final String whose, final Object what1, final Object what2, final boolean back) {
		Integer num = numOrStringTest(what1, true);
		int offset = (num == null) ? -1 : num;
		int extraOffset1 = 0;
		int lh = whose.length();
		if (offset >= 0) {
			if (back) {
				offset = lh - offset;
				if (offset < 0)
					offset = 0;
				else if (offset < lh)
					offset++;
			} else {
				if (offset > lh)
					offset = lh;
			}
		} else {
			if (back)
				offset = whose.lastIndexOf((String) what1);
			else
				offset = whose.indexOf((String) what1);
			if (offset < 0)
				return "";
			extraOffset1 = ((String) what1).length();
		}
		int[] v = new int[2];
		v[0] = offset;
		v[1] = extraOffset1;
		num = numOrStringTest(what2, false);
		if (num != null)
			calcMiddle(whose, num, back, v);
		else
			calcMiddle(whose, (String) what2, back, v);
		offset = v[0];
		int howMany = v[1];
		if (offset < 0 || howMany <= 0 || offset >= lh)
			return "";
		if (howMany > lh - offset)
			howMany = lh - offset;

		return whose.substring(offset, offset + howMany);
	}

	/*----------------------------------------------------------------------------*/
	private static void calcMiddle(final String whose, int nWhat2, final boolean back, final int[] v) {
		int offset = v[0];
		int extraOffset1 = v[1];
		if (nWhat2 < 0) {
			int oldOffset = offset;
			nWhat2 = -nWhat2;
			offset -= nWhat2;
			if (offset < 0) {
				offset = 0;
				nWhat2 = oldOffset;
			}
		} else
			offset += extraOffset1;
		v[0] = offset;
		v[1] = nWhat2;
	}

	/*----------------------------------------------------------------------------*/
	private static void calcMiddle(final String whose, final String sWhat2, final boolean back, final int[] v) {
		int offset = v[0];
		int extraOffset1 = v[1];
		int i;
		int howMany;
		if (back)
			i = whose.substring(0, offset).lastIndexOf(sWhat2);
		else
			i = whose.substring(offset + extraOffset1).indexOf(sWhat2);
		int extra = 0;
		if (i < 0)
			i = back ? 0 : whose.length();
		else
			extra = sWhat2.length();
		if (back) {
			howMany = offset;
			offset = i + extra;
			howMany -= offset;
		} else {
			offset += extraOffset1;
			howMany = i;
		}
		v[0] = offset;
		v[1] = howMany;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(3)
	public static String atMiddle(final String whose, final Object what1, final Object what2) {
		return middle(whose, what1, what2, false);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(3)
	public static String atMiddleBack(final String whose, final Object what1, final Object what2) {
		return middle(whose, what1, what2, true);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Begins, @Ends, @Contains
	 */
	/*----------------------------------------------------------------------------*/
	private static ValueHolder begEndCont(final FormulaContext ctx, final ValueHolder[] params, final char fkt) {
		boolean res = false;
		for (Object w : params[0]) {
			String what = (String) w;
			for (Object h : params[1]) {
				String how = (String) h;
				if ((fkt == 'b' && what.startsWith(how))		// @Begins
						|| (fkt == 'e' && what.endsWith(how))	// @Ends
						|| (fkt == 'c' && what.contains(how))) { // @Contains
					res = true;
					break;
				}
			}
			if (res)
				break;
		}
		return boolValueHolder(ctx, res);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atBegins(final FormulaContext ctx, final ValueHolder[] params) {
		return begEndCont(ctx, params, 'b');
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atEnds(final FormulaContext ctx, final ValueHolder[] params) {
		return begEndCont(ctx, params, 'e');
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atContains(final FormulaContext ctx, final ValueHolder[] params) {
		return begEndCont(ctx, params, 'c');
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @LowerCase, @UpperCase, @ProperCase
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static String atLowerCase(final String what) {
		return what.toLowerCase();
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static String atUpperCase(final String what) {
		return what.toUpperCase();
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static String atProperCase(final String what) {
		int lh = what.length();
		if (lh == 0)
			return what;
		char[] cArr = new char[lh];
		boolean lastWasLetter = false;
		for (int i = 0; i < lh; i++) {
			char c = what.charAt(i);
			if (Character.isLetter(c)) {
				c = lastWasLetter ? Character.toLowerCase(c) : Character.toUpperCase(c);
				lastWasLetter = true;
			} else
				lastWasLetter = false;
			cArr[i] = c;
		}
		return new String(cArr);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Ascii (?), @Char, @NewLine
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static String atChar(final Number n) {
		int what = n.intValue();
		if (what < 0 || what > 255)
			throw new IllegalArgumentException("Expected Number between 0 and 255, got " + n.toString());
		char[] cArr = new char[1];
		cArr[0] = (char) what;
		return new String(cArr);
	}

	@ParamCount(0)
	public static String atNewLine() {
		return atChar(10);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Count, @Elements, @Length
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atCount(final ValueHolder[] params) {
		return new ValueHolder(params[0].size());
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atElements(final ValueHolder[] params) {
		int res = params[0].size();
		if (res == 1) {
			Object o = params[0].get(0);
			if (o == null || (o instanceof String && ((String) o).isEmpty()))
				res = 0;
		}
		return new ValueHolder(res);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static int atLength(final String whose) {
		return whose.length();
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Explode, @Implode
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 2 })
	public static ValueHolder atImplode(final ValueHolder[] params) {
		String sep = (params.length == 1) ? " " : params[1].getText(0);
		int bedarf = (params[0].size() - 1) * sep.length();
		for (Object o : params[0])
			bedarf += ((String) o).length();
		StringBuffer sb = new StringBuffer(bedarf);
		boolean first = true;
		for (Object o : params[0]) {
			if (first)
				first = false;
			else
				sb.append(sep);
			sb.append((String) o);
		}
		return new ValueHolder(sb.toString());
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @IsNull, @IsNumber, @IsText, @IsTime
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atIsNull(final FormulaContext ctx, final ValueHolder[] params) {
		Integer i = (Integer) atElements(params).get(0);
		return boolValueHolder(ctx, i == 0);
	}

	/*----------------------------------------------------------------------------*/
	public static boolean isXXX(final ValueHolder param0, final Class<?> cls) {
		for (Object o : param0)
			if (!cls.isInstance(o))
				return false;
		return true;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atIsNumber(final FormulaContext ctx, final ValueHolder[] params) {
		return boolValueHolder(ctx, isXXX(params[0], Number.class));
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atIsText(final FormulaContext ctx, final ValueHolder[] params) {
		return boolValueHolder(ctx, isXXX(params[0], String.class));
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atIsTime(final FormulaContext ctx, final ValueHolder[] params) {
		return boolValueHolder(ctx, isXXX(params[0], DateTime.class));
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Keywords
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 2, 3 })
	public static ValueHolder atKeywords(final ValueHolder[] params) {
		String seps = (params.length == 2) ? "?. ,!;:[](){}\"<>" : params[2].getText(0);
		boolean sepsEmpty = seps.isEmpty();
		String testStr;
		if (params[0].size() == 1)
			testStr = (String) params[0].get(0);
		else {
			int bedarf = params[0].size() - 1;
			for (Object o : params[0])
				bedarf += ((String) o).length();
			StringBuffer sb = new StringBuffer(bedarf + 10);
			boolean first = true;
			char c = sepsEmpty ? (char) 1 : seps.charAt(0);
			for (Object o : params[0]) {
				if (first)
					first = false;
				else
					sb.append(c);
				sb.append((String) o);
			}
			testStr = sb.toString();
		}
		TreeSet<String> ts = new TreeSet<String>();
		if (sepsEmpty)
			keywordsNoSeps(ts, testStr, params[1]);
		else
			keywordsWithSeps(ts, seps, testStr, params[1]);
		ValueHolder ret = new ValueHolder();
		ret.addAll(ts);
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static void keywordsNoSeps(final TreeSet<String> ts, final String testStr, final ValueHolder keywords) {
		for (Object o : keywords)
			if (testStr.indexOf((String) o) >= 0)
				ts.add((String) o);
	}

	/*----------------------------------------------------------------------------*/
	private static void keywordsWithSeps(final TreeSet<String> ts, final String seps, final String testStr, final ValueHolder keywords) {
		int lh = seps.length();
		StringBuffer sb = new StringBuffer(lh + 10);
		sb.append('[');
		for (int i = 0; i < lh; i++) {
			char c = seps.charAt(i);
			if (c == '[' || c == ']')
				sb.append('\\');
			sb.append(c);
		}
		sb.append(']');
		sb.append('+');
		String[] sArr = testStr.split(sb.toString());
		//		for (int i = 0; i < sArr.length; i++)
		//			System.out.println(sArr[i] + " ");
		//		System.out.println("");
		HashSet<String> hs = new HashSet<String>();
		for (int i = 0; i < sArr.length; i++)
			hs.add(sArr[i]);
		for (Object o : keywords)
			if (hs.contains(o))
				ts.add((String) o);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Like (?), @Matches (?), @RegExpMatches
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atRegExpMatches(final FormulaContext ctx, final ValueHolder[] params) {
		boolean res = false;
		for (Object r : params[1]) {
			Pattern regPatt = Pattern.compile((String) r);
			for (Object t : params[0]) {
				if (regPatt.matcher((String) t).matches()) {
					res = true;
					break;
				}
			}
			if (res)
				break;
		}
		return boolValueHolder(ctx, res);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Replace, @ReplaceSubstring
	 * 
	 * These functions operate a bit differently: E.g.
	 *		@Replace("a":"b"; "a":"b"; "A") --> "A, " whereas
	 *		@ReplaceSubstring("a":"b"; "a":"b"; "A") --> "A, A"
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(3)
	public static ValueHolder atReplace(final ValueHolder[] params) {
		Map<String, String> replacer = getReplaceMap(params[1], params[2]);
		ValueHolder ret = new ValueHolder();
		for (Object o : params[0]) {
			String s = (String) o;
			String t = replacer.get(s);
			ret.add(t == null ? s : t);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static Map<String, String> getReplaceMap(final ValueHolder froms, final ValueHolder tos) {
		Map<String, String> ret = new HashMap<String, String>();
		int fSz = froms.size();
		int tSz = tos.size();
		for (int i = 0; i < fSz; i++) {
			String from = froms.getText(i);
			String to = (i >= tSz) ? "" : tos.getText(i);
			if (!ret.containsKey(from))
				ret.put(from, to);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
}
