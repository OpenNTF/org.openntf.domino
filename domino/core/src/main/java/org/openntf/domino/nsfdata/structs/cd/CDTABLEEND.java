package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the end of a table. Use this structure when accessing a table in a rich text field.
 *
 */
public class CDTABLEEND extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned32 Spare = new Unsigned32();

	@Override
	public SIG getHeader() {
		return Header;
	}
}
