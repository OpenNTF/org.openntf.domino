package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This class appears to be undocumented. It was encountered in the "Notes Logo ..." MainNote document of CLIPART.NSF from the Nifty 50.
 * 
 * @since ???
 */
public class CDTIFF extends CDRecord {
	public final LSIG Header = inner(new LSIG());

	@Override
	public SIG getHeader() {
		return Header;
	}

}
