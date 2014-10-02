package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record contains size information for a layer box. The units (pixels, twips, etc.) for the Width and Height are set in the "Units"
 * members of the "Top", "Left", "Bottom" and "Right" members of the CDPOSITIONING structure. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDBOXSIZE extends CDRecord {

	public static final int SIZE;

	static {
		addFixed("Width", LENGTH_VALUE.class);
		addFixed("Height", LENGTH_VALUE.class);
		addFixedArray("Reserved", LENGTH_VALUE.class, 4);
		addFixedArray("dwReserved", Integer.class, 4);

		SIZE = getFixedStructSize();
	}

	public CDBOXSIZE(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDBOXSIZE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public LENGTH_VALUE getWidth() {
		return (LENGTH_VALUE) getStructElement("Width");
	}

	public LENGTH_VALUE getHeight() {
		return (LENGTH_VALUE) getStructElement("Height");
	}

}
