package org.openntf.domino.nsfdata.structs.cd;

import org.openntf.domino.nsfdata.structs.FONTID;
import org.openntf.domino.nsfdata.structs.SIG;
import org.openntf.domino.nsfdata.structs.WSIG;

/**
 * This structure defines the start of a run of text in a rich-text field. (editods.h)
 *
 */
public class CDTEXT extends CDRecord {

	public final WSIG Header = inner(new WSIG());
	public final FONTID FontID = inner(new FONTID());

	static {
		addVariableString("Text", "getTextLength");
	}

	@Override
	public SIG getHeader() {
		return Header;
	}

	public String getText() {
		return (String) getVariableElement("Text");
	}

	public void setText(final String text) {
		int resultSize = setVariableElement("Text", text);
		Header.Length.set(Header.size() + ((int) (resultSize + getStructSize())));
	}

	public int getTextLength() {
		return (int) (Header.getRecordLength() - Header.size() - FontID.size());
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
