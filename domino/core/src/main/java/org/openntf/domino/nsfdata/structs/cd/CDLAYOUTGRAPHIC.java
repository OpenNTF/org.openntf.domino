package org.openntf.domino.nsfdata.structs.cd;

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.BSIG;
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

	public final BSIG Header = inner(new BSIG());
	public final ELEMENTHEADER ElementHeader = inner(new ELEMENTHEADER());
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned8[] Reserved = array(new Unsigned8[16]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}
}
