package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

public class CDREGIONEND extends CDRecord {

	public final Unsigned16 RegionNum = new Unsigned16();
	/**
	 * Use getRegionName for access.
	 */
	@Deprecated
	public final Unsigned8[] RegionName = array(new Unsigned8[CDREGIONBEGIN.MAXREGIONNAME + 1]);

	public CDREGIONEND(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDREGIONEND(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public String getRegionName() {
		return ODSUtils.fromLMBCS(RegionName);
	}

}
