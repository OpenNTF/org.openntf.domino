package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the file segment data of a Cascading Style Sheet (CSS) and follows a CDFILEHEADER structure. The number of
 * segments in the file is specified in the CDFILEHEADER record. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDFILESEGMENT extends CDRecord {

	protected CDFILESEGMENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	@Override
	public int getExtraLength() {
		return getSegSize() - getDataSize();
	}

	/**
	 * @return Actual Size of image [sic] bits in bytes, ignoring any filler
	 */
	public short getDataSize() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Size of segment, is equal to or larger than DataSize if filler byte added to maintain word boundary
	 */
	public short getSegSize() {
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return Currently unused, but someday someone will be happy this is here
	 */
	public int getFlags() {
		return getData().getInt(getData().position() + 4);
	}

	/**
	 * @return Reserved for future use
	 */
	public int getReserved() {
		return getData().getInt(getData().position() + 8);
	}

	/**
	 * @return File bits for this segment
	 */
	public ByteBuffer getFileData() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 12);
		data.limit(data.position() + getDataSize());
		return data;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Data Size: " + getDataSize() + ", Seg Size: " + getSegSize() + ", Flags: "
				+ getFlags() + ", Reserved: " + getReserved() + "]";
	}
}
