package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record describes the Row Height property for a table. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDTABLEROWHEIGHT extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned16 RowHeight = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}
}
