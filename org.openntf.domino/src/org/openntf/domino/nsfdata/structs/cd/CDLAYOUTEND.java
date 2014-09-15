package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * The CDLAYOUTEND record marks the end of the elements defining a layout region within a form. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTEND extends CDRecord {

	static {
		addFixedArray("Reserved", Byte.class, 16);
	}

	public static final int SIZE = getFixedStructSize();

	public CDLAYOUTEND(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDLAYOUTEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}
}
