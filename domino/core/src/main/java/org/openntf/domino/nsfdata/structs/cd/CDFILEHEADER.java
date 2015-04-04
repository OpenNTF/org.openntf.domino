package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure is used to define a Cascading Style Sheet (CSS) that is part of a Domino database. CDFILESEGMENT structure(s) follow the
 * CDFILEHEADER. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDFILEHEADER extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final Unsigned16 FileExtLen = new Unsigned16();
	public final Unsigned32 FileDataSize = new Unsigned32();
	public final Unsigned32 SegCount = new Unsigned32();
	public final Unsigned32 Flags = new Unsigned32();
	public final Unsigned32 Reserved = new Unsigned32();

	static {
		addVariableAsciiString("FileExt", "FileExtLen");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	/**
	 * @return The file extension for the file
	 */
	public String getFileExt() {
		return (String) getVariableElement("FileExt");
	}

	public void setFileExt(final String fileExt) {
		setVariableElement("FileExt", fileExt);
	}
}
