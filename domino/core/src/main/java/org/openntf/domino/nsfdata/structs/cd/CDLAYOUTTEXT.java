package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ELEMENTHEADER;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * A text element in a layout region of a form is defined by a CDLAYOUTTEXT record. This record must be between a CDLAYOUT record and a
 * CDLAYOUTEND record. This record is usually followed by other CD records identifying text, graphical, or action elements associated with
 * the element. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTTEXT extends CDRecord {
	/**
	 * These flags are set in the "Flags" field of a CDLAYOUTTEXT record, and control operation of the field in the layout region.
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum Flag {
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
		 * Mask used to obtain only the text alignment bits.
		 */
		ALIGN_MASK(0x60000000),
		/**
		 * Center field contents vertically.
		 */
		VCENTER(0x80000000),
		/**
		 * Left to right order text.
		 */
		LTR(0x01000000),
		/**
		 * Right to left order text.
		 */
		RTL(0x02000000);

		/**
		 * Read only mask.
		 */
		public static final int RO_MASK = 0x03000000;
		/**
		 * Mask used to obtain only the valid text layout flag bits.
		 */
		public static final int FLAGS_MASK = 0xF0000000;

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

	static {
		addFixed("ElementHeader", ELEMENTHEADER.class);
		addFixed("Flags", Integer.class);
		addFixedArray("Reserved", Byte.class, 16);

		addVariableString("Text", "getTextSize");
	}

	public static final int SIZE = getFixedStructSize();

	public CDLAYOUTTEXT(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDLAYOUTTEXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public ELEMENTHEADER getElementHeader() {
		return (ELEMENTHEADER) getStructElement("ElementHeader");
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("Flags"));
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}

	public int getTextSize() {
		return (int) (getDataLength() - ELEMENTHEADER.SIZE - 20);
	}

	/**
	 * This record may be followed by 8-bit text data, if the record was created by an early Test Build of Notes Release 4.0. Normally,
	 * other CD records will follow containing the text and graphical elements associated with this text element.
	 * 
	 * @since Lotus Notes 4.0 Beta Build 134
	 * @deprecated Only applies to records created by early Test Builds of Lotus Notes 4.0
	 */
	@Deprecated
	public String getText() {
		return (String) getStructElement("Text");
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": ElementHeader=" + getElementHeader() + ", Flags=" + getFlags() + ", Text=" + getText()
				+ "]";
	}
}
