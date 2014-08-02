package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record identifies the paper color for a given document.
 * 
 * @author jgallagher
 * @since Lotus Notes/Domino 5.0
 */
public class CDCOLOR extends CDRecord {

	public CDCOLOR(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public COLOR_VALUE getColor() {
		ByteBuffer data = getData().duplicate();
		data.limit(data.position() + 4);
		return new COLOR_VALUE(data);
	}
}
