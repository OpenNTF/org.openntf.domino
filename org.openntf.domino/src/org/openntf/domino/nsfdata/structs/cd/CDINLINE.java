package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record gives information pertaining to shared resources and/or shared code in a form. A CDINLINE record may be preceded by a
 * CDBEGINRECORD and followed by a CDRESOURCE and then a CDENDRECORD. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDINLINE extends CDRecord {

	/**
	 * These flags are values for the dwFlags member of CDINLINE. A CDINLINE record may be preceded by a CDBEGINRECORD and followed by a
	 * CDRESOURCE and then a CDENDRECORD. (editods.h)
	 * 
	 * @since Lotus Notes/Domino 6.0
	 *
	 */
	public static enum Flag {
		UNKNOWN(0x00000000), SCRIPT_LIB(0x00000001), STYLE_SHEET(0x00000002), HTML(0x00000004), HTMLFILERES(0x00000008);

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
		addFixedUnsigned("wDataLength", Short.class);
		addFixed("dwFlags", Integer.class);
		addFixedArray("dwReserved", Integer.class, 4);
	}

	public CDINLINE(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Integer) getStructElement("dwFlags"));
	}
}
