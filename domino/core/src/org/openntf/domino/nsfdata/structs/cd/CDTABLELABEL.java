package org.openntf.domino.nsfdata.structs.cd;

import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

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

	public final WSIG Header = inner(new WSIG());
	/**
	 * Use getLabel for access.
	 */
	@Deprecated
	public final Unsigned8[] Label = array(new Unsigned8[128]);
	public final Unsigned16[] Reserved = array(new Unsigned16[3]);
	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned16 Flags = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getLabel() {
		return ODSUtils.fromLMBCS(Label);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((short) Flags.get());
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Label=" + getLabel() + ", Flags=" + getFlags() + "]";
	}
}
