package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Beginning header record to both a CDFRAMESET and CDFRAME record. (fsods.h)
 * 
 * @since Lotus Notes/Domino 5.0.1
 *
 */
public class CDFRAMESETHEADER extends CDRecord {

	public CDFRAMESETHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return See FRAMESETHEADER_VERSION
	 */
	public short getVersion() {
		// TODO make enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Total number of CDFRAMESET and CDFRAME recs that follow
	 */
	public int getRecCount() {
		return getData().getShort(getData().position() + 2) & 0xFFFF;
	}

	/**
	 * Reserved for future use, must be 0
	 */
	public int[] getReserved() {
		int[] result = new int[4];
		result[0] = getData().getInt(getData().position() + 4);
		result[1] = getData().getInt(getData().position() + 8);
		result[2] = getData().getInt(getData().position() + 12);
		result[3] = getData().getInt(getData().position() + 16);
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", RecCount: " + getRecCount() + "]";
	}
}
