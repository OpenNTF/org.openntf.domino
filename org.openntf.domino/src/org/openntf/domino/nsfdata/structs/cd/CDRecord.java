package org.openntf.domino.nsfdata.structs.cd;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class CDRecord implements Externalizable {
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
			return new BasicCDRecord(signature, data, dataLength);
		}
	}

	private CDSignature signature_;
	private ByteBuffer data_;
	private int dataLength_;

	protected CDRecord(final CDSignature signature, final ByteBuffer data, final int dataLength) {
		data.duplicate();
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

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		int len = in.readInt();
		byte[] storage = new byte[len];
		in.read(storage);
		data_ = ByteBuffer.wrap(storage);
		dataLength_ = in.readInt();
		signature_ = (CDSignature) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		byte[] storage = new byte[data_.limit() - data_.position()];
		data_.get(storage);
		out.writeInt(storage.length);
		out.write(storage);
		out.writeInt(dataLength_);
		out.writeObject(signature_);
	}
}
