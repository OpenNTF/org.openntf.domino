package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * The CD record contains Lotus Script object code. (editods.h)
 * 
 * @since Lotus Notes/Domino 4.6
 *
 */
public class CDLSOBJECT extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned32 CodeSize = new Unsigned32();
	public final Unsigned8[] Reserved = array(new Unsigned8[4]);

	static {
		addVariableData("ObjectCode", "CodeSize");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public byte[] getObjectCode() {
		return (byte[]) getVariableElement("ObjectCode");
	}
}
