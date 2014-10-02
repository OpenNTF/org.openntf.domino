package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * A portion of a Windows GDI metafile. This record must be preceded by a CDWINMETAHEADER record. Since Windows GDI metafiles can be large,
 * but Domino and Notes have an internal limit of 65,536 bytes (64kB) for a segment, a metafile may be divided into segments of up to 64kB;
 * each segment must be preceded by a CDWINMETASEG record. (editods.h)
 *
 */
public class CDWINMETASEG extends CDRecord {

	static {
		addFixedUnsigned("DataSize", Short.class);
		addFixedUnsigned("SegSize", Short.class);

		addVariableData("Data", "getDataSize");
	}

	public static final int SIZE = getFixedStructSize();

	public CDWINMETASEG(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDWINMETASEG(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return The actual size of metafile, in bytes (ignoring any filler)
	 */
	public int getDataSize() {
		return (Integer) getStructElement("DataSize");
	}

	/**
	 * @return The size of segment, in bytes
	 */
	public int getSegSize() {
		return (Integer) getStructElement("SegSize");
	}

	/**
	 * @return Windows Metafile Bits for this segment. Each segment must be <= 64K bytes.
	 */
	public byte[] getSegmentData() {
		return (byte[]) getStructElement("Data");
	}
}
