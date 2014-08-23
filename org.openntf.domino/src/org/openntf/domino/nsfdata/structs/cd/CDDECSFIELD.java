package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record gives information pertaining to data connection resource information in a field or form. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 */
public class CDDECSFIELD extends CDRecord {
	public static enum Flag {
		KEY_FIELD((short) 0x001), STORE_LOCALLY((short) 0x002);

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
		addFixedUpgrade("ExternalNameLength", Short.class);
		addFixedUpgrade("MetadataNameLength", Short.class);
		addFixedUpgrade("DCRNameLength", Short.class);
		addFixedArray("Spare", Short.class, 8);

		addVariableString("ExternalName", "getExternalNameLength");
		addVariableString("MetadataName", "getMetadataNameLength");
		addVariableString("DCRName", "getDCRNameLength");
	}

	public CDDECSFIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((Short) getStructElement("Flags"));
	}

	public int getExternalNameLength() {
		return (Integer) getStructElement("ExternalNameLength");
	}

	public int getMetadataNameLength() {
		return (Integer) getStructElement("MetadataNameLength");
	}

	public int getDCRNameLength() {
		return (Integer) getStructElement("DCRNameLength");
	}

	public short[] getSpare() {
		return (short[]) getStructElement("Spare");
	}

	public String getExternalName() {
		return (String) getStructElement("ExternalName");
	}

	public String getMetadataName() {
		return (String) getStructElement("MetadataName");
	}

	public String getDCRName() {
		return (String) getStructElement("DCRName");
	}
}
