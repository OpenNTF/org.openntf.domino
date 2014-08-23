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

	public CDWINMETAHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return The Windows mapping mode
	 */
	public short getMappingMode() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return The width of the drawing in world coordinates
	 */
	public short getWidth() {
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return The height of the drawing in world coordinates
	 */
	public short getHeight() {
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return The original display size of the metafile, measured in "twips" (1/1440 inch)
	 */
	public RECTSIZE getOriginalDisplaySize() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 6);
		data.limit(data.position() + RECTSIZE.SIZE);
		return new RECTSIZE(data);
	}

	/**
	 * @return The total size of the metafile data in bytes
	 */
	public long getMetafileSize() {
		return getData().getInt(getData().position() + RECTSIZE.SIZE + 6) & 0xFFFFFFFF;
	}

	public int getSegCount() {
		return getData().getShort(getData().position() + RECTSIZE.SIZE + 10) & 0xFFFF;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": MappingMode=" + getMappingMode() + ", Width=" + getWidth() + ", Height=" + getHeight()
				+ ", OriginalDisplaySize=" + getOriginalDisplaySize() + ", MetafileSize=" + getMetafileSize() + ", SegCount="
				+ getSegCount() + "]";
	}
}
