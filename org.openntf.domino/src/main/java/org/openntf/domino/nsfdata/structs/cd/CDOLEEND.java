package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies the end of an OLE Object in a rich text field. (editods.h)
 *
 */
public class CDOLEEND extends CDRecord {

	static {
		addFixed("Flags", Integer.class);
	}

	public static final int SIZE = getFixedStructSize();

	public CDOLEEND(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDOLEEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * Currently unused, but reserve some flags
	 */
	public int getFlags() {
		return (Integer) getStructElement("Flags");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + "]";
	}
}
