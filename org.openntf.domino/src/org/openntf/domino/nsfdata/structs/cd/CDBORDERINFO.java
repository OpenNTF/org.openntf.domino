package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record describes border information for a given table. This CD record will be preceded with CD record CDPRETABLEBEGIN both
 * encapsulated between a CDBEGINRECORD and a CDENDRECORD record with CD record signature CDPRETABLEBEGIN. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDBORDERINFO extends CDRecord {
	/**
	 * These contstants [sic] define the BorderStyle variable in the CD Record CDBORDERINFO (CDBORDERSTYLE_xxx).
	 */
	public static enum BorderStyle {
		/**
		 * There isn't any border style
		 */
		NONE((short) 0),
		/**
		 * Border is displayed as a solid line
		 */
		SOLID((short) 1),
		/**
		 * Border is displayed as a double line
		 */
		DOUBLE((short) 2),
		/**
		 * Border is inset
		 */
		INSET((short) 3),
		/**
		 * Border is outset
		 */
		OUTSET((short) 4),
		/**
		 * Border has a ridge
		 */
		RIDGE((short) 5),
		/**
		 * Border has a groove
		 */
		GROOVE((short) 6),
		/**
		 * Border is dotted
		 */
		DOTTED((short) 7),
		/**
		 * Border is dashed
		 */
		DASHED((short) 8),
		/**
		 * Border is displayed as a "picture frame" around table
		 */
		PICTURE((short) 9),
		/**
		 * Border is a graphic
		 */
		GRAPHIC((short) 10);

		private final short value_;

		private BorderStyle(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static BorderStyle valueOf(final short typeCode) {
			for (BorderStyle type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching BorderStyle found for type code " + typeCode);
		}
	}

	public static enum BorderFlag {
		/**
		 * Display a solid silhouette of the visual object
		 */
		DROP_SHADOW((short) 0x0001);

		private final short value_;

		private BorderFlag(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<BorderFlag> valuesOf(final short flags) {
			Set<BorderFlag> result = EnumSet.noneOf(BorderFlag.class);
			for (BorderFlag flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	static {
		addFixed("Flags", Integer.class);
		addFixed("BorderStyle", Short.class);
		addFixedUpgrade("BorderWidthTop", Short.class);
		addFixedUpgrade("BorderWidthLeft", Short.class);
		addFixedUpgrade("BorderWidthBottom", Short.class);
		addFixedUpgrade("BorderWidthRight", Short.class);
		addFixed("dwSpare", Integer.class);
		addFixed("BorderFlags", Short.class);
		addFixedUpgrade("DropShadowWidth", Short.class);
		addFixedUpgrade("InnerWidthTop", Short.class);
		addFixedUpgrade("InnerWidthLeft", Short.class);
		addFixedUpgrade("InnerWidthBottom", Short.class);
		addFixedUpgrade("InnerWidthRight", Short.class);
		addFixedUpgrade("OuterWidthTop", Short.class);
		addFixedUpgrade("OuterWidthLeft", Short.class);
		addFixedUpgrade("OuterWidthBottom", Short.class);
		addFixedUpgrade("OuterWidthRight", Short.class);
		addFixed("Color", COLOR_VALUE.class);
		addFixedArray("wSpares", Short.class, 5);
	}

	public CDBORDERINFO(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * Not Used must be 0
	 */
	public int getFlags() {
		return (Integer) getStructElement("Flags");
	}

	/**
	 * @return CDBORDERSTYLE_xxx
	 */
	public BorderStyle getBorderStyle() {
		return BorderStyle.valueOf((Short) getStructElement("BorderStyle"));
	}

	/**
	 * @return Thickness Top
	 */
	public int getBorderWidthTop() {
		return (Integer) getStructElement("BorderWidthTop");
	}

	/**
	 * @return Thickness Left
	 */
	public int getBorderWidthLeft() {
		return (Integer) getStructElement("BorderWidthLeft");
	}

	/**
	 * @return Thickness Bottom
	 */
	public int getBorderWidthBottom() {
		return (Integer) getStructElement("BorderWidthBottom");
	}

	/**
	 * @return Thickness Right
	 */
	public int getBorderWidthRight() {
		return (Integer) getStructElement("BorderWidthRight");
	}

	public int getSpare() {
		return (Integer) getStructElement("dwSpare");
	}

	/**
	 * @return CDBORDER_FLAGS_xxx
	 */
	public Set<BorderFlag> getBorderFlags() {
		return BorderFlag.valuesOf((Short) getStructElement("BorderFlags"));
	}

	/**
	 * @return Border Effects Drop Shadow Width
	 */
	public int getDropShadowWidth() {
		return (Integer) getStructElement("DropShadowWidth");
	}

	/**
	 * @return Inside Thickness Top
	 */
	public int getInnerWidthTop() {
		return (Integer) getStructElement("InnerWidthTop");
	}

	/**
	 * @return Inside Thickness Left
	 */
	public int getInnerWidthLeft() {
		return (Integer) getStructElement("InnerWidthLeft");
	}

	/**
	 * @return Inside Thickness Bottom
	 */
	public int getInnerWidthBottom() {
		return (Integer) getStructElement("InnerWidthBottom");
	}

	/**
	 * @return Inside Thickness Right
	 */
	public int getInnerWidthRight() {
		return (Integer) getStructElement("InnerWidthRight");
	}

	/**
	 * @return Outside Thickness Top
	 */
	public int getOuterWidthTop() {
		return (Integer) getStructElement("OuterWidthTop");
	}

	/**
	 * @return Outside Thickness Left
	 */
	public int getOuterWidthLeft() {
		return (Integer) getStructElement("OuterWidthLeft");
	}

	/**
	 * @return Outside Thickness Bottom
	 */
	public int getOuterWidthBottom() {
		return (Integer) getStructElement("OuterWidthBottom");
	}

	/**
	 * @return Outside Thickness Right
	 */
	public int getOuterWidthRight() {
		return (Integer) getStructElement("OuterWidthRight");
	}

	/**
	 * @return Border Color
	 */
	public COLOR_VALUE getColor() {
		return (COLOR_VALUE) getStructElement("Color");
	}

	public short[] getSpares() {
		return (short[]) getStructElement("wSpares");
	}
}
