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

	protected CDRecord(final CDSignature cdSig) {
		// TODO implement this
	}

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
	public long getDataLength() {
		return signature_.getLength() - signature_.getSigLength();
	}

	/**
	 * @return Any additional byte at the end of the record used for word alignment
	 */
	public int getExtraLength() {
		return (int) (getDataLength() % 2);
	}

	@Override
	public long getStructSize() {
		return signature_.getLength();
	}

	@Override
	protected byte[] getBytes() {
		byte[] parentBytes = super.getBytes();
		byte[] sigBytes = getSignature().getBytes();
		byte[] result = new byte[parentBytes.length + sigBytes.length];
		System.arraycopy(sigBytes, 0, result, 0, sigBytes.length);
		System.arraycopy(parentBytes, 0, result, sigBytes.length, parentBytes.length);
		return result;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		int sigLength = in.readInt();
		byte[] sigBytes = new byte[sigLength];
		in.read(sigBytes);
		signature_ = CDSignature.sigForData(ByteBuffer.wrap(sigBytes));
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		super.writeExternal(out);
		byte[] sigBytes = signature_.getBytes();
		out.writeInt(sigBytes.length);
		out.writeObject(sigBytes);
	}
}
