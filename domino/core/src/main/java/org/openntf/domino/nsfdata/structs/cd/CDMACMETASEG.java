package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A portion of a Macintosh metafile. This record must be preceded by a CDMACMETAHEADER record. Since metafiles can be large, but Domino and
 * Notes have an internal limit of 65,536 bytes (64kB) for a segment, a metafile may be divided into segments of up to 64kB; each segment
 * must be preceded by a CDMACMETASEG record. (editods.h)
 * 
 * @since forever
 *
 */
public class CDMACMETASEG extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final Unsigned16 DataSize = new Unsigned16();
	public final Unsigned16 SegSize = new Unsigned16();

	static {
		addVariableData("MetafileData", "DataSize");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public byte[] getMetafileData() {
		return (byte[]) getVariableElement("MetafileData");
	}
}
