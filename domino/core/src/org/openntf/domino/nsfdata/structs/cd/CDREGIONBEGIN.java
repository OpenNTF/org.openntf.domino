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

	public final Unsigned16 Version = new Unsigned16();
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned16 RegionNum = new Unsigned16();
	/**
	 * Use getRegionName for access.
	 */
	@Deprecated
	public final Unsigned8[] RegionName = array(new Unsigned8[MAXREGIONNAME + 1]);

	public CDREGIONBEGIN(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDREGIONBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public String getRegionName() {
		return ODSUtils.fromLMBCS(RegionName);
	}
}
