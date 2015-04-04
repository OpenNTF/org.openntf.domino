package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.CROPRECT;
import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.RECTSIZE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The CDGRAPHIC record contains information used to control display of graphic objects in a document. This record marks the beginning of a
 * composite graphic object, and must be present for any graphic object to be loaded or displayed. (editods.h)
 *
 */
public class CDGRAPHIC extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final RECTSIZE DestSize = inner(new RECTSIZE());
	public final RECTSIZE CropSize = inner(new RECTSIZE());
	public final CROPRECT CropOffset = inner(new CROPRECT());
	public final Unsigned16 fResize = new Unsigned16();
	// TODO make enum
	public final Unsigned8 Version = new Unsigned8();
	public final Unsigned8 bFlags = new Unsigned8();
	public final Unsigned16 wReserved = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

}
