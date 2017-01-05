package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This class appears to be undocumented. It was encountered in the "Choo-Choo Bitmap" Artwork document of CLIPART.NSF from the Nifty 50;
 * presumably, it's the predecessor format to CDBITMAPHEADER+CDBITMAPSEGMENT.
 * 
 * @since ???
 */
public class CDBITMAP extends CDRecord {
	public final WSIG Header = inner(new WSIG());

	@Override
	public SIG getHeader() {
		return Header;
	}
}
