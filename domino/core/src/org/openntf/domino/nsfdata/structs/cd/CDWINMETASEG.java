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

	public final Unsigned16 DataSize = new Unsigned16();
	public final Unsigned16 SegSize = new Unsigned16();

	static {
		addVariableData("Data", "DataSize");
	}

	public CDWINMETASEG(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDWINMETASEG(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Windows Metafile Bits for this segment. Each segment must be <= 64K bytes.
	 */
	public byte[] getSegmentData() {
		return (byte[]) getVariableElement("Data");
	}
}
