package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * Beginning header record to both a CDFRAMESET and CDFRAME record. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class CDFRAMESETHEADER extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	// TODO make enum
	public final Unsigned16 Version = new Unsigned16();
	public final Unsigned16 RecCount = new Unsigned16();
	public final Unsigned32[] Reserved = array(new Unsigned32[4]);

	@Override
	public SIG getHeader() {
		return Header;
	}
}
