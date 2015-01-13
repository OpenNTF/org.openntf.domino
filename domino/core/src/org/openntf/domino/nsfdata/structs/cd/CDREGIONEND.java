package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

public class CDREGIONEND extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 RegionNum = new Unsigned16();
	/**
	 * Use getRegionName for access.
	 */
	@Deprecated
	public final Unsigned8[] RegionName = array(new Unsigned8[CDREGIONBEGIN.MAXREGIONNAME + 1]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getRegionName() {
		return ODSUtils.fromLMBCS(RegionName);
	}

}
