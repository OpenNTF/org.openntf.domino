package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record allows for additional information to be provided for a graphic. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDVERTICALALIGN extends CDRecord {
	public static enum AlignmentType {
		BASELINE, CENTER, TOP, UNUSED3, UNUSED4, BOTTOM
	}

	public final BSIG Header = inner(new BSIG());
	public final Enum16<AlignmentType> Alignment = new Enum16<AlignmentType>(AlignmentType.values());

	@Override
	public SIG getHeader() {
		return Header;
	}
}
