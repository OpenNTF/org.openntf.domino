package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This structure is used to specify the position in which a bitmap is painted. (global.h)
 *
 */
public class RECTSIZE extends AbstractStruct {
	public final Unsigned16 width = new Unsigned16();
	public final Unsigned16 height = new Unsigned16();

	public RECTSIZE() {
		super();
	}

	public RECTSIZE(final ByteBuffer data) {
		super(data);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Width=" + width.get() + ", Height=" + height.get() + "]";
	}
}
