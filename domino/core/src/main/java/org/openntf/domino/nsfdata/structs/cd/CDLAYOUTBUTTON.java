package org.openntf.domino.nsfdata.structs.cd;

import java.util.Collections;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.ELEMENTHEADER;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A button in a layout region of a form is defined by a CDLAYOUTBUTTON record. This record must be between a CDLAYOUT record and a
 * CDLAYOUTEND record. This record is usually followed by other CD records identifying text, graphical, or action elements associated with
 * the button. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTBUTTON extends CDRecord {

	public final BSIG Header = inner(new BSIG());
	public final ELEMENTHEADER ElementHeader = inner(new ELEMENTHEADER());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned8[] Reserved = array(new Unsigned8[16]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<?> getFlags() {
		return Collections.emptySet();
	}

}
