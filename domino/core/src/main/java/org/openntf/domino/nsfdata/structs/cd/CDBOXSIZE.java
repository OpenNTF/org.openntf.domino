package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record contains size information for a layer box. The units (pixels, twips, etc.) for the Width and Height are set in the "Units"
 * members of the "Top", "Left", "Bottom" and "Right" members of the CDPOSITIONING structure. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDBOXSIZE extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final LENGTH_VALUE Width = inner(new LENGTH_VALUE());
	public final LENGTH_VALUE Height = inner(new LENGTH_VALUE());
	public final LENGTH_VALUE[] Reserved = array(new LENGTH_VALUE[4]);
	public final Unsigned32[] dwReserved = array(new Unsigned32[4]);

	@Override
	public SIG getHeader() {
		return Header;
	}

}
