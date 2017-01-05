package org.openntf.calendars;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Interface for a pair of two Calendar objects specifying a range of time.
 */
public interface CalendarRangeInterface extends Serializable {
	/**
	 * Gets the first entry in the range
	 * 
	 * @return the first Calendar entry in the object
	 */
	public Calendar first();

	/**
	 * Gets the last entry in the range
	 * 
	 * @return the the last Calendar entry in the object.
	 */
	public Calendar last();

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
	public boolean isValid();

	/**
	 * Adds the specified Calendar to this object.
	 * 
	 * @param calendar
	 *            Calendar object to add. Null values will not be added.
	 * 
	 * @return Flag indicating if calendar was added to this object.
	 */
	public boolean add(final Calendar calendar);

	/**
	 * Adds the specified Calendar-convertable to this object.
	 * 
	 * @param object
	 *            Calendar-convertable object to add. Null values and Objects which cannot be converted to a Calendar object will not be
	 *            added.
	 * 
	 * @return Flag indicating if object was added to this object.
	 */
	public boolean addObject(final Object object);

	/**
	 * Adds all non-null values from set to this object.
	 * 
	 * @param set
	 *            Set from which to construct this object. All non-null values from set will be added to this object.
	 * 
	 * @return Flag indicating if this set changed as a result of the call.
	 */
	public boolean addAll(final Set<Calendar> set);

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
	public boolean contains(final Calendar calendar);

	/**
	 * Returns true if this object contains a Calendar-Covertable object for the specified object.
	 * 
	 * A Calendar-Convertable object is any object which can be converted to a Calendar.
	 * 
	 * More formally, returns true if and only if for a Calendar object o constructed from object; this CalendarRange contains contains a
	 * Calendar entry e such that (o==null ? false : o.equals(e)).
	 * 
	 * @param object
	 *            the object from which to construct a Calendar object to test against members of this object
	 * 
	 * @return Flag indicating if this object contains a Calender object for the specified object.
	 */
	public boolean contains(final Object object);

	/**
	 * Tests a Calendar-Convertable Object to determine if it falls within the range of Calendar entries within this object.
	 * 
	 * A Calendar-Convertable object is any object which can be converted to a Calendar.
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
	public boolean isInRange(final Object object);

	/**
	 * Tests a Calendar-Convertable Object to determine if it falls within the range of Calendar entries within this object.
	 * 
	 * A Calendar-Convertable object is any object which can be converted to a Calendar.
	 * 
	 * Returns false if the specifieds Object is null.
	 * 
	 * Conditionally performs an inclusive test.
	 * 
	 * @param object
	 *            Object to test
	 * 
	 * @param inclusive
	 *            Flag indicating to include begin and end as valid dates for checking.
	 * 
	 * @return Flag indicating if the Object falls within this object's range.
	 */
	public boolean isInRange(final Object object, final boolean inclusive);

	/**
	 * Tests a CalendarRange object to determine if there is an intersection with this object's Range.
	 * 
	 * Note this tests the RANGE encompassed by this object, not just specific entry instances.
	 * 
	 * NOTE both objects MUST be valid (non-null values for first and last entries)
	 * 
	 * Performs an inclusive test, the first and last entries included.
	 * 
	 * @param calendarrange
	 *            Object to test for intersection
	 * 
	 * @return A new CalendarRange object encompassing the intersection between this and the the specified object.
	 */
	public CalendarRange getIntersection(final CalendarRange cr);

	/**
	 * Tests a CalendarRange object to determine if there is an intersection with this object's Range.
	 * 
	 * Note this tests the RANGE encompassed by this object, not just specific entry instances.
	 * 
	 * NOTE both objects MUST be valid (non-null values for first and last entries)
	 * 
	 * Performs an inclusive test, the first and last entries included.
	 * 
	 * @param calendarrange
	 *            Object to test for intersection
	 * 
	 * @return Flag indicating if an intersection exists between this and the specified object.
	 */
	public boolean isIntersectionExists(final CalendarRange cr);

	/**
	 * Gets an Hours and Minutes String for the range specified by this object
	 * 
	 * Returns the Hours and Minutes between the first and last Calendar entries in this object.
	 * 
	 * @return String representing the Hours and Minutes, delimited ":"
	 */
	public String getHoursMinutes();

	/**
	 * Gets an ths minutes for the range specified by this object
	 * 
	 * Returns the minutes between the first and last Calendar entries in this object.
	 * 
	 * @return String representing the Hours and Minutes, delimited ":"
	 */
	public long getMinutes();

	/**
	 * Gets a Union of this object's Range and another CalendarRange.
	 * 
	 * The Union CalendarRange will include the earliest first() and the latest last() entry of the two objects
	 * 
	 * NOTE both objects MUST be valid (non-null values for first and last entries)
	 * 
	 * @param calendarrange
	 *            Object to use for union
	 * 
	 * @return A new CalendarRange object encompassing the union between this and the the specified object.
	 */
	public CalendarRange getUnion(final CalendarRange cr);

	/**
	 * Tests a CalendarRange object to determine if a Union can be created.
	 * 
	 * The Union CalendarRange will include the earliest first() and the latest last() entry of the two objects
	 * 
	 * NOTE both objects MUST be valid (non-null values for first and last entries)
	 * 
	 * @param calendarrange
	 *            Object to use for union
	 * 
	 * @return Flag indicating if a Union can be created between this and the specified CalendarEntry
	 */
	public boolean isUnionExists(final CalendarRange cr);

	/**
	 * Generates a new CalendarRange object from the existing object.
	 * 
	 * @return a new CalendarRange object using values from the existing object.
	 */
	public CalendarRange toCalendarRange();

}
