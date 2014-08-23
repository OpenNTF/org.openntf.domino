package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the start of a run of text in a rich-text field. (editods.h)
 *
 */
public class CDTEXT extends CDRecord {

	static {
		addFixed("FontID", FONTID.class);

		addVariableString("Text", "getTextLength");
	}

	public CDTEXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public FONTID getFontId() {
		return (FONTID) getStructElement("FontID");
	}

	public String getText() {
		return (String) getStructElement("Text");
	}

	public int getTextLength() {
		return getDataLength() - 4;
	}

	@Override
	public int getExtraLength() {
		// Text is always an even number of bytes in LMBCS, even when Length doesn't indicate this
		return getTextLength() % 2;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Font ID: " + getFontId() + ", Text: " + getText() + "]";
	}
}
