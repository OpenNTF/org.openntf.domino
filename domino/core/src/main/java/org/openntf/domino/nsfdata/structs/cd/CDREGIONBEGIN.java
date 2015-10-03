package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This CD Record is used within mail templates. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDREGIONBEGIN extends CDRecord {

	public static final int MAXREGIONNAME = 35;

	public final WSIG Header = inner(new WSIG());
	public final Unsigned16 Version = new Unsigned16();
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned16 RegionNum = new Unsigned16();
	/**
	 * Use getRegionName for access.
	 */
	@Deprecated
	public final Unsigned8[] RegionName = array(new Unsigned8[MAXREGIONNAME + 1]);

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getRegionName() {
		return ODSUtils.fromLMBCS(RegionName);
	}
}
