package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.RECTSIZE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * Identifies a Windows Graphics Device Interface (GDI) metafile embedded in a rich text field. This record must be preceded by a CDGRAPHIC
 * record. Since Windows GDI metafiles can be large, but Domino and Notes have an internal limit of 65,536 bytes (64kB) for a segment, a
 * metafile may be divided into segments of up to 64kB; each segment must be preceded by a CDWINMETASEG record. (editods.h)
 *
 */
public class CDWINMETAHEADER extends CDRecord {

	public final Signed16 mm = new Signed16();
	public final Signed16 xExt = new Signed16();
	public final Signed16 yExt = new Signed16();
	public final RECTSIZE OriginalDisplaySize = inner(new RECTSIZE());
	public final Unsigned32 MetafileSize = new Unsigned32();
	public final Unsigned16 SegCount = new Unsigned16();

	public CDWINMETAHEADER(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDWINMETAHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

}
