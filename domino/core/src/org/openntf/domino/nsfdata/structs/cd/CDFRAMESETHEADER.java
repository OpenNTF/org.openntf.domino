package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Beginning header record to both a CDFRAMESET and CDFRAME record. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class CDFRAMESETHEADER extends CDRecord {

	// TODO make enum
	public final Unsigned16 Version = new Unsigned16();
	public final Unsigned16 RecCount = new Unsigned16();
	public final Unsigned32[] Reserved = array(new Unsigned32[4]);

	public CDFRAMESETHEADER(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDFRAMESETHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
