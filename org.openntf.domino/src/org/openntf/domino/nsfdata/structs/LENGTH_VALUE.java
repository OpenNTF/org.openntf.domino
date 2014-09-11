package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This record contains information for storing a length to disk. (editods.h)
 *
 */
public class LENGTH_VALUE extends AbstractStruct {

	public static final int SIZE = 12;

	static {
		addFixed("Flags", Short.class);
		addFixed("Length", Double.class);
		addFixed("Units", Byte.class);
		addFixed("Reserved", Byte.class);
	}

	public LENGTH_VALUE() {
		super();
	}

	public LENGTH_VALUE(final ByteBuffer data) {
		super(data);
	}

	@Override
	public long getStructSize() {
		return SIZE;
	}

	/**
	 * @return See CDLENGTH_FLAGS_xxx
	 */
	public short getFlags() {
		// TODO make enum
		return (Short) getStructElement("Flags");
	}

	/**
	 * @return Length of the record
	 */
	public double getLength() {
		return (Double) getStructElement("Length");
	}

	/**
	 * @return See CDLENGTH_UNITS_xxx
	 */
	public byte getUnits() {
		// TODO make enum
		return (Byte) getStructElement("Units");
	}

	/**
	 * @return Reserved for future use
	 */
	public byte getReserved() {
		return (Byte) getStructElement("Reserved");
	}
}
