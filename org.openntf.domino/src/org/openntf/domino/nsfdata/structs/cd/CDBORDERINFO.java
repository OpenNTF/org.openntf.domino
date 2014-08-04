package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record describes border information for a given table. This CD record will be preceded with CD record CDPRETABLEBEGIN both
 * encapsulated between a CDBEGINRECORD and a CDENDRECORD record with CD record signature CDPRETABLEBEGIN. (editods.h)
 * 
 * @author jgallagher
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

	public CDBORDERINFO(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * Not Used must be 0
	 */
	public int getFlags() {
		return getData().getInt(getData().position() + 0);
	}

	/**
	 * @return CDBORDERSTYLE_xxx
	 */
	public BorderStyle getBorderStyle() {
		return BorderStyle.valueOf(getData().getShort(getData().position() + 4));
	}

	/**
	 * @return Thickness Top
	 */
	public short getBorderWidthTop() {
		return getData().getShort(getData().position() + 6);
	}

	/**
	 * @return Thickness Left
	 */
	public short getBorderWidthLeft() {
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return Thickness Bottom
	 */
	public short getBorderWidthBottom() {
		return getData().getShort(getData().position() + 10);
	}

	/**
	 * @return Thickness Right
	 */
	public short getBorderWidthRight() {
		return getData().getShort(getData().position() + 12);
	}

	public int getSpare() {
		return getData().getInt(getData().position() + 14);
	}

	/**
	 * @return CDBORDER_FLAGS_xxx
	 */
	public Set<BorderFlag> getBorderFlags() {
		return BorderFlag.valuesOf(getData().getShort(getData().position() + 18));
	}

	/**
	 * @return Border Effects Drop Shadow Width
	 */
	public short getDropShadowWidth() {
		return getData().getShort(getData().position() + 20);
	}

	/**
	 * @return Inside Thickness Top
	 */
	public short getInnerWidthTop() {
		return getData().getShort(getData().position() + 22);
	}

	/**
	 * @return Inside Thickness Left
	 */
	public short getInnerWidthLeft() {
		return getData().getShort(getData().position() + 24);
	}

	/**
	 * @return Inside Thickness Bottom
	 */
	public short getInnerWidthBottom() {
		return getData().getShort(getData().position() + 26);
	}

	/**
	 * @return Inside Thickness Right
	 */
	public short getInnerWidthRight() {
		return getData().getShort(getData().position() + 28);
	}

	/**
	 * @return Outside Thickness Top
	 */
	public short getOuterWidthTop() {
		return getData().getShort(getData().position() + 30);
	}

	/**
	 * @return Outside Thickness Left
	 */
	public short getOuterWidthLeft() {
		return getData().getShort(getData().position() + 32);
	}

	/**
	 * @return Outside Thickness Bottom
	 */
	public short getOuterWidthBottom() {
		return getData().getShort(getData().position() + 34);
	}

	/**
	 * @return Outside Thickness Right
	 */
	public short getOuterWidthRight() {
		return getData().getShort(getData().position() + 36);
	}

	/**
	 * @return Border Color
	 */
	public COLOR_VALUE getColor() {
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 38);
		data.limit(data.position() + 6);
		return new COLOR_VALUE(data);
	}

	public short[] getSpares() {
		short[] result = new short[5];
		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 44);
		data.limit(data.position() + 10);
		for (int i = 0; i < 5; i++) {
			result[i] = data.getShort();
		}
		return result;
	}
}
