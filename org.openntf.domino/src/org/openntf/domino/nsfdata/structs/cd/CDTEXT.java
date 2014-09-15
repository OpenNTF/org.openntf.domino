package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure defines the start of a run of text in a rich-text field. (editods.h)
 *
 */
public class CDTEXT extends CDRecord {

	static {
		addFixed("FontID", FONTID.class);

		addVariableString("Text", "getTextLength");
	}

	public static final int SIZE = getFixedStructSize();

	public CDTEXT(final CDSignature cdSig) {
		super(new WSIG(cdSig, cdSig.getSize() + SIZE), ByteBuffer.wrap(new byte[SIZE]));
	}

	public CDTEXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public FONTID getFontId() {
		return (FONTID) getStructElement("FontID");
	}

	public void setFontId(final FONTID font) {
		setStructElement("FontID", font);
	}

	public String getText() {
		return (String) getStructElement("Text");
	}

	public void setText(final String text) {
		int resultSize = setStructElement("Text", text);
		getSignature().setDataLength(resultSize + getFixedStructSize());
	}

	public int getTextLength() {
		return (int) (getDataLength() - FONTID.SIZE);
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
