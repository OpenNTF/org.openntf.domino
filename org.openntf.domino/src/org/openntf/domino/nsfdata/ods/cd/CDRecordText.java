package org.openntf.domino.nsfdata.ods.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.ods.ODSUtils;

public class CDRecordText extends CDRecord {
	private static final long serialVersionUID = 1L;

	public CDRecordText(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		super(signature, data, dataLength);
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
