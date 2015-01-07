package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.AbstractStruct;
import org.openntf.domino.nsfdata.structs.SIG;

public abstract class CDRecord extends AbstractStruct {

	@Override
	public void init() {
		super.init();
		getHeader().setRecordLength(size());
	}

	@Override
	public void init(final ByteBuffer data) {
		super.init(data);
		getHeader().setRecordLength(data.limit() - data.position());
	}

	public abstract SIG getHeader();
}
