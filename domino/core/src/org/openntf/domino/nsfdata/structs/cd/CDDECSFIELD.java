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

	/**
	 * Use getFlags for access.
	 */
	@Deprecated
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned16 ExternalNameLength = new Unsigned16();
	public final Unsigned16 MetadataNameLength = new Unsigned16();
	public final Unsigned16 DCRNameLength = new Unsigned16();
	public final Unsigned16[] Spare = array(new Unsigned16[8]);

	static {
		addVariableString("ExternalName", "ExternalNameLength");
		addVariableString("MetadataName", "MetadataNameLength");
		addVariableString("DCRName", "DCRNameLength");
	}

	public CDDECSFIELD(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDDECSFIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf((short) Flags.get());
	}

	public String getExternalName() {
		return (String) getVariableElement("ExternalName");
	}

	public String getMetadataName() {
		return (String) getVariableElement("MetadataName");
	}

	public String getDCRName() {
		return (String) getVariableElement("DCRName");
	}
}
