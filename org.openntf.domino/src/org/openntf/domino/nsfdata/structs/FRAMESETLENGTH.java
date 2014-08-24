package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * One structure for each row or column. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class FRAMESETLENGTH extends AbstractStruct {
	public static enum LengthType {
		PIXELS((short) 1), PERCENTAGE((short) 2), RELATIVE((short) 3);

		private final short value_;

		private LengthType(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static LengthType valueOf(final short typeCode) {
			for (LengthType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching LengthType found for type code " + typeCode);
		}
	}

	public static final int SIZE = 4;

	static {
		addFixed("Type", Short.class);
		addFixedUnsigned("Value", Short.class);
	}

	public FRAMESETLENGTH(final ByteBuffer data) {
		super(data);
	}

	@Override
	public long getStructSize() {
		return SIZE;
	}

	/**
	 * @return xxx_LengthType
	 */
	public LengthType getType() {
		return LengthType.valueOf((Short) getStructElement("Type"));
	}

	/**
	 * @return The value of the ROWS or COLS attribute
	 */
	public int getValue() {
		return (Integer) getStructElement("Value");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Type=" + getType() + ", Value=" + getValue() + "]";
	}
}
