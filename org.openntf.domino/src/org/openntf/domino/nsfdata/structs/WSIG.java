package org.openntf.domino.nsfdata.structs;

import org.openntf.domino.nsfdata.structs.cd.CDSignature;

/**
 * Every CD record begins with a header. There are three types of headers, BSIG, WSIG, and LSIG. The first byte of the header is a signature
 * which identifies the type of the header and the type of the CD record that follows. (ods.h)
 * 
 * @author jgallagher
 *
 */
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