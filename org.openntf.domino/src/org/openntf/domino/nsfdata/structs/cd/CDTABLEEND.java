package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the end of a table. Use this structure when accessing a table in a rich text field.
 * 
 * @author jgallagher
 *
 */
public class CDTABLEEND extends CDRecord {

	public CDTABLEEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getSpare() {
		return getData().getInt(getData().position() + 0);
	}
}
