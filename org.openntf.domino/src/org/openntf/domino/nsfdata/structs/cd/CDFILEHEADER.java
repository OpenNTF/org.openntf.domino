package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure is used to define a Cascading Style Sheet (CSS) that is part of a Domino database. CDFILESEGMENT structure(s) follow the
 * CDFILEHEADER. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDFILEHEADER extends CDRecord {

	static {
		addFixedUnsigned("FileExtLen", Short.class);
		addFixedUnsigned("FileDataSize", Integer.class);
		addFixedUnsigned("SegCount", Integer.class);
		addFixed("Flags", Integer.class);
		addFixed("Reserved", Integer.class);

		addVariableString("FileExt", "getFileExtLen");
	}

	protected CDFILEHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Length of file extenstion [sic]
	 */
	public int getFileExtLen() {
		return (Integer) getStructElement("FileExtLen");
	}

	/**
	 * @return Size (in bytes) of the file data
	 */
	public int getFileDataSize() {
		return ((Long) getStructElement("FileDataSize")).intValue();
	}

	/**
	 * @return Number of CDFILESEGMENT records expected to follow
	 */
	public int getSegCount() {
		return ((Long) getStructElement("SegCount")).intValue();
	}

	/**
	 * @return Flags (currently unused)
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
	 * @return The file extension for the file
	 */
	public String getFileExt() {
		return (String) getStructElement("FileExt");
	}
}
