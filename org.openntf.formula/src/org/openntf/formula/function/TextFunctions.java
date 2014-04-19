/*
 * © Copyright FOCONIS AG, 2014
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
package org.openntf.formula.function;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openntf.formula.AbstractFunctionSet;
import org.openntf.formula.DateTime;
import org.openntf.formula.Formatter;
import org.openntf.formula.Formatter.LotusDateTimeOptions;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.FormulaParseException;
import org.openntf.formula.FormulaParser;
import org.openntf.formula.Formulas;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.ValueHolder.DataType;
import org.openntf.formula.annotation.DiffersFromLotus;
import org.openntf.formula.annotation.OpenNTF;
import org.openntf.formula.annotation.ParamCount;

public enum TextFunctions {
	;

	public static class FunctionSet extends AbstractFunctionSet {

		public FunctionSet() {
			super(TextFunctions.class);
		}
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

		ValueHolder vh1 = params[0];
		ValueHolder vh2 = params[1];
		for (int i1 = 0; i1 < vh1.size; i1++) {
			String what = vh1.getString(i1);
			for (int i2 = 0; i2 < vh2.size; i2++) {
				String how = vh2.getString(i2);
				if ((fkt == 'b' && what.startsWith(how))		// @Begins
						|| (fkt == 'e' && what.endsWith(how))	// @Ends
						|| (fkt == 'c' && what.contains(how))) { // @Contains
					return ctx.TRUE;
				}
			}
		}
		return ctx.FALSE;
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
	 * @Char, @NewLine
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
	public static ValueHolder atNewLine(final FormulaContext ctx) {
		return ctx.NEWLINE;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Count, @Elements, @Length, @Trim
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atCount(final ValueHolder[] params) {
		return ValueHolder.valueOf(params[0].size);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atElements(final ValueHolder[] params) {
		int res = params[0].size;
		if (res == 1 && params[0].dataType == DataType.STRING && params[0].getString(0).isEmpty())
			res = 0;
		return ValueHolder.valueOf(res);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static int atLength(final String whose) {
		return whose.length();
	}

	/*----------------------------------------------------------------------------*/
	@DiffersFromLotus("Lotus doesn't trim tabs")
	@ParamCount(1)
	public static String atTrim(final String whom) {
		int lh = whom.length();
		char[] buff = new char[lh + 1];
		boolean lastWasSpace = true;
		int filled = 0;
		for (int i = 0; i < lh; i++) {
			char c = whom.charAt(i);
			if (Character.isWhitespace(c))
				lastWasSpace = true;
			else {
				if (lastWasSpace && filled != 0)
					buff[filled++] = ' ';
				buff[filled++] = c;
				lastWasSpace = false;
			}
		}
		return (filled == lh) ? whom : new String(buff, 0, filled);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Explode, @Implode
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 4 })
	public static ValueHolder atExplode(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder vh = params[0];

		String seps = " ,;";
		boolean includeEmpties = false;
		boolean newLineIsSep = true;
		if (params.length >= 2)
			seps = params[1].getString(0);
		if (params.length >= 3)
			includeEmpties = params[2].getBoolean(0);
		if (params.length >= 4)
			newLineIsSep = params[3].getBoolean(0);
		if (newLineIsSep)
			seps += "\n";
		if (seps.isEmpty())
			return vh;
		Vector<String> res = new Vector<String>(100, 100);
		for (int i = 0; i < vh.size; i++)
			explodeOne(res, vh.getString(i), seps, includeEmpties);
		int sz = res.size();
		ValueHolder ret = ValueHolder.createValueHolder(String.class, sz);
		for (int i = 0; i < sz; i++)
			ret.add(res.get(i));
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static void explodeOne(final Vector<String> res, final String what, final String seps, final boolean includeEmpties) {
		int begin = 0;
		int end;
		int lh = what.length();
		for (end = 0; end < lh; end++) {
			char c = what.charAt(end);
			if (seps.indexOf(c) < 0)
				continue;
			int korrEnd = end;
			if (c == '\n' && end > 0 && what.charAt(end - 1) == '\r' && end > begin)
				korrEnd--;
			if (begin < korrEnd || includeEmpties)
				res.add(what.substring(begin, korrEnd));
			begin = end + 1;
		}
		if (begin < end || includeEmpties)
			res.add(what.substring(begin, end));
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 2 })
	public static ValueHolder atImplode(final ValueHolder[] params) {
		String sep = (params.length == 1) ? " " : params[1].getString(0);
		ValueHolder vh = params[0];
		int bedarf = (vh.size - 1) * sep.length();
		for (int i = 0; i < vh.size; i++)
			bedarf += vh.getString(i).length();
		StringBuilder sb = new StringBuilder(bedarf);
		boolean first = true;
		for (int i = 0; i < vh.size; i++) {
			if (first)
				first = false;
			else
				sb.append(sep);
			sb.append(vh.getString(i));
		}
		return ValueHolder.valueOf(sb.toString());
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Word
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(3)
	public static ValueHolder atWord(final ValueHolder[] params) {
		ValueHolder vhSep = ValueHolder.valueOf(params[1].getString(0));
		int which = params[2].getInt(0);
		ValueHolder vh = params[0];
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		Vector<String> aux = new Vector<String>(100, 100);
		for (int i = 0; i < vh.size; i++)
			word4One(ret, aux, vh.getString(i), vhSep, which);
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static void word4One(final ValueHolder ret, final Vector<String> aux, final String whose, final ValueHolder vhSep, int which) {
		aux.clear();
		simpleSplitOne(aux, whose, vhSep, true);
		int sz = aux.size();
		String w;
		if (which >= 0) {
			if (which > 0)
				which--;
			w = (which >= sz) ? "" : aux.elementAt(which);
		} else {
			which = sz + which;
			w = (which >= 0) ? aux.elementAt(which) : "";
		}
		ret.add(w);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @SplitSimple, @SplitRegExp
	 */
	/*----------------------------------------------------------------------------*/
	/**
	 * Similar to @Explode, but respects the whole splitting strings. That is, every string in params[0] will be split along every string in
	 * params[1]. The optional parameter params[2] may contain the option [NOEMPTIES] (case-insensitive) to tell the method not to include
	 * empty splits in the result set.
	 * 
	 * @return same as @Explode
	 */
	@OpenNTF
	@ParamCount({ 2, 3 })
	public static ValueHolder atSplitSimple(final ValueHolder[] params) {
		boolean noEmpties = false;
		if (params.length == 3) {
			ValueHolder options = params[2];
			for (int i = 0; i < options.size; i++) {
				String opt = options.getString(i);
				if ("[NOEMPTIES]".equalsIgnoreCase(opt))
					noEmpties = true;
				else
					throw new IllegalArgumentException("Illegal Option: " + opt);
			}
		}
		ValueHolder vh = params[0];
		Vector<String> res = new Vector<String>(100, 100);
		for (int i = 0; i < vh.size; i++)
			simpleSplitOne(res, vh.getString(i), params[1], !noEmpties);
		int sz = res.size();
		ValueHolder ret = ValueHolder.createValueHolder(String.class, sz);
		for (int i = 0; i < sz; i++)
			ret.add(res.get(i));
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static void simpleSplitOne(final Vector<String> res, final String whom, final ValueHolder sepStrings,
			final boolean includeEmpties) {
		int pos = 0;
		int lh = whom.length();
		while (pos <= lh) {
			int found = -1;
			int minIndex = lh + 1;
			for (int i = 0; i < sepStrings.size; i++) {
				int ind = whom.indexOf(sepStrings.getString(i), pos);
				if (ind >= 0 && ind < minIndex) {
					found = i;
					minIndex = ind;
				}
			}
			if (found == -1) {
				if (pos < lh)
					res.add(whom.substring(pos));
				else if (includeEmpties)
					res.add("");
				break;
			}
			res.add(whom.substring(pos, minIndex));
			pos = minIndex + sepStrings.getString(found).length();
		}
	}

	/*----------------------------------------------------------------------------*/
	/**
	 * Similar to @SplitSimple, but acts like String.split. That is, every string in params[0] will be split along the corresponding regular
	 * expression in params[1]. The optional parameter params[2] may contain the option [NOEMPTIES] (case-insensitive) to tell the method
	 * not to include empty splits in the result set.
	 * 
	 * @return same as @Explode
	 */
	@OpenNTF
	@ParamCount({ 2, 3 })
	public static ValueHolder atSplitRegExp(final ValueHolder[] params) {
		boolean noEmpties = false;
		if (params.length == 3) {
			ValueHolder options = params[2];
			for (int i = 0; i < options.size; i++) {
				String opt = options.getString(i);
				if ("[NOEMPTIES]".equalsIgnoreCase(opt))
					noEmpties = true;
				else
					throw new IllegalArgumentException("Illegal Option: " + opt);
			}
		}
		ValueHolder vh = params[0];
		ValueHolder regs = params[1];
		String reg = null;
		Vector<String> res = new Vector<String>(100, 100);
		for (int i = 0; i < vh.size; i++) {
			if (i < regs.size)
				reg = regs.getString(i);
			regExpSplitOne(res, vh.getString(i), reg, !noEmpties);
		}
		int sz = res.size();
		ValueHolder ret = ValueHolder.createValueHolder(String.class, sz);
		for (int i = 0; i < sz; i++)
			ret.add(res.get(i));
		return ret;

	}

	/*----------------------------------------------------------------------------*/
	private static void regExpSplitOne(final Vector<String> res, final String which, final String reg, final boolean includeEmpties) {
		String[] splits = which.split(reg);
		for (int j = 0; j < splits.length; j++)
			if (!splits[j].isEmpty() || includeEmpties)
				res.add(splits[j]);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @IsNull, @IsNumber, @IsText, @IsTime
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atIsNull(final FormulaContext ctx, final ValueHolder[] params) {
		Integer i = atElements(params).getInt(0);
		return (i == 0) ? ctx.TRUE : ctx.FALSE;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atIsNumber(final FormulaContext ctx, final ValueHolder[] params) {
		return params[0].dataType.numeric ? ctx.TRUE : ctx.FALSE;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atIsText(final FormulaContext ctx, final ValueHolder[] params) {
		return (params[0].dataType == DataType.STRING) ? ctx.TRUE : ctx.FALSE;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atIsTime(final FormulaContext ctx, final ValueHolder[] params) {
		return (params[0].dataType == DataType.DATETIME) ? ctx.TRUE : ctx.FALSE;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Failure
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static String atFailure(final String cause) {
		return cause;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @ToNumber, @TextToNumber
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atToNumber(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder vh = params[0];
		if (vh.dataType.numeric)
			return vh;
		if (vh.dataType != DataType.STRING)
			throw new IllegalArgumentException("String or Number expected");
		return (strToNumber(ctx, vh, true));
	}

	/*----------------------------------------------------------------------------*/
	@DiffersFromLotus("Lotus gives e.g. @TextToNumber(\"1\":\"a\":\"2\")=1:@ERROR:2")
	@ParamCount(1)
	public static ValueHolder atTextToNumber(final FormulaContext ctx, final ValueHolder[] params) {
		return strToNumber(ctx, params[0], false);
	}

	/*----------------------------------------------------------------------------*/
	@SuppressWarnings("deprecation")
	private static ValueHolder strToNumber(final FormulaContext ctx, final ValueHolder vh, final boolean emptySpecial) {
		ValueHolder ret = ValueHolder.createValueHolder(Double.class, vh.size);
		String val = null;
		for (int i = 0; i < vh.size; i++) {
			val = vh.getString(i);
			if (val.isEmpty() && emptySpecial)
				val = "0";
			ret.add(ctx.getFormatter().parseNumber(val, true));	// lenient=true
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @ToTime, @TextToTime
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atToTime(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder vh = params[0];
		if (vh.dataType == DataType.DATETIME)
			return vh;
		if (vh.dataType != DataType.STRING)
			throw new IllegalArgumentException("String or DateTime expected");
		return strToDateTime(ctx, vh);
	}

	/*----------------------------------------------------------------------------*/
	@DiffersFromLotus("Lotus gives e.g. @IsError(@TextToTime(\"Nonsense\"))=0")
	@ParamCount(1)
	public static ValueHolder atTextToTime(final FormulaContext ctx, final ValueHolder[] params) {
		return strToDateTime(ctx, params[0]);
	}

	/*----------------------------------------------------------------------------*/
	private static ValueHolder strToDateTime(final FormulaContext ctx, final ValueHolder vh) {
		ValueHolder ret = ValueHolder.createValueHolder(DateTime.class, vh.size);
		Formatter formatter = ctx.getFormatter();
		String val = null;
		for (int i = 0; i < vh.size; i++) {
			val = vh.getString(i);
			ret.add(formatter.parseDate(val, true));
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Keywords
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 2, 3 })
	public static ValueHolder atKeywords(final ValueHolder[] params) {
		boolean explicitSeps = (params.length == 3);
		String seps = explicitSeps ? params[2].getString(0) : "?. \t\r\n,!;:[](){}\"<>";
		Vector<String> res = new Vector<String>(params[1].size + 1);
		if (seps.isEmpty())
			keywordsNoSeps(res, params[0], params[1]);
		else
			keywordsWithSeps(res, seps, explicitSeps, params[0], params[1]);
		int sz = res.size();
		ValueHolder ret = ValueHolder.createValueHolder(String.class, sz);
		for (int i = 0; i < sz; i++)
			ret.add(res.get(i));
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static void keywordsNoSeps(final Vector<String> res, final ValueHolder testStrs, final ValueHolder keywords) {
		for (int i = 0; i < keywords.size; i++) {
			String keyword = keywords.getString(i);
			for (int j = 0; j < testStrs.size; j++) {
				if (testStrs.getString(j).indexOf(keyword) >= 0) {
					res.add(keyword);
					break;
				}
			}
		}
	}

	/*----------------------------------------------------------------------------*/
	private static void keywordsWithSeps(final Vector<String> res, final String seps, final boolean explicitSeps,
			final ValueHolder testStrs, final ValueHolder keywords) {
		int bedarf = testStrs.size + 1;
		for (int i = 0; i < testStrs.size; i++)
			bedarf += testStrs.getString(i).length();
		char[] buffer = new char[bedarf + 10];
		int count = 0;
		for (int i = 0; i < testStrs.size; i++) {
			String ts = testStrs.getString(i);
			int lh = ts.length();
			boolean putIt = !explicitSeps;
			boolean lastWasSep = true;
			for (int j = 0; j < lh; j++) {
				char c = ts.charAt(j);
				if (seps.indexOf(c) >= 0) {
					if (!lastWasSep)
						buffer[count++] = (char) 0;
					lastWasSep = true;
					putIt = true;
					continue;
				}
				if (putIt) {
					buffer[count++] = c;
					lastWasSep = false;
				}
			}
			if (!lastWasSep)
				buffer[count++] = (char) 0;
		}
		String testStr = new String(buffer, 0, count);
		for (int i = 0; i < keywords.size; i++) {
			String keyword = keywords.getString(i);
			if (testStr.indexOf(keyword) >= 0)
				res.add(keyword);
		}
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Matches, @Like, @MatchesRegExp
	 */
	/*----------------------------------------------------------------------------*/
	@DiffersFromLotus("Logical operations on patterns using &,|,! aren't yet supported")
	@ParamCount(2)
	public static ValueHolder atMatches(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder vhTester = params[0];
		ValueHolder vhPatterns = params[1];
		for (int ip = 0; ip < vhPatterns.size; ip++) {
			TextMatchLotus tml = new TextMatchLotus(vhPatterns.getString(ip));
			for (int it = 0; it < vhTester.size; it++)
				if (tml.matches(vhTester.getString(it)))
					return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 2, 3 })
	public static ValueHolder atLike(final FormulaContext ctx, final ValueHolder[] params) {
		char escape = 0;
		if (params.length == 3) {
			String e = params[2].getString(0);
			if (e != null && e.length() != 0)
				escape = e.charAt(0);
		}
		ValueHolder vhTester = params[0];
		ValueHolder vhPatterns = params[1];
		for (int ip = 0; ip < vhPatterns.size; ip++) {
			TextMatchLotus tml = new TextMatchLotus(vhPatterns.getString(ip), escape);
			for (int it = 0; it < vhTester.size; it++)
				if (tml.matches(vhTester.getString(it)))
					return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	/*----------------------------------------------------------------------------*/
	@OpenNTF
	@ParamCount(2)
	public static ValueHolder atMatchesRegExp(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder vhTester = params[0];
		ValueHolder vhPatterns = params[1];
		for (int ip = 0; ip < vhPatterns.size; ip++) {
			Pattern regPatt = Pattern.compile(vhPatterns.getString(ip));
			for (int it = 0; it < vhTester.size; it++)
				if (regPatt.matcher(vhTester.getString(it)).matches())
					return ctx.TRUE;
		}
		return ctx.FALSE;
	}

	/*----------------------------------------------------------------------------*/
	@OpenNTF
	@ParamCount(3)
	public static ValueHolder atMatchesGetCaptGroups(final FormulaContext ctx, final ValueHolder[] params) {
		String tester = params[0].getString(0);
		String pattern = params[1].getString(0);
		String resName = params[2].getString(0);
		Pattern regPatt = Pattern.compile(pattern);
		Matcher matsch = regPatt.matcher(tester);
		if (!matsch.matches())
			return ValueHolder.valueOf(0);
		int numGroups = matsch.groupCount();
		ValueHolder resValue = ValueHolder.createValueHolder(String.class, numGroups + 1);
		for (int i = 0; i <= numGroups; i++)
			resValue.add(matsch.group(i));
		ctx.setVarLC(resName.toLowerCase(), resValue);
		return ValueHolder.valueOf(resValue.size);
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
		ValueHolder vh = params[0];
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		for (int i = 0; i < vh.size; i++) {
			String s = vh.getString(i);
			String t = replacer.get(s);
			ret.add(t == null ? s : t);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static Map<String, String> getReplaceMap(final ValueHolder froms, final ValueHolder tos) {
		Map<String, String> ret = new HashMap<String, String>();
		for (int i = 0; i < froms.size; i++) {
			String from = froms.getString(i);
			String to = (i >= tos.size) ? "" : tos.getString(i);
			if (!ret.containsKey(from))
				ret.put(from, to);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(3)
	public static ValueHolder atReplaceSubstring(final ValueHolder[] params) {
		ValueHolder tests = params[0];
		ValueHolder ret = ValueHolder.createValueHolder(String.class, tests.size);
		for (int i = 0; i < tests.size; i++)
			ret.add(replaceString(tests.getString(i), params[1], params[2]));
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static String replaceString(String what, final ValueHolder froms, final ValueHolder tos) {
		String to = tos.getString(0);
		for (int i = 0; i < froms.size; i++) {
			if (i != 0 && i < tos.size)
				to = tos.getString(i);
			what = what.replace(froms.getString(i), to);
		}
		return what;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Select, @Subset
	 * 
	 * Concerning @Select, again, Lotus behaves a bit funny:
	 * @Select(4) ---> 4  @Select(0) ---> ERROR  @Select(-1) ---> ERROR
	 * @Select(n;1;2;4) ---> 1 for every n<=1 e.g. @Select(-11;1;2;4) ---> 1
	 */
	/*----------------------------------------------------------------------------*/
	@DiffersFromLotus("@Select with only one parameter isn't supported")
	@ParamCount({ 2, 99 })
	public static ValueHolder atSelect(final ValueHolder[] params) {
		int which = params[0].getInt(0);
		if (which <= 1)
			which = 1;
		else if (which >= params.length)
			which = params.length - 1;
		return params[which];
	}

	/*----------------------------------------------------------------------------*/
	@SuppressWarnings("deprecation")
	@ParamCount(2)
	public static ValueHolder atSubset(final ValueHolder[] params) {
		int count = params[1].getInt(0);
		if (count == 0)
			throw new IllegalArgumentException("Second argument to @Subset is 0");
		int first;
		int last;
		ValueHolder vh = params[0];
		if (count > 0) {
			first = 0;
			last = (count > vh.size) ? vh.size : count;
		} else {
			count = -count;
			last = vh.size;
			first = (count > last) ? 0 : last - count;
		}
		ValueHolder ret = vh.newInstance(last - first);
		/*
		 * To avoid boxing/unboxing of primitives:
		 */
		switch (vh.dataType) {
		case BOOLEAN:
			for (int i = first; i < last; i++)
				ret.add(vh.getBoolean(i));
			break;
		case DOUBLE:
			for (int i = first; i < last; i++)
				ret.add(vh.getDouble(i));
			break;

		case INTEGER:
			for (int i = first; i < last; i++)
				ret.add(vh.getInt(i));
			break;
		default:
			for (int i = first; i < last; i++)
				ret.add(vh.getObject(i));
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @IsMember, @IsNotMember, @Member
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atIsMember(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder whos = params[0];
		ValueHolder where = params[1];
		for (int i = 0; i < whos.size; i++) {
			String who = whos.getString(i);
			int j;
			for (j = 0; j < where.size; j++)
				if (where.getString(j).equals(who))
					break;
			if (j == where.size)
				return ctx.FALSE;
		}
		return ctx.TRUE;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atIsNotMember(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder whos = params[0];
		ValueHolder where = params[1];
		for (int i = 0; i < whos.size; i++) {
			String who = whos.getString(i);
			for (int j = 0; j < where.size; j++)
				if (where.getString(j).equals(who))
					return ctx.FALSE;
		}
		return ctx.TRUE;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atMember(final FormulaContext ctx, final ValueHolder[] params) {
		String who = params[0].getString(0);	// Notes accepts a list of Strings here, but ignores all but the first entry
		ValueHolder where = params[1];
		int j;
		for (j = 0; j < where.size; j++)
			if (where.getString(j).equals(who))
				break;
		if (j == where.size)
			j = -1;
		return ValueHolder.valueOf(j + 1);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Unique, @Repeat
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 1 })
	public static ValueHolder atUnique(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder vh = params[0];
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		for (int i = 0; i < vh.size; i++) {
			String s = vh.getString(i);
			int j;
			for (j = 0; j < ret.size; j++)
				if (ret.getString(j).equals(s))
					break;
			if (j == ret.size)
				ret.add(s);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 2, 3 })
	public static ValueHolder atRepeat(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder vh = params[0];
		int repeator = params[1].getInt(0);
		if (repeator < 0)
			repeator = -repeator;	// Notes behaviour!
		int cutter = 65000;	// The Notes limit is unclear: The documentation says 1024, but seems to be greater.
		if (params.length == 3) {
			int i = params[2].getInt(0);
			if (i < 0)
				i = -i;	// Notes behaviour!
			if (i < 65000)
				cutter = i;
		}
		int bedarf = 0;
		for (int i = 0; i < vh.size; i++) {
			int lh = vh.getString(i).length();
			if (lh > bedarf)
				bedarf = lh;
		}
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		if (repeator == 0 || cutter == 0 || bedarf == 0) {
			for (int i = 0; i < vh.size; i++)
				ret.add("");
			return ret;
		}
		StringBuilder sb = new StringBuilder(bedarf * repeator);
		for (int i = 0; i < vh.size; i++) {
			sb.setLength(0);
			String s = vh.getString(i);
			for (int j = 0; j < repeator; j++)
				sb.append(s);
			ret.add((sb.length() > cutter) ? sb.substring(0, cutter) : sb.toString());
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Compare
	 */
	/*----------------------------------------------------------------------------*/
	@DiffersFromLotus({ "Options [ACCENT(IN)SENSITIVE] and [PITCH(IN)SENSITIVE] aren't yet supported",
			"String compare is done via String.compareTo" })
	@ParamCount({ 2, 3 })
	public static ValueHolder atCompare(final ValueHolder[] params) {
		boolean caseSensitive = true;
		if (params.length == 3) {
			ValueHolder options = params[2];
			for (int i = 0; i < options.size; i++) {
				String opt = options.getString(i);
				if ("[CASESENSITIVE]".equalsIgnoreCase(opt))
					caseSensitive = true;
				else if ("[CASEINSENSITIVE]".equalsIgnoreCase(opt))
					caseSensitive = false;
				else if (!"[ACCENTSENSITIVE]".equalsIgnoreCase(opt) && !"[ACCENTINSENSITIVE]".equalsIgnoreCase(opt)
						&& !"[PITCHSENSITIVE]".equalsIgnoreCase(opt) && !"[PITCHINSENSITIVE]".equalsIgnoreCase(opt))
					throw new IllegalArgumentException("Illegal Option: " + opt);
			}
		}
		ValueHolder vh1 = params[0];
		ValueHolder vh2 = params[1];
		int max = (vh1.size >= vh2.size) ? vh1.size : vh2.size;
		ValueHolder ret = ValueHolder.createValueHolder(Integer.class, max);
		String s1 = null;
		String s2 = null;
		for (int i = 0; i < max; i++) {
			if (i < vh1.size)
				s1 = vh1.getString(i);
			if (i < vh2.size)
				s2 = vh2.getString(i);
			int cmp;
			if (s1 == null)
				cmp = (s2 == null) ? 0 : -1;
			else
				cmp = caseSensitive ? s1.compareTo(s2) : s1.compareToIgnoreCase(s2);
			if (cmp < 0)
				cmp = -1;
			else if (cmp > 0)
				cmp = 1;
			ret.add(cmp);
		}
		return (ret);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @FileDir
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static String atFileDir(final String fileName) {
		int indSlash = fileName.lastIndexOf('/');
		int indBackSlash = -1;
		if ("\\".equals(System.getProperty("file.separator")))
			indBackSlash = fileName.lastIndexOf('\\');
		if (indSlash < 0 && indBackSlash < 0)
			return "";
		if (indSlash < indBackSlash)
			indSlash = indBackSlash;
		return fileName.substring(0, indSlash + 1);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Text
	 */
	/*----------------------------------------------------------------------------*/
	@DiffersFromLotus("Not all Lotus formats are yet supported")
	@SuppressWarnings("deprecation")
	@ParamCount({ 1, 2 })
	public static ValueHolder atText(final FormulaContext ctx, final ValueHolder params[]) {
		ValueHolder vh = params[0];
		String format = null;
		if (params.length == 2) {
			format = params[1].getString(0).toUpperCase();
			if (format.isEmpty() || (!vh.dataType.numeric && vh.dataType != DataType.DATETIME))
				format = null;
		}
		if (vh.dataType.numeric)
			return doAtTextNumber(ctx, vh, format);
		if (vh.dataType == DataType.DATETIME)
			return doAtTextDateTime(ctx, vh, format);
		if (vh.dataType == DataType.STRING)
			return vh;
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		for (int i = 0; i < vh.size; i++)
			ret.add(vh.get(i).toString());
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static ValueHolder doAtTextNumber(final FormulaContext ctx, final ValueHolder vh, final String format) {
		Formatter.LotusNumberOptions lno = new Formatter.LotusNumberOptions();
		if (format == null)
			lno.format = 'G';
		else {
			boolean invalidFormat = false;
			int lh = format.length();
			for (int i = 0; i < lh; i++) {
				char c = format.charAt(i);
				if (c == ',') {
					lno.useGrouping = true;
					continue;
				}
				if (c == '(' || c == ')') {
					lno.negativeAsParentheses = true;
					continue;
				}
				if (c == 'G' || c == 'F' || c == 'C' || c == '%' || c == 'S') {
					if (lno.format != 0) {
						invalidFormat = true;
						break;
					}
					lno.format = c;
					continue;
				}
				if (!Character.isDigit(c)) {
					invalidFormat = true;
					break;
				}
				if (lno.fractionDigits != -1) {
					invalidFormat = true;
					break;
				}
				StringBuilder sb = new StringBuilder(64);
				sb.append(c);
				for (i++; i < lh; i++) {
					c = format.charAt(i);
					if (!Character.isDigit(c)) {
						i--;
						break;
					}
					sb.append(c);
				}
				try {
					lno.fractionDigits = Integer.parseInt(sb.toString());
				} catch (NumberFormatException e) {
					invalidFormat = true;
					break;
				}
			}
			if (invalidFormat)
				throw new IllegalArgumentException("Invalid Number format: '" + format + "'");
		}
		if (lno.format == 0)
			lno.format = 'G';
		if (lno.format == 'G' || lno.format == '%')
			lno.fractionDigits = -1;
		else if (lno.format == 'F' && lno.fractionDigits == -1)
			lno.fractionDigits = 2;
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		for (int i = 0; i < vh.size; i++)
			ret.add(ctx.getFormatter().formatNumber(vh.getDouble(i), lno));
		//ret.add(DominoFormatter.getInstance(DateTimeFunctions.iLocale).formatNumber(vh.getDouble(i), lno));
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static ValueHolder doAtTextDateTime(final FormulaContext ctx, final ValueHolder vh, final String format) {
		Formatter.LotusDateTimeOptions ldto = new Formatter.LotusDateTimeOptions();
		boolean invalidFormat = false;
		while (format != null) {
			int lh = format.length();
			if (lh == 0)
				break;
			invalidFormat = true;
			if ((lh & 1) != 0)
				break;
			int i;
			for (i = 0; i < lh; i += 2) {
				char opt = format.charAt(i);
				char optNum = format.charAt(i + 1);
				if (!Character.isDigit(optNum))
					break;
				if (opt == 'D') {
					if (optNum == '0')
						ldto.dOption = LotusDateTimeOptions.D_YMD;
					else if (optNum == '1')
						ldto.dOption = LotusDateTimeOptions.D_YMD_YOPT;
					else if (optNum == '2')
						ldto.dOption = LotusDateTimeOptions.D_MD;
					else if (optNum == '3')
						ldto.dOption = LotusDateTimeOptions.D_YM;
					else
						break;
				} else if (opt == 'T') {
					if (optNum == '0')
						ldto.tOption = LotusDateTimeOptions.T_HMS;
					else if (optNum == '1')
						ldto.tOption = LotusDateTimeOptions.T_HM;
					else
						break;
				} else if (opt == 'Z') {
					if (optNum == '0')
						ldto.zOption = LotusDateTimeOptions.Z_CONV;
					else if (optNum == '1')
						ldto.zOption = LotusDateTimeOptions.Z_DISP_OPT;
					else if (optNum == '2')
						ldto.zOption = LotusDateTimeOptions.Z_DISP_ALW;
					else
						break;
				} else if (opt == 'S') {
					if (optNum == '0')
						ldto.sOption = LotusDateTimeOptions.S_D_ONLY;
					else if (optNum == '1')
						ldto.sOption = LotusDateTimeOptions.S_T_ONLY;
					else if (optNum == '2')
						ldto.sOption = LotusDateTimeOptions.S_DT;
					else if (optNum == '3')
						ldto.sOption = LotusDateTimeOptions.S_DT_TY;
					else
						break;
				} else
					break;
			}
			if (i == lh)
				invalidFormat = false;
			break;
		}
		if (invalidFormat)
			throw new IllegalArgumentException("Invalid DateTime format: '" + format + "'");
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		for (int i = 0; i < vh.size; i++)
			ret.add(ctx.getFormatter().formatDateTime(vh.getDateTime(i), ldto));
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @TextToDateTimeF, @TextFromDateTimeF
	 */
	/*----------------------------------------------------------------------------*/
	@OpenNTF
	@ParamCount({ 2, 3 })
	public static ValueHolder atTextToDateTimeF(final FormulaContext ctx, final ValueHolder params[]) {
		boolean parseLenient = false;
		if (params.length == 3) {
			String opt = params[2].getString(0);
			if (!"[LENIENT]".equalsIgnoreCase(opt))
				throw new IllegalArgumentException("Invalid option: '" + opt + "'");
			parseLenient = true;
		}
		String format = params[1].getString(0);
		ValueHolder vh = params[0];
		ValueHolder ret = ValueHolder.createValueHolder(DateTime.class, vh.size);
		for (int i = 0; i < vh.size; i++)
			ret.add(ctx.getFormatter().parseDateWithFormat(vh.getString(i), format, parseLenient));
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@OpenNTF
	@ParamCount(2)
	public static String atTextFromDateTimeF(final FormulaContext ctx, final DateTime sdt, final String format) {
		return ctx.getFormatter().formatDateTimeWithFormat(sdt, format);
	}

	/*----------------------------------------------------------------------------*/

	public static ValueHolder atListSupportedFunctions(final FormulaContext ctx) {
		FunctionFactory ff = Formulas.getFunctionFactory();

		List<String> functions = new ArrayList<String>(ff.getFunctions().keySet());
		Collections.sort(functions);
		ValueHolder ret = ValueHolder.createValueHolder(String.class, functions.size());
		for (String func : functions) {
			ret.add(func);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @URLDecode, @URLEncode
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atURLDecode(final ValueHolder params[]) {
		return urlDecEnc(params, false);
	}

	@ParamCount(2)
	public static ValueHolder atURLEncode(final ValueHolder params[]) {
		return urlDecEnc(params, true);
	}

	private static ValueHolder urlDecEnc(final ValueHolder[] params, final boolean encode) {
		String opt = params[0].getString(0);
		if (!"domino".equalsIgnoreCase(opt) && (!"utf-8".equalsIgnoreCase(opt)))
			throw new IllegalArgumentException("Decode type '" + opt + "' not supported");
		ValueHolder vh = params[1];
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		try {
			for (int i = 0; i < vh.size; i++) {
				String what = vh.getString(i);
				ret.add(encode ? URLEncoder.encode(what, "UTF-8").replace("+", "%20") : URLDecoder.decode(what, "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Error in URLEn/Decoder: " + e.getMessage());
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @CheckFormulaSyntax
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atCheckFormulaSyntax(final FormulaContext ctx, final ValueHolder params[]) {
		FormulaParser parser = ctx.getParser();
		try {
			parser.parse(params[0].getString(0));
			return ValueHolder.valueOf("1");
		} catch (FormulaParseException e) {
			return ValueHolder.valueOf(e.getMessage());
		}
	}

	/*----------------------------------------------------------------------------*/
}
