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

	public final Unsigned16 PABID = new Unsigned16();
	public final Unsigned8[] Reserved = array(new Unsigned8[8]);

	static {
		addVariableData("Formula", "getFormulaLength");
	}

	public CDPABHIDE(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDPABHIDE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getFormulaLength() {
		return (int) (getDataLength() - 10);
	}

	public NSFCompiledFormula getFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("Formula"));
	}
}
