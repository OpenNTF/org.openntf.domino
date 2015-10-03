package org.openntf.domino.nsfdata.structs;

import java.util.EnumSet;
import java.util.Set;

/**
 * This structure describes the format of one column in a view. This structure is one of the components of a $VIEWFORMAT item in a view
 * note. A $VIEWFORMAT item contains one view column format descriptor per column. (viewfmt.h)
 * 
 * @since forever
 *
 */
public class VIEW_COLUMN_FORMAT extends AbstractStruct {
	public static enum FormatSignature {
		SIGNATURE((short) 0x4356), SIGNATURE2((short) 0x4357), SIGNATURE3((short) 0x4358), SIGNATURE4((short) 0x4359),
		SIGNATURE5((short) 0x4360), SIGNATURE6((short) 0x4361);

		private final short value_;

		private FormatSignature(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static FormatSignature valueOf(final short value) {
			for (FormatSignature sig : values()) {
				if (sig.getValue() == value) {
					return sig;
				}
			}
			throw new IllegalArgumentException("No " + FormatSignature.class.getSimpleName() + " found for value " + value);
		}
	}

	public static enum Flag1 {
		Sort(0x0001), SortCategorize(0x0002), SortDescending(0x0004), Hidden(0x0008), Response(0x0010), HideDetail(0x0020), Icon(0x0040),
		NoResize(0x0080), ResortAscending(0x0100), ResortDescending(0x0200), Twistie(0x0400), ResortToView(0x0800), SecondResort(0x1000),
		SecondResortDescending(0x2000), CaseInsensitiveSort(0x4000), AccentInsensitiveSort(0x8000);

		private final short value_;

		private Flag1(final short value) {
			value_ = value;
		}

		private Flag1(final int value) {
			value_ = (short) value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<Flag1> valuesOf(final short flags) {
			Set<Flag1> result = EnumSet.noneOf(Flag1.class);
			for (Flag1 flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public static enum Flag2 {
		DisplayAlignment(0x0003), SubtotalCode(0x003c), HeaderAlignment(0x00c0), SortPermute(0x0100), SecondResortUniqueSort(0x0200),
		SecondResortCategorized(0x0400), SecondResortPermute(0x0800), SecondResortPermutePair(0x1000), ShowValuesAsLinks(0x2000),
		DisplayReadingOrder(0x4000), HeaderReadingOrder(0x8000);

		private final short value_;

		private Flag2(final short value) {
			value_ = value;
		}

		private Flag2(final int value) {
			value_ = (short) value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<Flag2> valuesOf(final short flags) {
			Set<Flag2> result = EnumSet.noneOf(Flag2.class);
			for (Flag2 flag : values()) {
				if ((flag.getValue() & flags) > 0) {
					result.add(flag);
				}
			}
			return result;
		}
	}

	public static enum DataType {
		NUMBER, TIMEDATE, TEXT
	}

	/**
	 * Use getSignature() for access
	 */
	@Deprecated
	public final Unsigned16 Signature = new Unsigned16();
	/**
	 * Use getFlags1() for access
	 */
	@Deprecated
	public final Unsigned16 Flags1 = new Unsigned16();
	public final Unsigned16 ItemNameSize = new Unsigned16();
	public final Unsigned16 TitleSize = new Unsigned16();
	public final Unsigned16 FormulaSize = new Unsigned16();
	public final Unsigned16 ConstantValueSize = new Unsigned16();
	public final Unsigned16 DisplayWidth = new Unsigned16();
	public final FONTID FontID = inner(new FONTID());
	/**
	 * Use getFlags2() for access
	 */
	@Deprecated
	public final Unsigned16 Flags2 = new Unsigned16();
	public final NFMT NumberFormat = inner(new NFMT());
	public final TFMT TimeFormat = inner(new TFMT());
	// TODO FORMROUT.NSF contained 16424
	public final Unsigned16 FormatDataType = new Unsigned16();
	//	public final Enum16<DataType> FormatDataType = new Enum16<DataType>(DataType.values());
	// TODO match to LDDELIM_xxx
	public final Unsigned16 ListSep = new Unsigned16();

	public FormatSignature getSignature() {
		return FormatSignature.valueOf((short) Signature.get());
	}

	public Set<Flag1> getFlags1() {
		return Flag1.valuesOf((short) Flags1.get());
	}

	public Set<Flag2> getFlags2() {
		return Flag2.valuesOf((short) Flags2.get());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Signature=" + getSignature() + ", Flags1=" + getFlags1() + ", ItemNameSize="
				+ ItemNameSize.get() + ", TitleSize=" + TitleSize.get() + ", FormulaSize=" + FormulaSize.get() + ", ConstantValueSize="
				+ ConstantValueSize.get() + ", DisplayWidth=" + DisplayWidth.get() + ", FontID=" + FontID + ", Flags2=" + getFlags2()
				+ ", NumberFormat=" + NumberFormat + ", TimeFormat=" + TimeFormat + ", FormatDataType=" + FormatDataType.get()
				+ ", ListSep=" + ListSep.get() + "]";
	}
}
