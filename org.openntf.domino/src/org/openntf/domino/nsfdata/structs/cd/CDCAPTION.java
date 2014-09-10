package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record defines the properties of a caption for a grapic [sic] record. The actual caption text follows the fixed part of the
 * record.
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDCAPTION extends CDRecord {
	public static enum Position {
		BELOW_CENTER((byte) 0), MIDDLE_CENTER((byte) 1);

		private final byte value_;

		private Position(final byte value) {
			value_ = value;
		}

		public byte getValue() {
			return value_;
		}

		public static Position valueOf(final byte typeCode) {
			for (Position type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Position found for type code " + typeCode);
		}
	}

	static {
		addFixedUnsigned("wLength", Short.class);
		addFixed("Position", Byte.class);
		addFixed("FontID", FONTID.class);
		addFixed("FontColor", COLOR_VALUE.class);
		addFixedArray("Reserved", Byte.class, 11);

		addVariableAsciiString("Caption", "getCaptionLength");
	}

	public CDCAPTION(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Caption text length
	 */
	public int getCaptionLength() {
		return (Integer) getStructElement("wLength");
	}

	/**
	 * @return CAPTION_POSITION_xxx
	 */
	public Position getPosition() {
		return Position.valueOf((Byte) getStructElement("Position"));
	}

	public FONTID getFontId() {
		return (FONTID) getStructElement("FontID");
	}

	public COLOR_VALUE getFontColor() {
		return (COLOR_VALUE) getStructElement("FontColor");
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}

	public String getCaption() {
		return (String) getStructElement("Caption");
	}
}
