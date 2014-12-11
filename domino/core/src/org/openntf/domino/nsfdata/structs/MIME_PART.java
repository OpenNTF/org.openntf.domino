package org.openntf.domino.nsfdata.structs;

import java.nio.ByteBuffer;

/**
 * The MIME_PART structure stores the mime parts for items of TYPE_MIME_PART. (mimeods.h)
 * 
 * @since Lotus Notes/Domino 5.0.7
 *
 */
public class MIME_PART extends AbstractStruct {

	public static final int MIME_PART_HAS_BOUNDARY = 0x00000001;
	public static final int MIME_PART_HAS_HEADERS = 0x00000002;
	public static final int MIME_PART_BODY_IN_DBOBJECT = 0x00000004;
	public static final int MIME_PART_SHARED_DBOBJECT = 0x00000008;
	public static final int MIME_PART_SKIP_FOR_CONVERSION = 0x00000010;

	public static enum PartType {
		UNDEFINED, PROLOG, BODY, EPILOG, RETRIEVE_INFO, MESSAGE
	}

	public final Unsigned16 wVersion = new Unsigned16();
	public final Unsigned32 dwFlags = new Unsigned32();
	public final Enum8<PartType> cPartType = new Enum8<PartType>(PartType.values());
	public final Unsigned8 cSpare = new Unsigned8();
	/**
	 * Bytes of variable length part data NOT including data in DB object
	 */
	public final Unsigned16 wByteCount = new Unsigned16();
	/**
	 * Length of the boundary string
	 */
	public final Unsigned16 wBoundaryLen = new Unsigned16();
	/**
	 * Length of the headers
	 */
	public final Unsigned16 wHeadersLen = new Unsigned16();
	public final Unsigned16 wSpare = new Unsigned16();
	public final Unsigned32 dwSpare = new Unsigned32();

	public MIME_PART() {
		super();
	}

	// TODO add support for following MIME data
	public MIME_PART(final ByteBuffer data) {
		super(data);
	}

	public long getFlags() {
		// TODO create enum
		return dwFlags.get();
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
