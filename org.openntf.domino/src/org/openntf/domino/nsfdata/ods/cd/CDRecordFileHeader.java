package org.openntf.domino.nsfdata.ods.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.ods.ODSUtils;

public class CDRecordFileHeader extends CDRecord {
	private static final long serialVersionUID = 1L;

	public CDRecordFileHeader(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		super(signature, data, dataLength);
	}

	public short getFileExtLen() {
		return getData().getShort(getData().position() + 0);
	}

	public int getFileDataSize() {
		return getData().getInt(getData().position() + 2);
	}

	public int getSegCount() {
		return getData().getInt(getData().position() + 6);
	}

	public int getFlags() {
		return getData().getInt(getData().position() + 10);
	}

	public int getReserved() {
		return getData().getInt(getData().position() + 14);
	}

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
