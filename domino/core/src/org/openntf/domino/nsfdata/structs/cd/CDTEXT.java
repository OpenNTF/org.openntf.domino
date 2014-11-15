package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the start of a run of text in a rich-text field. (editods.h)
 *
 */
public class CDTEXT extends CDRecord {

	public final FONTID FontID = inner(new FONTID());

	static {
		addVariableString("Text", "getTextLength");
	}

	public CDTEXT(final CDSignature cdSig) {
		super(cdSig);
	}

	public CDTEXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public String getText() {
		return (String) getVariableElement("Text");
	}

	public void setText(final String text) {
		int resultSize = setVariableElement("Text", text);
		getSignature().setDataLength((int) (resultSize + getStructSize()));
	}

	public int getTextLength() {
		return (int) (getDataLength() - FontID.getStructSize());
	}

	@Override
	public int getExtraLength() {
		// Text is always an even number of bytes in LMBCS, even when Length doesn't indicate this
		return getTextLength() % 2;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Font ID: " + FontID + ", Text: " + getText() + "]";
	}
}
