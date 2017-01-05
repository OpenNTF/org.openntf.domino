package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies the end of a DDE link.
 * 
 * @since forever
 *
 */
public class CDDDEEND extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned32 Flags = new Unsigned32();

	@Override
	public SIG getHeader() {
		return Header;
	}

}
