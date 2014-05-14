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
			ValueHolder vhd = ctx.getParam("FOC_DATE_MAP_KEY");
			if (vhd == ValueHolder.valueDefault() || vhd == ValueHolder.valueNothing())
				refDate = formatter.getNewInitializedSDTInstance(new Date(), false, true);
			else
				refDate = formatter.getCopyOfSDTInstance(vhd.getDateTime(0));
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
			throw new UnsupportedOperationException("@FocFormat doesn't yet support formatting objects of "
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
}
