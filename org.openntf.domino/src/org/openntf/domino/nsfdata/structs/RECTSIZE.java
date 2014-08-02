package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure is used to specify the position in which a bitmap is painted. (global.h)
 * 
 * @author jgallagher
 *
 */
public class RECTSIZE extends AbstractStruct {

	public RECTSIZE(final ByteBuffer data) {
		super(data);
	}

	public short getWidth() {
		return getData().getShort(getData().position() + 0);
	}

	public short getHeight() {
		return getData().getShort(getData().position() + 2);
	}

	@Override
	public int getStructSize() {
		return 4;
	}
}
