package org.openntf.domino.nsfdata.structs;

import java.util.EnumSet;
import java.util.Set;

/**
 * This structure contains view format information for views saved in Notes 2.0 and later. (viewfmt.h)
 * 
 * @since Lotus Notes 2.0
 *
 */
public class VIEW_TABLE_FORMAT2 extends AbstractStruct {
	public static enum SpacingType {
		SINGLE_SPACE, ONE_POINT_25_SPACE, ONE_POINT_50_SPACE, ONE_POINT_75_SPACE, DOUBLE_SPACE
	}

	public static enum Flag1 {
		HAS_LINK_COLUMN(0x01), HTML_PASSTHRU(0x02);

		private final byte value_;

		private Flag1(final int value) {
			value_ = (byte) value;
		}

		public byte getValue() {
			return value_;
		}

		public static Set<Flag1> valuesOf(final byte flags) {
			Set<Flag1> result = EnumSet.noneOf(Flag1.class);
			for (Flag1 flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public static final short VALID_VIEW_FORMAT_SIG = 0x2BAD;
	public static final short VIEW_TABLE_MAX_LINE_COUNT = 10;

	public final Unsigned16 Length = new Unsigned16();
	public final Unsigned16 BackgroundColor = new Unsigned16();
	public final Unsigned16 V2BorderColor = new Unsigned16();
	public final FONTID TitleFont = inner(new FONTID());
	public final FONTID UnreadFont = inner(new FONTID());
	public final FONTID TotalsFont = inner(new FONTID());
	public final Unsigned16 AutoUpdateSeconds = new Unsigned16();
	public final Unsigned16 AlternateBackgroundColor = new Unsigned16();
	public final Unsigned16 wSig = new Unsigned16();
	public final Unsigned8 LineCount = new Unsigned8();
	// TODO investigate why this doesn't work as an enum (observed values of 8 and 68 in Nifty 50)
	//	public final Enum8<SpacingType> Spacing = new Enum8<SpacingType>(SpacingType.values());
	public final Unsigned8 Spacing = new Unsigned8();
	public final Unsigned16 BackgroundColorExt = new Unsigned16();
	public final Unsigned8 HeaderLineCount = new Unsigned8();
	/**
	 * Use getFlags1() for access
	 */
	@Deprecated
	public final Unsigned8 Flags1 = new Unsigned8();
	public final Unsigned16[] Spare = array(new Unsigned16[4]);

	public boolean isValid() {
		return (short) wSig.get() == VALID_VIEW_FORMAT_SIG;
	}

	public Set<Flag1> getFlags1() {
		return Flag1.valuesOf((byte) Flags1.get());
	}
}
