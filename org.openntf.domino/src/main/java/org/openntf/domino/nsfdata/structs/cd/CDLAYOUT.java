package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

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

	static {
		addFixedUnsigned("wLeft", Short.class);
		addFixedUnsigned("wWidth", Short.class);
		addFixedUnsigned("wHeight", Short.class);
		addFixed("Flags", Integer.class);
		addFixedUnsigned("wGridSize", Short.class);
		addFixedArray("Reserved", Byte.class, 14);
	}

	public static final int SIZE = getFixedStructSize();

	public CDLAYOUT(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDLAYOUT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Left margin of the layout region in "twips"
	 */
	public int getLeft() {
		return (Integer) getStructElement("wLeft");
	}

	/**
	 * @return Width of the layout region in "twips"
	 */
	public int getWidth() {
		return (Integer) getStructElement("wWidth");
	}

	/**
	 * @return Height of the layout region in "twips"
	 */
	public int getHeight() {
		return (Integer) getStructElement("wHeight");
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("Flags"));
	}

	/**
	 * @return Spacing of grid points in "twips"
	 */
	public int getGridSize() {
		return (Integer) getStructElement("wGridSize");
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}
}
