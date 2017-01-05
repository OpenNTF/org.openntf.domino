package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The CDLAYOUTEND record marks the end of the elements defining a layout region within a form. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTEND extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final Unsigned8[] Reserved = array(new Unsigned8[16]);

	@Override
	public SIG getHeader() {
		return Header;
	}
}
