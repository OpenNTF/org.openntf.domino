package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the start of a "hot" region in a rich text field. Clicking on a hot region causes some other action to occur.
 * For instance, clicking on a popup will cause a block of text associated with that popup to be displayed. (editods.h)
 * 
 * @author jgallagher
 *
 */
public class CDHOTSPOTBEGIN extends CDRecord {

	public CDHOTSPOTBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return HOTSPOTREC_TYPE_xxx
	 */
	public short getType() {
		// TODO make enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return HOTSPOTREC_RUNFLAG_xxx
	 */
	public int getFlags() {
		// TODO make enum
		return getData().getInt(getData().position() + 2);
	}

	public int getHotspotDataLength() {
		return getData().getShort(getData().position() + 6) & 0xFFFF;
	}

	public ByteBuffer getHotspotData() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 8);
		return data;
	}

	// TODO add accessors for data
}
