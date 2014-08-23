package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the end of an OLE Object in a rich text field. (editods.h)
 *
 */
public class CDOLEEND extends CDRecord {

	public CDOLEEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * Currently unused, but reserve some flags
	 */
	public int getFlags() {
		return getData().getInt(getData().position() + 0);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + "]";
	}
}
