/*
 * � Copyright FOCONIS AG, 2014
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

import java.util.Locale;
import java.util.TreeSet;

import org.openntf.formula.DateTime;
import org.openntf.formula.Formatter;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.annotation.ParamCount;

import com.ibm.icu.util.Calendar;

public enum DateTimeFunctions {
	;

	public static class Factory extends FunctionFactory {

		public Factory() {
			super(DateTimeFunctions.class);
		}
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Today, @Tomorrow, @Yesterday, @Now
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atToday(final FormulaContext ctx) {
		return todayTomorrYester(ctx, "TODAY");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atTomorrow(final FormulaContext ctx) {
		return todayTomorrYester(ctx, "TOMORROW");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atYesterday(final FormulaContext ctx) {
		return todayTomorrYester(ctx, "YESTERDAY");
	}

	/*----------------------------------------------------------------------------*/
	private static ValueHolder todayTomorrYester(final FormulaContext ctx, final String which) {
		return ValueHolder.valueOf(ctx.getFormatter().parseDate(which));
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 0, 2 })
	public static ValueHolder atNow(final FormulaContext ctx, final ValueHolder params[]) {
		if (params == null || params.length == 0) {
			DateTime sdt = getSDTDefaultLocale(ctx);
			sdt.setNow();
			return ValueHolder.valueOf(sdt);
		}
		throw new UnsupportedOperationException("@Now with parameters isn't yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	private static DateTime getSDTDefaultLocale(final FormulaContext ctx) {
		return ctx.getFormatter().getNewSDTInstance();
	}

	/*----------------------------------------------------------------------------*/
	private static DateTime getSDTCopy(final FormulaContext ctx, final DateTime sdt) {
		return ctx.getFormatter().getCopyOfSDTInstance(sdt);
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
	/*
	 * @Date, @Time, @TimeMerge
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 6 })
	public static ValueHolder atDate(final FormulaContext ctx, final ValueHolder params[]) {
		if (params.length == 2)
			throw new IllegalArgumentException("Expected: 1, 3, or 6 parameters");
		if (params.length >= 3) {
			DateTime sdt = getSDTDefaultLocale(ctx);
			sdt.setLocalDate(params[0].getInt(0), params[1].getInt(0), params[2].getInt(0));
			// 4 or 5 parameters are accepted by Lotus, but parameters 4 and 5 are ignored
			if (params.length == 6)
				sdt.setLocalTime(params[3].getInt(0), params[4].getInt(0), params[5].getInt(0), 0);
			else
				sdt.setAnyTime();
			return ValueHolder.valueOf(sdt);	// Multiple values are accepted, but ignored by Lotus
		}
		ValueHolder vh = params[0];	//Remains: length=1, which works completely different
		ValueHolder ret = ValueHolder.createValueHolder(DateTime.class, vh.size);
		for (int i = 0; i < vh.size; i++)
			ret.add(getSDTCopy(ctx, vh.getDateTime(i)));
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 6 })
	public static ValueHolder atTime(final FormulaContext ctx, final ValueHolder params[]) {
		if (params.length == 2)
			throw new IllegalArgumentException("Expected: 1, 3, or 6 parameters");
		// 4 or 5 parameters are accepted by Lotus, but they are ignored
		if (params.length >= 3 && params.length <= 5) {		// I was surprised.
			DateTime sdt = getSDTDefaultLocale(ctx);
			sdt.setAnyDate();
			sdt.setAnyTime();
			return ValueHolder.valueOf(sdt);
		}
		if (params.length == 6)		// I was surprised once more!
			return atDate(ctx, params);
		ValueHolder vh = params[0];	//Remains: length=1, which works completely different
		ValueHolder ret = ValueHolder.createValueHolder(DateTime.class, vh.size);
		for (int i = 0; i < vh.size; i++) {
			DateTime sdt = getSDTCopy(ctx, vh.getDateTime(i));
			sdt.setAnyDate();
			ret.add(sdt);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 2, 3 })
	public static ValueHolder atTimeMerge(final FormulaContext ctx, final ValueHolder params[]) {
		if (params.length == 3)
			throw new UnsupportedOperationException("@TimeMerge with 3 parameters not yet implemented");
		ValueHolder dates = params[0];
		ValueHolder times = params[1];
		ValueHolder ret = ValueHolder.createValueHolder(DateTime.class, dates.size);
		for (int i = 0; i < dates.size; i++) {	// If there are more times than dates, they are ignored by Lotus
			DateTime date = dates.getDateTime(i);
			DateTime time = times.getDateTime(i);
			Calendar calDate = date.toJavaCal();
			Calendar calTime = time.toJavaCal();
			DateTime sdt = getSDTDefaultLocale(ctx);
			if (date.isAnyDate())
				sdt.setAnyDate();
			else
				sdt.setLocalDate(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH) + 1, calDate.get(Calendar.DAY_OF_MONTH));
			if (time.isAnyTime())
				sdt.setAnyTime();
			else
				sdt.setLocalTime(calTime.get(Calendar.HOUR_OF_DAY), calTime.get(Calendar.MINUTE), calTime.get(Calendar.SECOND),
						calTime.get(Calendar.MILLISECOND));
			ret.add(sdt);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Zone, @TimeZoneToText, @TimeToTextInZone 
	 */
	/*----------------------------------------------------------------------------*/
	//	@ParamCount({ 0, 1 })
	//	public static ValueHolder atZone(final FormulaContext ctx, final ValueHolder params[]) {
	//		throw new UnsupportedOperationException("Method not yet implemented");
	//	}
	//
	//	/*----------------------------------------------------------------------------*/
	//	@ParamCount({ 1, 2 })
	//	public static ValueHolder atTimeZoneToText(final FormulaContext ctx, final ValueHolder params[]) {
	//		throw new UnsupportedOperationException("Method not yet implemented");
	//	}
	//
	//	/*----------------------------------------------------------------------------*/
	//	@ParamCount({ 2, 3 })
	//	public static ValueHolder atTimeToTextInZone(final FormulaContext ctx, final ValueHolder params[]) {
	//		throw new UnsupportedOperationException("Method not yet implemented");
	//	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @BusinessDays
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 2, 4 })
	public static ValueHolder atBusinessDays(final FormulaContext ctx, final ValueHolder params[]) {
		ValueHolder fromDays = params[0];
		ValueHolder toDays = params[1];
		boolean[] excludeDays = new boolean[8];
		for (int i = 0; i < 8; i++)
			excludeDays[i] = false;
		if (params.length >= 3) {
			ValueHolder exclVH = params[2];
			for (int i = 0; i < exclVH.size; i++) {
				int ex = exclVH.getInt(i);
				if (ex >= 1 && ex <= 7)
					excludeDays[ex] = true;
			}
		}
		TreeSet<DateTime> excludeDates = new TreeSet<DateTime>(fromDays.getDateTime(0));
		if (params.length == 4) {
			ValueHolder exclVH = params[3];
			for (int i = 0; i < exclVH.size; i++) {
				DateTime sdt = exclVH.getDateTime(i);
				if (sdt.isAnyDate())
					continue;
				if (!sdt.isAnyTime()) {
					sdt = getSDTCopy(ctx, sdt);
					sdt.setAnyTime();
				}
				excludeDates.add(sdt);
			}
		}
		int max = (fromDays.size >= toDays.size) ? fromDays.size : toDays.size;
		ValueHolder ret = ValueHolder.createValueHolder(int.class, max);
		for (int i = 0; i < max; i++) {
			DateTime sdtFrom = fromDays.getDateTime(i);
			DateTime sdtTo = toDays.getDateTime(i);
			int busDays;
			if (sdtFrom.isAnyDate() || sdtTo.isAnyDate())
				busDays = -1;
			else
				busDays = calcBusDays(ctx, sdtFrom, sdtTo, excludeDays, excludeDates);
			ret.add(busDays);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	private static int calcBusDays(final FormulaContext ctx, DateTime sdtFrom, DateTime sdtTo, final boolean[] excludeDays,
			final TreeSet<DateTime> excludeDates) {
		sdtFrom = getSDTCopy(ctx, sdtFrom);
		sdtFrom.setAnyTime();
		if (!sdtTo.isAnyTime()) {
			sdtTo = getSDTCopy(ctx, sdtTo);
			sdtTo.setAnyTime();
		}
		if (sdtFrom.compare(sdtFrom, sdtTo) > 0)
			return -1;
		int ret = 0;
		do {
			if (!excludeDays[sdtFrom.toJavaCal().get(Calendar.DAY_OF_WEEK)] && !excludeDates.contains(sdtFrom))
				ret++;
			sdtFrom.adjustDay(1);
		} while (sdtFrom.compare(sdtFrom, sdtTo) <= 0);
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Adjust
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 7, 8 })
	public static ValueHolder atAdjust(final FormulaContext ctx, final ValueHolder params[]) {
		if (params.length == 8)
			throw new UnsupportedOperationException("@Adjust with 8 parameters not yet implemented");
		ValueHolder toAdjust = params[0];
		int adjustYears = params[1].getInt(0);	// Multiple "Adjustors" are ignored by Lotus
		int adjustMonths = params[2].getInt(0);
		int adjustDays = params[3].getInt(0);
		int adjustHours = params[4].getInt(0);
		int adjustMinutes = params[5].getInt(0);
		int adjustSeconds = params[6].getInt(0);
		ValueHolder ret = ValueHolder.createValueHolder(DateTime.class, toAdjust.size);
		for (int i = 0; i < toAdjust.size; i++) {
			DateTime sdt = getSDTCopy(ctx, toAdjust.getDateTime(i));
			if (!sdt.isAnyDate()) {
				if (adjustYears != 0)
					sdt.adjustYear(adjustYears);
				if (adjustMonths != 0)
					sdt.adjustMonth(adjustMonths);
				if (adjustDays != 0)
					sdt.adjustDay(adjustDays);
			}
			if (!sdt.isAnyTime()) {
				if (adjustHours != 0)
					sdt.adjustHour(adjustHours);
				if (adjustMinutes != 0)
					sdt.adjustMinute(adjustMinutes);
				if (adjustSeconds != 0)
					sdt.adjustSecond(adjustSeconds);
			}
			ret.add(sdt);
		}
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @ListLocales, @SetLocale, @PTest (for testing with a different Locale)
	 */
	/*----------------------------------------------------------------------------*/
	private static DateTime getSDTLocalSetLocale() {
		return org.openntf.formula.Formulas.getFormatter(iLocale).getNewSDTInstance();
	}

	/*----------------------------------------------------------------------------*/
	/*private*/static Locale iLocale = null;
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
	@ParamCount(1)
	public static String atDTTest(final String s) {
		DateTime sdt = getSDTLocalSetLocale();
		sdt.setLocalTime(s);
		return sdt.getLocalTime();
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static String atNTest(final String s) {
		Formatter formatter = org.openntf.formula.Formulas.getFormatter(iLocale);
		Number n = formatter.parseNumber(s, true);
		return formatter.formatNumber(n);
	}
	/*----------------------------------------------------------------------------*/
}
