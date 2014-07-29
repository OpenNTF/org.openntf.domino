package org.openntf.domino.nsfdata.structs;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MIME_PART implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MIME_PART_HAS_BOUNDARY = 0x00000001;
	public static final int MIME_PART_HAS_HEADERS = 0x00000002;
	public static final int MIME_PART_BODY_IN_DBOBJECT = 0x00000004;
	public static final int MIME_PART_SHARED_DBOBJECT = 0x00000008;
	public static final int MIME_PART_SKIP_FOR_CONVERSION = 0x00000010;

	private final ByteBuffer data_;

	public MIME_PART(final ByteBuffer data) {
		data_ = data.duplicate();
		data_.order(ByteOrder.LITTLE_ENDIAN);
	}

	public short getVersion() {
		return data_.getShort(data_.position() + 0);
	}

	public int getFlags() {
		return data_.getInt(data_.position() + 2);
	}

	public byte getPartType() {
		return data_.get(data_.position() + 6);
	}

	public byte getSpare() {
		return data_.get(data_.position() + 7);
	}

	public short getByteCount() {
		return data_.getShort(data_.position() + 8);
	}

	public short getBoundaryLen() {
		return data_.getShort(data_.position() + 10);
	}

	public short getHeadersLen() {
		return data_.getShort(data_.position() + 12);
	}

	public short getSpare2() {
		return data_.getShort(data_.position() + 14);
	}

	public int getSpare3() {
		return data_.getInt(data_.position() + 16);
	}

	public boolean hasBoundary() {
		return (getFlags() & MIME_PART_HAS_BOUNDARY) > 0;
	}

	public boolean hasHeaders() {
		return (getFlags() & MIME_PART_HAS_HEADERS) > 0;
	}

	public boolean isBodyInDBObject() {
		return (getFlags() & MIME_PART_BODY_IN_DBOBJECT) > 0;
	}

	public boolean isSkipForConversion() {
		return (getFlags() & MIME_PART_SKIP_FOR_CONVERSION) > 0;
	}
}
