package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

public class CDFILESEGMENT extends CDRecord {
	private static final long serialVersionUID = 1L;

	public CDFILESEGMENT(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		super(signature, data, dataLength);
	}

	@Override
	public int getExtraLength() {
		return getSegSize() - getDataSize();
	}

	public short getDataSize() {
		return getData().getShort(getData().position() + 0);
	}

	public short getSegSize() {
		return getData().getShort(getData().position() + 2);
	}

	public int getFlags() {
		return getData().getInt(getData().position() + 4);
	}

	public int getReserved() {
		return getData().getInt(getData().position() + 8);
	}

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
