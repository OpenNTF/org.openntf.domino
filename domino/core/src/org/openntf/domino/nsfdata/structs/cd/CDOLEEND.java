package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies the end of an OLE Object in a rich text field. (editods.h)
 *
 */
public class CDOLEEND extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned32 Flags = new Unsigned32();

	@Override
	public SIG getHeader() {
		return Header;
	}

	//	@Override
	//	public String toString() {
	//		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + "]";
	//	}
}
