package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the end of a table. Use this structure when accessing a table in a rich text field.
 *
 */
public class CDTABLEEND extends CDRecord {

	public final Unsigned32 Spare = new Unsigned32();

	public CDTABLEEND(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDTABLEEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
