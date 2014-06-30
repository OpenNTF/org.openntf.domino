package org.openntf.calendars;

import java.util.Calendar;
import java.util.Set;

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
		if (null == first) {
			this._alpha = null;
		} else {
			this.setFirst(Dates.getCalendar(first));
		}
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
		if (null == last) {
			this._omega = null;
		} else {
			this.setLast(Dates.getCalendar(last));
		}
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

	public void setFirstTime(final Object time) {
		if (null != time) {
			this.setFirst((null == this._alpha) ? time : Dates.getDate(this._alpha, time));
		}
	}

	public void setLastTime(final Object time) {
		if (null != time) {
			this.setLast((null == this._omega) ? time : Dates.getDate(this._omega, time));
		}
	}

	/**
	 * Validates the order of entries within this object.
	 * 
	 * If a non-null entry exists for both the first and last entries, this method verifies that the first entry represents a moment in time
	 * NOT AFTER to that of the last entry. If the first entry represents a moment AFTER that of the last entry, this method swaps them.
	 * 
	 * 
	 * @return Flag indicating if the Calendar range is Valid.
	 * 
	 * @see CalendarRange#isValid()
	 * 
	 */
	public boolean validate() {
		if (this.isValid()) {
			return true;
		}

		final Calendar alpha = this._alpha;
		final Calendar omega = this._omega;

		if ((null != alpha) && (null != omega) && Dates.isAfter(alpha, omega)) {
			final Calendar temp = alpha;
			this._alpha = omega;
			this._omega = temp;

			return this.isValid();
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
	@Override
	public boolean add(final Calendar calendar) {
		if (null != calendar) {
			if (null == this._alpha) {
				if (null == this._omega) {
					this._alpha = calendar;
					return true;
				} else {
					if (Dates.isBefore(calendar, this._omega)) {
						this._alpha = calendar;
						return true;
					}
					if (Dates.isAfter(calendar, this._omega)) {
						this._alpha = this._omega;
						this._omega = calendar;
					}

					return false;
				}
			}

			if (null == this._omega) {
				if (Dates.isAfter(calendar, this._alpha)) {
					this._omega = calendar;
					return true;
				} else if (Dates.isBefore(calendar, this._alpha)) {
					this._omega = this._alpha;
					this._alpha = calendar;
					return true;
				}
			}

			if (Dates.isBefore(calendar, this._alpha)) {
				this._alpha = calendar;
				return true;
			}

			if (Dates.isAfter(calendar, this._omega)) {
				this._omega = calendar;
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean addObject(final Object object) {
		if (null != object) {
			final Calendar c = Dates.getCalendar(object);
			return (null == c) ? false : this.add(c);
		}

		return false;
	}

	/**
	 * Adds all non-null values from set to this object.
	 * 
	 * @param set
	 *            Set from which to construct this object. All non-null values from set will be added to this object.
	 * 
	 * @return Flag indicating if this set changed as a result of the call.
	 */
	@Override
	public boolean addAll(final Set<Calendar> set) {
		if ((null == set) || set.isEmpty()) {
			return false;
		}
		boolean result = false;

		this.validate();
		for (final Calendar calendar : set) {
			final boolean temp = this.add(calendar);
			if (temp && !result) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * Gets the first entry in the range
	 * 
	 * NOTE: No guarantee is made that the first entry is PRIOR to the last entry unless {@link CalendarRange#validate()} is called
	 * immediately prior calling this method.
	 * 
	 * @return the first Calendar entry in the object
	 */
	@Override
	public Calendar first() {
		// this.validate();
		return this._alpha;
	}

	/**
	 * Gets the last entry in the range
	 * 
	 * NOTE: No guarantee is made that the last entry is AFTER the first entry unless {@link CalendarRange#validate()} is called immediately
	 * prior calling this method.
	 * 
	 * @return the last Calendar entry in the object
	 */
	@Override
	public Calendar last() {
		// this.validate();
		return this._omega;
	}

	@Override
	public boolean isValid() {
		return ((null != this.first()) && (null != this.last()) && !Dates.isBefore(this.last(), this.first()));
	}

	@Override
	public boolean contains(final Calendar calendar) {
		return (null == calendar) ? false : (calendar.equals(this.first()) || calendar.equals(this.last()));
	}

	@Override
	public boolean contains(final Object object) {
		if (null != object) {
			final Calendar calendar = Dates.getCalendar(object);
			return (null == calendar) ? false : this.contains(calendar);
		}

		return false;
	}

	@Override
	public boolean isInRange(final Object object) {
		return this.isInRange(object, true);
	}

	@Override
	public boolean isInRange(final Object object, final boolean inclusive) {
		return Dates.isInRange(object, this.first(), this.last(), inclusive);
	}

	@Override
	public CalendarRange getIntersection(final CalendarRange cr) {
		if ((null != cr) && this.isValid() && cr.isValid()) {
			final Calendar first = (this.first().equals(cr.first()) || Dates.isAfter(this.first(), cr.first())) ? this.first() : cr.first();
			final Calendar last = (this.last().equals(cr.last()) || Dates.isBefore(this.last(), cr.last())) ? this.last() : cr.last();

			return (!Dates.isAfter(first, last)) ? new CalendarRange(first, last) : null;
		}

		return null;
	}

	@Override
	public boolean isIntersectionExists(final CalendarRange cr) {
		return (null != this.getIntersection(cr));
	}

	@Override
	public String getHoursMinutes() {
		return (this.isValid()) ? Dates.getHoursMinutesBetween(this.first(), this.last()) : "";
	}

	@Override
	public long getMinutes() {
		return (this.isValid()) ? Dates.getMinutesBetween(this.first(), this.last()) : 0;
	}

	@Override
	public CalendarRange getUnion(final CalendarRange cr) {
		if ((null != cr) && this.isValid() && cr.isValid()) {
			final Calendar first = (Dates.isBefore(this.first(), cr.first())) ? this.first() : cr.first();
			final Calendar last = (Dates.isAfter(this.last(), cr.last())) ? this.last() : cr.last();

			return new CalendarRange(first, last);
		}

		return null;
	}

	@Override
	public boolean isUnionExists(final CalendarRange cr) {
		return (null != this.getUnion(cr));
	}

	@Override
	public CalendarRange toCalendarRange() {
		final CalendarRange result = new CalendarRange();
		result.setFirst(this.first());
		result.setLast(this.last());
		return result;
	}

}
