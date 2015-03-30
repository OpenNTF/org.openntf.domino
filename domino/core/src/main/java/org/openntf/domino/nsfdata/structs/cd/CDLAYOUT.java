package org.openntf.domino.nsfdata.structs.cd;

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.BSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The definition for a layout region on a form is stored as CD records in the $Body item of the form note. The layout region begins with a
 * CDLAYOUT record and ends with a CDLAYOUTEND record. Other records in the layout region define buttons, graphics, fields, or other rich
 * text elements. (editods.h)
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDLAYOUT extends CDRecord {
	/**
	 * These flags are set in the "Flags" field of a CDLAYOUT record, and control options for operation of the layout region.
	 * 
	 * @since Lotus Notes 4.1
	 *
	 */
	public static enum Flag {
		/**
		 * Display the border for the layout region.
		 */
		SHOWBORDER(0x00000001),
		/**
		 * Display the grid when designing the form.
		 */
		SHOWGRID(0x00000002),
		/**
		 * Force layout elements to be located on grid coordinates.
		 */
		SNAPTOGRID(0x00000004),
		/**
		 * Use 3D effects when drawing layout elements. Note: Renamed from "3DSTYLE" due to Java naming requirements
		 */
		USE3DSTYLE(0x00000008),
		/**
		 * Layout elements right to left.
		 */
		RTL(0x00000010),
		/**
		 * Do not wrap elements.
		 */
		DONTWRAP(0x00000020);

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
	public final Unsigned16 wLeft = new Unsigned16();
	public final Unsigned16 wWidth = new Unsigned16();
	public final Unsigned16 wHeight = new Unsigned16();
	/**
	 * Use getFlags for access
	 */
	@Deprecated
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned16 wGridSize = new Unsigned16();
	public final Unsigned8[] Reserved = array(new Unsigned8[14]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((int) Flags.get());
	}
}
