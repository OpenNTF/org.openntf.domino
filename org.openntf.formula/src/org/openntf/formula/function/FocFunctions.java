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

import java.util.Date;
import java.util.Map;

import org.openntf.formula.DateTime;
import org.openntf.formula.Formatter;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.Function;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.ValueHolder.DataType;
import org.openntf.formula.annotation.ParamCount;

import com.ibm.icu.util.Calendar;

/*----------------------------------------------------------------------------*/
public enum FocFunctions {

	;

	public static class Functions extends FunctionSet {
		private static final Map<String, Function> functionSet = FunctionFactory.getFunctions(FocFunctions.class);

		@Override
		public Map<String, Function> getFunctions() {
			return functionSet;
		}
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @FocDate
	 */
	/*----------------------------------------------------------------------------*/
	private static int getRelativePart(final FormulaContext ctx, final Object o, final boolean ultimoAllowed, final boolean[] auxRes) {
		if (o instanceof Number) {
			int part = ((Number) o).intValue();
			auxRes[0] = (part > 0);
			return part;
		}
		if (!(o instanceof String))
			throw new IllegalArgumentException("Expected Number or String, got " + o.getClass());
		String s = (String) o;
		if (ultimoAllowed && s.equalsIgnoreCase("ultimo")) {
			auxRes[0] = true;
			auxRes[1] = true;
			return 1;
		}
		int part = ctx.getFormatter().parseNumber(s).intValue();
		auxRes[0] = (part > 0 && Character.isDigit(s.charAt(0)));
		return part;
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 3, 4 })
	public static ValueHolder atFocDate(final FormulaContext ctx, final ValueHolder[] params) {
		boolean[] aux = new boolean[2];
		int yearPart = getRelativePart(ctx, params[0].getObject(0), false, aux);
		boolean yearIsAbs = aux[0];
		int monthPart = getRelativePart(ctx, params[1].getObject(0), false, aux);
		boolean monthIsAbs = aux[0];
		int dayPart = getRelativePart(ctx, params[2].getObject(0), true, aux);
		boolean dayIsAbs = aux[0];
		boolean dayIsUltimo = aux[1];
		DateTime refDate;
		Formatter formatter = ctx.getFormatter();
		if (params.length == 4)
			refDate = formatter.getCopyOfSDTInstance(params[3].getDateTime(0));
		else {
			Object o = ctx.getParam("@FocDate:Bezug");
			if (o instanceof DateTime)
				refDate = formatter.getCopyOfSDTInstance((DateTime) o);
			else
				refDate = formatter.getNewInitializedSDTInstance(new Date(), false, true);
		}
		if (yearIsAbs || monthIsAbs || dayIsAbs) {
			Calendar cal = refDate.toJavaCal();
			int year = yearIsAbs ? yearPart : cal.get(Calendar.YEAR);
			int month = monthIsAbs ? monthPart : cal.get(Calendar.MONTH) + 1;
			int day = dayIsAbs ? dayPart : cal.get(Calendar.DAY_OF_MONTH);
			refDate.setLocalDate(year, month, day);
		}
		if (!yearIsAbs && yearPart != 0)
			refDate.adjustYear(yearPart);
		if (!monthIsAbs && monthPart != 0)
			refDate.adjustMonth(monthPart);
		if (!dayIsAbs && dayPart != 0)
			refDate.adjustDay(dayPart);
		if (dayIsUltimo) {
			refDate.adjustMonth(1);
			refDate.adjustDay(-1);
		}
		refDate.setAnyTime();
		return ValueHolder.valueOf(refDate);
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @FocFormat
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(2)
	public static ValueHolder atFocFormat(final FormulaContext ctx, final ValueHolder[] params) {
		ValueHolder whatVH = params[0];
		String how = params[1].getString(0);
		if (whatVH.dataType != DataType.DATETIME)
			throw new UnsupportedOperationException("@FocFormat doesn't yet support formatting objects of class "
					+ whatVH.getObject(0).getClass());
		if (how.equalsIgnoreCase("General Date") || how.equalsIgnoreCase("Long Date") || how.equalsIgnoreCase("Medium Date")
				|| how.equalsIgnoreCase("Short Date") || how.equalsIgnoreCase("Long Time") || how.equalsIgnoreCase("Medium Time")
				|| how.equalsIgnoreCase("Short Time"))
			throw new UnsupportedOperationException("@FocFormat doesn't yet support date format '" + how + "'");
		how = how.replace('m', 'M');
		how = how.replace('n', 'm');
		ValueHolder ret = ValueHolder.createValueHolder(String.class, whatVH.size);
		for (int i = 0; i < whatVH.size; i++)
			ret.add(ctx.getFormatter().formatDateTimeWithFormat(whatVH.getDateTime(i), how));
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @FocResolve
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atFocResolve(final FormulaContext ctx, final ValueHolder[] params) {
		return ValueHolder.valueOf(ctx.getParam(params[0].getString(0)));
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atP(final FormulaContext ctx, final ValueHolder[] params) {
		return ValueHolder.valueOf(ctx.getParam(Integer.toString(params[0].getInt(0))));
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atP1(final FormulaContext ctx) {
		return p1To9(ctx, 1);
	}

	@ParamCount(0)
	public static ValueHolder atP2(final FormulaContext ctx) {
		return p1To9(ctx, 2);
	}

	@ParamCount(0)
	public static ValueHolder atP3(final FormulaContext ctx) {
		return p1To9(ctx, 3);
	}

	@ParamCount(0)
	public static ValueHolder atP4(final FormulaContext ctx) {
		return p1To9(ctx, 4);
	}

	@ParamCount(0)
	public static ValueHolder atP5(final FormulaContext ctx) {
		return p1To9(ctx, 5);
	}

	@ParamCount(0)
	public static ValueHolder atP6(final FormulaContext ctx) {
		return p1To9(ctx, 6);
	}

	@ParamCount(0)
	public static ValueHolder atP7(final FormulaContext ctx) {
		return p1To9(ctx, 7);
	}

	@ParamCount(0)
	public static ValueHolder atP8(final FormulaContext ctx) {
		return p1To9(ctx, 8);
	}

	@ParamCount(0)
	public static ValueHolder atP9(final FormulaContext ctx) {
		return p1To9(ctx, 9);
	}

	private static ValueHolder p1To9(final FormulaContext ctx, final int i) {
		return atP(ctx, new ValueHolder[] { ValueHolder.valueOf(i) });
	}
	/*----------------------------------------------------------------------------*/
}
