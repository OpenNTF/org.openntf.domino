package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ELEMENTHEADER;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A graphical element in a layout region of a form is defined by a CDLAYOUTGRAPHIC record. This record must be between a CDLAYOUT record
 * and a CDLAYOUTEND record. This record is usually followed by other CD records identifying text, graphical, or action elements associated
 * with the graphical element. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUTGRAPHIC extends CDRecord {
	/**
	 * These flags are set in the "Flags" field of a CDLAYOUTGRAPHIC record, and control options for the graphical element in the layout
	 * region.
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum Flag {
		BUTTON(0x00000001);

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
	}

	public CDLAYOUTGRAPHIC(final SIG signature, final ByteBuffer data) {
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

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": ElementHeader=" + getElementHeader() + ", Flags=" + getFlags() + "]";
	}
}
