package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure holds the format for character time/date strings. You set up this structure based on the time/date format you want to use.
 * Definitions for the various fields of this structure are found in TDFMT_xxx, TTFMT_xxx, TZFMT_xxx, and TSFMT_xxx. (misc.h)
 *
 */
public class TFMT extends AbstractStruct {
	public static final int SIZE = 4;

	static {
		addFixed("Date", Byte.class);
		addFixed("Time", Byte.class);
		addFixed("Zone", Byte.class);
		addFixed("Structure", Byte.class);
	}

	public TFMT(final ByteBuffer data) {
		super(data);
	}

	@Override
	public int getStructSize() {
		return SIZE;
	}

	/**
	 * @return Date Display Format
	 */
	public byte getDate() {
		// TODO create enum, TDFMT_xxx
		return (Byte) getStructElement("Date");
	}

	/**
	 * @return Time Display Format
	 */
	public byte getTime() {
		// TODO create enum, TTFMT_xxx
		return (Byte) getStructElement("Time");
	}

	/**
	 * @return Time Zone Display Format
	 */
	public byte getZone() {
		// TODO create enum, TZFMT_xxx
		return (Byte) getStructElement("Zone");
	}

	/**
	 * @return Overall Date/Time Structure
	 */
	public byte getStructure() {
		// TODO create enum, TSFMT_xxx
		return (Byte) getStructElement("Structure");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Date: " + getDate() + ", Time: " + getTime() + ", Zone: " + getZone()
				+ ", Structure: " + getStructure() + "]";
	}
}
