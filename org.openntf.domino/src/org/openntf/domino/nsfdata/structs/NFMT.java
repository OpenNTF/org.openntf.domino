package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure holds the format for character text number strings. You set up this structure based on the number format you want to use.
 * Definitions for the various fields of this structure are found in NFMT_xxx and NATTR_xxx. (misc.h)
 * 
 * @author jgallagher
 *
 */
public class NFMT extends AbstractStruct {

	public NFMT(final ByteBuffer data) {
		super(data);
	}

	@Override
	public int getStructSize() {
		return 4;
	}

	/**
	 * @return Number of decimal digits
	 */
	public byte getDigits() {
		return getData().get(getData().position() + 0);
	}

	/**
	 * @return Display Format
	 */
	public byte getFormat() {
		return getData().get(getData().position() + 1);
	}

	/**
	 * @return Display Attributes
	 */
	public byte getAttributes() {
		return getData().get(getData().position() + 2);
	}

	public byte getUnused() {
		return getData().get(getData().position() + 3);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Digits: " + getDigits() + ", Format: " + getFormat() + ", Attributes: "
				+ getAttributes() + "]";
	}
}
