package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.AbstractStruct;
import org.openntf.domino.nsfdata.structs.SIG;

public abstract class CDRecord extends AbstractStruct {

	@Override
	public void init() {
		super.init();
		getHeader().setRecordLength(size());
	}

	@Override
	public void init(final ByteBuffer data) {
		super.init(data);
		getHeader().setRecordLength(data.limit() - data.position());
	}

	@Override
	public long getTotalSize() {
		return getHeader().getRecordLength() + getExtraLength();
	}

	public abstract SIG getHeader();

	//	public static CDRecord create(final SIG signature, final ByteBuffer data) {
	//		Class<? extends CDRecord> instanceClass = signature.getSignature().getInstanceClass();
	//		if (instanceClass != null) {
	//			try {
	//				return instanceClass.getDeclaredConstructor(SIG.class, ByteBuffer.class).newInstance(signature, data);
	//			} catch (Throwable t) {
	//				throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
	//			}
	//		} else {
	//			return new BasicCDRecord(signature, data);
	//		}
	//	}
	//
	//	private SIG signature_;
	//
	//	protected CDRecord(final CDSignature cdSig) {
	//		//		super();
	//		signature_ = cdSig.getSig(size());
	//		byte[] byteData = new byte[(int) getStructSize()];
	//		setByteBuffer(ByteBuffer.wrap(byteData).order(ByteOrder.LITTLE_ENDIAN), 0);
	//	}
	//
	//	protected CDRecord(final SIG signature, final ByteBuffer data) {
	//		super(data);
	//		signature_ = signature;
	//	}
	//
	//	/**
	//	 * @return Signature and length of this record
	//	 */
	//	public SIG getSignature() {
	//		return signature_;
	//	}
	//
	//	/**
	//	 * @return The length (in bytes) of the data portion of this record
	//	 */
	//	public long getDataLength() {
	//		return signature_.getLength() - signature_.getSigLength();
	//	}
	//
	/**
	 * @return Any additional byte at the end of the record used for word alignment
	 */
	@Override
	public int getExtraLength() {
		return (int) (getHeader().getRecordLength() % 2);
	}
	//
	//	@Override
	//	public long getStructSize() {
	//		return signature_.getLength();
	//	}
	//
	//	@Override
	//	protected byte[] getBytes() {
	//		byte[] parentBytes = super.getBytes();
	//		SIG signature = getSignature();
	//		signature.setDataLength(this.getTotalSize());
	//		byte[] sigBytes = getSignature().getBytes();
	//		byte[] result = new byte[parentBytes.length + sigBytes.length];
	//		System.arraycopy(sigBytes, 0, result, 0, sigBytes.length);
	//		System.arraycopy(parentBytes, 0, result, sigBytes.length, parentBytes.length);
	//		return result;
	//	}
	//
	//	@Override
	//	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
	//		super.readExternal(in);
	//		int sigLength = in.readInt();
	//		byte[] sigBytes = new byte[sigLength];
	//		in.read(sigBytes);
	//		signature_ = CDSignature.sigForData(ByteBuffer.wrap(sigBytes));
	//	}
	//
	//	@Override
	//	public void writeExternal(final ObjectOutput out) throws IOException {
	//		super.writeExternal(out);
	//		byte[] sigBytes = signature_.getBytes();
	//		out.writeInt(sigBytes.length);
	//		out.writeObject(sigBytes);
	//	}
}
