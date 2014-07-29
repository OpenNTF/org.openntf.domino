package org.openntf.domino.nsfdata.structs.cd;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CDRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	public static CDRecord create(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		switch (signature) {
		case FILEHEADER:
			return new CDFILEHEADER(signature, data, dataLength);
		case FILESEGMENT:
			return new CDFILESEGMENT(signature, data, dataLength);
		case PABDEFINITION:
			return new CDPABDEFINITION(signature, data, dataLength);
		case TEXT:
			return new CDTEXT(signature, data, dataLength);
		case LINK2:
			return new CDLINK2(signature, data, dataLength);
		default:
			return new CDRecord(signature, data, dataLength);
		}
	}

	private final CDSignature signature_;
	private final ByteBuffer data_;
	private final int dataLength_;

	protected CDRecord(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		data.order(ByteOrder.LITTLE_ENDIAN);
		signature_ = signature;
		data_ = data;
		dataLength_ = dataLength;
	}

	public CDSignature getSignature() {
		return signature_;
	}

	public ByteBuffer getData() {
		return data_;
	}

	public int getDataLength() {
		return dataLength_;
	}

	public int getExtraLength() {
		return 0;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "]";
	}
}
