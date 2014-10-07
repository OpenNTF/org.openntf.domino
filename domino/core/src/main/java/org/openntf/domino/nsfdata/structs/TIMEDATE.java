package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This is the Domino binary time/date format. This structure only should be interpreted using the C API time/date calls -- it cannot easily
 * be parsed directly. (global.h)
 *
 */
public class TIMEDATE extends AbstractStruct {
	public static final int SIZE = 8;

	static {
		addFixed("Innards1", Integer.class);
		addFixed("Innards2", Integer.class);
	}

	public TIMEDATE() {
		super();
	}

	public TIMEDATE(final ByteBuffer data) {
		super(data);
	}

	@Override
	public long getStructSize() {
		return SIZE;
	}

	public int getInnards1() {
		return (Integer) getStructElement("Innards1");
	}

	public void setInnards1(final int value) {
		setStructElement("Innards1", value);
	}

	public int getInnards2() {
		return (Integer) getStructElement("Innards2");
	}

	public void setInnards2(final int value) {
		setStructElement("Innards2", value);
	}

}
