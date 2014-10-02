package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure is used to define a JPEG or GIF Image that is part of a Domino document. The CDIMAGEHEADER structure follows a CDGRAPHIC
 * structure. CDIMAGESEGMENT structure(s) then follow the CDIMAGEHEADER. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDIMAGEHEADER extends CDRecord {

	static {
		addFixed("ImageType", Short.class);
		addFixedUnsigned("Width", Short.class);
		addFixedUnsigned("Height", Short.class);
		addFixedUnsigned("ImageDataSize", Integer.class);
		addFixedUnsigned("SegCount", Integer.class);
		addFixed("Flags", Integer.class);
		addFixed("Reserved", Integer.class);
	}

	public static final int SIZE = getFixedStructSize();

	public CDIMAGEHEADER(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDIMAGEHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Type of image (e.g., GIF, JPEG)
	 */
	public short getImageType() {
		// TODO create enum
		return (Short) getStructElement("ImageType");
	}

	/**
	 * @return Width of the image (in pixels)
	 */
	public int getWidth() {
		return (Integer) getStructElement("Width");
	}

	/**
	 * @return Height of the image (in pixels)
	 */
	public int getHeight() {
		return (Integer) getStructElement("Height");
	}

	/**
	 * @return Size (in bytes) of the image data
	 */
	public long getImageDataSize() {
		return (Long) getStructElement("ImageDataSize");
	}

	/**
	 * @return Number of CDIMAGESEGMENT records expected to follow
	 */
	public long getSegCount() {
		return (Long) getStructElement("SegCount");
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
}
