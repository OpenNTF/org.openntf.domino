package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * The CDTARGET structure specifies the target (ie: the frame) where a resource link hotspot is to be displayed. It is followed by variable
 * length data whose length is specified in the TargetLength member. This variable length data specifies the target frame. The format of the
 * variable length data is specified by the Flags member of the CDTARGET structure. If no flags are specified, then the data following the
 * CDTARGET record is a character string containing the name of the target frame. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDTARGET extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 TargetLength = new Unsigned16();
	// TODO make enum
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned32 Reserved = new Unsigned32();

	static {
		addVariableString("Target", "TargetLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getTarget() {
		return (String) getVariableElement("Target");
	}
}
