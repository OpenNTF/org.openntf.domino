package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This structure defines the start of a run of text in a rich-text field. (editods.h)
 * 
 * @author jgallagher
 *
 */
public class CDTEXT extends CDRecord {

	public CDTEXT(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

	public int getFontId() {
		return getData().getInt(getData().position() + 0);
	}

	public String getText() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 4);
		data.limit(data.position() + getTextLength());
		return ODSUtils.fromLMBCS(data);
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
