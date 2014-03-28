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

import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ValueHolder;

public enum DateTimeFunctions {
	;
	/*----------------------------------------------------------------------------*/
	/*
	 * @Today, @Tomorrow, @Yesterday, @Now
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atToday(final FormulaContext ctx) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atTomorrow(final FormulaContext ctx) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(0)
	public static ValueHolder atYesterday(final FormulaContext ctx) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 0, 2 })
	public static ValueHolder atNow(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Year, @Month, @Day, @Hour, @Minute, @Second, @Weekday
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atYear(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atMonth(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atDay(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atHour(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atMinute(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atSecond(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount(1)
	public static ValueHolder atWeekday(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	/*
	 * @Date, @Time, @TimeMerge
	 */
	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 6 })
	public static ValueHolder atDate(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 1, 6 })
	public static ValueHolder atTime(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
	}

	/*----------------------------------------------------------------------------*/
	@ParamCount({ 2, 3 })
	public static ValueHolder atTimeMerge(final FormulaContext ctx, final ValueHolder params[]) {
		throw new UnsupportedOperationException("Method not yet implemented");
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
}
