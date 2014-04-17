package org.openntf.calendars;

import java.io.Serializable;
import java.util.Calendar;

import org.openntf.domino.utils.Dates;

/**
 * Carrier for a pair of two Calendar objects specifying a range of time.
 */
public class CalendarRange implements Serializable {

	private static final long serialVersionUID = 1L;

	private Calendar _alpha;
	private Calendar _omega;

	/**
	 * Zero-Argument Constructor
	 */
	public CalendarRange() {
	}

	public CalendarRange(final Calendar first, final Calendar last) {
		this.setFirst(first);
		this.setLast(last);
	}

	/*
	 * ***************************************************
	 * ***************************************************
	 * 
	 * PUBLIC Getters / Setters
	 * 
	 * ***************************************************
	 * ***************************************************
	 */

	/**
	 * @return the first date in the range
	 */
	public Calendar first() {
		return this._alpha;
	}

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
		this._alpha = Dates.getCalendar(first);
	}

	/**
	 * @return the the last date in the range
	 */
	public Calendar last() {
		return this._omega;
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
		this._omega = Dates.getCalendar(last);
	}

	/*
	 * ***************************************************
	 * ***************************************************
	 * 
	 * PUBLIC Methods
	 * 
	 * ***************************************************
	 * ***************************************************
	 */

	/**
	 * Indicates if this CalendarRange object is valid.
	 * 
	 * Rules for determing if a CalendarRange object is valid are as follows:
	 * 
	 * <ul>
	 * <li>The First calendar object in the range must not be null</li>
	 * <li>The Last calendar object in the range must not be null</li>
	 * <li>The Last calendar object may not represent a date / time which is PRIOR to the First calendar object (they may be equal)</li>
	 * </ul>
	 * 
	 * @return Flag indicating if the CalendarRange object is valid
	 * 
	 */
	public boolean isValid() {
		return ((null != this.first()) && (null != this.last()) && !Dates.isBefore(this.last(), this.first()));
	}

	/**
	 * Returns true if this set contains the specified element.
	 * 
	 * More formally, returns true if and only if this set contains Calendar entry e such that (calendar==null ? false :
	 * calendar.equals(e)).
	 * 
	 * @param calendar
	 *            element whose presence in this CalendarRange is to be tested
	 * 
	 * @return Flag indicating if this CalendarRange contains the specified element.
	 */
	public boolean contains(final Calendar calendar) {
		return (null == calendar) ? false : (calendar.equals(this.first()) || calendar.equals(this.last()));
	}

	/**
	 * Returns true if this object contains a Calendar object for the specified object.
	 * 
	 * More formally, returns true if and only if for a Calendar object o constructed from object; this CalendarRange contains contains a
	 * Calendar entry e such that (o==null ? false : o.equals(e)).
	 * 
	 * @param object
	 *            the object from which to construct a Calendar object to test against members of this object
	 * 
	 * @return Flag indicating if this object contains a Calender object for the specified object.
	 */
	public boolean contains(final Object object) {
		if (null != object) {
			final Calendar calendar = Dates.getCalendar(object);
			return (null == calendar) ? false : this.contains(calendar);
		}

		return false;
	}

	/**
	 * Tests an Object to determine if it falls within the range of Calendar entries within this object.
	 * 
	 * Returns false if the specifieds Object is null.
	 * 
	 * Performs an inclusive test, returns true if the specified Object is equal to either the first or last entry.
	 * 
	 * @param object
	 *            Object to test
	 * 
	 * @return Flag indicating if the Object falls within this object's range.
	 */
	public boolean isInRange(final Object object) {
		return Dates.isInRange(object, this.first(), this.last(), true);
	}

	/**
	 * Tests a CalendarRange object to determine if there is an intersection with this object's Range.
	 * 
	 * Note this tests the RANGE encompassed by this object, not just specific entry instances.
	 * 
	 * Performs an inclusive test, the first and last entries are valid (except for null values)
	 * 
	 * @param calendarrange
	 *            Object to test for intersection
	 * 
	 * @return A new CalendarRange object encompassing the intersection between this and the the specified object.
	 */
	public CalendarRange getIntersection(final CalendarRange cr) {
		if ((null != cr) && (null != cr.first()) && (null != cr.last()) && (null != this.first()) && (null != this.last())) {
			final Calendar first = (!Dates.isAfter(cr.first(), this.first())) ? this.first() : cr.first();
			final Calendar last = (!Dates.isAfter(cr.last(), this.last())) ? this.last() : cr.last();
			if (!Dates.isAfter(first, last)) {
				return new CalendarRange(first, last);
			}
		}

		return null;
	}

	/**
	 * Tests a CalendarRange object to determine if there is an intersection with this object's Range.
	 * 
	 * Note this tests the RANGE encompassed by this object, not just specific entry instances.
	 * 
	 * Performs an inclusive test, the first and last entries are valid (except for null values)
	 * 
	 * @param calendarrange
	 *            Object to test for intersection
	 * 
	 * @return Flag indicating if an intersection exists between this and the specified object.
	 */
	public boolean isIntersectionExists(final CalendarRange cr) {
		return (null != this.getIntersection(cr));
	}

}
