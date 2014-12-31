package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The CD record contains Lotus Script object code. (editods.h)
 * 
 * @since Lotus Notes/Domino 4.6
 *
 */
public class CDLSOBJECT extends CDRecord {

	public final Unsigned32 CodeSize = new Unsigned32();
	public final Unsigned8[] Reserved = array(new Unsigned8[4]);

	static {
		addVariableData("ObjectCode", "CodeSize");
	}

	public CDLSOBJECT(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDLSOBJECT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public byte[] getObjectCode() {
		return (byte[]) getVariableElement("ObjectCode");
	}
}
