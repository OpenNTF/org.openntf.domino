package org.openntf.calendars;

import java.util.Calendar;
import java.util.TreeSet;

import org.openntf.domino.utils.Dates;

/**
 * Carrier for a set of Calendar objects specifying a range of time.
 */
public class CalendarSet extends TreeSet<Calendar> implements CalendarRangeInterface {

	private static final long serialVersionUID = 1L;

	/**
	 * Zero-Argument Constructor
	 */
	public CalendarSet() {
		super();
	}

	/**
	 * Default Constructor
	 * 
	 * @param first
	 *            The first Calendar entry for the range.
	 * @param last
	 *            The last Calendar entry for the range.
	 */
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

	/*
	 * ***************************************************
	 * ***************************************************
	 * 
	 * CalendarRangeInterface Methods
	 * 
	 * ***************************************************
	 * ***************************************************
	 */
	public boolean contains(final Calendar calendar) {
		return super.contains(calendar);
	}

	public String getHoursMinutes() {
		return this.toCalendarRange().getHoursMinutes();
	}

	public CalendarRange getIntersection(final CalendarRange cr) {
		return this.toCalendarRange().getIntersection(cr);
	}

	public long getMinutes() {
		return this.toCalendarRange().getMinutes();
	}

	public CalendarRange getUnion(final CalendarRange cr) {
		return this.toCalendarRange().getUnion(cr);
	}

	public boolean isInRange(final Object object) {
		return this.toCalendarRange().isInRange(object);
	}

	public boolean isInRange(final Object object, final boolean inclusive) {
		return this.toCalendarRange().isInRange(object, inclusive);
	}

	public boolean isIntersectionExists(final CalendarRange cr) {
		return this.toCalendarRange().isIntersectionExists(cr);
	}

	public boolean isUnionExists(final CalendarRange cr) {
		return this.toCalendarRange().isUnionExists(cr);
	}

	public boolean isValid() {
		return this.toCalendarRange().isValid();
	}

	public CalendarRange toCalendarRange() {
		return new CalendarRange(this.first(), this.last());
	}

}
