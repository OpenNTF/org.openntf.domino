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

	static {
		addFixed("DestSize", RECTSIZE.class);
		addFixed("CropSize", RECTSIZE.class);
		addFixed("CropOffset", CROPRECT.class);
		addFixed("fResize", Short.class);
		addFixed("Version", Byte.class);
		addFixed("bFlags", Byte.class);
		addFixed("wReserved", Short.class);
	}

	protected CDGRAPHIC(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return Destination Display size in TWIPS (1/1440 inch)
	 */
	public RECTSIZE getDestSize() {
		return (RECTSIZE) getStructElement("DestSize");
	}

	/**
	 * @return Reserved
	 */
	public RECTSIZE getCropSize() {
		return (RECTSIZE) getStructElement("CropSize");
	}

	/**
	 * @return Reserved
	 */
	public CROPRECT getCropOffset() {
		return (CROPRECT) getStructElement("CropOffset");
	}

	/**
	 * @return True if user resized object
	 */
	public boolean isResize() {
		return (Short) getStructElement("fRezize") != 0;
	}

	/**
	 * @return CDGRAPHIC_VERSIONxxx
	 */
	public byte getVersion() {
		// TODO create enum
		return (Byte) getStructElement("Version");
	}

	/**
	 * @return Ignored before CDGRAPHIC_VERSION3
	 */
	public byte getFlags() {
		// TODO create enum
		return (Byte) getStructElement("bFlags");
	}

	public short getReserved() {
		return (Short) getStructElement("wReserved");
	}
}
