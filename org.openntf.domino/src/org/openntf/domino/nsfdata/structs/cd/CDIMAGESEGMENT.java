package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

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

	protected CDIMAGESEGMENT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Actual Size of image bits in bytes, ignoring any filler
	 */
	public short getDataSize() {
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return Size of segment, is equal to or larger than DataSize if filler byte added to maintain word boundary
	 */
	public short getSegSize() {
		return getData().getShort(getData().position() + 2);
	}

	public ByteBuffer getImageData() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 4);
		data.limit(data.position() + getDataSize());
		return data;
	}
}
