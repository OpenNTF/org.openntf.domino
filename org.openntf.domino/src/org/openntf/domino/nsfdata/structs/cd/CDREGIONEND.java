package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

public class CDREGIONEND extends CDRecord {

	static {
		addFixed("RegionNum", Short.class);
		addFixedArray("RegionName", Byte.class, CDREGIONBEGIN.MAXREGIONNAME + 1);
	}

	public static final int SIZE = getFixedStructSize();

	public CDREGIONEND(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
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
