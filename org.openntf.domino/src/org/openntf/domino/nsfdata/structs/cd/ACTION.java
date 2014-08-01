package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.ODSUtils;

public class ACTION extends CDRecord {
	private static final long serialVersionUID = 1L;

	protected ACTION(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		super(signature, data, dataLength);
	}

	public short getType() {
		return getData().getShort(getData().position() + 0);
	}

	public short getIconIndex() {
		return getData().getShort(getData().position() + 2);
	}

	public int getFlags() {
		return getData().getInt(getData().position() + 4);
	}

	public short getTitleLen() {
		return getData().getShort(getData().position() + 8);
	}

	public short getFormulaLen() {
		return getData().getShort(getData().position() + 10);
	}

	public int getShareId() {
		return getData().getInt(getData().position() + 12);
	}

	public String getTitle() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 16);
		data.limit(data.position() + getTitleLen());
		return ODSUtils.fromLMBCS(data);
	}

	public ByteBuffer getActionData() {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + 16 + getTitleLen());
		data.limit(data.position() + (getDataLength() + getSignature().getSize() - getTitleLen() - getFormulaLen()));
		return data;
	}

	public String getFormula() {
		// TODO determine whether this is compiled or decompiled formula
		ByteBuffer data = getData().duplicate();
		data.position(data.limit() - getFormulaLen());
		return ODSUtils.fromLMBCS(data);
	}

	@Override
	public int getExtraLength() {
		// TODO Determine if this is true
		return getDataLength() % 2;
	}
}
