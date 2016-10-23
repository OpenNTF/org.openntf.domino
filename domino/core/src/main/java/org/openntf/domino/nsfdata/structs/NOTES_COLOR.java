package org.openntf.domino.nsfdata.structs;

public class NOTES_COLOR {

	/**
	 * These symbols are used to specify text color, graphic color and background color in a variety of C API structures. (colorid.h)
	 *
	 */
	public static enum StandardColor {
		BLACK((short) 0), WHITE((short) 1), RED((short) 2), GREEN((short) 3), BLUE((short) 4), MAGENTA((short) 5), YELLOW((short) 6),
		CYAN((short) 7), DKRED((short) 8), DKGREEN((short) 9), DKBLUE((short) 10), DKMAGENTA((short) 11), DKYELLOW((short) 12),
		DKCYAN((short) 13), GRAY((short) 14), LTGRAY((short) 15);

		private final short value_;

		private StandardColor(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static StandardColor valueOf(final short typeCode) {
			for (StandardColor type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching StandardColor found for type code " + typeCode);
		}
	}

	private final short value_;

	public NOTES_COLOR(final short value) {
		value_ = value;
	}

	public StandardColor getStandardColor() {
		try {
			return StandardColor.valueOf(value_);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public boolean isStandard() {
		return getStandardColor() != null;
	}

	public boolean isPassThrough() {
		return value_ == -1;
	}

	public short getValue() {
		return value_;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": value=" + value_ + ", StandardColor=" + getStandardColor() + "]";
	}
}
