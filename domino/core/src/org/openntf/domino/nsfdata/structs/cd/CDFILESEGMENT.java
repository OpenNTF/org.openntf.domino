package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the file segment data of a Cascading Style Sheet (CSS) and follows a CDFILEHEADER structure. The number of
 * segments in the file is specified in the CDFILEHEADER record. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDFILESEGMENT extends CDRecord {

	public final Unsigned16 DataSize = new Unsigned16();
	public final Unsigned16 SegSize = new Unsigned16();
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned32 Reserved = new Unsigned32();

	static {
		addVariableData("FileData", "DataSize");
	}

	public CDFILESEGMENT(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDFILESEGMENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	@Override
	public int getExtraLength() {
		return SegSize.get() - DataSize.get();
	}

	/**
	 * @return File bits for this segment
	 */
	public byte[] getFileData() {
		return (byte[]) getVariableElement("FileData");
	}
}
