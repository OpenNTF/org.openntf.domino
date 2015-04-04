package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This doesn't appear in the API documentation, but is presumably part of Composite Apps.
 *
 */
public class CDPDEF_MAIN extends CDRecord {

	public final WSIG Header = inner(new WSIG());

	@Override
	public SIG getHeader() {
		return Header;
	}
}
