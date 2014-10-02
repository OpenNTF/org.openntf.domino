package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * Specifies a cropping rectangle for display of graphical data. (editods.h)
 *
 */
public class CROPRECT extends AbstractStruct {

	public static final int SIZE = 8;

	static {
		addFixedUnsigned("left", Short.class);
		addFixedUnsigned("top", Short.class);
		addFixedUnsigned("right", Short.class);
		addFixedUnsigned("bottom", Short.class);
	}

	public CROPRECT() {
		super();
	}

	public CROPRECT(final ByteBuffer data) {
		super(data);
	}

	public int getLeft() {
		return (Integer) getStructElement("left");
	}

	public int getTop() {
		return (Integer) getStructElement("top");
	}

	public int getRight() {
		return (Integer) getStructElement("right");
	}

	public int getBottom() {
		return (Integer) getStructElement("bottom");
	}

	@Override
	public long getStructSize() {
		return SIZE;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Left=" + getLeft() + ", Top=" + getTop() + ", Right=" + getRight() + ", Bottom="
				+ getBottom() + "]";
	}
}
