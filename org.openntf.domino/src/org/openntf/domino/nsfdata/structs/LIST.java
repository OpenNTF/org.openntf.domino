package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * This datatype is used by several data structures to specify that a field contains a list of one or more values. A list is made up of one
 * or more items of a datatype, separated by a delimiter. (global.h)
 * 
 * @author jgallagher
 *
 */
public class LIST extends AbstractStruct {

	public LIST(final ByteBuffer data) {
		super(data);
	}

	@Override
	public int getStructSize() {
		return 2;
	}

	/**
	 * This is a WORD - unsigned short - so upgrade it to int for Java's sake
	 */
	public int getListEntries() {
		return getData().getShort(getData().position() + 0) & 0xFFFF;
	}
}
