package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.CROPRECT;
import org.openntf.domino.nsfdata.structs.RECTSIZE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The CDGRAPHIC record contains information used to control display of graphic objects in a document. This record marks the beginning of a
 * composite graphic object, and must be present for any graphic object to be loaded or displayed. (editods.h)
 *
 */
public class CDGRAPHIC extends CDRecord {

	protected CDGRAPHIC(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Destination Display size in TWIPS (1/1440 inch)
	 */
	public RECTSIZE getDestSize() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 0);
		data.limit(data.position() + RECTSIZE.SIZE);
		return new RECTSIZE(data);
	}

	/**
	 * @return Reserved
	 */
	public RECTSIZE getCropSize() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + RECTSIZE.SIZE);
		data.limit(data.position() + RECTSIZE.SIZE);
		return new RECTSIZE(data);
	}

	/**
	 * @return Reserved
	 */
	public CROPRECT getCropOffset() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + RECTSIZE.SIZE + RECTSIZE.SIZE);
		data.limit(data.position() + CROPRECT.SIZE);
		return new CROPRECT(data);
	}

	/**
	 * @return True if user resized object
	 */
	public boolean isResize() {
		short value = getData().getShort(getData().position() + RECTSIZE.SIZE + RECTSIZE.SIZE + CROPRECT.SIZE);
		return value != 0;
	}

	/**
	 * @return CDGRAPHIC_VERSIONxxx
	 */
	public byte getVersion() {
		// TODO create enum
		return getData().get(getData().position() + 2 + RECTSIZE.SIZE + RECTSIZE.SIZE + CROPRECT.SIZE);
	}

	/**
	 * @return Ignored before CDGRAPHIC_VERSION3
	 */
	public byte getFlags() {
		// TODO create enum
		return getData().get(getData().position() + 3 + RECTSIZE.SIZE + RECTSIZE.SIZE + CROPRECT.SIZE);
	}

	public short getReserved() {
		return getData().getShort(getData().position() + 4 + RECTSIZE.SIZE + RECTSIZE.SIZE + CROPRECT.SIZE);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": DestSize=" + getDestSize() + ", CropSize=" + getCropSize() + ", CropOffset="
				+ getCropOffset() + ", Resize=" + isResize() + ", Version=" + getVersion() + ", Flags=" + getFlags() + "]";
	}
}
