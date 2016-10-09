package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record identifies the paper color for a given document.
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDCOLOR extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final COLOR_VALUE Color = inner(new COLOR_VALUE());

	@Override
	public SIG getHeader() {
		return Header;
	}
}
