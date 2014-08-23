package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The designer of a form or view may define custom actions for that form or view. The attributes for the button bar are stored in the
 * CDACTIONBAR record in the $ACTIONS and/or $V5ACTIONS item for the design note describing the form or view. (actods.h)
 * 
 * @since Lotus Notes/Domino 4.5
 */
public class CDACTIONBAR extends CDRecord {
	public static enum LineStyle {
		/**
		 * CDACTIONBAR dividing line height of 1
		 */
		SINGLE((short) 1),
		/**
		 * CDACTIONBAR dividing line height of 2
		 */
		DOUBLE((short) 2),
		/**
		 * CDACTIONBAR dividing line height of 3
		 */
		TRIPLE((short) 3),
		/**
		 * CDACTIONBAR dividing line height of 2 separate lines
		 */
		TWO((short) 4);

		private final short value_;

		private LineStyle(final short value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static LineStyle valueOf(final short styleCode) {
			for (LineStyle style : values()) {
				if (style.getValue() == styleCode) {
					return style;
				}
			}
			throw new IllegalArgumentException("No matching LineStyle found for type code " + styleCode);
		}
	}

	public static enum BorderStyle {
		/**
		 * No border selected
		 */
		NONE((short) 0),
		/**
		 * Maximum border selected within window size
		 */
		MAX((short) 1),
		/**
		 * Border under buttons only
		 */
		VAR((short) 2),
		/**
		 * Border calculated with Border Width
		 */
		ABS((short) 3);

		private final short value_;

		private BorderStyle(final short value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static BorderStyle valueOf(final short styleCode) {
			for (BorderStyle style : values()) {
				if (style.getValue() == styleCode) {
					return style;
				}
			}
			throw new IllegalArgumentException("No matching BorderStyle found for type code " + styleCode);
		}
	}

	public static enum Flag {
		NO_SYS_COLOR(0x00000001),
		/**
		 * Right justify buttons
		 */
		ALIGN_RIGHT(0x00000002),
		/**
		 * Buttons are transparent
		 */
		TRANS_BUTTONS(0x00000004),
		/**
		 * Buttons use system color
		 */
		SYS_BUTTONS(0x00000008),
		/**
		 * Image resource used for button background
		 */
		BTNBCK_IMGRSRC(0x00000010),
		/**
		 * Image resource used for bar background
		 */
		BARBCK_IMGRSRC(0x00000020),
		/**
		 * Use the Padding setting instead of default 2 pixels
		 */
		SET_PADDING(0x00000040),
		/**
		 * Use applet in browser
		 */
		USE_APPLET(0x00000080),
		/**
		 * Use Height setting instead of default ICON_DEFAULT_HEIGHT
		 */
		SET_HEIGHT(0x00000100),
		/**
		 * if ACTION_BAR_FLAG_SET_HEIGHT, use absolute height spec'd by user
		 */
		ABSOLUTE_HEIGHT(0x00000200),
		/**
		 * if ACTION_BAR_FLAG_SET_HEIGHT, use background image's height
		 */
		BACKGROUND_HEIGHT(0x00000400),
		/**
		 * Use Width setting instead of default width
		 */
		SET_WIDTH(0x00000800),
		/**
		 * if ACTION_BAR_FLAG_SET_WIDTH, use background image's width
		 */
		BACKGROUND_WIDTH(0x00001000),
		/**
		 * Always show the drop down hinky if a button has a menu no matter what the border style is
		 */
		SHOW_HINKY_ALWAYS(0x00002000),
		/**
		 * suppress the system actions in the right click pop-up (views only)
		 */
		SUPPRESS_SYS_POPUPS(0x00004000),
		/**
		 * use a JS (dojo) control to render the action bar on the web
		 * 
		 * @since IBM Lotus Notes/Domino 8.5.0
		 */
		USE_JSCONTROL(0x00008000),
		/**
		 * use a custom js control to render the action bar on the web
		 * 
		 * @since IBM Lotus Notes/Domino 8.5.0
		 */
		USE_CUSTOMJS(0x00010000);

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

	public CDACTIONBAR(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Background color index
	 */
	public short getBackColor() {
		// TODO map to color?
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Line color index
	 */
	public short getLineColor() {
		// TODO map to color?
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return Style of line
	 */
	public LineStyle getLineStyle() {
		return LineStyle.valueOf(getData().getShort(getData().position() + 4));
	}

	/**
	 * @return Border style
	 */
	public BorderStyle getBorderStyle() {
		return BorderStyle.valueOf(getData().getShort(getData().position() + 6));
	}

	/**
	 * @return Border width (twips)
	 */
	public short getBorderWidth() {
		return getData().getShort(getData().position() + 8);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf(getData().getInt(getData().position() + 10));
	}

	/**
	 * @return ID of Shared Action
	 */
	public int getShareId() {
		return getData().getInt(getData().position() + 14);
	}

	public int getFontId() {
		// TODO map to font
		return getData().getInt(getData().position() + 18);
	}

	/**
	 * @return Height of the Button
	 */
	public short getBtnHeight() {
		return getData().getShort(getData().position() + 22);
	}

	/**
	 * @return Height spacing
	 */
	public short getHeightSpc() {
		return getData().getShort(getData().position() + 24);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", LineStyle: " + getLineStyle() + ", BorderStyle: " + getBorderStyle() + ", Flags: "
				+ getFlags() + "]";
	}
}
