package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD Record is used within mail templates. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDREGIONBEGIN extends CDRecord {

	public static final int MAXREGIONNAME = 35;

	static {
		addFixed("Version", Short.class);
		addFixed("Flags", Short.class);
		addFixed("RegionNum", Short.class);
		addFixedArray("RegionName", Byte.class, MAXREGIONNAME + 1);
	}

	public CDREGIONBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public short getVersion() {
		return (Short) getStructElement("Version");
	}

	public short getFlags() {
		return (Short) getStructElement("Flags");
	}

	public short getRegionNum() {
		return (Short) getStructElement("RegionNum");
	}

	public String getRegionName() {
		return ODSUtils.fromLMBCS((byte[]) getStructElement("RegionName"));
	}
}
