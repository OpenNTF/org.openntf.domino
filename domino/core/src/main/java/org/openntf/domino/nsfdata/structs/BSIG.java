package org.openntf.domino.nsfdata.structs;


/**
 * Every CD record begins with a header. There are three types of headers, BSIG, WSIG, and LSIG. The first byte of the header is a signature
 * which identifies the type of the header and the type of the CD record that follows. (ods.h)
 *
 */
public class BSIG extends SIG {
	public final Unsigned8 Signature = new Unsigned8();
	public final Unsigned8 Length = new Unsigned8();

	@Override
	public long getRecordLength() {
		return Length.get();
	}

	@Override
	public void setRecordLength(final long length) {
		Length.set((short) length);
	}

	@Override
	public int getSigIdentifier() {
		return Signature.get() & 0xFF;
	}

	@Override
	public void setSigIdentifier(final int identifier) {
		Signature.set((short) identifier);
	}
}
