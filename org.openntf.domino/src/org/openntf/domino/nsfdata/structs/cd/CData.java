package org.openntf.domino.nsfdata.structs.cd;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.openntf.domino.nsfdata.structs.SIG;

public class CData implements Serializable, Iterator<CDRecord> {
	private static final long serialVersionUID = 1L;

	private final ByteBuffer data_;
	private CDRecord current_ = null;

	public CData(final ByteBuffer data) {
		data_ = data.duplicate();
		data_.order(ByteOrder.LITTLE_ENDIAN);
	}

	@Override
	public boolean hasNext() {
		return data_.hasRemaining();
	}

	@Override
	public CDRecord next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		//		System.out.println("opening pos: " + data_.position());

		// This has the side effect of incrementing the buffer by two for two gets
		SIG sig = CDSignature.sigForData(data_.duplicate());

		// Skip past the Signature's two bytes and the length of the Length value
		// The length value from the signature is the length of the data PLUS the length of the header
		data_.position(data_.position() + sig.getSigLength());

		//		System.out.println("pos: " + data_.position());
		//		System.out.println("limit: " + data_.limit());
		//		System.out.println("sig.getLength: " + sig.getLength());
		//		System.out.println("sig.getSigLength: " + sig.getSigLength());
		int dataLength = sig.getLength() - sig.getSigLength();
		//		System.out.println("data length: " + dataLength);

		// Now the ByteBuffer is positioned at the start of the data
		// Create a view starting at the start of the data and going the length of the data
		ByteBuffer recordData = data_.duplicate();
		//		System.out.println("want to limit record data to " + dataLength);
		//		recordData.limit(recordData.position() + dataLength);
		//		System.out.println("recordData pos: " + recordData.position());
		//		System.out.println("recordData limit: " + recordData.limit());
		current_ = CDRecord.create(sig.getSignature(), recordData, dataLength);

		// Skip past the data for the next record
		//		System.out.println("extra length is " + result.getExtraLength());
		data_.position(data_.position() + dataLength + current_.getExtraLength());

		return current_;
	}

	@Override
	public void remove() {
		// TODO implement removal
		throw new UnsupportedOperationException();
	}
}