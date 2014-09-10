package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This datatype is used by several data structures to specify that a field contains a list of one or more values. A list is made up of one
 * or more items of a datatype, separated by a delimiter. (global.h)
 *
 */
public class LIST extends AbstractStruct {

	public static final int SIZE = 2;

	static {
		addFixedUnsigned("ListEntries", Short.class);
	}

	public LIST(final ByteBuffer data) {
		super(data);
	}

	@Override
	public long getStructSize() {
		return SIZE;
	}

	public int getListEntries() {
		return (Integer) getStructElement("ListEntries");
	}
}
