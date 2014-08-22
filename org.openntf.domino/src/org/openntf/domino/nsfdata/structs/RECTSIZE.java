package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure is used to specify the position in which a bitmap is painted. (global.h)
 *
 */
public class RECTSIZE extends AbstractStruct {
	public static final int SIZE = 4;

	public RECTSIZE(final ByteBuffer data) {
		super(data);
	}

	public int getWidth() {
		return getData().getShort(getData().position() + 0) & 0xFFFF;
	}

	public int getHeight() {
		return getData().getShort(getData().position() + 2) & 0xFFFF;
	}

	@Override
	public int getStructSize() {
		return SIZE;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Width=" + getWidth() + ", Height=" + getHeight() + "]";
	}
}
