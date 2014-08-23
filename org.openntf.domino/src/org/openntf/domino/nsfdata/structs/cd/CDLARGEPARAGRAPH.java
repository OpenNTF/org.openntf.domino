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

	public CDLARGEPARAGRAPH(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	/**
	 * @return See LARGEPARAGRAPH_xxx
	 */
	public short getVersion() {
		// TODO make enum
		return getData().getShort(getData().position() + 0);
	}

	/**
	 * @return See CDLARGEPARAGRAPH_xxx
	 */
	public short getFlags() {
		// TODO make enum
		return getData().getShort(getData().position() + 2);
	}

	/**
	 * @return Future use
	 */
	public int[] getSpare() {
		int[] result = new int[2];
		result[0] = getData().getInt(getData().position() + 4);
		result[1] = getData().getInt(getData().position() + 8);
		return result;
	}
}
