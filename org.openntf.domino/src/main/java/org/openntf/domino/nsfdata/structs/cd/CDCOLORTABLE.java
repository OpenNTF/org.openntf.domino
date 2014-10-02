package org.openntf.domino.nsfdata.structs.cd;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * A color table is one of the optional records following a CDBITMAPHEADER record. The color table specifies the mapping between 8-bit
 * bitmap samples and 24-bit Red/Green/Blue colors. (editods.h)
 *
 */
public class CDCOLORTABLE extends CDRecord {

	public static final int SIZE = 0;

	public CDCOLORTABLE(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDCOLORTABLE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Color[] getColors() {
		// This doesn't fit into the usual pattern
		int count = (int) (getDataLength() / 3);
		Color[] result = new Color[count];
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < count; i++) {
			int r = data.get() & 0xFF;
			int g = data.get() & 0xFF;
			int b = data.get() & 0xFF;
			result[i] = new Color(r, g, b);
		}
		return result;
	}
}
