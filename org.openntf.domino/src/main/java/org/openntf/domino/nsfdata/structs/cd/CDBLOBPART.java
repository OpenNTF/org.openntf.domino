package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD record is used in conjunction with CD record CDEVENT. If a CDEVENT record has an ActionType of ACTION_TYPE_JAVASCRIPT then
 * CDBLOBPART contains the JavaScript code. There may be more then one CDBLOBPART record for each CDEVENT. Therefore it may be necessary to
 * loop thorough all of the CDBLOBPART records to read in the complete JavaScript code. (editods.h)
 *
 */
public class CDBLOBPART extends CDRecord {

	public static final int SIZE;

	static {
		addFixed("OwnerSig", Short.class);
		addFixedUnsigned("Length", Short.class);
		addFixedUnsigned("BlobMax", Short.class);
		addFixedArray("Reserved", Byte.class, 8);

		addVariableData("BlobData", "getLength");

		SIZE = getFixedStructSize();
	}

	public CDBLOBPART(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDBLOBPART(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Sig of the owner of this data
	 */
	public short getOwnerSig() {
		// TODO convert to signature
		return (Short) getStructElement("OwnerSig");
	}

	/**
	 * @return Length of the data that follows
	 */
	public int getLength() {
		return (Integer) getStructElement("Length");
	}

	/**
	 * @return Block size used by the writer of this blob
	 */
	public int getBlobMax() {
		return (Integer) getStructElement("BlobMax");
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}

	public byte[] getBlobData() {
		return (byte[]) getStructElement("BlobData");
	}
}
