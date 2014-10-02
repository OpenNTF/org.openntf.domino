/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.util.function;

import java.io.Serializable;
import java.util.Comparator;

/**
 * <p> A comparator to be used for element equality as well as for 
 *     ordering. Implementing classes should ensure that:
 *     <ul>
 *        <li> The {@link #compare compare} function is consistent with 
 *             {@link #equal(Object, Object) equal}. If two objects 
 *             {@link #compare compare} to {@code 0} then they are 
 *             {@link #equal(Object, Object) equal} and the 
 *             reciprocal is true (this ensures that sorted collections/maps
 *             do not break the general contract of their parent class based on
 *             object equal).</li>
 *        <li> The {@link #hashOf hash} function is consistent with
 *             {@link #equal(Object, Object) equal}: If two objects are equal, 
 *             they have the same hashcode (the reciprocal is not true).</li>
 *        <li> The {@code null} value is supported (even for 
 *             {@link #compare comparisons}) and the {@link #hashOf(Object)
 *             hashcode} value of {@code null} is {@code 0}.</li>
 *     </ul>
 * </p>
 * 
 * @param <T> the type of objects that may be compared for equality or order.
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 6.0, July 21, 2013
 * @see Equalities
 */
public interface Equality<T> extends EqualityComparer<T>, Comparator<T>, Serializable {

    /**
     * Returns the hash code for the specified object (consistent with 
     * {@link #equal(Object, Object)}). 
     * Two objects considered equal have the same hash code. 
     * The hash code of <code>null</code> is always <code>0</code>.
     * 
     * @param  object the object to return the hashcode for.
     * @return the hashcode for the specified object.
     */
    int hashOf(T object);

    /**
     * Indicates if the specified objects can be considered equal.
     * This methods is equivalent to {@code (compare(o1, o2) == 0)} but 
     * usually faster.
     * 
     * @param left the first object (or <code>null</code>).
     * @param right the second object (or <code>null</code>).
     * @return <code>true</code> if both objects are considered equal;
     *         <code>false</code> otherwise. 
     */
    boolean equal(T left, T right);

    /**
     * Compares the specified objects for order. Returns a negative integer, 
     * zero, or a positive integer as the first argument is less than, possibly 
     * equal to, or greater than the second. Implementation classes should 
     * ensure that comparisons with {@code null} is supported.
     * 
     * @param left the first object.
     * @param right the second object.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, possibly equal to, or greater than the second.
     */
    int compare(T left, T right);

}
