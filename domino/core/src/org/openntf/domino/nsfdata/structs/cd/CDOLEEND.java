package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the end of an OLE Object in a rich text field. (editods.h)
 *
 */
public class CDOLEEND extends CDRecord {

	public final Unsigned32 Flags = new Unsigned32();

	public CDOLEEND(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDOLEEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	//	@Override
	//	public String toString() {
	//		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + "]";
	//	}
}
