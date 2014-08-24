package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.AbstractStruct;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This defines part of the structure of a font table item in a note. A font table item in a note allows rich text in the note to be
 * displayed using fonts other than those defined in FONT_FACE_xxx. (editods.h)
 *
 */
public class CDFONTTABLE extends CDRecord {
	/**
	 * This defines part of the structure of a font table item in a note. A font table item in a note allows rich text in the note to be
	 * displayed using fonts other than those defined in FONT_FACE_xxx. (editods.h)
	 *
	 */
	public static class CDFACE extends AbstractStruct {
		public static enum Pitch {
			DEFAULT((short) 0x00), FIXED((short) 0x01), VARIABLE((short) 0x02), MONO_FONT((short) 0x08);

			private final short value_;

			private Pitch(final short value) {
				value_ = value;
			}

			public short getValue() {
				return value_;
			}
		}

		public static enum Family {
			DONTCARE((short) 0x00), ROMAN((short) 0x10), SWISS((short) 0x20), MODERN((short) 0x30), SCRIPT((short) 0x40),
			DECORATIVE((short) 0x50);

			private final short value_;

			private Family(final short value) {
				value_ = value;
			}

			public short getValue() {
				return value_;
			}
		}

		public static final int MAXFACESIZE = 32;
		public static final int SIZE = 2 + MAXFACESIZE;

		public static final int TRUETYPE_FONTTYPE = 0x004;

		static {
			addFixed("Face", Byte.class);
			addFixed("Family", Byte.class);
			addFixedArray("Name", Byte.class, MAXFACESIZE);
		}

		public CDFACE(final ByteBuffer data) {
			super(data);
		}

		@Override
		public long getStructSize() {
			return SIZE;
		}

		/**
		 * @return ID number of face
		 */
		public byte getId() {
			return (Byte) getStructElement("Face");
		}

		/**
		 * @return Font Family
		 */
		public byte getFamilyField() {
			return (Byte) getStructElement("Family");
		}

		public boolean isTrueType() {
			return (getFamilyField() & TRUETYPE_FONTTYPE) > 0;
		}

		public Pitch getPitch() {
			for (Pitch pitch : Pitch.values()) {
				if ((getFamilyField() & pitch.getValue()) > 0) {
					return pitch;
				}
			}
			return null;
		}

		public Family getFamily() {
			for (Family family : Family.values()) {
				if ((getFamilyField() & family.getValue()) > 0) {
					return family;
				}
			}
			return null;
		}

		public String getName() {
			return ODSUtils.fromLMBCS((byte[]) getStructElement("Name"));
		}

		@Override
		public String toString() {
			return "[" + getClass().getSimpleName() + ": ID=" + getId() + ", TrueType=" + isTrueType() + ", Pitch=" + getPitch()
					+ ", Family=" + getFamily() + ", Name=" + getName() + "]";
		}
	}

	static {
		addFixedUnsigned("Fonts", Short.class);

		addVariableArray("FontFaces", "getFontCount", CDFACE.class);
	}

	public CDFONTTABLE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getFontCount() {
		return (Integer) getStructElement("Fonts");
	}

	public CDFACE[] getFonts() {
		return (CDFACE[]) getStructElement("FontFaces");
	}
}
