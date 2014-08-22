package org.openntf.domino.nsfdata.structs.cd;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.AbstractStruct;
import org.openntf.domino.nsfdata.structs.SIG;

public abstract class CDRecord extends AbstractStruct {

	public static CDRecord create(final SIG signature, final ByteBuffer data) {
		Class<? extends CDRecord> instanceClass = signature.getSignature().getInstanceClass();
		if (instanceClass != null) {
			try {
				return instanceClass.getDeclaredConstructor(SIG.class, ByteBuffer.class).newInstance(signature, data);
			} catch (Throwable t) {
				throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
			}
		} else {
			return new BasicCDRecord(signature, data);
		}
	}

	private SIG signature_;

	protected CDRecord(final SIG signature, final ByteBuffer data) {
		super(data);
		signature_ = signature;
	}

	/**
	 * @return Signature and length of this record
	 */
	public SIG getSignature() {
		return signature_;
	}

	/**
	 * @return The length (in bytes) of the data portion of this record
	 */
	public int getDataLength() {
		return signature_.getLength() - signature_.getSigLength();
	}

	/**
	 * @return Any additional byte at the end of the record used for word alignment
	 */
	public int getExtraLength() {
		return getDataLength() % 2;
	}

	@Override
	public int getStructSize() {
		return signature_.getLength();
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ", Signature: " + getSignature() + "]";
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		signature_ = (SIG) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		super.writeExternal(out);
		// TODO change this to write the signature bytes directly
		out.writeObject(signature_);
	}
}
