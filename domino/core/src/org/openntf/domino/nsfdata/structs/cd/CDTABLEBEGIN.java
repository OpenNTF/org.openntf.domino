package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the beginning of a table. It contains information about the format and size of the table. Use this structure
 * when accessing a table in a rich text field. As of R5, this structure is preceded by a CDPRETABLEBEGIN structure. The CDPRETABLEBEGIN
 * structure specifies additional table properties. (editods.h)
 *
 */
public class CDTABLEBEGIN extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned16 LeftMargin = new Unsigned16();
	public final Unsigned16 HorizInterCellSpace = new Unsigned16();
	public final Unsigned16 VertInterCellSpace = new Unsigned16();
	public final Unsigned16 V4HorizInterCellSpace = new Unsigned16();
	public final Unsigned16 V4VertInterCellSpace = new Unsigned16();
	// TODO make enum
	public final Unsigned16 Flags = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

}
