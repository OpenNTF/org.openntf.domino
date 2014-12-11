package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record is used in conjunction with CD record CDEVENT. If a CDEVENT record has an ActionType of ACTION_TYPE_JAVASCRIPT then
 * CDBLOBPART contains the JavaScript code. There may be more then one CDBLOBPART record for each CDEVENT. Therefore it may be necessary to
 * loop thorough all of the CDBLOBPART records to read in the complete JavaScript code. (editods.h)
 *
 */
public class CDBLOBPART extends CDRecord {
	public final Unsigned16 OwnerSig = new Unsigned16();
	public final Unsigned16 Length = new Unsigned16();
	public final Unsigned16 BlobMax = new Unsigned16();
	public final Unsigned8[] Reserved = array(new Unsigned8[8]);

	static {
		addVariableData("BlobData", "Length");
	}

	public CDBLOBPART(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDBLOBPART(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public byte[] getBlobData() {
		return (byte[]) getVariableElement("BlobData");
	}
}
