package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The bitmap data is divided into segments to optimize data storage within Domino. It is recommended that each segment be no larger than
 * 10k bytes. For best display speed, the segments sould be as large as possible, up to the practical 10k limit. A scanline must be
 * contained within a single segment, and cannot be divided between two segments. A bitmap must contain at least one segment, but may have
 * many segments. (editods.h)
 * 
 * @author jgallagher
 *
 */
public class CDBITMAPSEGMENT extends CDRecord {

	public CDBITMAPSEGMENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * Reserved for future use
	 */
	public int[] getReserved() {
		int[] result = new int[2];
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 0);
		result[0] = data.get();
		result[1] = data.get();
		return result;
	}

	/**
	 * @return Number of compressed scanlines in seg
	 */
	public int getScanlineCount() {
		return getData().getShort(getData().position() + 8) & 0xFFFF;
	}

	/**
	 * @return Size, in bytes, of compressed data
	 */
	public int getDataSize() {
		return getData().getShort(getData().position() + 12) & 0xFFFF;
	}

	// TODO uncompress the data (see docs)
	@Override
	public ByteBuffer getData() {
		ByteBuffer result = getData().duplicate();
		result.position(result.position() + 14);
		return result;
	}
}
