package org.openntf.domino.nsfdata.structs;

import org.openntf.domino.nsfdata.structs.cd.CDSignature;

/**
 * Every CD record begins with a header. There are three types of headers, BSIG, WSIG, and LSIG. The first byte of the header is a signature
 * which identifies the type of the header and the type of the CD record that follows. (ods.h)
 *
 */
public class BSIG extends SIG {
	private static final long serialVersionUID = 1L;

	public static final int SIZE = 2;

	public BSIG(final CDSignature signature, final int length) {
		super(signature, length);
	}

	@Override
	public int getSigLength() {
		return SIZE;	// length is the high-order byte
	}

	@Override
	public byte[] getBytes() {
		byte[] result = getSignature().getBytes();
		result[1] = (byte) getLength();
		return result;
	}
}
