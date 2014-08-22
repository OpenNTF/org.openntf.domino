package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.NOTES_COLOR;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the appearance of the bar used with collapsible sections.
 * 
 * @since Lotus Notes 4.1
 *
 */
public class CDBAR extends CDRecord {

	/**
	 * On-disk flags for CDBAR (Collapsible Sections). For collapsible sections indicated by a tab or a tab with a diagonal border, the
	 * CDBAR record (with its Flags member set to BARREC_BORDER_OTHER) is followed by a CDDATAFLAGS structure with its Flags member set to
	 * BARREC_DATA_BORDER_TAB (for tab) or BARREC_DATA_BORDER_DIAG (for diagonal).
	 * 
	 * @since Lotus Notes 4.5
	 *
	 */

	// TODO: This needs to be rationalized somehow - the borders share values with earlier consts, but documentation is unclear
	public static enum Flag {
		/**
		 * Section can be expanded only if the current user has editing privileges.
		 */
		DISABLED_FOR_NON_EDITORS(1),
		/**
		 * Section is expanded.
		 */
		EXPANDED(2),
		/**
		 * The bar is preview only.
		 */
		PREVIEW(4),
		/**
		 * Border is not visible around section title.
		 */
		BORDER_INVISIBLE(0x1000),
		/**
		 * Bar caption is a formula.
		 */
		ISFORMULA(0x2000),
		/**
		 * Caption is hidden when bar is expanded.
		 */
		HIDE_EXPANDED(0x4000), POSTREPLYSECTION(0x8000), AUTO_EXP_READ(0x20), AUTO_EXP_PRE(0x20), AUTO_EXP_EDIT(0x40),
		AUTO_EXP_PRINT(0x80), AUTO_COL_READ(0x100), AUTO_COL_PRE(0x200), AUTO_COL_EDIT(0x400), AUTO_COL_PRINT(0x800), AUTO_COL_MASK(0xF00),

		AUTO_ED_EXP_READ(0x10000), AUTO_ED_EXP_PRE(0x20000), AUTO_ED_EXP_EDIT(0x40000), AUTO_ED_EXP_PRINT(0x80000),

		AUTO_ED_COL_READ(0x100000), AUTO_ED_COL_PRE(0x200000), AUTO_ED_COL_EDIT(0x400000), AUTO_ED_COL_PRINT(0x800000),

		/**
		 * Whether the user can edit this bar or not.
		 */
		INTENDED(0x1000000),
		/**
		 * The bar has an explicit color reference.
		 */
		HAS_COLOR(0x4000000),

		/**
		 * Draw a shadow around the border of the bar.
		 */
		BORDER_SHADOW(0),
		/**
		 * No default border style.
		 */
		BORDER_NONE(1),
		/**
		 * Bar is single thickness.
		 */
		BORDER_SINGLE(2),
		/**
		 * Bar is double thickness.
		 */
		BORDER_DOUBLE(3),
		/**
		 * Bar is triple thickness.
		 */
		BORDER_TRIPLE(4),
		/**
		 * Bar is two lines.
		 */
		BORDER_TWOLINE(5),
		/**
		 * Shaded box is visible around section title. Open and close is indicated by icon on right of section.
		 */
		BORDER_WINDOWCAPTION(6),
		/**
		 * Used to create tab or diagonal sections.
		 */
		BORDER_OTHER(7),
		/**
		 * Box is visible around section title with shading blended to match the background.
		 */
		BORDER_GRADIENT(7),
		/**
		 * Section is indicated by a tab.
		 */
		BORDER_TAB(8),
		/**
		 * Section is indicated by a tab with a diagonal border.
		 */
		BORDER_DIAG(9), BORDER_DUOCOLOR(10),
		/**
		 * Unused
		 */
		INDENTED(0x80000000);

		public static final int AUTO_EXP_MASK = 0xf0;
		public static final int AUTO_PRE_MASK = AUTO_COL_PRE.getValue() | AUTO_EXP_PRE.getValue();
		public static final int AUTO_READ_MASK = AUTO_COL_READ.getValue() | AUTO_EXP_READ.getValue();
		public static final int AUTO_EDIT_MASK = AUTO_COL_EDIT.getValue() | AUTO_EXP_EDIT.getValue();
		public static final int AUTO_PRINT_MASK = AUTO_COL_EDIT.getValue() | AUTO_EXP_PRINT.getValue();
		/**
		 * We will make use (in the code) of the fact that the auto expand/collapse flags for editors are simply shifted left twelve bits
		 * from the normal expand/collapse flags.
		 */
		public static final int AUTO_ED_SHIFT = 12;
		public static final int AUTO_ED_EXP_MASK = 0xf00000;
		public static final int AUTO_ED_COL_MASK = 0xF00000;
		public static final int AUTO_ED_PRE_MASK = AUTO_ED_COL_PRE.getValue() | AUTO_ED_EXP_PRE.getValue();
		public static final int AUTO_ED_READ_MASK = AUTO_ED_COL_READ.getValue() | AUTO_ED_EXP_READ.getValue();
		public static final int AUTO_ED_EDIT_MASK = AUTO_ED_COL_EDIT.getValue() | AUTO_ED_EXP_EDIT.getValue();
		public static final int AUTO_ED_PRINT_MASK = AUTO_ED_COL_PRINT.getValue() | AUTO_ED_EXP_PRINT.getValue();

		/**
		 * Mask used for getting and setting the border type.
		 */
		public static final int BORDER_MASK = 0x70000000;

		/**
		 * Indicate explicitly those bits that we want to save on-disk so that we insure that the others are zero when we save to disk so
		 * that we can use later.
		 */
		public static final int ODS_MASK = 0xF4FFEFF7;

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

	public CDBAR(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf(getData().getInt(getData().position() + 0));
	}

	/**
	 * Specifies the font, size, and color of the bar title.
	 */
	public FONTID getFontID() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 4);
		data.limit(data.position() + FONTID.SIZE);
		return new FONTID(data);
	}

	/**
	 * If the BARREC_HAS_COLOR bit is set in the Flags field then immediately following this structure there will be a WORD that identifies
	 * the value of the color. This color value comes from the defines in the file COLORID.H that begin with NOTES_COLOR_xxx
	 */
	public NOTES_COLOR getColor() {
		if (getFlags().contains(Flag.HAS_COLOR)) {
			short color = getData().getShort(getData().position() + FONTID.SIZE + 4);
			return new NOTES_COLOR(color);
		}
		return null;
	}

	public String getCaption() {
		int preceding = getFlags().contains(Flag.HAS_COLOR) ? 2 : 0;

		int length = getDataLength() - 4 - FONTID.SIZE - preceding;
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + FONTID.SIZE + 4 + preceding);
		data.limit(data.position() + length);
		return ODSUtils.fromLMBCS(data);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Flags=" + getFlags() + ", FontID=" + getFontID() + ", Color=" + getColor()
				+ ", Caption=" + getCaption() + "]";
	}
}
