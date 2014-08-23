package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ELEMENTHEADER;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A field in a layout region of a form is defined by a CDLAYOUTFIELD record. This record must be between a CDLAYOUT record and a
 * CDLAYOUTEND record. This record is usually followed by other CD records identifying text, graphical, or action elements associated with
 * the field. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTFIELD extends CDRecord {
	/**
	 * These flags are set in the "Flags" field of a CDLAYOUTFIELD record, and control options for operation of the field in the layout
	 * region. (editods.h)
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum Flag {
		/**
		 * Draw the border with a single line.
		 */
		SINGLELINE(0x00000001),
		/**
		 * Allow vertical scrolling in the field.
		 */
		VSCROLL(0x00000002),
		/**
		 * Allow multiple selections within the field if the field is a listbox or checkbox.
		 * 
		 * Note: This must not be sampled by any design mode code. It is, in effect, "write only" for design elements. Play mode elements,
		 * on the other hand, can rely on its value.
		 */
		MULTISEL(0x00000004),
		/**
		 * Field content is static (cannot be edited).
		 */
		STATIC(0x00000008),
		/**
		 * Do not draw a border.
		 */
		NOBORDER(0x00000010),
		/**
		 * Field is displayed as an image, not as text.
		 */
		IMAGE(0x00000020),
		/**
		 * Text Left to Right
		 */
		LTR(0x01000000),
		/**
		 * Text Right to Left
		 */
		RTL(0x02000000),
		/**
		 * Display with a transparent background.
		 */
		TRANS(0x10000000),
		/**
		 * Left-justify text.
		 */
		LEFT(0x00000000),
		/**
		 * Center text.
		 */
		CENTER(0x20000000),
		/**
		 * Right-justify text.
		 */
		RIGHT(0x40000000),
		/**
		 * Center field contents vertically.
		 */
		VCENTER(0x80000000);

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

	public static enum FieldType {
		TEXT((byte) 0), CHECK((byte) 1), RADIO((byte) 2), LIST((byte) 3), COMBO((byte) 4);

		private final byte value_;

		private FieldType(final byte value) {
			value_ = value;
		}

		public byte getValue() {
			return value_;
		}

		public static FieldType valueOf(final byte typeCode) {
			for (FieldType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching FieldType found for type code " + typeCode);
		}
	}

	static {
		addFixed("ElementHeader", ELEMENTHEADER.class);
		addFixed("Flags", Integer.class);
		addFixed("bFieldType", Byte.class);
		addFixedArray("Reserved", Byte.class, 15);
	}

	public CDLAYOUTFIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public ELEMENTHEADER getElementHeader() {
		return (ELEMENTHEADER) getStructElement("ElementHeader");
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("Flags"));
	}

	public FieldType getFieldType() {
		return FieldType.valueOf((Byte) getStructElement("bFieldType"));
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}
}
