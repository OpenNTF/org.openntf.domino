package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure holds the format for character time/date strings. You set up this structure based on the time/date format you want to use.
 * Definitions for the various fields of this structure are found in TDFMT_xxx, TTFMT_xxx, TZFMT_xxx, and TSFMT_xxx. (misc.h)
 * 
 * @author jgallagher
 *
 */
public class TFMT extends AbstractStruct {

	public TFMT(final ByteBuffer data) {
		super(data);
	}

	@Override
	public int getStructSize() {
		return 4;
	}

	/**
	 * @return Date Display Format
	 */
	public byte getDate() {
		// TODO create enum, TDFMT_xxx
		return getData().get(getData().position() + 0);
	}

	/**
	 * @return Time Display Format
	 */
	public byte getTime() {
		// TODO create enum, TTFMT_xxx
		return getData().get(getData().position() + 1);
	}

	/**
	 * @return Time Zone Display Format
	 */
	public byte getZone() {
		// TODO create enum, TZFMT_xxx
		return getData().get(getData().position() + 2);
	}

	/**
	 * @return Overall Date/Time Structure
	 */
	public byte getStructure() {
		// TODO create enum, TSFMT_xxx
		return getData().get(getData().position() + 3);
	}

}
