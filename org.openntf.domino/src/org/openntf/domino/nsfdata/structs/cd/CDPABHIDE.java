package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This record contains the "Hide When" formula for a paragraph attributes block. (editods.h)
 * 
 * @since Lotus Notes 4.0
 *
 */
public class CDPABHIDE extends CDRecord {

	public CDPABHIDE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public short getPabId() {
		return getData().getShort(getData().position() + 0);
	}

	public byte[] getReserved() {
		byte[] result = new byte[8];
		getData().duplicate().get(result, 2, 8);
		return result;
	}

	public NSFCompiledFormula getFormula() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 10);
		return new NSFCompiledFormula(data);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": PABID=" + getPabId() + ", Formula=" + getFormula() + "]";
	}
}
