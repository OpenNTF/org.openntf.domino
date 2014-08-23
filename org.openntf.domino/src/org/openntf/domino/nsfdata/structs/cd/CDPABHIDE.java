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

	static {
		addFixed("PABID", Short.class);
		addFixedArray("Reserved", Byte.class, 8);

		addVariableData("Formula", "getFormulaLength");
	}

	public CDPABHIDE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public short getPabId() {
		return (Short) getStructElement("PABID");
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}

	public int getFormulaLength() {
		return getDataLength() - 10;
	}

	public NSFCompiledFormula getFormula() {
		return new NSFCompiledFormula((byte[]) getStructElement("Formula"));
	}
}
