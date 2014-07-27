package org.openntf.domino.nsfdata.cd;

public class WSIG extends SIG {
	private static final long serialVersionUID = 1L;

	public WSIG(final CDSignature signature, final short length) {
		super(signature, length);
	}

	@Override
	public int getSigLength() {
		return 2 + 2;
	}
}