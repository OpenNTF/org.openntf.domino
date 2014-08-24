package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record gives information pertaining to Background Data for a Table, specifically the 'Cell Image' repeat value. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDCELLBACKGROUNDDATA extends CDRecord {

	static {
		addFixed("Repeat", Byte.class);
		addFixed("Spare", Byte.class);
		addFixed("SpareDWORD", Integer.class);
	}

	public CDCELLBACKGROUNDDATA(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public byte getRepeat() {
		// TODO make enum
		return (Byte) getStructElement("Repeat");
	}
}
