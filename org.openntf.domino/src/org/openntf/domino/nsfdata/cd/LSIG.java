package org.openntf.domino.nsfdata.cd;

public class LSIG extends SIG {
	private static final long serialVersionUID = 1L;

	public LSIG(final CDSignature signature, final int length) {
		super(signature, length);
	}

	@Override
	public int getSigLength() {
		return 2 + 4;
	}
}