package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

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

	static {
		addFixedUnsigned("TargetLength", Short.class);
		addFixed("Flags", Short.class);
		addFixed("Reserved", Integer.class);

		addVariableString("Target", "TargetLength");
	}

	public CDTARGET(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public short getFlags() {
		// TODO make enum
		return (Short) getStructElement("Flags");
	}

	public String getTarget() {
		return (String) getStructElement("Target");
	}
}
