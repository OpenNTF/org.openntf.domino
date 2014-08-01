package org.openntf.domino.nsfdata.structs.cd;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.AbstractSequentialList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.nsfdata.structs.SIG;

public class CData extends AbstractSequentialList<CDRecord> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final ByteBuffer data_;
	@SuppressWarnings("unused")
	private int startingPosition_;
	private transient List<CDRecord> fetched_;

	public CData(final ByteBuffer data) {
		data_ = data.duplicate();
		data_.order(ByteOrder.LITTLE_ENDIAN);
		startingPosition_ = data_.position();
	}

	@Override
	public ListIterator<CDRecord> listIterator(final int index) {
		return new CDataIterator(index);
	}

	@Override
	public int size() {
		// We'll have to process the whole thing to get this value
		int breaker = 0;
		while (data_.hasRemaining()) {
			fetchNextRecord();
			if (breaker > 100000) {
				throw new RuntimeException("Too many records!");
			}
		}
		return fetched_.size();
	}

	private CDRecord fetchNextRecord() {
		if (!data_.hasRemaining()) {
			throw new NoSuchElementException();
		}
		//		System.out.println("opening pos: " + data_.position());

		// This has the side effect of incrementing the buffer by two for two gets
		SIG sig = CDSignature.sigForData(data_.duplicate());

		// Skip past the Signature's two bytes and the length of the Length value
		// The length value from the signature is the length of the data PLUS the length of the header
		data_.position(data_.position() + sig.getSigLength());

		int dataLength = sig.getLength() - sig.getSigLength();

		// Now the ByteBuffer is positioned at the start of the data
		// Create a view starting at the start of the data and going the length of the data
		ByteBuffer recordData = data_.duplicate();
		CDRecord record = CDRecord.create(sig.getSignature(), recordData, dataLength);

		// Skip past the data for the next record
		data_.position(data_.position() + dataLength + record.getExtraLength());

		if (fetched_ == null) {
			fetched_ = new LinkedList<CDRecord>();
		}
		fetched_.add(record);

		return fetched_.get(fetched_.size() - 1);
	}

	private class CDataIterator implements ListIterator<CDRecord> {
		private final int start_;
		private int index_;

		public CDataIterator(final int start) {
			start_ = start;
			index_ = start - 1;
		}

		@Override
		public void add(final CDRecord record) {
			throw new UnimplementedException();
		}

		@Override
		public boolean hasNext() {
			return data_.hasRemaining();
		}

		@Override
		public boolean hasPrevious() {
			return index_ > start_;
		}

		@Override
		public CDRecord next() {
			if (!hasNext()) {
				throw new IndexOutOfBoundsException("No records remaining");
			}
			index_++;
			if (fetched_ == null || index_ == fetched_.size()) {
				return fetchNextRecord();
			} else {
				return fetched_.get(index_);
			}
		}

		@Override
		public int nextIndex() {
			return index_ + 1;
		}

		@Override
		public CDRecord previous() {
			if (index_ <= start_) {
				throw new IndexOutOfBoundsException("No previous record");
			}
			index_--;
			return fetched_.get(index_);
		}

		@Override
		public int previousIndex() {
			return index_ - 1;
		}

		@Override
		public void remove() {
			throw new UnimplementedException();
		}

		@Override
		public void set(final CDRecord record) {
			throw new UnimplementedException();
		}

	}
}