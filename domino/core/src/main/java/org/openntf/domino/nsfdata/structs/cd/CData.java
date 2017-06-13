package org.openntf.domino.nsfdata.structs.cd;

import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.AbstractSequentialList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.nsfdata.structs.SIG;

public class CData extends AbstractSequentialList<CDRecord> implements Externalizable {
	private static final long serialVersionUID = 1L;

	private ByteBuffer data_;
	@SuppressWarnings("unused")
	private int startingPosition_;
	private transient List<CDRecord> fetched_;

	public CData(final ByteBuffer data) {
		data_ = data.duplicate();
		data_.order(ByteOrder.LITTLE_ENDIAN);
		startingPosition_ = data_.position();
	}

	public CData(final byte[] data) {
		this(ByteBuffer.wrap(data));
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
			if (breaker++ > 100000) {
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
		//		System.out.println("opening capacity: " + data_.capacity());

		// Peek at the next couple bytes to get SIG and find the appropriate record type
		SIG sig = CDSignature.sigForData(data_.duplicate().order(ByteOrder.LITTLE_ENDIAN));
		//		System.out.println("making " + sig);

		// Now the ByteBuffer is positioned at the start of the data
		// Create a view starting at the start of the data and going the length of the data
		ByteBuffer recordData = data_.duplicate().order(ByteOrder.LITTLE_ENDIAN);
		long recordSize = sig.getRecordLength() + (sig.getRecordLength() % 2);
		recordData.limit((int) (recordData.position() + recordSize));
		//CDRecord record = CDRecord.create(sig, recordData);
		CDRecord record = null;
		try {
			record = CDSignature.instanceClassForSig(sig).newInstance();
		} catch (Exception e) {
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		}
		record.init(recordData);

		// Skip past the data for the next record
		try {
			//			System.out.println("moving forward " + recordSize);
			data_.position((int) (data_.position() + recordSize));
		} catch (IllegalArgumentException e) {
			throw e;
		}

		if (fetched_ == null) {
			fetched_ = new LinkedList<CDRecord>();
		}
		fetched_.add(record);

		return fetched_.get(fetched_.size() - 1);
	}

	public byte[] getBytes() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			for (CDRecord rec : this) {
				bos.write(rec.getBytes());
			}
			return bos.toByteArray();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		int capacity = in.readInt();
		data_ = ByteBuffer.allocate(capacity);
		for (int i = 0; i < capacity; i++) {
			data_.put((byte) in.read());
		}
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		ByteBuffer data = data_.duplicate();
		data.position(0);
		out.writeInt(data.capacity());
		while (data.hasRemaining()) {
			out.write(data.get());
		}
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