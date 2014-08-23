package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure specifies the start of a "hot" region in a rich text field. Clicking on a hot region causes some other action to occur.
 * For instance, clicking on a popup will cause a block of text associated with that popup to be displayed. (editods.h)
 *
 */
public class CDHOTSPOTBEGIN extends CDRecord {

	static {
		addFixed("Type", Short.class);
		addFixed("Flags", Integer.class);
		addFixedUnsigned("DataLength", Short.class);

		addVariableData("Data", "getHotspotDataLength");
	}

	public CDHOTSPOTBEGIN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return HOTSPOTREC_TYPE_xxx
	 */
	public short getType() {
		// TODO make enum
		return (Short) getStructElement("Type");
	}

	/**
	 * @return HOTSPOTREC_RUNFLAG_xxx
	 */
	public int getFlags() {
		// TODO make enum
		return (Integer) getStructElement("Flags");
	}

	public int getHotspotDataLength() {
		return (Integer) getStructElement("DataLength");
	}

	public byte[] getHotspotData() {
		return (byte[]) getStructElement("Data");
	}

	// TODO add accessors for data
	//			/*  if HOTSPOTREC_RUNFLAG_SIGNED, WORD SigLen then SigData follows. */
}
