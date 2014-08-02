package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The designer of a form or view may define custom actions associated with that form or view. Actions may be presented to the user as
 * buttons on the action button bar or as options on the "Actions" menu. (actods.h)
 * 
 * @author jgallagher
 * @since Lotus Notes 4.5
 *
 */
public class CDACTION extends CDRecord {
	/**
	 * The action type based on the API documentation (ACTION_xxx (type)).
	 */
	public static enum Type {
		RUN_FORMULA((short) 1), RUN_SCRIPT((short) 2), RUN_AGENT((short) 3), OLDSYS_COMMAND((short) 4), SYS_COMMAND((short) 5),
		PLACEHLDER((short) 7), RUN_JAVASCRIPT((short) 8);

		private final short value_;

		private Type(final short value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static Type valueOf(final short typeCode) {
			for (Type type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Type found for type code " + typeCode);
		}
	}

	/**
	 * The action flags based on the API documentation (ACTION_xxx (flag)).
	 */
	public static enum Flag {
		SHOW_IN_MENU(0x00000001), SHOW_IN_BAR(0x00000002), SHOW_WHEN_PREVIEWING(0x00000004), SHOW_WHEN_READING(0x00000008),
		SHOW_WHEN_EDITING(0x00000010), SHOW_ON_OLE_LAUNCH(0x00000020), OLE_CLOSE_WHEN_CHOSEN(0x00000040), NO_FORMULA(0x00000080),
		SHOW_WHEN_PREVEDITING(0x00000100), OLE_DOC_WINDOW_TO_FRONT(0x00001000), HIDE_FROM_NOTES(0x00002000), HIDE_FROM_WEB(0x00004000),
		READING_ORDER_RTL(0x00008000), SHARED(0x00010000), MODIFIED(0x00020000), ALWAYS_SHARED(0x00040000), ALIGN_ICON_RIGHT(0x00080000),
		IMAGE_RESOURCE_ICON(0x00100000), FRAME_TARGET(0x00400000), TEXT_ONLY_IN_MENU(0x00800000), BUTTON_TO_RIGHT(0x01000000),
		HIDE_FROM_MOBILE(0x04000000), SHOW_IN_POPUPMENU(0x10000000), MAKE_SPLIT_BUTTON(0x20000000);

		public static final int ODS_FLAG_MASK = 0x75F9F1FF;

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

	// The actual signature for SIG_CD_ACTION is a WORD type, but the structure expects an LSIG for some reason
	private static final int SIG_BUFFER = 2;

	protected CDACTION(final SIG signature, final ByteBuffer data) {
		super(signature, data);

		System.out.println(this);
	}

	/**
	 * @return Type of action (formula, script, etc.)
	 */
	public Type getType() {
		short typeCode = getData().getShort(getData().position() + 0 + SIG_BUFFER);
		return Type.valueOf(typeCode);
	}

	//	public short getType() {
	//		return getData().getShort(getData().position() + 0 + SIG_BUFFER);
	//	}

	/**
	 * @return Index into array of icons
	 */
	public short getIconIndex() {
		return getData().getShort(getData().position() + 2 + SIG_BUFFER);
	}

	/**
	 * @return Action flags
	 */
	public Set<Flag> getFlags() {
		int flags = getData().getInt(getData().position() + 4 + SIG_BUFFER);
		return Flag.valuesOf(flags);
	}

	/**
	 * @return Length (in bytes) of action's title
	 */
	public short getTitleLen() {
		return getData().getShort(getData().position() + 8 + SIG_BUFFER);
	}

	/**
	 * @return Length (in bytes) of "hide when" formula
	 */
	public short getFormulaLen() {
		return getData().getShort(getData().position() + 10 + SIG_BUFFER);
	}

	/**
	 * @return Share ID of the Shared Action
	 */
	public int getShareId() {
		return getData().getInt(getData().position() + 12 + SIG_BUFFER);
	}

	public String getTitle() {
		int titleLen = getTitleLen() & 0xFFFF;

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 16 + SIG_BUFFER);
		data.limit(data.position() + titleLen);
		return ODSUtils.fromLMBCS(data);
	}

	public ByteBuffer getActionData() {
		int titleLen = getTitleLen() & 0xFFFF;
		int formulaLen = getFormulaLen() & 0xFFF;

		ByteBuffer data = getData().duplicate();
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(data.position() + 16 + titleLen + SIG_BUFFER);
		data.limit(data.position() + (getSignature().getLength() - 16 - titleLen - formulaLen));
		return data;
	}

	public NSFCompiledFormula getFormula() {
		int length = getFormulaLen();
		if (length > 0) {
			ByteBuffer data = getData().duplicate();
			data.position(data.limit() - getFormulaLen());
			return new NSFCompiledFormula(data);
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Type: " + getType() + ", IconIndex: " + getIconIndex() + ", Flags: " + getFlags()
				+ ", Title: " + getTitle() + ", Formula: " + getFormula() + ", ShareId: " + getShareId() + "]";
	}
}
