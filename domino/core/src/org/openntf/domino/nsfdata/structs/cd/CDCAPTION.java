package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record defines the properties of a caption for a grapic [sic] record. The actual caption text follows the fixed part of the
 * record.
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDCAPTION extends CDRecord {
	public static enum CaptionPosition {
		BELOW_CENTER, MIDDLE_CENTER
	}

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 wLength = new Unsigned16();
	public final Enum8<CaptionPosition> Position = new Enum8<CaptionPosition>(CaptionPosition.values());
	public final FONTID FontID = inner(new FONTID());
	public final COLOR_VALUE FontColor = inner(new COLOR_VALUE());
	public final Unsigned8[] Reserved = array(new Unsigned8[11]);

	static {
		addVariableAsciiString("Caption", "wLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getCaption() {
		return (String) getVariableElement("Caption");
	}
}
