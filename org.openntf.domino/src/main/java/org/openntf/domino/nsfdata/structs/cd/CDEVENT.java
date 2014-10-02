package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD structure defines simple actions, formulas or LotusScript within a given image map. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDEVENT extends CDRecord {
	public static enum Flag {
		EVENT_HAS_LIBRARIES(0x00000001);

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

	public static enum EventType {
		ONCLICK((short) 1), ONDBLCLICK((short) 2), ONMOUSEDOWN((short) 3), ONMOUSEUP((short) 4), ONMOUSEOVER((short) 5),
		ONMOUSEMOVE((short) 6), ONMOUSEOUT((short) 7), ONKEYPRESS((short) 8), ONKEYDOWN((short) 9), ONKEYUP((short) 10),
		ONFOCUS((short) 11), ONBLUR((short) 12), ONLOAD((short) 13), ONUNLOAD((short) 14), HEADER((short) 15), ONSUBMIT((short) 16),
		ONRESET((short) 17), ONCHANGE((short) 18), ONERROR((short) 19), ONHELP((short) 20), ONSELECT((short) 21),
		/**
		 * This isn't really an event
		 */
		LIBRARY((short) 22);

		private final short value_;

		private EventType(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static EventType valueOf(final short typeCode) {
			for (EventType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching EventType found for type code " + typeCode);
		}
	}

	public static enum ActionType {
		FORMULA((short) 1), CANNED_ACTION((short) 2), LOTUS_SCRIPT((short) 3), JAVASCRIPT((short) 4);

		private final short value_;

		private ActionType(final short value) {
			value_ = value;
		}

		public short getValue() {
			return value_;
		}

		public static ActionType valueOf(final short typeCode) {
			for (ActionType type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching ActionType found for type code " + typeCode);
		}
	}

	static {
		addFixed("Flags", Integer.class);
		addFixed("EventType", Short.class);
		addFixed("ActionType", Short.class);
		addFixedUnsigned("ActionLength", Integer.class);
		addFixedUnsigned("SignatureLength", Short.class);
		addFixedArray("Reserved", Byte.class, 14);

		addVariableData("Action", "getActionLength");
		addVariableData("Signature", "getSignatureLength");
	}

	public static final int SIZE = getFixedStructSize();

	public CDEVENT(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDEVENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("Flags"));
	}

	public EventType getEventType() {
		return EventType.valueOf((Short) getStructElement("EventType"));
	}

	public ActionType getActionType() {
		return ActionType.valueOf((Short) getStructElement("ActionType"));
	}

	public int getActionLength() {
		return ((Long) getStructElement("ActionLength")).intValue();
	}

	public int getSignatureLength() {
		return (Integer) getStructElement("SignatureLength");
	}

	public byte[] getReserved() {
		return (byte[]) getStructElement("Reserved");
	}

	public byte[] getAction() {
		return (byte[]) getStructElement("Action");
	}

	public byte[] getEventSignature() {
		return (byte[]) getStructElement("Signature");
	}
}
