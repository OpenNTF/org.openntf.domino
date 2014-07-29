package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class CDPABDEFINITION extends CDRecord {
	private static final long serialVersionUID = 1L;

	public CDPABDEFINITION(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		super(signature, data, dataLength);
	}

	public short getId() {
		return getData().getShort(getData().position() + 0);
	}

	public short getJustifyMode() {
		return getData().getShort(getData().position() + 2);
	}

	public short getLineSpacing() {
		return getData().getShort(getData().position() + 4);
	}

	public short getParagraphSpacingBefore() {
		return getData().getShort(getData().position() + 6);
	}

	public short getParagraphSpacingAfter() {
		return getData().getShort(getData().position() + 8);
	}

	public short getLeftMargin() {
		return getData().getShort(getData().position() + 10);
	}

	public short getRightMargin() {
		return getData().getShort(getData().position() + 12);
	}

	public short getFirstLineLeftMargin() {
		return getData().getShort(getData().position() + 14);
	}

	public short[] getTab() {
		short[] result = new short[20];
		ByteBuffer tabsRaw = getData().duplicate();
		tabsRaw.position(tabsRaw.position() + 16);
		ShortBuffer tabs = tabsRaw.asShortBuffer();
		tabs.get(result);
		return result;
	}

	public short getFlags() {
		return getData().getShort(getData().position() + 54);
	}

	public int getTabTypes() {
		return getData().getShort(getData().position() + 56);
	}

	public short getFlags2() {
		return getData().getShort(getData().position() + 60);
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
