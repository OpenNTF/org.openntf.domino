package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record identifies the paper color for a given document.
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDCOLOR extends CDRecord {
	public final COLOR_VALUE Color = inner(new COLOR_VALUE());

	public CDCOLOR(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDCOLOR(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
