package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

public class BasicCDRecord extends CDRecord {
	private static final long serialVersionUID = 1L;

	private SIG header_;

	protected BasicCDRecord(final SIG header, final ByteBuffer data) {
		header_ = header;
	}

	@Override
	public SIG getHeader() {
		return header_;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Signature=" + getHeader() + "]";
	}
}