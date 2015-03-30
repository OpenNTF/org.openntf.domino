package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The bitmap data is divided into segments to optimize data storage within Domino. It is recommended that each segment be no larger than
 * 10k bytes. For best display speed, the segments sould be as large as possible, up to the practical 10k limit. A scanline must be
 * contained within a single segment, and cannot be divided between two segments. A bitmap must contain at least one segment, but may have
 * many segments. (editods.h)
 *
 */
public class CDBITMAPSEGMENT extends CDRecord {
	public final LSIG Header = inner(new LSIG());
	public final Unsigned32[] Reserved = array(new Unsigned32[2]);
	public final Unsigned16 ScanlineCount = new Unsigned16();
	public final Unsigned16 DataSize = new Unsigned16();

	static {
		addVariableData("BitmapData", "DataSize");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	// TODO uncompress the data (see docs)
	public byte[] getBitmapData() {
		return (byte[]) getVariableElement("BitmapData");
	}
}
