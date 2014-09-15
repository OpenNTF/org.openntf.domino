package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure defines the file segment data of a Cascading Style Sheet (CSS) and follows a CDFILEHEADER structure. The number of
 * segments in the file is specified in the CDFILEHEADER record. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDFILESEGMENT extends CDRecord {

	static {
		addFixedUnsigned("DataSize", Short.class);
		addFixedUnsigned("SegSize", Short.class);
		addFixed("Flags", Integer.class);
		addFixed("Reserved", Integer.class);

		addVariableData("FileData", "DataSize");
	}

	public static final int SIZE = getFixedStructSize();

	public CDFILESEGMENT(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDFILESEGMENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	@Override
	public int getExtraLength() {
		return getSegSize() - getDataSize();
	}

	/**
	 * @return Actual Size of image [sic] bits in bytes, ignoring any filler
	 */
	public int getDataSize() {
		return (Integer) getStructElement("DataSize");
	}

	/**
	 * @return Size of segment, is equal to or larger than DataSize if filler byte added to maintain word boundary
	 */
	public int getSegSize() {
		return (Integer) getStructElement("SegSize");
	}

	/**
	 * @return Currently unused, but someday someone will be happy this is here
	 */
	public int getFlags() {
		return (Integer) getStructElement("Flags");
	}

	/**
	 * @return Reserved for future use
	 */
	public int getReserved() {
		return (Integer) getStructElement("Reserved");
	}

	/**
	 * @return File bits for this segment
	 */
	public byte[] getFileData() {
		return (byte[]) getStructElement("FileData");
	}
}
