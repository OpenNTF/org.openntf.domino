package org.openntf.domino.nsfdata.structs.cd;

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

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
	public static enum Style {
		/**
		 * There isn't any border style
		 */
		NONE,
		/**
		 * Border is displayed as a solid line
		 */
		SOLID,
		/**
		 * Border is displayed as a double line
		 */
		DOUBLE,
		/**
		 * Border is inset
		 */
		INSET,
		/**
		 * Border is outset
		 */
		OUTSET,
		/**
		 * Border has a ridge
		 */
		RIDGE,
		/**
		 * Border has a groove
		 */
		GROOVE,
		/**
		 * Border is dotted
		 */
		DOTTED,
		/**
		 * Border is dashed
		 */
		DASHED,
		/**
		 * Border is displayed as a "picture frame" around table
		 */
		PICTURE,
		/**
		 * Border is a graphic
		 */
		GRAPHIC;
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

	public final WSIG Header = inner(new WSIG());
	public final Unsigned32 Flags = new Unsigned32();
	public final Enum16<Style> BorderStyle = new Enum16<Style>(Style.values());
	public final Unsigned16 BorderWidthTop = new Unsigned16();
	public final Unsigned16 BorderWidthLeft = new Unsigned16();
	public final Unsigned16 BorderWidthBottom = new Unsigned16();
	public final Unsigned16 BorderWidthRight = new Unsigned16();
	public final Unsigned32 dwSpare = new Unsigned32();
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned16 BorderFlags = new Unsigned16();
	public final Unsigned16 DropShadowWidth = new Unsigned16();
	public final Unsigned16 InnerWidthTop = new Unsigned16();
	public final Unsigned16 InnerWidthLeft = new Unsigned16();
	public final Unsigned16 InnerWidthBottom = new Unsigned16();
	public final Unsigned16 InnerWidthRight = new Unsigned16();
	public final Unsigned16 OuterWidthTop = new Unsigned16();
	public final Unsigned16 OuterWidthLeft = new Unsigned16();
	public final Unsigned16 OuterWidthBottom = new Unsigned16();
	public final Unsigned16 OuterWidthRight = new Unsigned16();
	public final COLOR_VALUE Color = inner(new COLOR_VALUE());
	public final Unsigned16[] wSpares = array(new Unsigned16[5]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * @return CDBORDER_FLAGS_xxx
	 */
	public Set<BorderFlag> getBorderFlags() {
		return BorderFlag.valuesOf((short) BorderFlags.get());
	}

}
