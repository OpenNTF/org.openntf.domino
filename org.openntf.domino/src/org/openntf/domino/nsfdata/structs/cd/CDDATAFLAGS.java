package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

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

	public CDDATAFLAGS(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return number of flags
	 */
	public short getNumFlags() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Element these flags are for, CD_xxx_ELEMENT
	 */
	public ElemType getElemType() {
		return ElemType.valueOf(getData().getShort(getData().position() + 2));
	}

	/**
	 * @return Future
	 */
	public int getReserved() {
		return getData().getInt(getData().position() + 4);
	}

	public int[] getFlags() {
		int[] result = new int[getNumFlags()];
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 8);
		for (int i = 0; i < getNumFlags(); i++) {
			result[i] = data.getInt();
		}
		return result;
	}
}
