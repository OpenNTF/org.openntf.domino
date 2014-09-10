package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record identifies the paper color for a given document.
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDCOLOR extends CDRecord {

	static {
		addFixed("Color", COLOR_VALUE.class);
	}

	public CDCOLOR(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public COLOR_VALUE getColor() {
		return (COLOR_VALUE) getStructElement("Color");
	}
}
