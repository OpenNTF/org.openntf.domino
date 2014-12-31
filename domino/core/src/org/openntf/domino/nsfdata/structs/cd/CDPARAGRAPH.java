package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the start of a new paragraph within a rich-text field. Each paragraph in a rich text field may have different
 * style attributes, such as indentation and interline spacing. Use this structure when accessing a rich text field at the level of the CD
 * records. (editods.h)
 *
 */
public class CDPARAGRAPH extends CDRecord {

	public CDPARAGRAPH(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDPARAGRAPH(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

}
