package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;
import org.openntf.domino.nsfdata.structs.cd.CDHOTSPOTBEGIN.Type;

/**
 * A CDPLACEHOLDER record stores additional information about various embedded type CD records, such as CDEMBEDDEDCTL, CDEMBEDDEDOUTLINE and
 * other embedded CD record types defined in HOTSPOTREC_TYPE_xxx. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDPLACEHOLDER extends CDRecord {
	public static enum Flag {
		/** Fit to window */
		FITTOWINDOW(0x00000001),
		/** Draw background */
		DRAWBACKGROUND(0x00000002),
		/** Use percentage */
		USEPERCENTAGE(0x00000004),
		/** Scrollbars */
		SCROLLBARS(0x00000008),
		/** Contents only */
		CONTENTSONLY(0x00000010),
		/** Align center */
		ALIGNCENTER(0x00000020),
		/** Align right */
		ALIGNRIGHT(0x00000040),
		/** Fit to window height */
		FITTOWINDOWHEIGHT(0x00000080),
		/** Tile image */
		TILEIMAGE(0x00000100),
		/** Display horizontally */
		DISPLAYHORZ(0x00000200),
		/** Don't expand selections */
		DONTEXPANDSELECTIONS(0x00000400),
		/** Expand current */
		EXPANDCURRENT(0x00000800),
		/** Fit contents width */
		FITCONTENTSWIDTH(0x00001000),
		/** Fixed width */
		FIXEDWIDTH(0x00002000),
		/** Fixed height */
		FIXEDHEIGHT(0x00004000),
		/** Fit contents */
		FITCONTENTS(0x00008000),
		/** Proportional width */
		PROP_WIDTH(0x00010000),
		/** Proportional width and height */
		PROP_BOTH(0x00020000),
		/** Scrollers */
		SCROLLERS(0x00040000);

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
	 * @since Lotus Notes/Domino 5.0
	 */
	public static enum Alignment {
		LEFT((short) 0), CENTER((short) 1), RIGHT((short) 2);

		private final short value_;

		private Alignment(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Alignment valueOf(final short typeCode) {
			for (Alignment type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Alignment found for type code " + typeCode);
		}
	}

	static {
		addFixed("Type", Short.class);
		addFixed("Flags", Integer.class);
		addFixedUnsigned("Width", Short.class);
		addFixedUnsigned("Height", Short.class);
		addFixed("FontID", FONTID.class);
		addFixedUnsigned("Characters", Short.class);
		addFixedUnsigned("SpaceBetween", Short.class);
		addFixed("TextAlignment", Short.class);
		addFixedUnsigned("SpaceWord", Short.class);
		addFixedArray("SubFontID", FONTID.class, 2);
		addFixedUnsigned("SpaceWord", Short.class);
		addFixed("BackgroundColor", COLOR_VALUE.class);
		addFixed("ColorRGB", COLOR_VALUE.class);
		addFixed("SpareWord", Short.class);
		addFixedArray("Spare", Integer.class, 3);
	}

	public static final int SIZE = getFixedStructSize();

	public CDPLACEHOLDER(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDPLACEHOLDER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Type getType() {
		return Type.valueOf((Short) getStructElement("Type"));
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("Flags"));
	}

	/**
	 * @return The width of the embedded element
	 */
	public int getWidth() {
		return (Integer) getStructElement("Width");
	}

	/**
	 * @return The height of the embedded element
	 */
	public int getHeight() {
		return (Integer) getStructElement("Height");
	}

	/**
	 * @return Font information of the embedded element
	 */
	public FONTID getFontId() {
		return (FONTID) getStructElement("FontID");
	}

	public int getCharacters() {
		return (Integer) getStructElement("Characters");
	}

	public int getSpaceBetween() {
		return (Integer) getStructElement("SpaceBetween");
	}

	public Alignment getTextAlignment() {
		return Alignment.valueOf((Short) getStructElement("TextAlignment"));
	}

	public int getSpaceWord() {
		return (Integer) getStructElement("SpaceWord");
	}

	/**
	 * @return Sub Font information of embedded element
	 */
	public FONTID getSubFontId1() {
		return ((FONTID[]) getStructElement("SubFontID"))[0];
	}

	/**
	 * @return Sub Font information of embedded element
	 */
	public FONTID getSubFontId2() {
		return ((FONTID[]) getStructElement("SubFontID"))[1];
	}

	public int getPlaceholderDataLength() {
		return (Integer) getStructElement("DataLength");
	}

	public COLOR_VALUE getBackgroundColor() {
		return (COLOR_VALUE) getStructElement("BackgroundColor");
	}

	public COLOR_VALUE getColorRGB() {
		return (COLOR_VALUE) getStructElement("ColorRGB");
	}
}
