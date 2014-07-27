package org.openntf.domino.nsfdata.ods;

import org.openntf.domino.nsfdata.ods.cd.CDSignature;

public class BSIG extends SIG {
	private static final long serialVersionUID = 1L;

	public BSIG(final CDSignature signature, final int length) {
		super(signature, length);
	}

	@Override
	public int getSigLength() {
		return 2;	// length is the high-order byte
	}
}
