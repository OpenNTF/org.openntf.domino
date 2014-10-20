package org.openntf.domino.nsfdata.structs;

/**
 * (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public enum Repeat {
	UNKNOWN((byte) 0), ONCE((byte) 1), VERT((byte) 2), HORIZ((byte) 3), BOTH((byte) 4), SIZE((byte) 5), CENTER((byte) 6);

	private final byte value_;

	private Repeat(final byte value) {
		value_ = value;
	}

	public byte getValue() {
		return value_;
	}

	public static Repeat valueOf(final byte typeCode) {
		for (Repeat type : values()) {
			if (type.getValue() == typeCode) {
				return type;
			}
		}
		throw new IllegalArgumentException("No matching Repeat found for type code " + typeCode);
	}
}