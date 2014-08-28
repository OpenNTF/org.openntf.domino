package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the appearance of a button in a rich text field. (editods.h)
 *
 */
public class CDBUTTON extends CDRecord {
	/**
	 * Possible values for the Flags member of the CDBUTTON structure.
	 * 
	 * @since Lotus Notes/Domino 5.0.3
	 *
	 */
	public static enum Flag {
		/**
		 * Button is unused.
		 */
		UNUSED((short) 0x0000),
		/**
		 * Script is attached to this button.
		 */
		RUNFLAG_SCRIPT((short) 0x0001),
		/**
		 * Works with width flags BUTTON_RUNFLAG_FIXED, BUTTON_FUNFLAG_MINIMUM, BUTTON_RUNFLAG_CONTENT and BUTTON_RUNFLAG_WIDTHMASK.. If
		 * text doesn't "fit" in the specified width, determines if text should be wrapped (and button made taller) or not.
		 */
		RUNFLAG_NOWRAP((short) 0x0002),
		/**
		 * Button uses right-to-left reading order.
		 */
		RUNFLAG_RTL((short) 0x0100),
		/**
		 * Button has a fixed width of specified size.
		 */
		RUNFLAG_FIXED((short) 0x0200),
		/**
		 * Button has a fixed width of specified size UNLESS text is too wide to fit in which case it's made wider to accommodate the text.
		 */
		RUNFLAG_MINIMUM((short) 0x0400),
		/**
		 * Button is as wide as is necessary to accommodate the text.
		 */
		RUNFLAG_CONTENT((short) 0x0800),
		/**
		 * Button width is a fixed number of characters.
		 */
		RUNFLAG_PROPORTIONAL((short) 0x4000),
		/**
		 * Button has focus.
		 */
		FOCUS_ON((short) 0x8000),
		/**
		 * Button edges are rounded.
		 */
		EDGE_ROUNDED((short) 0x1000),
		/**
		 * Button edges are square.
		 */
		EDGE_SQUARE((short) 0x2000);

		/**
		 * Determines which of the flag bits are written to disk as opposed to just being used while the button "is running" to record the
		 * current state of the button (e.g., if the button has been pressed and is currently executing script bits). In other words, it's
		 * the AND mask for all the bits that are going to be written to disk.
		 */
		public static final short ODS_MASK = 0x7F02;
		/**
		 * AND mask to see if BUTTON_RUNFLAG_FIXED, BUTTON_RUNFLAG_MINIMUM, BUTTON_RUNFLAG_CONTENT, BUTTON_RUNFLAG_NOWRAP and
		 * BUTTON_RUNFLAG_PROPORTIONAL flags are set. If none of these flags are set, the button is "MAXIMUM" size. "Maximum" means the
		 * button is as wide as is necessary to accommodate the text up-to the specified maximum width. If the text doesn't fit at the
		 * maximum width, it's wrapped onto a second (third, fourth, ...) line and the button is made correspondingly taller. Minimum
		 * allowed value is "2.00".
		 */
		public static final short RUNFLAG_WIDTH_MASK = (short) (RUNFLAG_FIXED.getValue() | RUNFLAG_MINIMUM.getValue()
				| RUNFLAG_CONTENT.getValue() | RUNFLAG_PROPORTIONAL.getValue());

		private final short value_;

		private Flag(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static Set<Flag> valuesOf(final short flags) {
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
		addFixed("Flags", Short.class);
		addFixedUnsigned("Width", Short.class);
		addFixedUnsigned("Height", Short.class);
		addFixedUnsigned("Lines", Short.class);
		addFixed("FontID", FONTID.class);

		addVariableString("Text", "getTextLen");
	}

	public CDBUTTON(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Short) getStructElement("Flags"));
	}

	/**
	 * @return The width of the button in TWIPS (see the symbolic definition for ONEINCH for more information).
	 */
	public int getWidth() {
		return (Integer) getStructElement("Width");
	}

	/**
	 * Reserved. Should be set to NULL.
	 */
	public int getHeight() {
		return (Integer) getStructElement("Height");
	}

	/**
	 * @return The maximum number of lines of text to use to display the button text.
	 */
	public int getLines() {
		return (Integer) getStructElement("Lines");
	}

	public FONTID getFontId() {
		return (FONTID) getStructElement("FontID");
	}

	public int getTextLen() {
		return (int) (getDataLength() - 8 - FONTID.SIZE);
	}

	public String getText() {
		return (String) getStructElement("Text");
	}
}
