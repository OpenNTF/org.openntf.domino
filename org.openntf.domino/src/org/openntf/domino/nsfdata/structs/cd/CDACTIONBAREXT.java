package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record defines the Action Bar attributes. It is an extension of the CDACTIONBAR record. It is found within a $V5ACTIONS item and
 * is preceded by a CDACTIONBAR record. (actods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 */
public class CDACTIONBAREXT extends CDRecord {
	public static enum BackgroundRepeat {
		DEFAULT((short) 0),
		/**
		 * Image repeats once in upper left of action bar
		 */
		REPEATONCE((short) 1),
		/**
		 * Image repeats vertically along left of action bar
		 */
		REPEATVERT((short) 2),
		/**
		 * Image repeats horizontally along top of action bar
		 */
		REPEATHORIZ((short) 3),
		/**
		 * Image "tiles" (repeats) to fit action bar
		 */
		TILE((short) 4),
		/**
		 * Image is divided and "tiled" (repeated) to fit action bar
		 */
		CENTER_TILE((short) 5),
		/**
		 * Image is sized to fit action bar
		 */
		REPEATSIZE((short) 6),
		/**
		 * Image is centered in action bar
		 */
		REPEATCENTER((short) 7);

		private final short value_;

		private BackgroundRepeat(final short value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static BackgroundRepeat valueOf(final short typeCode) {
			for (BackgroundRepeat type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching BackgroundRepeat found for type code " + typeCode);
		}
	}

	public static enum ButtonWidth {
		/**
		 * Width is calculated based on text length and image width
		 */
		DEFAULT((byte) 0),
		/**
		 * Width is at least button background image width or wider if needed to fit text and image
		 */
		BACKGROUND((byte) 1),
		/**
		 * Width is set to value in wBtnWidthAbsolute
		 */
		ABSOLUTE((byte) 2);

		private final byte value_;

		private ButtonWidth(final byte value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static ButtonWidth valueOf(final byte typeCode) {
			for (ButtonWidth type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching ButtonWidth found for type code " + typeCode);
		}
	}

	public static enum Justify {
		LEFT((byte) 0), CENTER((byte) 1), RIGHT((byte) 2);

		private final byte value_;

		private Justify(final byte value) {
			value_ = value;
		}

		public byte getValue() {
			return value_;
		}

		public static Justify valueOf(final byte typeCode) {
			for (Justify type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Justify found for type code " + typeCode);
		}
	}

	public static enum Flag {
		/**
		 * Width style is valid
		 */
		WIDTH_STYLE_VALID(0x00000001);

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
		addFixed("BackColor", COLOR_VALUE.class);
		addFixed("LineColor", COLOR_VALUE.class);
		addFixed("FontColor", COLOR_VALUE.class);
		addFixed("ButtonColor", COLOR_VALUE.class);
		addFixed("BtnBorderDisplay", Short.class);
		addFixedUpgrade("wAppletHeight", Short.class);
		addFixed("wBarBackgroundRepeat", Short.class);
		addFixed("BtnWidthStyle", Byte.class);
		addFixed("BtnTextJustify", Byte.class);
		addFixedUpgrade("wBtnWidthAbsolute", Short.class);
		addFixedUpgrade("wBtnInternalMargin", Short.class);
		addFixed("dwFlags", Integer.class);
		addFixed("barFontID", FONTID.class);
		addFixed("barHeight", LENGTH_VALUE.class);
		addFixedArray("Spare", Integer.class, 12);
	}

	public CDACTIONBAREXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public COLOR_VALUE getBackColor() {
		return (COLOR_VALUE) getStructElement("BackColor");
	}

	public COLOR_VALUE getLineColor() {
		return (COLOR_VALUE) getStructElement("LineColor");
	}

	public COLOR_VALUE getFontColor() {
		return (COLOR_VALUE) getStructElement("FontColor");
	}

	public COLOR_VALUE getButtonColor() {
		return (COLOR_VALUE) getStructElement("ButtonColor");
	}

	public short getBtnBorderDisplay() {
		return (Short) getStructElement("BtnBorderDisplay");
	}

	/**
	 * This is always recalculated on save
	 */
	public int getAppletHeight() {
		return (Integer) getStructElement("wAppletHeight");
	}

	public BackgroundRepeat getBarBackgroundRepeat() {
		return BackgroundRepeat.valueOf((Short) getStructElement("wBarBackgroundRepeat"));
	}

	public ButtonWidth getBtnWidthStyle() {
		return ButtonWidth.valueOf((Byte) getStructElement("BtnWidthStyle"));
	}

	public Justify getBtnTextJustify() {
		return Justify.valueOf((Byte) getStructElement("BtnTextJustify"));
	}

	/**
	 * Valid only if BtnWidthStyle is ACTIONBAR_BUTTON_WIDTH_ABSOLUTE
	 */
	public int getBtnWidthAbsolute() {
		return (Integer) getStructElement("wBtnWidthAbsolute");
	}

	/**
	 * @return Extra margin on the inside right and left edges of a button to space image and text away from the right and left edges
	 */
	public int getBtnInternalMargin() {
		return (Integer) getStructElement("wBtnInternalMargin");
	}

	/**
	 * @return See ACTIONBAREXT_xxx flags
	 */
	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("dwFlags"));
	}

	/**
	 * @return Used in conjunction with barHeight
	 */
	public FONTID getBarFontId() {
		return (FONTID) getStructElement("barFontID");
	}

	public LENGTH_VALUE getBarHeight() {
		return (LENGTH_VALUE) getStructElement("barHeight");
	}

	/**
	 * @return Leaving many spares for future mouse down/ mouse over colors and whatever else we want
	 */
	public int[] getSpare() {
		return (int[]) getStructElement("Spare");
	}

	@Override
	public String toString() {
		return buildDebugString();
	}
}
