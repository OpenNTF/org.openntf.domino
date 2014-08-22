package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure is used to define a Cascading Style Sheet (CSS) that is part of a Domino database. CDFILESEGMENT structure(s) follow the
 * CDFILEHEADER. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDFILEHEADER extends CDRecord {

	protected CDFILEHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Length of file extenstion [sic]
	 */
	public short getFileExtLen() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Size (in bytes) of the file data
	 */
	public int getFileDataSize() {
		return getData().getInt(getData().position() + 2);
	}

	/**
	 * @return Number of CDFILESEGMENT records expected to follow
	 */
	public int getSegCount() {
		return getData().getInt(getData().position() + 6);
	}

	/**
	 * @return Flags (currently unused)
	 */
	public int getFlags() {
		return getData().getInt(getData().position() + 10);
	}

	/**
	 * @return Reserved for future use
	 */
	public int getReserved() {
		return getData().getInt(getData().position() + 14);
	}

	/**
	 * @return The file extension for the file
	 */
	public String getExtension() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 18);
		data.limit(data.position() + getFileExtLen());
		return ODSUtils.fromLMBCS(data);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", File Ext Len: " + getFileExtLen() + ", File Data Size: " + getFileDataSize()
				+ ", Seg Count: " + getSegCount() + ", Flags: " + getFlags() + ", Reserved: " + getReserved() + "]";
	}
}
