package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.NOTES_COLOR;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Specifies a horizontal line (editods.h)
 * 
 * @since Lotus Notes/Domino 4.6
 *
 */
public class CDHRULE extends CDRecord {
	/**
	 * Flag values stored in the Flags field of the CDHRULE record. (editods.h)
	 * 
	 * @since Lotus Notes/Domino 4.6
	 *
	 */
	public static enum Flag {
		/**
		 * Use the color specified in the record when drawing the line.
		 */
		USECOLOR(0x00000001),
		/**
		 * Use the gradient color specified in the record when drawing the line.
		 */
		USEGRADIENT(0x00000002),
		/**
		 * Fit the horizontal length of the line to the window where the document is being displayed.
		 */
		FITTOWINDOW(0x00000004),
		/**
		 * Do not display 3D effects when drawing the line.
		 */
		NOSHADOW(0x00000008);

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

	/**
	 * Default value for Height member of the CDRULE structure in TWIPS.
	 */
	public static final short DEFAULTHRULEHEIGHT = 7;
	/**
	 * Default value for Width member of the CDRULE structure in TWIPS.
	 */
	public static final short DEFAULTHRULEWIDTH = 720;

	public CDHRULE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf(getData().getInt(getData().position() + 0));
	}

	/**
	 * Use DEFAULTHRULEWIDTH for the default.
	 * 
	 * @return The horizontal length of the line in TWIPS (see the symbolic definition for ONEINCH for more information).
	 */
	public int getWidth() {
		return getData().getShort(getData().position() + 4) & 0xFFFF;
	}

	/**
	 * Use DEFAULTHRULEHEIGHT for the default.
	 * 
	 * @return The height of the line (or thickness) in TWIPS.
	 */
	public int getHeight() {
		return getData().getShort(getData().position() + 6) & 0xFFFF;
	}

	/**
	 * @return The color used to draw the line.
	 */
	public NOTES_COLOR getColor() {
		return new NOTES_COLOR(getData().getShort(getData().position() + 8));
	}

	/**
	 * @return The gradient color used to draw the line.
	 */
	public NOTES_COLOR getGradientColor() {
		return new NOTES_COLOR(getData().getShort(getData().position() + 10));
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + ", Width=" + getWidth() + ", Height=" + getHeight()
				+ ", Color=" + getColor() + ", GradientColor=" + getGradientColor() + "]";
	}
}
