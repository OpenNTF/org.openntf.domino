package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure is placed at the start of each paragraph in a rich-text field, and specifies which CDPABDEFINITION is used as the format
 * for the paragraph. (editods.h)
 *
 */
public class CDPABREFERENCE extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned16 PABID = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}
}
