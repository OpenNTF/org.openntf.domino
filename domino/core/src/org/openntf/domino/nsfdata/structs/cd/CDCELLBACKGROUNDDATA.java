package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.Repeat;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record gives information pertaining to Background Data for a Table, specifically the 'Cell Image' repeat value. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDCELLBACKGROUNDDATA extends CDRecord {

	public static final int SIZE;

	static {
		addFixed("Repeat", Byte.class);
		addFixed("Spare", Byte.class);
		addFixed("SpareDWORD", Integer.class);

		SIZE = getFixedStructSize();
	}

	public CDCELLBACKGROUNDDATA(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDCELLBACKGROUNDDATA(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Repeat getRepeat() {
		return Repeat.valueOf((Byte) getStructElement("Repeat"));
	}
}
