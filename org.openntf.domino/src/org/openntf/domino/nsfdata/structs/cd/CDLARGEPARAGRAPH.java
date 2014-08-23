package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * The 64K limit on paragraphs has been removed in Notes/Domino 6. To ensure backward compatibility, "large" paragraphs are broken into
 * smaller paragraphs which are bracketed by a CDLARGEPARAGRAPH record with its Flags member set to CDLARGEPARAGRAPH_BEGIN and a
 * CDLARGEPARAGRAPH record with its Flags member set to CDLARGEPARAGRAPH_END. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 */
public class CDLARGEPARAGRAPH extends CDRecord {

	static {
		addFixed("Version", Short.class);
		addFixed("Flags", Short.class);
		addFixedArray("Spare", Integer.class, 2);
	}

	public CDLARGEPARAGRAPH(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return See LARGEPARAGRAPH_xxx
	 */
	public short getVersion() {
		// TODO make enum
		return (Short) getStructElement("Version");
	}

	/**
	 * @return See CDLARGEPARAGRAPH_xxx
	 */
	public short getFlags() {
		// TODO make enum
		return (Short) getStructElement("Flags");
	}

	/**
	 * @return Future use
	 */
	public int[] getSpare() {
		return (int[]) getStructElement("Spare");
	}
}
