package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.NSFCompiledFormula;
import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The designer of a form or view may define custom actions associated with that form or view. Actions may be presented to the user as
 * buttons on the action button bar or as options on the "Actions" menu. (actods.h)
 * 
 * @since Lotus Notes/Domino 4.5
 *
 */
public class CDACTION extends CDRecord {
	/**
	 * The action type based on the API documentation (ACTION_xxx (type)).
	 */
	public static enum Type {
		RUN_FORMULA((short) 1), RUN_SCRIPT((short) 2), RUN_AGENT((short) 3), OLDSYS_COMMAND((short) 4), SYS_COMMAND((short) 5),
		PLACEHOLDER((short) 7), RUN_JAVASCRIPT((short) 8);

		private final short value_;

		private Type(final short value) {
			value_ = value;
		}

		public short getValue() {
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
		/**
		 * Display in the "Actions" pull-down menu
		 */
		SHOW_IN_MENU(0x00000001),
		/**
		 * Display in the action button bar
		 */
		SHOW_IN_BAR(0x00000002),
		/**
		 * Show the action when previewing the document
		 */
		SHOW_WHEN_PREVIEWING(0x00000004),
		/**
		 * Show the action when reading the document
		 */
		SHOW_WHEN_READING(0x00000008),
		/**
		 * Show the action when editing the document
		 */
		SHOW_WHEN_EDITING(0x00000010),
		/**
		 * Show when launching OLE object
		 */
		SHOW_ON_OLE_LAUNCH(0x00000020),
		/**
		 * Close the OLE object when this action is selected
		 */
		OLE_CLOSE_WHEN_CHOSEN(0x00000040),
		/**
		 * No "hide when" formula is stored for this action
		 */
		NO_FORMULA(0x00000080),
		/**
		 * Show when editing in the preview window
		 */
		SHOW_WHEN_PREVEDITING(0x00000100),
		/**
		 * Bring the OLE document window to the front when this action is selected
		 */
		OLE_DOC_WINDOW_TO_FRONT(0x00001000),
		/**
		 * Do not display in Notes
		 */
		HIDE_FROM_NOTES(0x00002000),
		/**
		 * Do not display in Web browsers
		 */
		HIDE_FROM_WEB(0x00004000),
		/**
		 * Action reading order from right
		 */
		READING_ORDER_RTL(0x00008000),
		/**
		 * Action is shared
		 */
		SHARED(0x00010000),
		/**
		 * Action has been modified (not saved on disk)
		 */
		MODIFIED(0x00020000),
		/**
		 * Flag not saved on disk
		 */
		ALWAYS_SHARED(0x00040000),
		/**
		 * Right align action button
		 */
		ALIGN_ICON_RIGHT(0x00080000),
		/**
		 * Use graphic image icon
		 */
		IMAGE_RESOURCE_ICON(0x00100000),
		/**
		 * Use target frame
		 */
		FRAME_TARGET(0x00400000),
		/**
		 * Shows only icon in button bar. (Displays text in the Action menu if ACTION_SHOW_IN_MENU flag is set)
		 */
		TEXT_ONLY_IN_MENU(0x00800000),
		/**
		 * Show button on opposite side from action bar direction
		 */
		BUTTON_TO_RIGHT(0x01000000),
		/**
		 * Hide action from mobile users
		 */
		HIDE_FROM_MOBILE(0x04000000),
		/**
		 * Show action in pop-up menu
		 */
		SHOW_IN_POPUPMENU(0x10000000),
		/**
		 * LI: 4602.02, Provide support for "Split button" for java action bar
		 */
		MAKE_SPLIT_BUTTON(0x20000000);

		/**
		 * need update to include the bitwise flags for the Flags field. ex. one for LI 4602.02
		 */
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

	static {
		addFixed("Type", Short.class);
		addFixedUpgrade("IconIndex", Short.class);
		addFixed("Flags", Integer.class);
		addFixedUpgrade("TitleLen", Short.class);
		addFixedUpgrade("FormulaLen", Short.class);
		addFixed("ShareId", Integer.class);

		addVariableString("Title", "getTitleLen");
		addVariableData("ActionData", "getActionDataLen");
		addVariableData("Formula", "getFormulaLen");
	}

	protected CDACTION(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Type of action (formula, script, etc.)
	 */
	public Type getType() {
		short typeCode = (Short) getStructElement("Type");
		return Type.valueOf(typeCode);
	}

	/**
	 * @return Index into array of icons
	 */
	public int getIconIndex() {
		return (Integer) getStructElement("IconIndex");
	}

	/**
	 * @return Action flags
	 */
	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("Flags"));
	}

	/**
	 * @return Length (in bytes) of action's title
	 */
	public int getTitleLen() {
		return (Integer) getStructElement("TitleLen");
	}

	/**
	 * @return Length (in bytes) of "hide when" formula
	 */
	public int getFormulaLen() {
		return (Integer) getStructElement("FormulaLen");
	}

	/**
	 * @return Share ID of the Shared Action
	 */
	public int getShareId() {
		return (Integer) getStructElement("ShareId");
	}

	public String getTitle() {
		return (String) getStructElement("Title");
	}

	public int getActionDataLen() {
		// This is an oddball one, since there's no ActionDataLen - it's implied by the total length minus everything else
		return getSignature().getLength() - LSIG.SIZE - 16 - getTitleLen() - getFormulaLen();
	}

	public byte[] getActionData() {
		return (byte[]) getStructElement("ActionData");
	}

	public NSFCompiledFormula getFormula() {
		int length = getFormulaLen();
		if (length > 0) {
			return new NSFCompiledFormula((byte[]) getStructElement("Formula"));
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Type: " + getType() + ", IconIndex: " + getIconIndex() + ", Flags: " + getFlags()
				+ ", Title: " + getTitle() + ", ActionData=" + getActionData() + ", Formula: " + getFormula() + ", ShareId: "
				+ getShareId() + "]";
	}
}
