package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the cell of a table. Use this structure when accessing a table in a rich text field. (editods.h)
 *
 */
public class CDTABLECELL extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned8 Row = new Unsigned8();
	public final Unsigned8 Column = new Unsigned8();
	public final Unsigned16 LeftMargin = new Unsigned16();
	public final Unsigned16 RightMargin = new Unsigned16();
	public final Unsigned16 FractionalWidth = new Unsigned16();
	// TODO make enum
	public final Unsigned8 Border = new Unsigned8();
	// TODO make enum
	public final Unsigned8 Flags = new Unsigned8();
	// TODO make enum
	public final Unsigned16 v42Border = new Unsigned16();
	public final Unsigned8 RowSpan = new Unsigned8();
	public final Unsigned8 ColumnSpan = new Unsigned8();
	public final Unsigned16 BackgroundColor = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

}
