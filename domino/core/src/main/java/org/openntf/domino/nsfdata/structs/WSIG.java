package org.openntf.domino.nsfdata.structs;


/**
 * Every CD record begins with a header. There are three types of headers, BSIG, WSIG, and LSIG. The first byte of the header is a signature
 * which identifies the type of the header and the type of the CD record that follows. (ods.h)
 *
 */
public class WSIG extends SIG {

	public final Unsigned16 Signature = new Unsigned16();
	public final Unsigned16 Length = new Unsigned16();

	@Override
	public long getRecordLength() {
		return Length.get();
	}

	@Override
	public void setRecordLength(final long length) {
		Length.set((int) length);
	}

	@Override
	public int getSigIdentifier() {
		return Signature.get() & 0xFF;
	}

	@Override
	public void setSigIdentifier(final int identifier) {
		Signature.set(identifier);
	}
}