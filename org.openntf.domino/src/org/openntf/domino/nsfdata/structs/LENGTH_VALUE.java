package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This record contains information for storing a length to disk.
 * 
 * @author jgallagher
 *
 */
public class LENGTH_VALUE extends AbstractStruct {

	public LENGTH_VALUE(final ByteBuffer data) {
		super(data);
	}

	@Override
	public int getStructSize() {
		return 12;
	}

	/**
	 * @return See CDLENGTH_FLAGS_xxx
	 */
	public short getFlags() {
		// TODO make enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Length of the record
	 */
	public double getLength() {
		return getData().getDouble(getData().position() + 2);
	}

	/**
	 * @return See CDLENGTH_UNITS_xxx
	 */
	public byte getUnits() {
		// TODO make enum
		return getData().get(getData().position() + 10);
	}

	/**
	 * @return Reserved for future use
	 */
	public byte getReserved() {
		return getData().get(getData().position() + 11);
	}
}
