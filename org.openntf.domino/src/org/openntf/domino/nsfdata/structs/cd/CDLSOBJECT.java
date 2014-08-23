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

	public CDLSOBJECT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public long getCodeSize() {
		return getData().getInt(getData().position() + 0) & 0xFFFFFFFF;
	}

	public byte[] getReserved() {
		byte[] result = new byte[4];
		getData().duplicate().get(result, 4, 4);
		return result;
	}

	public byte[] getObjectCode() {
		int length = getDataLength() - 8;
		byte[] result = new byte[length];
		getData().duplicate().get(result, 8, length);
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": CodeSize=" + getCodeSize() + "]";
	}
}
