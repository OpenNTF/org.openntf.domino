package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies a format for paragraphs in a rich-text field. There may be more than one paragraph using the same paragraph
 * format, but there may be no more than one CDPABDEFINITION with the same ID in a rich-text field. (editods.h)
 *
 */
public class CDPABDEFINITION extends CDRecord {

	public static final int MAXTABS = 20;

	static {
		addFixed("PABID", Short.class);
		addFixed("JustifyMode", Short.class);
		addFixedUnsigned("LineSpacing", Short.class);
		addFixedUnsigned("ParagraphSpacingBefore", Short.class);
		addFixedUnsigned("ParagraphSpacingAfter", Short.class);
		addFixedUnsigned("LeftMargin", Short.class);
		addFixedUnsigned("RightMargin", Short.class);
		addFixedUnsigned("FirstLineLeftMargin", Short.class);
		addFixedUnsigned("Tabs", Short.class);
		addFixedArray("Tab", Short.class, MAXTABS);
		addFixed("Flags", Short.class);
		addFixed("TabTypes", Integer.class);
		addFixed("Flags2", Short.class);
	}

	protected CDPABDEFINITION(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return ID of this PAB
	 */
	public short getId() {
		return (Short) getStructElement("PABID");
	}

	/**
	 * @return Paragraph justification type
	 */
	public short getJustifyMode() {
		// TODO create enum
		return (Short) getStructElement("JustifyMode");
	}

	/**
	 * @return (2*(Line Spacing-1)) (0:1,1:1.5,2:2,etc)
	 */
	public int getLineSpacing() {
		return (Integer) getStructElement("LineSpacing");
	}

	/**
	 * @return # LineSpacing units above para
	 */
	public int getParagraphSpacingBefore() {
		return (Integer) getStructElement("ParagraphSpacingBefore");
	}

	/**
	 * @return # LineSpacing units below para
	 */
	public int getParagraphSpacingAfter() {
		return (Integer) getStructElement("ParagraphSpacingAfter");
	}

	/**
	 * @return Leftmost margin, twips rel to abs left (16 bits = about 44")
	 */
	public int getLeftMargin() {
		return (Integer) getStructElement("LeftMargin");
	}

	/**
	 * @return Rightmost margin, twips rel to abs right (16 bits = about 44") Special value "0" means right margin will be placed 1" from
	 *         right edge of paper, regardless of paper size.
	 */
	public int getRightMargin() {
		return (Integer) getStructElement("RightMargin");
	}

	/**
	 * @return Leftmost margin on first line (16 bits = about 44")
	 */
	public int getFirstLineLeftMargin() {
		return (Integer) getStructElement("FirstLineLeftMargin");
	}

	/**
	 * @return Number of tab stops in table
	 */
	public int getTabs() {
		return (Integer) getStructElement("Tabs");
	}

	/**
	 * @return Table of tab stop positions, negative value means decimal tab (15 bits = about 22")
	 */
	public short[] getTab() {
		return (short[]) getStructElement("Tab");
	}

	/**
	 * @return Paragraph attribute flags - PABFLAG_xxx
	 */
	public short getFlags() {
		// TODO create enum
		return (Short) getStructElement("Flags");
	}

	/**
	 * @return 2 bits per tab
	 */
	public int getTabTypes() {
		return (Integer) getStructElement("TabTypes");
	}

	/**
	 * @return Extra paragraph attribute flags - PABFLAG2_xxx
	 */
	public short getFlags2() {
		// TODO create enum
		return (Short) getStructElement("Flags2");
	}

	//	@Override
	//	public int getExtraLength() {
	//		// This is a weird one. There may be several types of extra data following this:
	//		// 0-6 WORDs based on flags in Flags2
	//		// a potential DWORD based on Flags2 matching PABFLAG2_MORE_FLAGS
	//		// a potential final DWORD if the previous one exists and is EXTENDEDPABFLAGS3
	//
	//		// TODO property enum-ify these, from editdlft.h
	//		int PABFLAG2_LM_OFFSET = 0x0004;
	//		int PABFLAG2_LM_PERCENT = 0x0008;
	//		int PABFLAG2_FLLM_OFFSET = 0x0010;
	//		int PABFLAG2_FLLM_PERCENT = 0x0020;
	//		int PABFLAG2_RM_OFFSET = 0x0040;
	//		int PABFLAG2_RM_PERCENT = 0x0080;
	//
	//		short flags2 = getFlags2();
	//
	//		int result = 0;
	//		if ((flags2 & PABFLAG2_LM_OFFSET) > 0) {
	//			result += 2;
	//		}
	//		if ((flags2 & PABFLAG2_LM_PERCENT) > 0) {
	//			result += 2;
	//		}
	//		if ((flags2 & PABFLAG2_FLLM_OFFSET) > 0) {
	//			result += 2;
	//		}
	//		if ((flags2 & PABFLAG2_FLLM_PERCENT) > 0) {
	//			result += 2;
	//		}
	//		if ((flags2 & PABFLAG2_RM_OFFSET) > 0) {
	//			result += 2;
	//		}
	//		if ((flags2 & PABFLAG2_RM_PERCENT) > 0) {
	//			result += 2;
	//		}
	//
	//		// now we want to see if there's a DWORD after whatever that result was
	//		int PABFLAG2_MORE_FLAGS = 0x8000;
	//		if ((flags2 & PABFLAG2_MORE_FLAGS) > 0) {
	//			// Not only do we want to note that this value exists, but we also
	//			// want to read it, as it could denote ANOTHER value
	//			int extDword = getData().getInt(getData().position() + result);
	//			result += 4;
	//
	//			long EXTENDEDPABFLAGS3 = 0x00000001L;
	//			if (extDword == EXTENDEDPABFLAGS3) {
	//				result += 4;
	//			}
	//		}
	//
	//		return result;
	//	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", ID: " + getId() + ", Justify Mode: " + getJustifyMode() + ", Line Spacing: "
				+ getLineSpacing() + ", Paragraph Spacing Before: " + getParagraphSpacingBefore() + ", Paragraph Spacing After: "
				+ getParagraphSpacingAfter() + ", Left Margin: " + getLeftMargin() + ", Right Margin: " + getRightMargin()
				+ ", First Line Left Margin: " + getFirstLineLeftMargin() + ", Tab: " + getTab() + ", Flags: " + getFlags()
				+ ", Tab Types: " + getTabTypes() + ", Flags2: " + getFlags2() + "]";
	}
}
