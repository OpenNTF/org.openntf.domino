package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record describes the Row Height property for a table. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDTABLEROWHEIGHT extends CDRecord {

	static {
		addFixedUnsigned("RowHeight", Short.class);
	}

	public static final int SIZE = getFixedStructSize();

	public CDTABLEROWHEIGHT(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDTABLEROWHEIGHT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getRowHeight() {
		return (Integer) getStructElement("RowHeight");
	}
}
