package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure defines the image segment data of a JPEG or GIF image and follows a CDIMAGEHEADER structure. The number of segments in the
 * image is contained in the CDIMAGEHEADER and specifies the number of CDIMAGESEGMENT structures to follow. An image segment size is 10250
 * bytes. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDIMAGESEGMENT extends CDRecord {

	static {
		addFixedUnsigned("DataSize", Short.class);
		addFixedUnsigned("SegSize", Short.class);

		addVariableData("Data", "getDataSize");
	}

	public static final int SIZE = getFixedStructSize();

	public CDIMAGESEGMENT(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDIMAGESEGMENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Actual Size of image bits in bytes, ignoring any filler
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

	public byte[] getImageData() {
		return (byte[]) getStructElement("Data");
	}
}
