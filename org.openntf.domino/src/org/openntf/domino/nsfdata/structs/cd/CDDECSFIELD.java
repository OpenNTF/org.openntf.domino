package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record gives information pertaining to data connection resource information in a field or form. (editods.h)
 * 
 * @author jgallagher
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

	public CDDECSFIELD(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public Set<Flag> getFlags() {
		return Flag.valuesOf(getData().getShort(getData().position() + 0));
	}

	public short getExternalNameLength() {
		return getData().getShort(getData().position() + 2);
	}

	public short getMetadataNameLength() {
		return getData().getShort(getData().position() + 4);
	}

	public short getDCRNameLength() {
		return getData().getShort(getData().position() + 6);
	}

	public short[] getSpare() {
		short[] result = new short[8];
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 8);
		for (int i = 0; i < 8; i++) {
			result[i] = data.getShort();
		}
		return result;
	}

	public String getExternalName() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 24);
		data.limit(data.position() + getExternalNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getMetadataName() {
		int preceding = getExternalNameLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 24 + preceding);
		data.limit(data.position() + getMetadataNameLength());
		return ODSUtils.fromLMBCS(data);
	}

	public String getDCRName() {
		int preceding = getExternalNameLength() + getMetadataNameLength();

		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 24 + preceding);
		data.limit(data.position() + getDCRNameLength());
		return ODSUtils.fromLMBCS(data);
	}
}
