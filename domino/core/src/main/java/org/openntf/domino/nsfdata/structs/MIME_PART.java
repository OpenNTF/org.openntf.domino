package org.openntf.domino.nsfdata.structs;

import java.util.EnumSet;
import java.util.Set;

/**
 * The MIME_PART structure stores the mime parts for items of TYPE_MIME_PART. (mimeods.h)
 * 
 * @since Lotus Notes/Domino 5.0.7
 *
 */
public class MIME_PART extends AbstractStruct {
	public static enum PartType {
		UNDEFINED, PROLOG, BODY, EPILOG, RETRIEVE_INFO, MESSAGE
	}

	/**
	 * The action flags based on the API documentation (MIME_PART_xxx)
	 */
	public static enum Flag {
		HAS_BOUNDARY(0x00000001), HAS_HEADERS(0x00000002), BODY_IN_DBOBJECT(0x00000004), SHARED_DBOBJECT(0x00000008),
		SKIP_FOR_CONVERSION(0x00000010);

		private final int value_;

		private Flag(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final int flags) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
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

	static {
		addVariableData("MIMEData", "wByteCount");
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) dwFlags.get());
	}

	public byte[] getMIMEData() {
		return (byte[]) getVariableElement("MIMEData");
	}
}
