package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies the end of a table. Use this structure when accessing a table in a rich text field.
 *
 */
public class CDTABLEEND extends CDRecord {

	static {
		addFixed("Spare", Integer.class);
	}

	public static final int SIZE = getFixedStructSize();

	public CDTABLEEND(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDTABLEEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getSpare() {
		return (Integer) getStructElement("Spare");
	}
}
