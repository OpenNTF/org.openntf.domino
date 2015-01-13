package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.LSIG;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the image segment data of a JPEG or GIF image and follows a CDIMAGEHEADER structure. The number of segments in the
 * image is contained in the CDIMAGEHEADER and specifies the number of CDIMAGESEGMENT structures to follow. An image segment size is 10250
 * bytes. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDIMAGESEGMENT extends CDRecord {

	public final LSIG Header = inner(new LSIG());
	public final Unsigned16 DataSize = new Unsigned16();
	public final Unsigned16 SegSize = new Unsigned16();

	static {
		addVariableData("Data", "DataSize");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public byte[] getImageData() {
		return (byte[]) getVariableElement("Data");
	}
}
