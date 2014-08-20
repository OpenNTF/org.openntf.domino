package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

public class MIME_PART extends AbstractStruct {

	public static final int MIME_PART_HAS_BOUNDARY = 0x00000001;
	public static final int MIME_PART_HAS_HEADERS = 0x00000002;
	public static final int MIME_PART_BODY_IN_DBOBJECT = 0x00000004;
	public static final int MIME_PART_SHARED_DBOBJECT = 0x00000008;
	public static final int MIME_PART_SKIP_FOR_CONVERSION = 0x00000010;

	// TODO add support for following MIME data
	public MIME_PART(final ByteBuffer data) {
		super(data);
	}

	/**
	 * @return MIME_PART Version
	 */
	public short getVersion() {
		return getData().getShort(getData().position() + 0);
	}

	public int getFlags() {
		// TODO create enum
		return getData().getInt(getData().position() + 2);
	}

	/**
	 * @return Type of MIME_PART body
	 */
	public byte getPartType() {
		// TODO create enum
		return getData().get(getData().position() + 6);
	}

	public byte getSpare() {
		return getData().get(getData().position() + 7);
	}

	/**
	 * @return Bytes of variable length part data NOT including data in DB object
	 */
	public short getByteCount() {
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return Length of the boundary string
	 */
	public short getBoundaryLen() {
		return getData().getShort(getData().position() + 10);
	}

	/**
	 * @return Length of the headers
	 */
	public short getHeadersLen() {
		return getData().getShort(getData().position() + 12);
	}

	public short getSpare2() {
		return getData().getShort(getData().position() + 14);
	}

	public int getSpare3() {
		return getData().getInt(getData().position() + 16);
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

	@Override
	public int getStructSize() {
		return 20;
	}
}
