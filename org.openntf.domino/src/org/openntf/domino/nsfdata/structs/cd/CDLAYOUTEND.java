package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

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

	public CDLAYOUTEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}
}
