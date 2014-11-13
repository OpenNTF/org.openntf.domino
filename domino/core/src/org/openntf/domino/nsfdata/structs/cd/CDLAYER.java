package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * The definition for a layer on a form is stored as CD records in the $Body item of the form note. A layer is comprised of a Layer Object
 * Run (pointer to box that represents the layer), Box Run and Position Data. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDLAYER extends CDRecord {

	static {
		addFixedArray("Reserved", Integer.class, 4);
	}

	public static final int SIZE = getFixedStructSize();

	public CDLAYER(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDLAYER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

}
