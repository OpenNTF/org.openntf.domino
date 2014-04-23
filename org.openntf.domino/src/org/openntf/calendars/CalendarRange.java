package org.openntf.calendars;

import java.util.Calendar;

import org.openntf.domino.utils.Dates;

/**
 * Carrier for a pair of two Calendar objects specifying a range of time.
 */
public class CalendarRange implements CalendarRangeInterface {

	private static final long serialVersionUID = 1L;

	private Calendar _alpha;
	private Calendar _omega;

	/**
	 * Zero-Argument Constructor
	 */
	public CalendarRange() {
	}

	/**
	 * Default Constructor
	 * 
	 * @param first
	 *            The first Calendar entry for the range.
	 * @param last
	 *            The last Calendar entry for the range.
	 */
	public CalendarRange(final Calendar first, final Calendar last) {
		this.setFirst(first);
		this.setLast(last);
	}

	/*
	 * ***************************************************
	 * ***************************************************
	 * 
	 * PUBLIC Setters
	 * 
	 * ***************************************************
	 * ***************************************************
	 */

	/**
	 * @param first
	 *            the first date in the range
	 */
	public void setFirst(final Calendar first) {
		this._alpha = first;
	}

	/**
	 * @param first
	 *            the first date in the range
	 */
	public void setFirst(final Object first) {
		this.setFirst(Dates.getCalendar(first));
	}

	/**
	 * @param last
	 *            the last date in the range
	 */
	public void setLast(final Calendar last) {
		this._omega = last;
	}

	/**
	 * @param last
	 *            the last date in the range
	 */
	public void setLast(final Object last) {
		this.setLast(Dates.getCalendar(last));
	}

	/*
	 * ***************************************************
	 * ***************************************************
	 * 
	 * PRIVATE Methods
	 * 
	 * ***************************************************
	 * ***************************************************
	 */
	private void validate() {
		final Calendar alpha = this._alpha;
		final Calendar omega = this._omega;
		if ((null != alpha) && (null != omega) && Dates.isAfter(alpha, omega)) {
			final Calendar temp = Dates.getCalendar();
			temp.setTime(alpha.getTime());
			this._alpha = omega;
			this._omega = temp;
		}
	}

	/*
	 * ***************************************************
	 * ***************************************************
	 * 
	 * CalendarRangeInterface Methods
	 * 
	 * ***************************************************
	 * ***************************************************
	 */
	public Calendar first() {
		this.validate();
		return this._alpha;
	}

	public Calendar last() {
		this.validate();
		return this._omega;
	}

	public boolean isValid() {
		return ((null != this.first()) && (null != this.last()) && !Dates.isBefore(this.last(), this.first()));
	}

	public boolean contains(final Calendar calendar) {
		return (null == calendar) ? false : (calendar.equals(this.first()) || calendar.equals(this.last()));
	}

	public boolean contains(final Object object) {
		if (null != object) {
			final Calendar calendar = Dates.getCalendar(object);
			return (null == calendar) ? false : this.contains(calendar);
		}

		return false;
	}

	public boolean isInRange(final Object object) {
		return this.isInRange(object, true);
	}

	public boolean isInRange(final Object object, final boolean inclusive) {
		return Dates.isInRange(object, this.first(), this.last(), inclusive);
	}

	public CalendarRange getIntersection(final CalendarRange cr) {
		if ((null != cr) && this.isValid() && cr.isValid()) {
			final Calendar first = (this.first().equals(cr.first()) || Dates.isAfter(this.first(), cr.first())) ? this.first() : cr.first();
			final Calendar last = (this.last().equals(cr.last()) || Dates.isBefore(this.last(), cr.last())) ? this.last() : cr.last();

			return (!Dates.isAfter(first, last)) ? new CalendarRange(first, last) : null;
		}

		return null;
	}

	public boolean isIntersectionExists(final CalendarRange cr) {
		return (null != this.getIntersection(cr));
	}

	public String getHoursMinutes() {
		return (this.isValid()) ? Dates.getHoursMinutesBetween(this.first(), this.last()) : "";
	}

	public long getMinutes() {
		return (this.isValid()) ? Dates.getMinutesBetween(this.first(), this.last()) : 0;
	}

	public CalendarRange getUnion(final CalendarRange cr) {
		if ((null != cr) && this.isValid() && cr.isValid()) {
			final Calendar first = (Dates.isBefore(this.first(), cr.first())) ? this.first() : cr.first();
			final Calendar last = (Dates.isAfter(this.last(), cr.last())) ? this.last() : cr.last();

			return new CalendarRange(first, last);
		}

		return null;
	}

	public boolean isUnionExists(final CalendarRange cr) {
		return (null != this.getUnion(cr));
	}

	public CalendarRange toCalendarRange() {
		final CalendarRange result = new CalendarRange();
		result.setFirst(this.first());
		result.setLast(this.last());
		return result;
	}

}
