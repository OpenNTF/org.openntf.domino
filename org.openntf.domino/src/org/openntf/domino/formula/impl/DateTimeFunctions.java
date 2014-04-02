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
package org.openntf.domino.formula.impl;

import java.util.Locale;

import org.openntf.domino.DateTime;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.SimpleDateTime;
import org.openntf.domino.formula.ValueHolder;

import com.ibm.icu.util.Calendar;

public enum DateTimeFunctions {
	;
	/*----------------------------------------------------------------------------*/
	/*
	 * @Today, @Tomorrow, @Yesterday, @Now
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atToday() {
		return todTomYesNow('H');	// Heute
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atTomorrow(final FormulaContext ctx) {
		return todTomYesNow('M');	// Morgen
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atYesterday(final FormulaContext ctx) {
		return todTomYesNow('G');	// Gestern
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 0, 2 })
	public static ValueHolder atNow(final FormulaContext ctx, final ValueHolder params[]) {
		if (params == null || params.length == 0)
			return todTomYesNow('J');	// Jetzt
		throw new UnsupportedOperationException("@Now with parameters isn't yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	private static ValueHolder todTomYesNow(final char which) {
		// TODO: correct return value of DateTime type
		SimpleDateTime sdt = getSimpleDateTime();
		sdt.setNow();
		if (which == 'M')
			sdt.adjustDay(1);
		else if (which == 'G')
			sdt.adjustDay(-1);
		String res;
		res = (which == 'J') ? sdt.getLocalTime() : sdt.getDateOnly();
		return ValueHolder.valueOf(res);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Year, @Month, @Day, @Hour, @Minute, @Second, @Weekday
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static int atYear(final DateTime dt) {
		return dt.toJavaCal().get(Calendar.YEAR);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static int atMonth(final DateTime dt) {
		return dt.toJavaCal().get(Calendar.MONTH) + 1;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static int atDay(final DateTime dt) {
		return dt.toJavaCal().get(Calendar.DAY_OF_MONTH);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static int atHour(final DateTime dt) {
		return dt.toJavaCal().get(Calendar.HOUR_OF_DAY);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static int atMinute(final DateTime dt) {
		return dt.toJavaCal().get(Calendar.MINUTE);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static int atSecond(final DateTime dt) {
		return dt.toJavaCal().get(Calendar.SECOND);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static int atWeekday(final DateTime dt) {
		return dt.toJavaCal().get(Calendar.DAY_OF_WEEK);
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static String atPTest(final String s) {
		SimpleDateTime sdt = getSimpleDateTime();
		sdt.setLocalTime(s);
		return sdt.getLocalTime();
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Date, @Time, @TimeMerge
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 6 })
	public static ValueHolder atDate(final ValueHolder params[]) {
		// TODO: correct return value of DateTime type
		if (params.length == 2)
			throw new IllegalArgumentException("Expected: 1, 3, or 6 parameters");
		SimpleDateTime sdt = getSimpleDateTime();
		if (params.length >= 3) {
			sdt.setLocalDate(params[0].getInt(0), params[1].getInt(0), params[2].getInt(0));
			// 4 or 5 parameters are accepted by Lotus, but they are ignored
			if (params.length == 6)
				sdt.setLocalTime(params[3].getInt(0), params[4].getInt(0), params[5].getInt(0), 0);
			String res = (params.length == 6) ? sdt.getLocalTime() : sdt.getDateOnly();
			return ValueHolder.valueOf(res);	// Multiple values are accepted, but ignored by Lotus
		}
		ValueHolder vh = params[0];	//Remains: length=1, which works completely different
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		for (int i = 0; i < vh.size; i++) {
			sdt.setLocalTime(vh.getDateTime(i).toJavaDate());
			ret.add(sdt.getDateOnly());
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 6 })
	public static ValueHolder atTime(final ValueHolder params[]) {
		// TODO: correct return value of DateTime type
		if (params.length == 2)
			throw new IllegalArgumentException("Expected: 1, 3, or 6 parameters");
		// 4 or 5 parameters are accepted by Lotus, but they are ignored
		if (params.length >= 3 && params.length <= 5)
			return ValueHolder.valueOf("");
		if (params.length == 6)	// I was surprised!
			return atDate(params);
		SimpleDateTime sdt = getSimpleDateTime();
		ValueHolder vh = params[0];	//Remains: length=1, which works completely different
		ValueHolder ret = ValueHolder.createValueHolder(String.class, vh.size);
		for (int i = 0; i < vh.size; i++) {
			sdt.setLocalTime(vh.getDateTime(i).toJavaDate());
			ret.add(sdt.getTimeOnly());
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 2, 3 })
	public static String atTimeMerge(final DateTime dtDate, final DateTime dtTime) {
		// TODO: correct return value of DateTime type
		Calendar calDate = dtDate.toJavaCal();
		Calendar calTime = dtTime.toJavaCal();
		SimpleDateTime sdt = getSimpleDateTime();
		sdt.setLocalDate(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH) + 1, calDate.get(Calendar.DAY_OF_MONTH));
		sdt.setLocalTime(calTime.get(Calendar.HOUR_OF_DAY), calTime.get(Calendar.MINUTE), calTime.get(Calendar.SECOND),
				calTime.get(Calendar.MILLISECOND));
		return sdt.getLocalTime();
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Zone, @TimeZoneToText, @TimeToTextInZone 
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 0, 1 })
	public static ValueHolder atZone(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 2 })
	public static ValueHolder atTimeZoneToText(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 2, 3 })
	public static ValueHolder atTimeToTextInZone(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @BusinessDays
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(4)
	public static ValueHolder atBusinessDays(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Adjust
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 7, 8 })
	public static ValueHolder atAdjust(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @ListLocales, @SetLocale
	 */
	/*----------------------------------------------------------------------------*/
	private static SimpleDateTime getSimpleDateTime() {
		return new SimpleDateTime(iLocale == null ? Locale.getDefault() : iLocale);
	}

	/*----------------------------------------------------------------------------*/
	private static Locale iLocale = null;
	private static String[] localStrs = { "GERMANY", "US", "CANADA", //
			"UK", "CHINA", "FRANCE" };
	private static Locale[] locales = { Locale.GERMANY, Locale.US, Locale.CANADA, //
			Locale.UK, Locale.CHINA, Locale.FRANCE };

	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atListLocales() {
		ValueHolder ret = ValueHolder.createValueHolder(String.class, localStrs.length);
		for (int i = 0; i < localStrs.length; i++)
			ret.add(localStrs[i]);
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atSetLocale(final FormulaContext ctx, final ValueHolder params[]) {
		String toSetS = params[0].getString(0);
		if (toSetS == "") {
			iLocale = null;
			return ctx.TRUE;
		}
		Locale toSet = null;
		for (int i = 0; i < localStrs.length; i++)
			if (toSetS.equalsIgnoreCase(localStrs[i])) {
				toSet = locales[i];
				break;
			}
		if (toSet != null)
			iLocale = toSet;
		return toSet != null ? ctx.TRUE : ctx.FALSE;
	}
	/*----------------------------------------------------------------------------*/
}
