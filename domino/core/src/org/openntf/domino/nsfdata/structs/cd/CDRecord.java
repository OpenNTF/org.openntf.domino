package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.AbstractStruct;
import org.openntf.domino.nsfdata.structs.SIG;

public abstract class CDRecord extends AbstractStruct {

	@Override
	public void init() {
		super.init();
		getHeader().setRecordLength(size());
	}

	// RPr: we must not overwrite the length. SIG should have parsed the correct length already
	//	@Override
	//	public void init(final ByteBuffer data) {
	//		super.init(data);
	//		getHeader().setRecordLength(data.limit() - data.position());
	//	}

	public abstract SIG getHeader();
}
