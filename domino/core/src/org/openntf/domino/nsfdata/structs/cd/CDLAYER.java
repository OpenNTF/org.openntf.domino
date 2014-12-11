package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The definition for a layer on a form is stored as CD records in the $Body item of the form note. A layer is comprised of a Layer Object
 * Run (pointer to box that represents the layer), Box Run and Position Data. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDLAYER extends CDRecord {

	public final Unsigned32[] Reserved = array(new Unsigned32[4]);

	public CDLAYER(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDLAYER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

}
