package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

public class BasicCDRecord extends CDRecord {
	private static final long serialVersionUID = 1L;

	protected BasicCDRecord(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		super(signature, data, dataLength);
	}

}
