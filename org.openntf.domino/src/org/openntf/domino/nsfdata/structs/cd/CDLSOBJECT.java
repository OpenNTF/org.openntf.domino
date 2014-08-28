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

	static {
		addFixedUnsigned("CodeSize", Integer.class);
		addFixedArray("Reserved", Byte.class, 4);

		addVariableData("ObjectCode", "getCodeSize");
	}

	public CDLSOBJECT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getCodeSize() {
		return ((Long) getStructElement("CodeSize")).intValue();
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}

	public byte[] getObjectCode() {
		return (byte[]) getStructElement("ObjectCode");
	}
}
