package org.openntf.calendars;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TreeSet;

import org.openntf.domino.utils.Dates;

/**
 * Carrier for a set of Calendar objects specifying a range of time.
 */
public class CalendarSet extends TreeSet<Calendar> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Zero-Argument Constructor
	 */
	public CalendarSet() {
		super();
	}

	public CalendarSet(final Calendar first, final Calendar last) {
		super();
		this.add(first);
		this.add(last);
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
	 * More formally, returns true if and only if this set contains Calendare entry e such that (calendar==null ? calendar==null :
	 * calendar.equals(e)).
	 * 
	 * @param calendar
	 *            element whose presence in this CalendarRange is to be tested
	 * 
	 * @return Flag indicating if this CalendarRange contains the specified element.
	 */
	public boolean contains(final Calendar calendar) {
		return super.contains(calendar);
	}

	/**
	 * Returns true if this object contains a Calendar object for the specified object.
	 * 
	 * More formally, returns true if and only if for a Calendar object c constructed from object; this CalendarRange contains contains a
	 * Calendar entry e such that (c==null ? e==null : c.equals(e)).
	 * 
	 * @param object
	 *            the object from which to construct a Calendar object to test against members of this object
	 * 
	 * @return Flag indicating if this object contains a Calender object for the specified object.
	 */
	@Override
	public boolean contains(final Object object) {
		return super.contains(Dates.getCalendar(object));
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

	@Override
	public boolean add(final Calendar calendar) {
		return (null == calendar) ? false : super.add(calendar);
	}

	public boolean addObject(final Object object) {
		if (null != object) {
			final Calendar c = Dates.getCalendar(object);
			return (null == c) ? false : this.add(c);
		}

		return false;
	}

	public CalendarRange toCalendarRange() {
		return new CalendarRange(this.first(), this.last());
	}

	/**
	 * Tests a CalendarSet object to determine if there is an intersection with this object's Range.
	 * 
	 * Note this tests the RANGE encompassed by this object, not just specific entry instances.
	 * 
	 * Performs an inclusive test, the first and last entries are valid (except for null values)
	 * 
	 * @param calendarset
	 *            Object to test for intersection
	 * 
	 * @return A new CalendarRange object encompassing the intersection between this and the the specified object.
	 */
	public CalendarRange getIntersection(final CalendarSet calendarset) {
		return (null == calendarset) ? null : this.toCalendarRange().getIntersection(calendarset.toCalendarRange());
	}

	/**
	 * Tests a CalendarSet object to determine if there is an intersection with this object's Range.
	 * 
	 * Note this tests the RANGE encompassed by this object, not just specific entry instances.
	 * 
	 * Performs an inclusive test, the first and last entries are valid (except for null values)
	 * 
	 * @param calendarset
	 *            Object to test for intersection
	 * 
	 * @return Flag indicating if an intersection exists between this and the specified object.
	 */
	public boolean isIntersectionExists(final CalendarSet calendarset) {
		return (null == calendarset) ? false : this.toCalendarRange().isIntersectionExists(calendarset.toCalendarRange());
	}

}
