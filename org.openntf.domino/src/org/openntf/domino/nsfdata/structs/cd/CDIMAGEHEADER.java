package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure is used to define a JPEG or GIF Image that is part of a Domino document. The CDIMAGEHEADER structure follows a CDGRAPHIC
 * structure. CDIMAGESEGMENT structure(s) then follow the CDIMAGEHEADER. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDIMAGEHEADER extends CDRecord {

	protected CDIMAGEHEADER(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Type of image (e.g., GIF, JPEG)
	 */
	public short getImageType() {
		// TODO create enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Width of the image (in pixels)
	 */
	public short getWidth() {
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return Height of the image (in pixels)
	 */
	public short getHeight() {
		return getData().getShort(getData().position() + 4);
	}

	/**
	 * @return Size (in bytes) of the image data
	 */
	public int getImageDataSize() {
		return getData().getInt(getData().position() + 6);
	}

	/**
	 * @return Number of CDIMAGESEGMENT records expected to follow
	 */
	public int getSegCount() {
		return getData().getInt(getData().position() + 10);
	}

	/**
	 * @return Flags (currently unused)
	 */
	public int getFlags() {
		// TODO create enum
		return getData().getInt(getData().position() + 14);
	}

	/**
	 * @return Reserved for future use
	 */
	public int getReserved() {
		return getData().getInt(getData().position() + 18);
	}
}
