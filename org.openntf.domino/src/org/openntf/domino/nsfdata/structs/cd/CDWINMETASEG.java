package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * A portion of a Windows GDI metafile. This record must be preceded by a CDWINMETAHEADER record. Since Windows GDI metafiles can be large,
 * but Domino and Notes have an internal limit of 65,536 bytes (64kB) for a segment, a metafile may be divided into segments of up to 64kB;
 * each segment must be preceded by a CDWINMETASEG record. (editods.h)
 *
 */
public class CDWINMETASEG extends CDRecord {

	public CDWINMETASEG(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return The actual size of metafile, in bytes (ignoring any filler)
	 */
	public int getDataSize() {
		return getData().getShort(getData().position() + 0) & 0xFFFF;
	}

	/**
	 * @return The size of segment, in bytes
	 */
	public int getSegSize() {
		return getData().getShort(getData().position() + 2) & 0xFFFF;
	}

	/**
	 * @return Windows Metafile Bits for this segment. Each segment must be <= 64K bytes.
	 */
	public byte[] getSegmentData() {
		int length = getDataSize();
		byte[] result = new byte[length];
		getData().get(result, 4, length);
		return result;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": DataSize=" + getDataSize() + ", SegSize=" + getSegSize() + "]";
	}
}
