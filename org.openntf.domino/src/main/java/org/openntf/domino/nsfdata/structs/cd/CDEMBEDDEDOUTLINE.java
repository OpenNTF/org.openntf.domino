package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.COLOR_VALUE;
import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record defines the attributes of an embedded outline. It is preceded by a CDHOTSPOTBEGIN and a CDPLACEHOLDER. The CD record,
 * CDPLACEHOLDER, further defines the CDEMBEDDEDOUTLINE. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDEMBEDDEDOUTLINE extends CDRecord {
	/**
	 * (editods.h)
	 * 
	 * @since Lotus Notes/Domino 5.0
	 *
	 */
	public static enum Flag {
		/** Embedded outline is displayed horizontally. */
		DISPLAYHORZ(0x00000001),
		/** Embedded outline is a background image. */
		HASIMAGELABEL(0x00000002),
		/** Embedded outline is a tile image. */
		TITLEIMAGE(0x00000004),
		/** Embedded outline use applet in browser. */
		USEAPPLET_INBROWSER(0x00000008),
		/** Embedded outline is a title. */
		TYPE_TITLE(0x00000010),
		/** Embedded outline show twistie. */
		SHOWTWISTIE(0x00000020),
		/** Embedded outline title is fixed. */
		TITLEFIXED(0x00000040),
		/** Embedded outline top level is fixed. */
		TOPLEVELFIXED(0x00000080),
		/** Embedded outline sub level is fixed. */
		SUBLEVELFIXED(0x00000100),
		/** Embedded outline is tree style. */
		TREE_STYLE(0x00000200),
		/** Embedded outline has name. */
		HASNAME(0x00000400),
		/** Embedded outline has target frame. */
		HASTARGETFRAME(0x00000800),
		/** Embedded outline is all the same. */
		ALLTHESAME(0x00001000),
		/** Embedded outline back is all the same. */
		BACK_ALLTHESAME(0x00002000),
		/** Embedded outline expand data. */
		EXPAND_DATA(0x00004000),
		/** Embedded outline expand all. */
		EXPAND_ALL(0x00008000),
		/** Embedded outline expand first. */
		EXPAND_FIRST(0x00010000),
		/** Embedded outline expand save. */
		EXPAND_SAVED(0x00020000),
		/** Embedded outline expand none. */
		EXPAND_NONE(0x00040000),
		/** Embedded outline has root name. */
		HASROOTNAME(0x00080000),
		/** Embedded outline has right to left reading order. */
		RTLREADING(0x00100000),
		/** Embedded outline has twistie image. */
		TWISTIEIMAGE(0x00200000),
		/** Embedded outline displays unread count in folder. */
		HANDLEFOLDERUNREAD(0x00400000),
		/** Embedded outline has an OS style twistie. */
		NEWSTYLE_TWISTIE(0x00800000),
		/** Embedded outline maintains the folder's unread marks. */
		MAINTAINFOLDERUNREAD(0x01000000),
		/** Use JavaScript (Dojo) control in the browser. */
		USEJSCTLINBROWSER(0x02000000),
		/** Use custom JavasScript control in the browser */
		USECUSTOMJSINBROWSER(0x04000000);

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

	public static enum Alignment {
		TOPLEFT((short) 0), TOPCENTER((short) 1), TOPRIGHT((short) 2), MIDDLELEFT((short) 3), MIDDLECENTER((short) 4),
		MIDDLERIGHT((short) 5), BOTTOMLEFT((short) 6), BOTTOMCENTER((short) 7), BOTTOMRIGHT((short) 8);

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

	public static enum TitleStyle {
		HIDE((short) 0), SIMPLE((short) 1), HIERARCHICAL((short) 2);

		private final short value_;

		private TitleStyle(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static TitleStyle valueOf(final short typeCode) {
			for (TitleStyle type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching TitleStyle found for type code " + typeCode);
		}
	}

	static {
		addFixed("Flags", Integer.class);
		addFixedArray("Unused", Integer.class, 3);
		addFixed("Alignment", Short.class);
		addFixedUnsigned("SpaceBetweenEntries", Short.class);
		addFixedUnsigned("LabelLength", Short.class);
		addFixed("Style", Short.class);
		addFixedUnsigned("Title_VOffset", Short.class);
		addFixedUnsigned("Title_HOffset", Short.class);
		addFixedUnsigned("Title_Height", Short.class);
		addFixedUnsigned("TopLevel_VOffset", Short.class);
		addFixedUnsigned("TopLevel_HOffset", Short.class);
		addFixedUnsigned("TopLevel_Height", Short.class);
		addFixedUnsigned("SubLevel_VOffset", Short.class);
		addFixedUnsigned("SubLevel_HOffset", Short.class);
		addFixedUnsigned("SubLevel_Height", Short.class);
		addFixedUnsigned("NameLength", Short.class);
		addFixedUnsigned("TargetFrameLength", Short.class);
		addFixedArray("SelectFontID", FONTID.class, 3);
		addFixedArray("MouseFontID", FONTID.class, 3);
		addFixedArrayUnsigned("Font_VOffset", Short.class, 3);
		addFixedArrayUnsigned("Font_HOffset", Short.class, 3);
		addFixedArray("Align", Short.class, 3);
		addFixed("Control_BackColor", COLOR_VALUE.class);
		addFixedArray("BackColor", COLOR_VALUE.class, 9);
		addFixedArray("SelectFontColor", COLOR_VALUE.class, 3);
		addFixedArray("Repeat", Short.class, 4);
		addFixedArray("Background_Align", Short.class, 4);
		addFixedArrayUnsigned("Background_VOffset", Short.class, 4);
		addFixedArrayUnsigned("Background_HOffset", Short.class, 4);
		addFixedArray("wBackground_Image", Short.class, 4);
		addFixedArray("NormalFontColor", COLOR_VALUE.class, 3);
		addFixedArray("MouseFontColor", COLOR_VALUE.class, 3);
		addFixedUnsigned("RootLength", Short.class);
		addFixedUnsigned("TopLevel_PixelHeight", Short.class);
		addFixedUnsigned("wColWidth", Short.class);
		addFixed("SpareWord", Short.class);
		addFixedArray("Spare", Integer.class, 4);
	}

	public static final int SIZE = getFixedStructSize();

	public CDEMBEDDEDOUTLINE(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDEMBEDDEDOUTLINE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("Flags"));
	}

	public Alignment getAlignment() {
		return Alignment.valueOf((Short) getStructElement("Alignment"));
	}

	public int getSpaceBetweenEntries() {
		return (Integer) getStructElement("SpaceBetweenElements");
	}

	public TitleStyle getStyle() {
		return TitleStyle.valueOf((Short) getStructElement("Style"));
	}

	public int getTitleVOffset() {
		return (Integer) getStructElement("Title_VOffset");
	}

	public int getTitleHOffset() {
		return (Integer) getStructElement("Title_HOffset");
	}

	public int getTitleHeight() {
		return (Integer) getStructElement("Title_Height");
	}

	public int getTopLevelVOffset() {
		return (Integer) getStructElement("TopLevel_VOffset");
	}

	public int getTopLevelHOffset() {
		return (Integer) getStructElement("TopLevel_HOffset");
	}

	public int getTopLevelHeight() {
		return (Integer) getStructElement("TopLevel_Height");
	}

	public int getSubLevelVOffset() {
		return (Integer) getStructElement("SubLevel_VOffset");
	}

	public int getSubLevelHOffset() {
		return (Integer) getStructElement("SubLevel_HOffset");
	}

	public int getSubLevelHeight() {
		return (Integer) getStructElement("SubLevel_Height");
	}

	public FONTID[] getSelectFontId() {
		return (FONTID[]) getStructElement("SelectFontID");
	}

	public FONTID[] getMouseFontId() {
		return (FONTID[]) getStructElement("MouseFontID");
	}

	public int[] getFontVOffset() {
		return (int[]) getStructElement("Font_VOffset");
	}

	public int[] getFontHOffset() {
		return (int[]) getStructElement("Font_HOffset");
	}

	public List<Alignment> getAlign() {
		short[] value = (short[]) getStructElement("Align");
		List<Alignment> result = new ArrayList<Alignment>(value.length);
		for (short val : value) {
			result.add(Alignment.valueOf(val));
		}
		return result;
	}

	public COLOR_VALUE getControlBackColor() {
		return (COLOR_VALUE) getStructElement("Control_BackColor");
	}

	public COLOR_VALUE[] getBackColor() {
		return (COLOR_VALUE[]) getStructElement("BackColor");
	}

	public COLOR_VALUE[] getSelectFontColor() {
		return (COLOR_VALUE[]) getStructElement("SelectFontColor");
	}

	public short[] getRepeat() {
		return (short[]) getStructElement("Repeat");
	}

	public List<Alignment> getBackgroundAlign() {
		short[] value = (short[]) getStructElement("Background_Align");
		List<Alignment> result = new ArrayList<Alignment>(value.length);
		for (short val : value) {
			result.add(Alignment.valueOf(val));
		}
		return result;
	}

	public int[] getBackgroundVOffset() {
		return (int[]) getStructElement("Background_VOffset");
	}

	public int[] getBackgroundHOffset() {
		return (int[]) getStructElement("Background_HOffset");
	}

	public short[] getBackgroundImage() {
		return (short[]) getStructElement("wBackground_Image");
	}

	public COLOR_VALUE[] getNormalFontColor() {
		return (COLOR_VALUE[]) getStructElement("NormalFontColor");
	}

	public COLOR_VALUE[] getMouseFontColor() {
		return (COLOR_VALUE[]) getStructElement("MouseFontColor");
	}

	public int getRootLength() {
		return (Integer) getStructElement("RootLength");
	}

	public int getTopLevelPixelHeight() {
		return (Integer) getStructElement("TopLevel_PixelHeight");
	}

	public int getColWidth() {
		return (Integer) getStructElement("wColWidth");
	}
}
