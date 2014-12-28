package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.RECTSIZE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Identifies a Macintosh metafile embedded in a rich text field. This record must be preceded by a CDGRAPHIC record. Since metafiles can be
 * large, but Domino and Notes has an internal limit of 65,536 bytes (64kB) for a segment, a metafile may be divided into segments of up to
 * 64kB; each segment must be preceded by a CDMACMETASEG record. (editods.h)
 * 
 * @since forever
 *
 */
public class CDMACMETAHEADER extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final RECTSIZE OriginalDisplaySize = inner(new RECTSIZE());
	public final Unsigned32 MetafileSize = new Unsigned32();
	public final Unsigned16 SegCount = new Unsigned16();

	@Override
	public SIG getHeader() {
		return Header;
	}

}
