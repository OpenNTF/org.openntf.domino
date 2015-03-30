package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This record contains the "Hide When" formula for a paragraph attributes block. (editods.h)
 * 
 * @since Lotus Notes 4.0
 *
 */
public class CDPABHIDE extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 PABID = new Unsigned16();
	public final Unsigned8[] Reserved = array(new Unsigned8[8]);

	static {
		addVariableData("Formula", "getFormulaLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public int getFormulaLength() {
		return (int) (Header.getRecordLength() - size());
	}

	public NSFCompiledFormula getFormula() {
		return new NSFCompiledFormula((byte[]) getVariableElement("Formula"));
	}
}
