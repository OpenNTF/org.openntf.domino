package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record identifies the paper color for a given document.
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDCOLOR extends CDRecord {

	public static final int SIZE;

	static {
		addFixed("Color", COLOR_VALUE.class);

		SIZE = getFixedStructSize();
	}

	public CDCOLOR(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDCOLOR(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public COLOR_VALUE getColor() {
		return (COLOR_VALUE) getStructElement("Color");
	}
}
