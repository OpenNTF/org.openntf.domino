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

	public CDTABLELABEL(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public String getLabel() {
		ByteBuffer data = getData().duplicate();
		data.limit(data.position() + 128);
		return ODSUtils.fromLMBCS(data);
	}

	public short[] getReserved() {
		short[] result = new short[3];
		for (int i = 0; i < 3; i++) {
			result[i] = getData().getShort(getData().position() + 128 + (i * 2));
		}
		return result;
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf(getData().getShort(getData().position() + 134));
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Label=" + getLabel() + ", Flags=" + getFlags() + "]";
	}
}
