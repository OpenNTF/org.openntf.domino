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

	public CDLAYOUTEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public byte[] getReserved() {
		byte[] result = new byte[16];
		getData().duplicate().get(result, 0, 16);
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "]";
	}
}
