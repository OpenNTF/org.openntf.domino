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

	static {
		addFixed("mm", Short.class);
		addFixed("xExt", Short.class);
		addFixed("yExt", Short.class);
		addFixed("OriginalDisplaySize", RECTSIZE.class);
		addFixedUnsigned("MetafileSize", Integer.class);
		addFixedUnsigned("SegCount", Short.class);
	}

	public CDWINMETAHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return The Windows mapping mode
	 */
	public short getMappingMode() {
		return (Short) getStructElement("mm");
	}

	/**
	 * @return The width of the drawing in world coordinates
	 */
	public short getWidth() {
		return (Short) getStructElement("xExt");
	}

	/**
	 * @return The height of the drawing in world coordinates
	 */
	public short getHeight() {
		return (Short) getStructElement("yExt");
	}

	/**
	 * @return The original display size of the metafile, measured in "twips" (1/1440 inch)
	 */
	public RECTSIZE getOriginalDisplaySize() {
		return (RECTSIZE) getStructElement("OriginalDisplaySize");
	}

	/**
	 * @return The total size of the metafile data in bytes
	 */
	public long getMetafileSize() {
		return (Long) getStructElement("MetafileSize");
	}

	public int getSegCount() {
		return (Integer) getStructElement("SegCount");
	}
}
