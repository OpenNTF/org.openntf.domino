/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.big.impl;

import static org.openntf.domino.big.NoteCoordinate.Utils.getReplidFromLong;
import static org.openntf.domino.big.NoteCoordinate.Utils.getUnidFromLongs;
import static org.openntf.domino.big.NoteCoordinate.Utils.insertByteArray;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;

import javolution.util.FastSortedTable;
import javolution.util.function.Equality;

@SuppressWarnings("nls")
public class LocalNoteList implements org.openntf.domino.big.LocalNoteList {
	private final static int BUFFER_SIZE = 16;

	private static ThreadLocal<byte[]> extreadbuffer_ = new ThreadLocal<byte[]>() {
		@Override
		protected byte[] initialValue() {
			return new byte[BUFFER_SIZE];
		}
	};

	static class LocalNoteCoordinate implements Externalizable {
		long x;
		long y;
		LocalNoteList parent_;

		public LocalNoteCoordinate(final org.openntf.domino.big.NoteCoordinate nc, final LocalNoteList parentList) {
			x = nc.getX();
			y = nc.getY();
			parent_ = parentList;
		}

		public LocalNoteCoordinate(final byte[] bytes, final LocalNoteList parentList) {
			parent_ = parentList;
			if (bytes.length >= BUFFER_SIZE) {
				this.x = Longs.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
				this.y = Longs.fromBytes(bytes[8], bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]);
			} else {
				throw new IllegalArgumentException("Can't construct new LocalNoteCoordinate from byte length of " + bytes.length);
			}
		}

		public LocalNoteCoordinate(final String unid, final LocalNoteList parentList) {
			parent_ = parentList;
			long[] longs = org.openntf.domino.big.NoteCoordinate.Utils.getLongsFromUnid(unid);
			x = longs[0];
			y = longs[1];
		}

		public NoteCoordinate toNoteCoordinate() {
			return new NoteCoordinate(parent_.getReplicaIdLong(), x, y);
		}

		@Override
		public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
			byte[] bytes = extreadbuffer_.get();
			in.read(bytes);
			this.x = Longs.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
			this.y = Longs.fromBytes(bytes[8], bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]);
		}

		@Override
		public void writeExternal(final ObjectOutput out) throws IOException {
			out.write(toByteArray());
		}

		public byte[] toByteArray() {
			byte[] result = new byte[BUFFER_SIZE];
			insertByteArray(x, result, 0);
			insertByteArray(y, result, 8);
			return result;
		}

		public int insertToByteArray(final byte[] bytes, final int pos) {
			insertByteArray(x, bytes, pos + 0);
			insertByteArray(y, bytes, pos + 8);
			return pos + BUFFER_SIZE;
		}

		public String getUNID() {
			return getUnidFromLongs(x, y);
		}

		public Document getDocument() {
			return parent_.getDatabase().getDocumentByUNID(getUNID(), true);
		}

		public Object get(final String key) {
			Object result = getDocument().get(key);
			if (result == null) {
				return null;
			} else {
				return result;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (x ^ (x >>> 32));
			result = prime * result + (int) (y ^ (y >>> 32));
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof LocalNoteCoordinate)) {
				return false;
			}
			LocalNoteCoordinate other = (LocalNoteCoordinate) obj;
			if (x != other.x) {
				return false;
			}
			if (y != other.y) {
				return false;
			}
			return true;
		}
	}

	static class NoteComparator implements Equality<LocalNoteCoordinate> {
		private static final long serialVersionUID = 1L;
		private String comparisonKey_;

		NoteComparator(final String key) {
			comparisonKey_ = key;
		}

		@Override
		@SuppressWarnings("unchecked")
		public int compare(final LocalNoteCoordinate arg0, final LocalNoteCoordinate arg1) {
			Object obj0 = arg0.get(comparisonKey_);
			Object obj1 = arg1.get(comparisonKey_);
			return ((Comparable<Object>) obj0).compareTo(obj1);
		}

		@Override
		public int hashOf(final LocalNoteCoordinate object) {
			return object.hashCode();
		}

		@Override
		public boolean equal(final LocalNoteCoordinate left, final LocalNoteCoordinate right) {
			return left.equals(right);
		}

	}

	public static class LocalNoteListIterator implements ListIterator<org.openntf.domino.big.NoteCoordinate> {
		protected ListIterator<LocalNoteCoordinate> delegate_;
		protected LocalNoteList parent_;

		public LocalNoteListIterator(final List<LocalNoteCoordinate> delegate, final LocalNoteList parent) {
			parent_ = parent;
			delegate_ = delegate.listIterator();
		}

		public LocalNoteListIterator(final List<LocalNoteCoordinate> delegate, final LocalNoteList parent, final int index) {
			parent_ = parent;
			delegate_ = delegate.listIterator(index);
		}

		@Override
		public void add(final org.openntf.domino.big.NoteCoordinate arg0) {
			if (parent_.validateNoteCoordinate(arg0)) {
				delegate_.add(new LocalNoteCoordinate(arg0, parent_));
			}
		}

		@Override
		public boolean hasNext() {
			return delegate_.hasNext();
		}

		@Override
		public boolean hasPrevious() {
			return delegate_.hasPrevious();
		}

		@Override
		public NoteCoordinate next() {
			return delegate_.next().toNoteCoordinate();
		}

		@Override
		public int nextIndex() {
			return delegate_.nextIndex();
		}

		@Override
		public NoteCoordinate previous() {
			return delegate_.previous().toNoteCoordinate();
		}

		@Override
		public int previousIndex() {
			return delegate_.previousIndex();
		}

		@Override
		public void remove() {
			delegate_.remove();
		}

		@Override
		public void set(final org.openntf.domino.big.NoteCoordinate arg0) {
			if (parent_.validateNoteCoordinate(arg0)) {
				delegate_.set(new LocalNoteCoordinate(arg0, parent_));
			}
		}
	}

	protected List<LocalNoteCoordinate> delegate_;
	protected long replid_ = Long.MIN_VALUE;
	protected Date buildDate_ = new Date(0);
	protected String serverName_ = "";
	private transient org.openntf.domino.Database db_;

	public LocalNoteList() {
		delegate_ = new ArrayList<LocalNoteCoordinate>();
	}

	public LocalNoteList(final NoteCollection nc, final Date buildDate) {
		buildDate_ = buildDate;
		replid_ = org.openntf.domino.big.NoteCoordinate.Utils.getLongFromReplid(nc.getAncestorDatabase().getReplicaID());
		delegate_ = new ArrayList<LocalNoteCoordinate>(nc.getCount());
		for (int nid : nc.getNoteIDs()) {
			String unid = nc.getUNID(Integer.toHexString(nid));
			delegate_.add(new LocalNoteCoordinate(unid, this));
		}
	}

	public LocalNoteList(final DocumentCollection dc, final Date buildDate) {
		buildDate_ = buildDate;
		replid_ = org.openntf.domino.big.NoteCoordinate.Utils.getLongFromReplid(dc.getAncestorDatabase().getReplicaID());
		delegate_ = new ArrayList<LocalNoteCoordinate>(dc.getCount());
		for (Document doc : dc) {
			String unid = doc.getUniversalID();
			delegate_.add(new LocalNoteCoordinate(unid, this));
		}
	}

	protected Database getDatabase() {
		if (db_ == null) {
			String replid = getReplidFromLong(replid_);
			Session session = Factory.getSession(SessionType.CURRENT);
			DbDirectory dir = session.getDbDirectory(serverName_);
			db_ = dir.openDatabaseByReplicaID(replid);
		}
		return db_;
	}

	protected boolean validateNoteCoordinate(final org.openntf.domino.big.NoteCoordinate e) {
		if (replid_ == Long.MIN_VALUE) {
			replid_ = e.getReplicaLong();
			return true;
		} else {
			if (e.getReplicaLong().equals(replid_)) {
				return true;
			} else {
				throw new IllegalArgumentException("Cannot add a NoteCoordinate from replica " + e.getReplicaId()
						+ " to a LocalNoteList for " + this.getReplicaIdString());
			}
		}
	}

	@Override
	public boolean add(final org.openntf.domino.big.NoteCoordinate e) {
		if (e == null)
			return true;
		if (validateNoteCoordinate(e)) {
			LocalNoteCoordinate local = new LocalNoteCoordinate(e, this);
			return delegate_.add(local);
		}
		return false;
	}

	protected boolean add(final LocalNoteCoordinate lnc) {
		return delegate_.add(lnc);
	}

	@Override
	public void add(final int index, final org.openntf.domino.big.NoteCoordinate element) {
		if (element != null) {
			if (validateNoteCoordinate(element)) {
				LocalNoteCoordinate local = new LocalNoteCoordinate(element, this);
				delegate_.add(index, local);
			}
		}
	}

	@Override
	public boolean addAll(final Collection<? extends org.openntf.domino.big.NoteCoordinate> c) {
		if (c == null)
			return true;

		boolean result = true;
		for (org.openntf.domino.big.NoteCoordinate nc : c) {
			result = add(nc);
			if (!result)
				break;
		}
		return result;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends org.openntf.domino.big.NoteCoordinate> c) {
		if (c == null)
			return true;

		List<LocalNoteCoordinate> convertList = new ArrayList<LocalNoteCoordinate>();
		for (org.openntf.domino.big.NoteCoordinate nc : c) {
			if (validateNoteCoordinate(nc)) {
				convertList.add(new LocalNoteCoordinate(nc, this));
			}
		}
		return delegate_.addAll(index, convertList);
	}

	@Override
	public void clear() {
		delegate_.clear();
	}

	@Override
	public boolean contains(final Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof org.openntf.domino.big.NoteCoordinate) {
			if (validateNoteCoordinate((org.openntf.domino.big.NoteCoordinate) o)) {
				return delegate_.contains(new LocalNoteCoordinate((org.openntf.domino.big.NoteCoordinate) o, this));
			} else {
				return false;
			}
		} else if (o instanceof org.openntf.domino.big.LocalNoteCoordinate) {
			return delegate_.contains(o);
		}
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		if (c == null) {
			return true;
		}
		for (Object raw : c) {
			if (!this.contains(raw)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public org.openntf.domino.big.NoteCoordinate get(final int index) {
		LocalNoteCoordinate lnc = delegate_.get(index);
		if (lnc != null) {
			return lnc.toNoteCoordinate();
		} else {
			return null;
		}
	}

	@Override
	public int indexOf(final Object o) {
		//TODO
		return delegate_.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return delegate_.isEmpty();
	}

	@Override
	public Iterator<org.openntf.domino.big.NoteCoordinate> iterator() {
		return new LocalNoteListIterator(delegate_, this);
	}

	@Override
	public int lastIndexOf(final Object o) {
		//TODO
		return delegate_.lastIndexOf(o);
	}

	@Override
	public ListIterator<org.openntf.domino.big.NoteCoordinate> listIterator() {
		return new LocalNoteListIterator(delegate_, this);
	}

	@Override
	public ListIterator<org.openntf.domino.big.NoteCoordinate> listIterator(final int index) {
		return new LocalNoteListIterator(delegate_, this, index);
	}

	@Override
	public org.openntf.domino.big.NoteCoordinate remove(final int index) {
		return delegate_.remove(index).toNoteCoordinate();
	}

	@Override
	public boolean remove(final Object o) {
		//TODO
		return delegate_.remove(o);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		//TODO
		return delegate_.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		// TODO
		return delegate_.retainAll(c);
	}

	@Override
	public org.openntf.domino.big.NoteCoordinate set(final int index, final org.openntf.domino.big.NoteCoordinate element) {
		if (validateNoteCoordinate(element)) {
			LocalNoteCoordinate lnc = new LocalNoteCoordinate(element, this);
			delegate_.set(index, lnc);
		}
		return element;
	}

	@Override
	public int size() {
		return delegate_.size();
	}

	@Override
	public Object[] toArray() {
		return delegate_.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return delegate_.toArray(a);
	}

	@Override
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		replid_ = arg0.readLong();
		buildDate_ = new Date(arg0.readLong());
		int size = arg0.readInt();
		byte[] buffer = new byte[BUFFER_SIZE];
		for (int i = 0; i < size; i++) {
			arg0.read(buffer);
			add(new LocalNoteCoordinate(buffer, this));
		}
	}

	@Override
	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.writeLong(replid_);
		arg0.writeLong(buildDate_.getTime());
		arg0.writeInt(size());
		for (LocalNoteCoordinate lnc : delegate_) {
			arg0.write(lnc.toByteArray());
		}
	}

	@Override
	public String getReplicaIdString() {
		return NoteCoordinate.Utils.getReplidFromLong(replid_);
	}

	@Override
	public long getReplicaIdLong() {
		return replid_;
	}

	@Override
	public byte[] toByteArray() {
		byte[] result = new byte[size() * BUFFER_SIZE];
		int pos = 0;
		for (org.openntf.domino.big.NoteCoordinate nc : this) {
			pos = nc.insertToByteArray(result, pos);
		}
		return result;
	}

	@Override
	public void loadByteArray(final byte[] bytes) {
		int size = bytes.length / BUFFER_SIZE;
		byte[] buffer = new byte[BUFFER_SIZE];
		for (int i = 0; i < size; i++) {
			System.arraycopy(bytes, i * BUFFER_SIZE, buffer, 0, BUFFER_SIZE);
			add(new LocalNoteCoordinate(buffer, this));
		}
	}

	@Override
	public List<org.openntf.domino.big.NoteCoordinate> subList(final int fromIndex, final int toIndex) {
		throw new UnimplementedException("LocalNoteList sublist not yet implemented");
	}

	@Override
	public void sortBy(final String key) {
		NoteComparator comp = new NoteComparator(key);
		FastSortedTable<LocalNoteCoordinate> newDel = new FastSortedTable<LocalNoteCoordinate>(comp);
		newDel.addAll(delegate_);
		delegate_ = newDel;
	}

	@Override
	public NoteList toFullNoteList() {
		//TODO NTF this could probably be made faster with some kind of clever copy of the delegate
		NoteList result = new NoteList();
		for (LocalNoteCoordinate lnc : delegate_) {
			result.add(lnc.toNoteCoordinate());
		}
		return result;
	}

	@Override
	public Date getBuildDate() {
		return buildDate_;
	}

	@Override
	public void setBuildDate(final Date date) {
		buildDate_ = date;
	}

	private static LocalNoteCoordinate[] LNC_ARRAY = new LocalNoteCoordinate[0];

	public Set<LocalNoteCoordinate> difference(final LocalNoteList otherList) {
		LocalNoteCoordinate[] myArray = delegate_.toArray(LNC_ARRAY);
		LocalNoteCoordinate[] otherArray = delegate_.toArray(LNC_ARRAY);
		ImmutableSet<LocalNoteCoordinate> mySet = ImmutableSet.copyOf(myArray);
		ImmutableSet<LocalNoteCoordinate> otherSet = ImmutableSet.copyOf(otherArray);
		return Sets.difference(mySet, otherSet);
	}

}
