package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javolution.util.function.Equality;

import org.openntf.domino.Document;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;

/*
 * @author Nathan T. Freeman
 * Don't use this. I was hoping the use of a List delegate and a BloomFilter would make it faster than a standard 
 * LinkedHashSet, but it doesn't.
 */
@Deprecated
public class NoteListUnique extends NoteList {
	private static Funnel<byte[]> NOTE_FUNNEL = Funnels.byteArrayFunnel();
	protected transient BloomFilter<byte[]> filter_;
	protected transient int removals_ = 0;
	protected transient int filterSize_ = 500;

	public NoteListUnique() {
	}

	public NoteListUnique(final int filterSize) {
		filterSize_ = filterSize;
	}

	public NoteListUnique(final DbCache cache) {
		super(cache);
	}

	public NoteListUnique(final DbCache cache, final Equality<NoteCoordinate> compare) {
		super(cache, compare);
	}

	protected BloomFilter<byte[]> getFilter() {
		return getFilter(filterSize_);
	}

	protected BloomFilter<byte[]> getFilter(final int defaultSize) {
		if (filter_ == null) {
			filter_ = BloomFilter.create(NOTE_FUNNEL, defaultSize);
			for (NoteCoordinate nc : this) {
				putFilter(nc);
			}
		}
		return filter_;
	}

	protected void resetFilter() {
		if (filter_ == null) {
			filter_ = BloomFilter.create(NOTE_FUNNEL, delegate_.size() * 2);
			for (NoteCoordinate nc : this) {
				putFilter(nc);
			}
		} else {
			BloomFilter<byte[]> newFilter = BloomFilter.create(NOTE_FUNNEL, delegate_.size() * 2);
			newFilter.putAll(filter_);
			filter_ = newFilter;
		}
	}

	protected boolean mightContain(final Object o) {
		//TODO NTF optimize based on whether removals have exceed some threshold
		byte[] bytes = null;
		if (o instanceof NoteCoordinate) {
			bytes = ((NoteCoordinate) o).toByteArray();
		} else if (o instanceof byte[]) {
			bytes = (byte[]) o;
		} else if (o instanceof Document) {
			bytes = org.openntf.domino.big.NoteCoordinate.Utils.getBytesFromDocument((Document) o);
		} else {
			return true;
		}
		return getFilter().mightContain(bytes);
	}

	protected boolean putFilter(final Object o) {
		byte[] bytes = null;
		if (o instanceof NoteCoordinate) {
			bytes = ((NoteCoordinate) o).toByteArray();
		} else if (o instanceof byte[]) {
			bytes = (byte[]) o;
		} else if (o instanceof Document) {
			bytes = org.openntf.domino.big.NoteCoordinate.Utils.getBytesFromDocument((Document) o);
		} else {
			return false;
		}
		return getFilter().put(bytes);
	}

	@Override
	public void add(final int index, final NoteCoordinate element) {
		if (index < 0 || index > size())
			throw new IndexOutOfBoundsException();
		if (!contains(element)) {
			putFilter(element);
			super.add(index, element);
		}
	}

	@Override
	public boolean add(final NoteCoordinate e) {
		if (!contains(e)) {
			putFilter(e);
			return super.add(e);
		} else {
			return false;
		}
	}

	@Override
	public boolean addAll(final Collection<? extends NoteCoordinate> c) {
		boolean result = false;
		//FIXME NTF This implementation is terrible...
		for (NoteCoordinate nc : c) {
			if (add(nc)) {
				result = true;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(final int index, final Collection<? extends NoteCoordinate> c) {
		if (index < 0 || index > size())
			throw new IndexOutOfBoundsException();
		boolean result = false;
		Iterator<NoteCoordinate> it = (Iterator<NoteCoordinate>) c.iterator();
		while (it.hasNext()) {
			NoteCoordinate nc = it.next();
			if (mightContain(nc)) {
				if (super.contains(nc)) {
					it.remove();
				}
			}
		}
		//FIXME NTF This implementation is terrible. It changes the original collection
		return super.addAll(index, c);
	}

	@Override
	public boolean contains(final Object o) {
		if (mightContain(o)) {
			return super.contains(o);
		}
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		for (Object o : c) {
			if (!mightContain(o)) {
				return false;
			}
		}
		return super.containsAll(c);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof NoteList))
			return false;
		if (obj instanceof NoteListUnique) {
			return ((NoteListUnique) obj).getFilter().equals(getFilter());
		}
		return Arrays.deepEquals(((NoteList) obj).toArray(), toArray());
	}

	@Override
	public int indexOf(final Object o) {
		if (!mightContain(o)) {
			return -1;
		}
		return super.indexOf(o);
	}

	@Override
	public int lastIndexOf(final Object o) {
		if (!mightContain(o)) {
			return -1;
		}
		return super.lastIndexOf(o);
	}

	@Override
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		super.readExternal(arg0);
		resetFilter();
	}

	@Override
	public boolean remove(final Object o) {
		boolean result = false;
		if (mightContain(o)) {
			removals_++;
			result = super.remove(o);
		}
		return result;
	}

	@Override
	public NoteCoordinate remove(final int index) {
		removals_++;
		return super.remove(index);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		removals_ = removals_ + c.size();
		return super.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		boolean result = super.retainAll(c);
		resetFilter();
		return result;
	}

	@Override
	public NoteCoordinate set(final int index, final NoteCoordinate element) {
		if (index < 0 || index > size())
			throw new IndexOutOfBoundsException();
		if (!mightContain(element)) {
			putFilter(element);
			return super.set(index, element);
		} else {
			NoteCoordinate chk = get(index);
			if (element.equals(chk)) {
				putFilter(element);
				return super.set(index, element);
			} else {
				if (!super.contains(element)) {
					putFilter(element);
					return super.set(index, element);
				} else {
					return element;
				}
			}
		}
	}

}
