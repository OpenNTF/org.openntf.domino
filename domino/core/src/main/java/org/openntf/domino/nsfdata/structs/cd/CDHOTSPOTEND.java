package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the end of a hot region in a rich text field. There are special cases for Release 4.x and Release 5.x hotspot
 * records which have either Lotus Script or Release 4.x (or 5.x) actions associated with them. These hotspots contain a CDHOTSPOTBEGIN
 * record with the signature SIG_CD_V4HOTSPOTBEGIN (or SIG_CD_V5HOTSPOTBEGIN), and a CDHOTSPOTEND record with the signature
 * SIG_CD_V4HOTSPOTEND (or SIG_CD_V5HOTSPOTEND). (editods.h)
 *
 */
public class CDHOTSPOTEND extends CDRecord {

	public final BSIG Header = inner(new BSIG());

	@Override
	public SIG getHeader() {
		return Header;
	}

}
