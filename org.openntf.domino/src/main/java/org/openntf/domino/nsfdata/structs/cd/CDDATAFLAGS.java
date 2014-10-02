package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * Contains collapsible section, button type, style sheet or field limit information for Notes/Domino 6. A CD record (CDBAR, CDBUTTON,
 * CDBORDERINFO, CDFIELDHINT, etc.) may be followed by a CDDATAFLAGS structure. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 */
public class CDDATAFLAGS extends CDRecord {
	public static enum ElemType {
		SECTION((short) 128), FIELDLIMIT((short) 129), BUTTONEX((short) 130), TABLECELL((short) 131);

		private final short value_;

		private ElemType(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static ElemType valueOf(final short typeCode) {
			for (ElemType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching ElemType found for type code " + typeCode);
		}
	}

	static {
		addFixedUnsigned("nFlags", Short.class);
		addFixed("elemType", Short.class);
		addFixed("dwReserved", Integer.class);

		addVariableArray("Flags", "getNumFlags", Integer.class);
	}

	public static final int SIZE = getFixedStructSize();

	public CDDATAFLAGS(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDDATAFLAGS(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return number of flags
	 */
	public int getNumFlags() {
		return (Integer) getStructElement("nFlags");
	}

	/**
	 * @return Element these flags are for, CD_xxx_ELEMENT
	 */
	public ElemType getElemType() {
		return ElemType.valueOf((Short) getStructElement("elemType"));
	}

	/**
	 * @return Future
	 */
	public int getReserved() {
		return (Integer) getStructElement("dwReserved");
	}

	public int[] getFlags() {
		return (int[]) getStructElement("Flags");
	}
}
