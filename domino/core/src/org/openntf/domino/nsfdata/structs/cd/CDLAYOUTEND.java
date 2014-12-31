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

	public final Unsigned8[] Reserved = array(new Unsigned8[16]);

	public CDLAYOUTEND(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDLAYOUTEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
