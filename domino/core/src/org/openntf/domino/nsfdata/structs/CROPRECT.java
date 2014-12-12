package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * Specifies a cropping rectangle for display of graphical data. (editods.h)
 *
 */
public class CROPRECT extends AbstractStruct {

	public final Unsigned16 left = new Unsigned16();
	public final Unsigned16 top = new Unsigned16();
	public final Unsigned16 right = new Unsigned16();
	public final Unsigned16 bottom = new Unsigned16();

	public CROPRECT() {
		super();
	}

	public CROPRECT(final ByteBuffer data) {
		super(data);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Left=" + left.get() + ", Top=" + top.get() + ", Right=" + right.get() + ", Bottom="
				+ bottom.get() + "]";
	}
}
