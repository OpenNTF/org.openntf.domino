package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * Specifies a cropping rectangle for display of graphical data. (editods.h)
 * 
 * @author jgallagher
 *
 */
public class CROPRECT extends AbstractStruct {

	public CROPRECT(final ByteBuffer data) {
		super(data);
	}

	public short getLeft() {
		return getData().getShort(getData().position() + 0);
	}

	public short getTop() {
		return getData().getShort(getData().position() + 2);
	}

	public short getRight() {
		return getData().getShort(getData().position() + 4);
	}

	public short getBottom() {
		return getData().getShort(getData().position() + 6);
	}

	@Override
	public int getStructSize() {
		return 8;
	}
}
