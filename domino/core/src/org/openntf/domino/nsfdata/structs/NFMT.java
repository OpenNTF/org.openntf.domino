package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure holds the format for character text number strings. You set up this structure based on the number format you want to use.
 * Definitions for the various fields of this structure are found in NFMT_xxx and NATTR_xxx. (misc.h)
 *
 */
public class NFMT extends AbstractStruct {
	public static enum NumFormat {
		GENERAL, FIXED, SCIENTIFIC, CURRENCY, BYTES
	}

	/**
	 * Number of decimal digits
	 */
	public final Unsigned8 Digits = new Unsigned8();
	/**
	 * Display Format
	 */
	public final Enum8<NumFormat> Format = new Enum8<NumFormat>(NumFormat.values());
	/**
	 * Display Attributes
	 */
	public final Unsigned8 Attributes = new Unsigned8();
	public final Unsigned8 Unused = new Unsigned8();

	public NFMT() {
		super();
	}

	public NFMT(final ByteBuffer data) {
		super(data);
	}

	public short getAttributes() {
		// TODO make enum
		return Attributes.get();
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Digits: " + Digits.get() + ", Format: " + Format.get() + ", Attributes: "
				+ getAttributes() + "]";
	}
}
