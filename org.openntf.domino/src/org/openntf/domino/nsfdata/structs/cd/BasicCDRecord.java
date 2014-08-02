package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

public class BasicCDRecord extends CDRecord {
	private static final long serialVersionUID = 1L;

	protected BasicCDRecord(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
