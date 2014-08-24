package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

public class CDREGIONEND extends CDRecord {

	static {
		addFixed("RegionNum", Short.class);
		addFixedArray("RegionName", Byte.class, CDREGIONBEGIN.MAXREGIONNAME + 1);
	}

	public CDREGIONEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public short getRegionNum() {
		return (Short) getStructElement("RegionNum");
	}

	public String getRegionName() {
		return ODSUtils.fromLMBCS((byte[]) getStructElement("RegionName"));
	}

}
