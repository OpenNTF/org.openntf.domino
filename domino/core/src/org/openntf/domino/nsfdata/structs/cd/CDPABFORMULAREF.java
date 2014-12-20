package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Starting in Release 4.0 of Notes, a paragraph may have an associated formula that determines when the paragraph is to be hidden. The
 * CDPABFORMULAREF is similar to the CDPABREFERENCE record, with an additional field that identifies the CDPABDEFINITION record containing
 * the CDPABHIDE record for the "Hide When" formula. (editods.h)
 * 
 * @since Lotus Notes 4.0
 *
 */
public class CDPABFORMULAREF extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned16 SourcePABID = new Unsigned16();
	public final Unsigned16 DestPABID = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": SourcePabId=" + SourcePABID.get() + ", DestPabId=" + DestPABID.get() + "]";
	}
}
