package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.BSIG;
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

	public final BSIG Header = inner(new BSIG());
	public final Unsigned16 nFlags = new Unsigned16();
	public final Unsigned16 elemType = new Unsigned16();
	public final Unsigned32 dwReserved = new Unsigned32();

	static {
		addVariableArray("Flags", "nFlags", Integer.class);
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * @return Element these flags are for, CD_xxx_ELEMENT
	 */
	public ElemType getElemType() {
		return ElemType.valueOf((short) elemType.get());
	}

	public int[] getFlags() {
		return (int[]) getVariableElement("Flags");
	}
}
