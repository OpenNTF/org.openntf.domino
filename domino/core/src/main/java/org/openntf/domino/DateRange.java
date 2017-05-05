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

import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents a range of dates and times.
 * <h3>Notable enhancements and changes</h3>
 * <ul>
 * <li>Get a list of days in the range using {@link org.openntf.domino.ext.DateRange#getDays()}</li>
 * <li>Get Java Date instances using {@link org.openntf.domino.impl.DateRange#getStartDate()} and
 * {@link org.openntf.domino.impl.DateRange#getEndDate()}</li>
 * </ul>
 * <h5>Creation</h5>
 * <p>
 * To create a new <code>DateRange</code> object, use {@link Session#createDateRange()}. To initialize the object, you can do one of the
 * following:
 * </p>
 * <ul>
 * <li>Assign values to the {@link Session#createDateRange(java.util.Date, java.util.Date)} parameters.</li>
 * <li>Assign to {@link #setStartDateTime(lotus.domino.DateTime)} and {@link #setEndDateTime(lotus.domino.DateTime)} DateTime values.</li>
 * <li>Assign to {@link #setText(String) Text} a string value that represents a date range. This consists of two string representations of
 * Domino-formatted date-time values separated by a hyphen (for example, "01/01/97 - 01/02/97").</li>
 * </ul>
 * <h5>Usage</h5>
 * <p>
 * When you assign {@link #setStartDateTime(lotus.domino.DateTime) StartDateTime} or {@link #setEndDateTime(lotus.domino.DateTime)
 * EndDateTime}, its value replaces the corresponding value in {@link #setText(String) Text}, and vice versa.
 * </p>
 */
public interface DateRange
		extends Base<lotus.domino.DateRange>, lotus.domino.DateRange, org.openntf.domino.ext.DateRange, Encapsulated, SessionDescendant {

	public static class Schema extends FactorySchema<DateRange, lotus.domino.DateRange, Session> {
		@Override
		public Class<DateRange> typeClass() {
			return DateRange.class;
		}

		@Override
		public Class<lotus.domino.DateRange> delegateClass() {
			return lotus.domino.DateRange.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * The ending date-time of a range.
	 * <p>
	 * Modifying the <code>DateTime</code> object on which <code>EndDateTime</code> is based implicitly modifies <code>EndDateTime</code>.
	 * This does not apply if <code>EndDateTime</code> is based on a <code>java.util.Date</code> object.
	 * </p>
	 *
	 * @return The ending date-time of a range.
	 *
	 */
	@Override
	public DateTime getEndDateTime();

	/**
	 * The session that contains a DateRange object.
	 */
	@Override
	public Session getParent();

	/**
	 * The starting date-time of a range.
	 * <p>
	 * Modifying the <code>DateTime</code> object on which <code>StartDateTime</code> is based implicitly modifies
	 * <code>StartDateTime</code>. This does not apply if <code>StartDateTime
	 * is based on a <code>java.util.Date</code> object.
	 * </p>
	 *
	 * @return The starting date-time of a range.
	 *
	 */
	@Override
	public DateTime getStartDateTime();

	/**
	 * The text associated with a range formatted as Domino date-time text.
	 */
	@Override
	public String getText();

	/**
	 * Sets the ending date-time of a range.
	 * <p>
	 * Modifying the <code>DateTime</code> object on which <code>EndDateTime</code> is based implicitly modifies <code>EndDateTime</code>.
	 * This does not apply if <code>EndDateTime</code> is based on a <code>java.util.Date</code> object.
	 * </p>
	 *
	 * @param end
	 *            The ending date-time, cannot be null
	 */
	@Override
	public void setEndDateTime(final lotus.domino.DateTime end);

	/**
	 * Sets the starting date-time of a range.
	 * <p>
	 * Modifying the <code>DateTime</code> object on which <code>StartDateTime</code> is based implicitly modifies
	 * <code>StartDateTime</code>. This does not apply if <code>StartDateTime
	 * is based on a <code>java.util.Date</code> object.
	 * </p>
	 *
	 * @param start
	 *            starting date-time, cannot be null
	 */
	@Override
	public void setStartDateTime(final lotus.domino.DateTime start);

	/**
	 * Sets the text associated with a range formatted as Domino date-time text.
	 *
	 * @param text
	 *            values separated by a hyphen (for example, "01/01/97 - 01/02/97")
	 */
	@Override
	public void setText(final String text);

}
