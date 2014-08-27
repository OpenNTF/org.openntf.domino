package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record allows for additional information to be provided for a graphic. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDVERTICALALIGN extends CDRecord {
	public static enum Alignment {
		BASELINE((short) 0), CENTER((short) 1), TOP((short) 2), BOTTOM((short) 5);

		private final short value_;

		private Alignment(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Alignment valueOf(final short typeCode) {
			for (Alignment type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Alignment found for type code " + typeCode);
		}
	}

	static {
		addFixed("Alignment", Short.class);
	}

	public CDVERTICALALIGN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Alignment getAlignment() {
		return Alignment.valueOf((Short) getStructElement("Alignment"));
	}
}
