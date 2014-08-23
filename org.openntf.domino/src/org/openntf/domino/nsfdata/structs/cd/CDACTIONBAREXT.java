package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

	public CDACTIONBAREXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public COLOR_VALUE getBackColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 0);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public COLOR_VALUE getLineColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 6);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public COLOR_VALUE getFontColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 12);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public COLOR_VALUE getButtonColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 18);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public short getBtnBorderDisplay() {
		return getData().getShort(getData().position() + 24);
	}

	/**
	 * This is always recalculated on save
	 */
	public short getAppletHeight() {
		return getData().getShort(getData().position() + 26);
	}

	public BackgroundRepeat getBarBackgroundRepeat() {
		return BackgroundRepeat.valueOf(getData().getShort(getData().position() + 28));
	}

	public ButtonWidth getBtnWidthStyle() {
		return ButtonWidth.valueOf(getData().get(getData().position() + 30));
	}

	public Justify getBtnTextJustify() {
		return Justify.valueOf(getData().get(getData().position() + 31));
	}

	/**
	 * Valid only if BtnWidthStyle is ACTIONBAR_BUTTON_WIDTH_ABSOLUTE
	 */
	public short getBtnWidthAbsolute() {
		return getData().getShort(getData().position() + 32);
	}

	/**
	 * @return Extra margin on the inside right and left edges of a button to space image and text away from the right and left edges
	 */
	public short getBtnInternalMargin() {
		return getData().getShort(getData().position() + 34);
	}

	/**
	 * @return See ACTIONBAREXT_xxx flags
	 */
	public Set<Flag> getFlags() {
		return Flag.valuesOf(getData().getInt(getData().position() + 36));
	}

	/**
	 * @return Used in conjunction with barHeight
	 */
	public FONTID getBarFontId() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 40);
		data.limit(data.position() + FONTID.SIZE);
		return new FONTID(data);
	}

	public LENGTH_VALUE getBarHeight() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 40 + FONTID.SIZE);
		data.limit(data.position() + 12);
		return new LENGTH_VALUE(data);
	}

	/**
	 * @return Leaving many spares for future mouse down/ mouse over colors and whatever else we want
	 */
	public int[] getSpare() {
		int[] result = new int[12];
		for (int i = 0; i < 12; i++) {
			result[i] = getData().getInt(getData().position() + 56 + (i * 4));
		}
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": BackColor=" + getBackColor() + ", LineColor=" + getLineColor() + ", FontColor="
				+ getFontColor() + ", ButtonColor=" + getButtonColor() + ", BtnBorderDisplay=" + getBtnBorderDisplay() + ", AppletHeight="
				+ getAppletHeight() + ", BarBackgroundRepeat=" + getBarBackgroundRepeat() + ", BtnWidthStyle=" + getBtnWidthStyle()
				+ ", BtnTextJustify=" + getBtnTextJustify() + ", BtnWidthAbsolute=" + getBtnWidthAbsolute() + ", BtnInternalMargin="
				+ getBtnInternalMargin() + ", Flags=" + getFlags() + ", BarFontId=" + getBarFontId() + ", BarHeight=" + getBarHeight()
				+ "]";
	}
}
