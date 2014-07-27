package org.openntf.domino.nsfdata.cd;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CDRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	public static CDRecord create(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		switch(signature) {
		case FILEHEADER:
			return new CDRecordFileHeader(signature, data, dataLength);
		case FILESEGMENT:
			return new CDRecordFileSegment(signature, data, dataLength);
		case PABDEFINITION:
			return new CDRecordParaDefinition(signature, data, dataLength);
		case TEXT:
			return new CDRecordText(signature, data, dataLength);
		case LINK2:
			return new CDRecordLink2(signature, data, dataLength);
		default:
			return new CDRecord(signature, data,  dataLength);
		}
	}

	private final CDSignature signature_;
	private final ByteBuffer data_;
	private final int dataLength_;

	public CDRecord(final CDSignature signature, final ByteBuffer data, final int dataLength) {
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
