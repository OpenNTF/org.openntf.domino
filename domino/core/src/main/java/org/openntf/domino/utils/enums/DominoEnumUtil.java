package org.openntf.domino.utils.enums;

import java.util.Collection;
import java.util.EnumSet;

/**
 * This class contains utility methods for dealing with {@link INumberEnum} Domino enums.
 *
 * @author Jesse Gallagher
 */
public final class DominoEnumUtil {
	private DominoEnumUtil() {
	}

	/**
	 * Given a Domino number-style enum, returns the enum constant for the provided <code>value</code>
	 */
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> T valueOf(final Class<T> clazz, final N value) {
		for (T enumVal : clazz.getEnumConstants()) {
			N enumValue = enumVal.getValue();
			if (enumValue.longValue() == value.longValue()) {
				return enumVal;
			}
		}
		return null;
	}

	/**
	 * Given a Domino number-style bitfield enum, returns the matching enum constants for the provided <code>value</code>
	 */
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> EnumSet<T> valuesOf(final Class<T> clazz, final N value) {
		EnumSet<T> result = EnumSet.noneOf(clazz);
		for (T enumVal : clazz.getEnumConstants()) {
			N enumValue = enumVal.getValue();
			if ((enumValue.longValue() & value.longValue()) != 0) {
				result.add(enumVal);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <N extends Number, T extends Enum<T> & INumberEnum<N>> N toBitField(final Class<T> clazz, final Collection<T> values) {
		Long result = new Long(0);
		for (T enumVal : values) {
			result |= enumVal.getValue().longValue();
		}

		// Boil the value down to the right size
		Class<N> numberClass = (Class<N>) clazz.getEnumConstants()[0].getValue().getClass();
		if (numberClass.equals(Byte.class)) {
			return (N) (new Byte(result.byteValue()));
		} else if (numberClass.equals(Short.class)) {
			return (N) (new Short(result.shortValue()));
		} else if (numberClass.equals(Integer.class)) {
			return (N) (new Integer(result.intValue()));
		} else {
			return (N) result;
		}
	}
}