package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD structure defines simple actions, formulas or LotusScript within a given image map. (editods.h)
 * 
 * @author jgallagher
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

	public CDEVENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf(getData().getInt(getData().position() + 0));
	}

	public EventType getEventType() {
		return EventType.valueOf(getData().getShort(getData().position() + 4));
	}

	public ActionType getActionType() {
		return ActionType.valueOf(getData().getShort(getData().position() + 6));
	}

	public long getActionLength() {
		return getData().getInt(getData().position() + 8) & 0xFFFFFFFFl;
	}

	public int getSignatureLength() {
		return getData().getShort(getData().position() + 12) & 0xFFFF;
	}

	public byte[] getReserved() {
		byte[] result = new byte[14];
		for (int i = 0; i < result.length; i++) {
			result[i] = getData().get(getData().position() + 14 + i);
		}
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Flags: " + getFlags() + ", EventType: " + getEventType() + ", ActionType: "
				+ getActionType() + "]";
	}
}
