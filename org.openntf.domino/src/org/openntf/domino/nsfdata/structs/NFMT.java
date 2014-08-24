package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure holds the format for character text number strings. You set up this structure based on the number format you want to use.
 * Definitions for the various fields of this structure are found in NFMT_xxx and NATTR_xxx. (misc.h)
 *
 */
public class NFMT extends AbstractStruct {
	public static final int SIZE = 4;

	static {
		addFixedUnsigned("Digits", Byte.class);
		addFixed("Format", Byte.class);
		addFixed("Attributes", Byte.class);
		addFixed("Unused", Byte.class);
	}

	public NFMT(final ByteBuffer data) {
		super(data);
	}

	@Override
	public long getStructSize() {
		return SIZE;
	}

	/**
	 * @return Number of decimal digits
	 */
	public short getDigits() {
		return (Short) getStructElement("Digits");
	}

	/**
	 * @return Display Format
	 */
	public byte getFormat() {
		// TODO make enum
		return (Byte) getStructElement("Format");
	}

	/**
	 * @return Display Attributes
	 */
	public byte getAttributes() {
		// TODO make enum
		return (Byte) getStructElement("Attributes");
	}

	public byte getUnused() {
		return (Byte) getStructElement("Unused");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Digits: " + getDigits() + ", Format: " + getFormat() + ", Attributes: "
				+ getAttributes() + "]";
	}
}
