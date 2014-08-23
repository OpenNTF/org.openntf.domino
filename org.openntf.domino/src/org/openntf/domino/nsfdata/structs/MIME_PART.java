package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * The MIME_PART structure stores the mime parts for items of TYPE_MIME_PART. (mimeods.h)
 * 
 * @since Lotus Notes/Domino 5.0.7
 *
 */
public class MIME_PART extends AbstractStruct {

	public static final int SIZE = 20;

	public static final int MIME_PART_HAS_BOUNDARY = 0x00000001;
	public static final int MIME_PART_HAS_HEADERS = 0x00000002;
	public static final int MIME_PART_BODY_IN_DBOBJECT = 0x00000004;
	public static final int MIME_PART_SHARED_DBOBJECT = 0x00000008;
	public static final int MIME_PART_SKIP_FOR_CONVERSION = 0x00000010;

	static {
		addFixed("wVersion", Short.class);
		addFixed("dwFlags", Integer.class);
		addFixed("cPartType", Byte.class);
		addFixed("cSpare", Byte.class);
		addFixedUpgrade("wByteCount", Short.class);
		addFixedUpgrade("wBoundaryLen", Short.class);
		addFixedUpgrade("wHeadersLen", Short.class);
		addFixed("wSpare", Short.class);
		addFixed("dwSpare", Integer.class);
	}

	// TODO add support for following MIME data
	public MIME_PART(final ByteBuffer data) {
		super(data);
	}

	/**
	 * @return MIME_PART Version
	 */
	public short getVersion() {
		return (Short) getStructElement("wVersion");
	}

	public int getFlags() {
		// TODO create enum
		return (Integer) getStructElement("dwFlags");
	}

	/**
	 * @return Type of MIME_PART body
	 */
	public byte getPartType() {
		// TODO create enum
		return (Byte) getStructElement("cPartType");
	}

	public byte getSpare() {
		return (Byte) getStructElement("cSpare");
	}

	/**
	 * @return Bytes of variable length part data NOT including data in DB object
	 */
	public int getByteCount() {
		return (Integer) getStructElement("wByteCount");
	}

	/**
	 * @return Length of the boundary string
	 */
	public int getBoundaryLen() {
		return (Integer) getStructElement("wBoundaryLen");
	}

	/**
	 * @return Length of the headers
	 */
	public int getHeadersLen() {
		return (Integer) getStructElement("wHeadersLen");
	}

	public short getSpare2() {
		return (Short) getStructElement("wSpare");
	}

	public int getSpare3() {
		return (Integer) getStructElement("dwSpare");
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
		return SIZE;
	}
}
