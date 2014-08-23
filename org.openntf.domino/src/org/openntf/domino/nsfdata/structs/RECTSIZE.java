package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure is used to specify the position in which a bitmap is painted. (global.h)
 *
 */
public class RECTSIZE extends AbstractStruct {
	public static final int SIZE = 4;

	static {
		addFixedUpgrade("width", Short.class);
		addFixedUpgrade("height", Short.class);
	}

	public RECTSIZE(final ByteBuffer data) {
		super(data);
	}

	public int getWidth() {
		return (Integer) getStructElement("width");
	}

	public int getHeight() {
		return (Integer) getStructElement("height");
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
