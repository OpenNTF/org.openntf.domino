package org.openntf.domino.nsfdata.structs;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class AbstractStruct implements Externalizable {

	private ByteBuffer data_;

	public AbstractStruct(final ByteBuffer data) {
		data_ = data.duplicate();
		data_.order(ByteOrder.LITTLE_ENDIAN);
	}

	public ByteBuffer getData() {
		return data_;
	}

	public abstract int getStructSize();

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
		data_.order(ByteOrder.LITTLE_ENDIAN);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		byte[] storage = new byte[data_.limit() - data_.position()];
		data_.get(storage);
		out.writeInt(storage.length);
		out.write(storage);
	}
}
