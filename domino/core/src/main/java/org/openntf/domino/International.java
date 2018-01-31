/*
 * Copyright 2013
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
package org.openntf.domino;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents the international settings in the operating environment, for example, the Regional Settings in the Windows Control Panel. When
 * settings change in the operating environment, Notes recognizes the new settings immediately.
 */
public interface International
		extends Base<lotus.domino.International>, lotus.domino.International, org.openntf.domino.ext.International, SessionDescendant {

	public static class Schema extends FactorySchema<International, lotus.domino.International, Session> {
		@Override
		public Class<International> typeClass() {
			return International.class;
		}

		@Override
		public Class<lotus.domino.International> delegateClass() {
			return lotus.domino.International.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * The string that denotes AM time, for example, "AM" in English.
	 */
	@Override
	public String getAMString();

	/**
	 * Indicates the number of decimal digits for number format.
	 */
	@Override
	public int getCurrencyDigits();

	/**
	 * The symbol that indicates a number is currency, for example, the dollar sign.
	 */
	@Override
	public String getCurrencySymbol();

	/**
	 * The character used to separate months, days, and years, for example, the slash.
	 */

	@Override
	public String getDateSep();

	/**
	 * The decimal separator for number format, for example, the decimal point.
	 */
	@Override
	public String getDecimalSep();

	/**
	 * The Notes session that contains this International object.
	 */
	@Override
	public Session getParent();

	/**
	 * The string that denotes PM time, for example, "PM" in English.
	 */
	@Override
	public String getPMString();

	/**
	 * The thousands separator in number format, for example, the comma.
	 */
	@Override
	public String getThousandsSep();

	/**
	 * The character used to separate hours, minutes, and seconds, for example, the colon.
	 */
	@Override
	public String getTimeSep();

	/**
	 * The UTS/GMT value of the time zone.
	 */
	@Override
	public int getTimeZone();

	/**
	 * The string that means today in a time-date specification, for example, "Today" in English.
	 */
	@Override
	public String getToday();

	/**
	 * The string that means tomorrow in a time-date specification, for example, "Tomorrow" in English.
	 */
	@Override
	public String getTomorrow();

	/**
	 * The string that means yesterday in a time-date specification, for example, "Yesterday" in English.
	 */
	@Override
	public String getYesterday();

	/**
	 * Indicates whether the currency format has a space between the currency symbol and the number.
	 * <p>
	 * Under UNIX, this property is derived from the language installed on the machine as indicated by the LANG environment variable.
	 * </p>
	 */
	@Override
	public boolean isCurrencySpace();

	/**
	 * Indicates whether the currency symbol follows the number in the currency format.
	 */
	@Override
	public boolean isCurrencySuffix();

	/**
	 * Indicates whether fractions have a zero before the decimal point in number format.
	 * <p>
	 * Under UNIX, this property is meaningless.
	 * </p>
	 */
	@Override
	public boolean isCurrencyZero();

	/**
	 * Indicates whether the order of the date format is day-month-year.
	 */
	@Override
	public boolean isDateDMY();

	/**
	 * Indicates whether the order of the date format is month-day-year.
	 */
	@Override
	public boolean isDateMDY();

	/**
	 * Indicates whether the order of the date format is year-month-day.
	 */
	@Override
	public boolean isDateYMD();

	/**
	 * Indicates whether the time format reflects daylight savings time.
	 */
	@Override
	public boolean isDST();

	/**
	 * Indicates whether the time format is 24-hour.
	 */
	@Override
	public boolean isTime24Hour();

}
