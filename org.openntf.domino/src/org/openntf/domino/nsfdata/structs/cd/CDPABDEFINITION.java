package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies a format for paragraphs in a rich-text field. There may be more than one paragraph using the same paragraph
 * format, but there may be no more than one CDPABDEFINITION with the same ID in a rich-text field. (editods.h)
 *
 */
public class CDPABDEFINITION extends CDRecord {
	protected CDPABDEFINITION(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return ID of this PAB
	 */
	public short getId() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Paragraph justification type
	 */
	public short getJustifyMode() {
		// TODO create enum
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return (2*(Line Spacing-1)) (0:1,1:1.5,2:2,etc)
	 */
	public short getLineSpacing() {
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return # LineSpacing units above para
	 */
	public short getParagraphSpacingBefore() {
		return getData().getShort(getData().position() + 6);
	}

	/**
	 * @return # LineSpacing units below para
	 */
	public short getParagraphSpacingAfter() {
		return getData().getShort(getData().position() + 8);
	}

	/**
	 * @return Leftmost margin, twips rel to abs left (16 bits = about 44")
	 */
	public short getLeftMargin() {
		return getData().getShort(getData().position() + 10);
	}

	/**
	 * @return Rightmost margin, twips rel to abs right (16 bits = about 44") Special value "0" means right margin will be placed 1" from
	 *         right edge of paper, regardless of paper size.
	 */
	public short getRightMargin() {
		return getData().getShort(getData().position() + 12);
	}

	/**
	 * @return Leftmost margin on first line (16 bits = about 44")
	 */
	public short getFirstLineLeftMargin() {
		return getData().getShort(getData().position() + 14);
	}

	/**
	 * @return Number of tab stops in table
	 */
	public short getTabs() {
		return getData().getShort(getData().position() + 16);
	}

	/**
	 * @return Table of tab stop positions, negative value means decimal tab (15 bits = about 22")
	 */
	public short[] getTab() {
		short[] result = new short[20];
		ByteBuffer tabsRaw = getData().duplicate();
		tabsRaw.position(tabsRaw.position() + 18);
		ShortBuffer tabs = tabsRaw.asShortBuffer();
		tabs.get(result);
		return result;
	}

	/**
	 * @return Paragraph attribute flags - PABFLAG_xxx
	 */
	public short getFlags() {
		// TODO create enum
		return getData().getShort(getData().position() + 56);
	}

	/**
	 * @return 2 bits per tab
	 */
	public int getTabTypes() {
		return getData().getShort(getData().position() + 58);
	}

	/**
	 * @return Extra paragraph attribute flags - PABFLAG2_xxx
	 */
	public short getFlags2() {
		// TODO create enum
		return getData().getShort(getData().position() + 62);
	}

	@Override
	public int getExtraLength() {
		// This is a weird one. There may be several types of extra data following this:
		// 0-6 WORDs based on flags in Flags2
		// a potential DWORD based on Flags2 matching PABFLAG2_MORE_FLAGS
		// a potential final DWORD if the previous one exists and is EXTENDEDPABFLAGS3

		// TODO property enum-ify these, from editdlft.h
		int PABFLAG2_LM_OFFSET = 0x0004;
		int PABFLAG2_LM_PERCENT = 0x0008;
		int PABFLAG2_FLLM_OFFSET = 0x0010;
		int PABFLAG2_FLLM_PERCENT = 0x0020;
		int PABFLAG2_RM_OFFSET = 0x0040;
		int PABFLAG2_RM_PERCENT = 0x0080;

		short flags2 = getFlags2();

		int result = 0;
		if ((flags2 & PABFLAG2_LM_OFFSET) > 0) {
			result += 2;
		}
		if ((flags2 & PABFLAG2_LM_PERCENT) > 0) {
			result += 2;
		}
		if ((flags2 & PABFLAG2_FLLM_OFFSET) > 0) {
			result += 2;
		}
		if ((flags2 & PABFLAG2_FLLM_PERCENT) > 0) {
			result += 2;
		}
		if ((flags2 & PABFLAG2_RM_OFFSET) > 0) {
			result += 2;
		}
		if ((flags2 & PABFLAG2_RM_PERCENT) > 0) {
			result += 2;
		}

		// now we want to see if there's a DWORD after whatever that result was
		int PABFLAG2_MORE_FLAGS = 0x8000;
		if ((flags2 & PABFLAG2_MORE_FLAGS) > 0) {
			// Not only do we want to note that this value exists, but we also
			// want to read it, as it could denote ANOTHER value
			int extDword = getData().getInt(getData().position() + result);
			result += 4;

			long EXTENDEDPABFLAGS3 = 0x00000001L;
			if (extDword == EXTENDEDPABFLAGS3) {
				result += 4;
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", ID: " + getId() + ", Justify Mode: " + getJustifyMode() + ", Line Spacing: "
				+ getLineSpacing() + ", Paragraph Spacing Before: " + getParagraphSpacingBefore() + ", Paragraph Spacing After: "
				+ getParagraphSpacingAfter() + ", Left Margin: " + getLeftMargin() + ", Right Margin: " + getRightMargin()
				+ ", First Line Left Margin: " + getFirstLineLeftMargin() + ", Tab: " + getTab() + ", Flags: " + getFlags()
				+ ", Tab Types: " + getTabTypes() + ", Flags2: " + getFlags2() + "]";
	}
}
