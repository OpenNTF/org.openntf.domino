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
package org.openntf.domino.formula;

import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.openntf.domino.ISimpleDateTime;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

public class DominoFormatter implements Formatter {
	/*----------------------------------------------------------------------------*/
	private static Map<Locale, Formatter> instances = new HashMap<Locale, Formatter>();
	/*----------------------------------------------------------------------------*/
	private Locale iLocale;

	private DominoFormatter(final Locale loc) {
		iLocale = loc;
	}

	public static synchronized Formatter getInstance(Locale loc) {
		if (loc == null)
			loc = Locale.getDefault();
		Formatter ret = instances.get(loc);
		if (ret == null)
			instances.put(loc, ret = new DominoFormatter(loc));
		return ret;
	}

	public static Formatter getDefaultInstance() {
		return getInstance(null);
	}

	public Locale getLocale() {
		return iLocale;
	}

	/*----------------------------------------------------------------------------*/
	public ISimpleDateTime getNewSDTInstance() {
		return new SimpleDateTime(iLocale);
	}

	public ISimpleDateTime getNewInitializedSDTInstance(final Date date, final boolean noDate, final boolean noTime) {
		ISimpleDateTime sdt = getNewSDTInstance();
		sdt.setLocalTime(date);
		if (noDate)
			sdt.setAnyDate();
		if (noTime)
			sdt.setAnyTime();
		return sdt;
	}

	public ISimpleDateTime getCopyOfSDTInstance(final ISimpleDateTime sdt) {
		return getNewInitializedSDTInstance(sdt.toJavaDate(), sdt.isAnyDate(), sdt.isAnyTime());
	}

	/*----------------------------------------------------------------------------*/
	public ISimpleDateTime parseDate(final String image) {
		return parseDate(image, false);
	}

	public ISimpleDateTime parseDate(final String image, final boolean parseLenient) {
		ISimpleDateTime ret = getNewSDTInstance();
		ret.setLocalTime(image, parseLenient);
		return ret;
	}

	public ISimpleDateTime parseDateWithFormat(final String image, final String format, final boolean parseLenient) {
		boolean[] noDT = new boolean[2];
		Calendar cal = parseDateToCalWithFormat(image, format, noDT, parseLenient);
		ISimpleDateTime ret = getNewInitializedSDTInstance(cal.getTime(), noDT[0], noDT[1]);
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	public Calendar parseDateToCal(String image, final boolean[] noDT, final boolean parseLenient) {
		image = image.trim();
		Calendar ret = Calendar.getInstance(iLocale);
		// Should an empty string lead to a DateTime with noDate=noTime=true?
		// (Lotus doesn't accept empty strings here.)
		char spec = 0;
		if (image.equalsIgnoreCase("TODAY"))
			spec = 'H';
		else if (image.equalsIgnoreCase("TOMORROW"))
			spec = 'M';
		else if (image.equalsIgnoreCase("YESTERDAY"))
			spec = 'G';
		if (spec != 0) {
			ret.setTime(new Date());
			if (spec == 'M')
				ret.add(Calendar.DAY_OF_MONTH, 1);
			else if (spec == 'G')
				ret.add(Calendar.DAY_OF_MONTH, -1);
			noDT[0] = false;
			noDT[1] = true;
			return ret;
		}
		ret.setLenient(false);
		ParsePosition p = new ParsePosition(0);
		boolean illegalDateString = false;
		for (;;) {
			ret.clear();
			/*
			 * First attempt: Take a full date-time format MEDIUM
			 */
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, iLocale);
			df.parse(image, ret, p);
			if (p.getErrorIndex() < 0)
				break;
			if (!ret.isSet(Calendar.DAY_OF_MONTH) || !ret.isSet(Calendar.MONTH)) {
				//Try with SHORT format			
				ret.clear();
				df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, iLocale);
				p.setIndex(0);
				p.setErrorIndex(-1);
				df.parse(image, ret, p);
				if (!ret.isSet(Calendar.DAY_OF_MONTH) || !ret.isSet(Calendar.MONTH)) {	// Give up with date
					ret.clear();
					p.setErrorIndex(0);
				}
			}
			if (ret.isSet(Calendar.MINUTE))
				break;
			/*
			 * If no time found yet (i.e. at least hour+minute like Lotus), try to fish it
			 */
			p.setIndex(p.getErrorIndex());
			p.setErrorIndex(-1);
			df = DateFormat.getTimeInstance(DateFormat.MEDIUM, iLocale);
			df.parse(image, ret, p);
			if (ret.isSet(Calendar.MINUTE))
				break;
			if (ret.isSet(Calendar.DAY_OF_MONTH)) { // Set back possible hour (in accordance with Lotus)
				ret.clear(Calendar.HOUR);
				ret.clear(Calendar.HOUR_OF_DAY);
				break;
			}
			/*
			 * Left: No date found, no time found
			 */
			illegalDateString = true;
			break;
		}
		//		System.out.println("Lh=" + image.length() + " Index=" + p.getIndex() + " ErrIndex=" + p.getErrorIndex());
		if (!illegalDateString && !parseLenient) {
			int lh = image.length();
			int errInd = p.getErrorIndex();
			illegalDateString = (errInd < 0 && p.getIndex() < lh) || (errInd >= 0 && errInd < lh);
		}
		if (illegalDateString)
			throw new IllegalArgumentException("Illegal date string '" + image + "'");
		boolean contDate = ret.isSet(Calendar.DAY_OF_MONTH);
		boolean contTime = ret.isSet(Calendar.MINUTE);
		if (ret.isSet(Calendar.YEAR)) {
			if (!contTime)
				ret.set(Calendar.HOUR_OF_DAY, 0);
		} else {
			Calendar now = Calendar.getInstance(iLocale);
			now.setTime(new Date());
			ret.set(Calendar.YEAR, now.get(Calendar.YEAR));
			if (!contDate) {
				ret.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
				ret.set(Calendar.MONTH, now.get(Calendar.MONTH));
			}
		}
		if (!ret.isSet(Calendar.MINUTE))
			ret.set(Calendar.MINUTE, 0);
		if (!ret.isSet(Calendar.SECOND))
			ret.set(Calendar.SECOND, 0);
		try {
			ret.getTime();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Illegal date string '" + image + "': " + e.getMessage());
		}
		noDT[0] = !contDate;
		noDT[1] = !contTime;
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	public Calendar parseDateToCalWithFormat(final String image, final String format, final boolean[] noDT, final boolean parseLenient) {
		Calendar ret = Calendar.getInstance(iLocale);
		ret.setLenient(false);
		ParsePosition p = new ParsePosition(0);
		ret.clear();
		SimpleDateFormat sdf = new SimpleDateFormat(format, iLocale);
		sdf.parse(image, ret, p);
		boolean contDate = ret.isSet(Calendar.YEAR) || ret.isSet(Calendar.MONTH) || ret.isSet(Calendar.DAY_OF_MONTH);
		boolean contTime = ret.isSet(Calendar.HOUR_OF_DAY) || ret.isSet(Calendar.HOUR) || ret.isSet(Calendar.MINUTE)
				|| ret.isSet(Calendar.SECOND);
		boolean illegalDateString = !contDate && !contTime;
		if (!illegalDateString && !parseLenient) {
			int lh = image.length();
			int errInd = p.getErrorIndex();
			illegalDateString = (errInd < 0 && p.getIndex() < lh) || (errInd >= 0 && errInd < lh);
		}
		if (illegalDateString)
			throw new IllegalArgumentException("Illegal date string '" + image + "' for format '" + format + "'");
		//		System.out.println("Y=" + ret.isSet(Calendar.YEAR) + " M=" + ret.isSet(Calendar.MONTH) + " D=" + ret.isSet(Calendar.DAY_OF_MONTH)
		//				+ " H=" + ret.isSet(Calendar.HOUR_OF_DAY) + "m=" + ret.isSet(Calendar.MINUTE) + " S=" + ret.isSet(Calendar.SECOND));
		if (!ret.isSet(Calendar.YEAR))
			ret.set(Calendar.YEAR, 1970);
		if (!ret.isSet(Calendar.MONTH))
			ret.set(Calendar.MONTH, 0);
		if (!ret.isSet(Calendar.DAY_OF_MONTH))
			ret.set(Calendar.DAY_OF_MONTH, 1);
		if (!ret.isSet(Calendar.HOUR_OF_DAY) && !ret.isSet(Calendar.HOUR))
			ret.set(Calendar.HOUR_OF_DAY, 0);
		if (!ret.isSet(Calendar.MINUTE))
			ret.set(Calendar.MINUTE, 0);
		if (!ret.isSet(Calendar.SECOND))
			ret.set(Calendar.SECOND, 1);
		try {
			ret.getTime();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Parsing '" + image + "' against '" + format + "' gives Calendar exception: "
					+ e.getMessage());
		}
		noDT[0] = !contDate;
		noDT[1] = !contTime;
		return ret;
	}

	/*----------------------------------------------------------------------------*/
	public Number parseNumber(final String image) {
		return parseNumber(image, false);
	}

	public Number parseNumber(String image, final boolean lenient) {
		image = image.trim();
		if (!image.isEmpty()) {
			NumberFormat nf = NumberFormat.getNumberInstance(iLocale);
			ParsePosition p = new ParsePosition(0);
			Number ret = nf.parse(image, p);
			int errIndex = p.getErrorIndex();
			//System.out.println("Ind=" + index + " ErrInd=" + errIndex);
			if (errIndex == -1) {
				if (p.getIndex() >= image.length() || lenient)
					return ret;
			} else if (errIndex != 0 && lenient)
				return ret;
		}
		throw new IllegalArgumentException("Illegal number string '" + image + "'");
	}

	/*----------------------------------------------------------------------------*/
	public String formatDateTime(final ISimpleDateTime sdt) {
		return sdt.getLocalTime();
	}

	public String formatDateTime(final ISimpleDateTime sdt, final LotusDateTimeOptions ldto) {
		if (ldto.nothingSet())
			return formatDateTime(sdt);
		String notSupported = "";
		if (ldto.dOption != -1 && ldto.dOption != LotusDateTimeOptions.D_YMD)
			notSupported += "," + ldto.dOptToStr();
		if (ldto.zOption != -1)
			notSupported += "," + ldto.zOptToStr();
		if (ldto.sOption == LotusDateTimeOptions.S_DT_TY)
			notSupported += "," + ldto.sOptToStr();
		if (!notSupported.isEmpty())
			throw new UnsupportedOperationException("Not yet supported formatting option(s): " + notSupported.substring(1));
		if (ldto.sOption == LotusDateTimeOptions.S_D_ONLY || sdt.isAnyTime())
			return sdt.getDateOnly();
		Calendar cal = sdt.toJavaCal();
		if (ldto.sOption == LotusDateTimeOptions.S_T_ONLY || sdt.isAnyDate()) {
			if (ldto.tOption != LotusDateTimeOptions.T_HM)
				return sdt.getTimeOnly();
			return sdt.isAnyTime() ? "" : formatCalTimeOnly(cal, TIMEFORMAT_SHORT);
		}
		int timeFormat = (ldto.tOption == LotusDateTimeOptions.T_HM) ? TIMEFORMAT_SHORT : TIMEFORMAT_MEDIUM;
		return formatCalDateTime(cal, timeFormat);
	}

	public String formatCalDateTime(final Calendar cal) {
		return formatCalDateTime(cal, TIMEFORMAT_LONG);
	}

	public String formatCalDateTime(final Calendar cal, final int timeFormat) {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, transTimeFormat(timeFormat), iLocale);
		df.setCalendar(cal);
		return df.format(cal.getTime());
	}

	public String formatCalDateOnly(final Calendar cal) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, iLocale);
		df.setCalendar(cal);
		return df.format(cal.getTime());
	}

	public String formatCalTimeOnly(final Calendar cal) {
		return formatCalTimeOnly(cal, TIMEFORMAT_MEDIUM);
	}

	public String formatCalTimeOnly(final Calendar cal, final int timeFormat) {
		DateFormat df = DateFormat.getTimeInstance(transTimeFormat(timeFormat), iLocale);
		df.setCalendar(cal);
		return df.format(cal.getTime());
	}

	private int transTimeFormat(final int timeFormat) {
		if (timeFormat == TIMEFORMAT_MEDIUM)
			return DateFormat.MEDIUM;
		if (timeFormat == TIMEFORMAT_SHORT)
			return DateFormat.SHORT;
		return DateFormat.LONG;
	}

	public String formatDateTimeWithFormat(final ISimpleDateTime sdt, final String format) {
		Calendar cal = sdt.toJavaCal();
		if (sdt.isAnyDate() || sdt.isAnyTime()) {
			Calendar calCopy = (Calendar) cal.clone();
			if (sdt.isAnyDate()) {
				calCopy.set(Calendar.YEAR, 1970);
				calCopy.set(Calendar.MONTH, 0);
				calCopy.set(Calendar.DAY_OF_MONTH, 1);
			}
			if (sdt.isAnyTime()) {
				calCopy.set(Calendar.HOUR_OF_DAY, 0);
				calCopy.set(Calendar.MINUTE, 0);
				calCopy.set(Calendar.SECOND, 0);
			}
			cal = calCopy;
		}
		return formatCalWithFormat(cal, format);
	}

	public String formatCalWithFormat(final Calendar cal, final String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, iLocale);
		sdf.setCalendar(cal);
		return sdf.format(cal.getTime());
	}

	/*----------------------------------------------------------------------------*/
	public String formatNumber(final Number n) {
		LotusNumberOptions lno = new LotusNumberOptions();
		lno.setDefault();
		return formatNumber(n, lno);
	}

	public String formatNumber(final Number n, final LotusNumberOptions lno) {
		NumberFormat nf;
		/*
		 * It would have been more convenient to use NumberFormat.getInstance(locale, style),
		 * but this method is private in com.ibm.icu_3.8.1.v20120530.jar.
		 * (Seems to be public as of ICU 4.2.)
		 */
		if (lno.format == 'C')
			nf = NumberFormat.getCurrencyInstance(iLocale);
		else if (lno.format == 'S')
			nf = NumberFormat.getScientificInstance(iLocale);
		else if (lno.format == '%')
			nf = NumberFormat.getPercentInstance(iLocale);
		else
			nf = NumberFormat.getNumberInstance(iLocale);
		nf.setGroupingUsed(lno.useGrouping);
		nf.setMaximumIntegerDigits(1000);
		if (lno.fractionDigits != -1) {
			nf.setMinimumFractionDigits(lno.fractionDigits);
			nf.setMaximumFractionDigits(lno.fractionDigits);
		} else
			nf.setMaximumFractionDigits(1000);
		String ret = nf.format(n);
		do {
			if (lno.format != 'G' || ret.length() <= 15)
				break;
			/*
			 * In this case, Lotus implicitly switches to scientific style.
			 * When useGrouping is in effect, the limit decreases from 15 to 12 in Lotus
			 * (i.e. the grouping bytes are likewise counted), but we are not going to
			 *  imitate this strange behaviour.
			 */
			String tester = ret;
			if (lno.useGrouping) {
				nf.setGroupingUsed(false);
				tester = nf.format(n);
			}
			int minus = (tester.charAt(0) == '-') ? 1 : 0;
			int lh = tester.length();
			if (lh - minus <= 15)
				break;
			int komma = minus;
			for (; komma < lh; komma++)
				if (!Character.isDigit(tester.charAt(komma)))
					break;
			if (komma - minus <= 15)
				break;
			nf = NumberFormat.getScientificInstance(iLocale);
			nf.setGroupingUsed(lno.useGrouping);
			ret = nf.format(n);
		} while (false);
		if (lno.negativeAsParentheses && ret.charAt(0) == '-')
			ret = '(' + ret.substring(1) + ')';
		return ret;
	}
	/*----------------------------------------------------------------------------*/
}
