package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.RECTSIZE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A rich text field may contain a bitmap image. There are three types, monochrome, 8-bit mapped color, and 16-bit color; a gray scale
 * bitmap is stored as an 8-bit color bitmap with a color table having entries [0, 0, 0], [1, 1, 1], . . . , [255, 255, 255]. All bitmaps
 * are stored as a single plane (some graphics devices support multiple planes). (editods.h)
 *
 */
public class CDBITMAPHEADER extends CDRecord {
	/**
	 * Option flags set in the CDBITMAPHEADER record
	 *
	 */
	public static enum Flag {
		/**
		 * Bitmap uses >16 colors or >4 grey scale levels
		 */
		REQUIRES_PALETTE((short) 1),
		/**
		 * Initialized by import code for "first time" importing of bitmaps from clipboard or file, to tell Notes that it should compute
		 * whether or not to use a color palette or not. All imports and API programs should initially set this bit to let the Editor
		 * compute whether it needs the palette or not.
		 */
		COMPUTE_PALETTE((short) 2);

		private final short value_;

		private Flag(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final short flags) {
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
		addFixed("Dest", RECTSIZE.class);
		addFixed("Crop", RECTSIZE.class);
		addFixed("Flags", Short.class);
		addFixed("wReserved", Short.class);
		addFixed("lReserved", Integer.class);
		addFixedUnsigned("Width", Short.class);
		addFixedUnsigned("Height", Short.class);
		addFixedUnsigned("BitsPerPixel", Short.class);
		addFixedUnsigned("SamplesPerPixel", Short.class);
		addFixedUnsigned("BitsPerSample", Short.class);
		addFixedUnsigned("SegmentCount", Short.class);
		addFixedUnsigned("ColorCount", Short.class);
		addFixedUnsigned("PatternCount", Short.class);
	}

	public CDBITMAPHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Dest bitmap height and width in pixels
	 */
	public RECTSIZE getDest() {
		return (RECTSIZE) getStructElement("Dest");
	}

	/**
	 * @return Crop destination dimensions (UNUSED)
	 */
	public RECTSIZE getCrop() {
		return (RECTSIZE) getStructElement("Crop");
	}

	/**
	 * If CDBITMAP_FLAG_REQUIRES_PALETTE is set, the color table is required
	 * 
	 * @return CDBITMAP_FLAGS (version 2 and later)
	 */
	public Set<Flag> getFlags() {
		return Flag.valuesOf((Short) getStructElement("Flags"));
	}

	/**
	 * Reserved for future use
	 */
	public short getReserved() {
		return (Short) getStructElement("wReserved");
	}

	/**
	 * Reserved for future use
	 */
	public int getReserved2() {
		return (Integer) getStructElement("lReserved");
	}

	/**
	 * @return Width of bitmap in pixels
	 */
	public int getWidth() {
		return (Integer) getStructElement("Width");
	}

	/**
	 * @return Height of bitmap in pixels
	 */
	public int getHeight() {
		return (Integer) getStructElement("Height");
	}

	/**
	 * @return Bits per pixel - must be 1, 8, or 16
	 */
	public int getBitsPerPixel() {
		return (Integer) getStructElement("BitsPerPixel");
	}

	/**
	 * @return For 1 or 8 bits per pixel, this is set to 1; for 16 bits per pixel, 3
	 */
	public int getSamplesPerPixel() {
		return (Integer) getStructElement("SamplesPerPixel");
	}

	/**
	 * @return For 1 bit per pixel, this is set to 1; for 8 bits, 8; for 16 bits, 5
	 */
	public int getBitsPerSample() {
		return (Integer) getStructElement("BitsPerSample");
	}

	/**
	 * @return Number of CDBITMAPSEGMENT records
	 */
	public int getSegmentCount() {
		return (Integer) getStructElement("SegmentCount");
	}

	/**
	 * @return Number of entries in the CDCOLORTABLE record (0-256)
	 */
	public int getColorCount() {
		return (Integer) getStructElement("ColorCount");
	}

	/**
	 * @return Number of entries in the CDPATTERNTABLE (0-64)
	 */
	public int getPatternCount() {
		return (Integer) getStructElement("PatternCount");
	}
}
