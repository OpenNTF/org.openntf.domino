package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This doesn't appear in the API documentation, but is presumably part of Composite Apps.
 *
 */
public class CDPDEF_TYPE extends CDRecord {

	public CDPDEF_TYPE(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDPDEF_TYPE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

}
