package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record further defines information for a table. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDTABLELABEL extends CDRecord {
	public static enum Flag {
		ROWLABEL((short) 0x0001), TABLABEL((short) 0x002);

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
		addFixedArray("Label", Byte.class, 128);
		addFixedArray("Reserved", Short.class, 3);
		addFixed("Flags", Short.class);
	}

	public CDTABLELABEL(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public String getLabel() {
		return ODSUtils.fromLMBCS((byte[]) getStructElement("Label"));
	}

	public short[] getReserved() {
		return (short[]) getStructElement("Reserved");
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Short) getStructElement("Flags"));
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Label=" + getLabel() + ", Flags=" + getFlags() + "]";
	}
}
