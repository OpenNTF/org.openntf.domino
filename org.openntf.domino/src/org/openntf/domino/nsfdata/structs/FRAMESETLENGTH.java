package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * One structure for each row or column. (fsods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class FRAMESETLENGTH extends AbstractStruct {

	public FRAMESETLENGTH(final ByteBuffer data) {
		super(data);
	}

	@Override
	public int getStructSize() {
		return 2;
	}

	/**
	 * @return xxx_LengthType
	 */
	public short getType() {
		// TODO make enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return The value of the ROWS or COLS attribute
	 */
	public int getValue() {
		return getData().getShort(getData().position() + 2) & 0xFFFF;
	}
}
