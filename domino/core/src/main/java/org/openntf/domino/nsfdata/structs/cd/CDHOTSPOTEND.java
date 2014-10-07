package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies the end of a hot region in a rich text field. There are special cases for Release 4.x and Release 5.x hotspot
 * records which have either Lotus Script or Release 4.x (or 5.x) actions associated with them. These hotspots contain a CDHOTSPOTBEGIN
 * record with the signature SIG_CD_V4HOTSPOTBEGIN (or SIG_CD_V5HOTSPOTBEGIN), and a CDHOTSPOTEND record with the signature
 * SIG_CD_V4HOTSPOTEND (or SIG_CD_V5HOTSPOTEND). (editods.h)
 *
 */
public class CDHOTSPOTEND extends CDRecord {

	public static final int SIZE = getFixedStructSize();

	public CDHOTSPOTEND(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDHOTSPOTEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

}
