package org.openntf.domino.utils.enums;

/**
 * This interface provides a standardized method for declaring Java enums that wrap bitmask/C-style enums.
 *
 * @author Jesse Gallagher
 */
public abstract interface INumberEnum<T extends Number> {
	/**
	 * @return the C-level value of the enum
	 */
	T getValue();
}