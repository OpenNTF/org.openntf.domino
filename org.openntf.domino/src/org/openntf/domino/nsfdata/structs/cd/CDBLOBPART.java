package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record is used in conjunction with CD record CDEVENT. If a CDEVENT record has an ActionType of ACTION_TYPE_JAVASCRIPT then
 * CDBLOBPART contains the JavaScript code. There may be more then one CDBLOBPART record for each CDEVENT. Therefore it may be necessary to
 * loop thorough all of the CDBLOBPART records to read in the complete JavaScript code. (editods.h)
 * 
 * @author jgallagher
 *
 */
public class CDBLOBPART extends CDRecord {

	public CDBLOBPART(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Sig of the owner of this data
	 */
	public short getOwnerSig() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Length of the data that follows
	 */
	public int getLength() {
		return getData().getShort(getData().position() + 2) & 0xFFFF;
	}

	/**
	 * @return Block size used by the writer of this blob
	 */
	public int getBlobMax() {
		return getData().getShort(getData().position() + 4) & 0xFFFF;
	}

	public byte[] getReserved() {
		byte[] result = new byte[8];
		for (int i = 0; i < result.length; i++) {
			result[i] = getData().get(getData().position() + 6 + i);
		}
		return result;
	}

	public ByteBuffer getBlobData() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 14);
		return data;
	}
}
