package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure specifies a format for paragraphs in a rich-text field. There may be more than one paragraph using the same paragraph
 * format, but there may be no more than one CDPABDEFINITION with the same ID in a rich-text field. (editods.h)
 *
 */
public class CDPABDEFINITION extends CDRecord {

	public static final int MAXTABS = 20;

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 PABID = new Unsigned16();
	// TODO make enum
	public final Unsigned16 JustifyMode = new Unsigned16();
	public final Unsigned16 LineSpacing = new Unsigned16();
	public final Unsigned16 ParagraphSpacingBefore = new Unsigned16();
	public final Unsigned16 ParagraphSpacingAfter = new Unsigned16();
	public final Unsigned16 LeftMargin = new Unsigned16();
	public final Unsigned16 RightMargin = new Unsigned16();
	public final Unsigned16 FirstLineLeftMargin = new Unsigned16();
	public final Unsigned16 Tabs = new Unsigned16();
	public final Signed16[] Tab = array(new Signed16[MAXTABS]);
	// TODO make enum
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned32 TabTypes = new Unsigned32();
	// TODO make enum
	public final Unsigned16 Flags2 = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
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

	//	@Override
	//	public String toString() {
	//		return "[" + getClass().getSimpleName() + ", ID: " + getId() + ", Justify Mode: " + getJustifyMode() + ", Line Spacing: "
	//				+ getLineSpacing() + ", Paragraph Spacing Before: " + getParagraphSpacingBefore() + ", Paragraph Spacing After: "
	//				+ getParagraphSpacingAfter() + ", Left Margin: " + getLeftMargin() + ", Right Margin: " + getRightMargin()
	//				+ ", First Line Left Margin: " + getFirstLineLeftMargin() + ", Tab: " + getTab() + ", Flags: " + getFlags()
	//				+ ", Tab Types: " + getTabTypes() + ", Flags2: " + getFlags2() + "]";
	//	}
}
