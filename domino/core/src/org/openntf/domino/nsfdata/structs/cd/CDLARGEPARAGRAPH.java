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

	public static final short CDLARGEPARAGRAPH_BEGIN = 0x0001;
	public static final short CDLARGEPARAGRAPH_END = 0x0002;

	// TODO make enum
	public final Unsigned16 Version = new Unsigned16();
	// TODO make enum
	public final Unsigned16 Flags = new Unsigned16();
	public final Unsigned32[] Spare = array(new Unsigned32[2]);

	public CDLARGEPARAGRAPH(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDLARGEPARAGRAPH(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
