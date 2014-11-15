package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record allows for additional information to be provided for a graphic. (editods.h)
 * 
 * @since Lotus Notes/Domino 5.0
 *
 */
public class CDVERTICALALIGN extends CDRecord {
	public static enum AlignmentType {
		BASELINE, CENTER, TOP, UNUSED3, UNUSED4, BOTTOM
	}

	public final Enum16<AlignmentType> Alignment = new Enum16<AlignmentType>(AlignmentType.values());

	public CDVERTICALALIGN(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDVERTICALALIGN(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}
}
