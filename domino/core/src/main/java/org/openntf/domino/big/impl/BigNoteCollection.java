/**
 * 
 */
package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.utils.Factory;

/**
 * @author nfreeman
 * 
 */
@Incomplete
public class BigNoteCollection implements org.openntf.domino.big.BigNoteCollection {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(BigNoteCollection.class.getName());
	private Map<String, int[]> idMap_;

	public static String getAddress(final Database db) {
		String result;
		if ("".equals(db.getServer())) {
			result = "";
		} else {
			result = db.getServer() + "!!";
		}
		result += db.getReplicaID();
		return result;
	}

	public static String getAddress(final Document doc) {
		final Database db = doc.getAncestorDatabase();
		String result = getAddress(db);
		result += doc.getNoteID();
		return result;
	}

	public static String getServerName(final String address) {
		final int bangbang = address.indexOf("!!");
		if (bangbang > 0) {
			return address.substring(0, bangbang - 1);
		} else {
			return "";
		}
	}

	public static String getReplicaId(final String address) {
		final int bangbang = address.indexOf("!!");
		if (bangbang > 0) {
			return address.substring(bangbang + 2, bangbang + 2 + 16);
		} else {
			return address.substring(0, 16);
		}
	}

	public static String getNoteid(final String address) {
		final int bangbang = address.indexOf("!!");
		if (bangbang > 0) {
			return address.substring(bangbang + 2 + 16);
		} else {
			return address.substring(16);
		}
	}

	public static Document getDocument(final Session session, final String server, final String replid, final String noteid) {
		final Database db = session.getDatabase("", "");
		db.openByReplicaID(server, replid);
		if (db.isOpen()) {
			final Document result = db.getDocumentByID(noteid);
			return result;
		} else {
			// TODO handle exception properly
			return null;
		}
	}

	public static class DocumentIterator implements Iterator<Document> {
		private final BigNoteCollection coll_;
		private Iterator<String> keyIterator_;
		@SuppressWarnings("unused")
		private final Set<String> completedKeys_ = new HashSet<String>();
		@SuppressWarnings("unused")
		private String currentKey_;
		private int[] currentInts_;
		private int currentIntLength_;
		private int currentPos_;

		DocumentIterator(final BigNoteCollection coll) {
			coll_ = coll;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			boolean result = false;
			if (keyIterator_ == null) {
				keyIterator_ = coll_.iterator();
			}
			if (currentPos_ >= currentIntLength_) {
				currentInts_ = getNextInts();
				if (currentInts_ != null) {
					currentIntLength_ = currentInts_.length;
					currentPos_ = 0;
					result = true;
				} else {
					result = false;
				}
			} else {
				result = true;
			}
			return result;
		}

		private int[] getNextInts() {
			int length = 0;
			String key = null;
			int[] ints = null;
			while (length == 0) {
				key = keyIterator_.next();
				if (key == null)
					return null;
				ints = coll_.getNoteIds(key);
				length = ints == null ? 0 : ints.length;
			}
			// currentKey_ = key;
			return ints;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Document next() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			// NOOP
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<String> iterator() {
		final Map<String, int[]> map = getIdMap();
		Set<String> set;
		synchronized (map) {
			set = Collections.unmodifiableSortedSet((SortedSet<String>) map.keySet());
		}
		return set.iterator();
	}

	public Iterator<Document> documents() {
		return new DocumentIterator(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.big.BigNoteCollection#add(java.lang.String, int)
	 */
	@Override
	public void add(final String filepath, final int additionSpecifier) {
		final int[] ints = new int[1];
		ints[0] = additionSpecifier;
		add(filepath, ints);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.big.BigNoteCollection#add(java.lang.String, int[])
	 */
	@Override
	public void add(final String filepath, final int[] ints) {
		Map<String, int[]> map = getIdMap();
		synchronized (map) {
			final int[] current = getIdMap().get(filepath);
			if (current == null) {
				getIdMap().put(filepath, ints);
			} else {
				// TODO potentially grow arrays in blocks
				final int[] newArray = new int[current.length + ints.length + 1];
				System.arraycopy(current, 0, newArray, 0, current.length);
				System.arraycopy(ints, 0, newArray, current.length + 1, ints.length);
				getIdMap().put(filepath, newArray);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.big.BigNoteCollection#add(java.lang.String, java.lang.String)
	 */
	@Override
	public void add(final String filepath, final String noteid) {
		final int nid = Integer.valueOf(noteid, 16).intValue();
		add(filepath, nid);
	}

	public void add(final NoteCollection nc) {
		String filepath = getAddress(nc.getAncestorDatabase());
		add(filepath, nc.getNoteIDs());
	}

	@SuppressWarnings("deprecation")
	public void add(final DocumentCollection dc) {
		add(Factory.toNoteCollection(dc));
	}

	public void add(final Document doc) {
		final String filepath = getAddress(doc.getAncestorDatabase());
		add(filepath, doc.getNoteID());
	}

	public void add(final View view) {
		final String filepath = getAddress(view.getAncestorDatabase());
		add(filepath, view.getDocument().getNoteID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		int keyCount = in.readInt();
		if (keyCount > 0) {
			idMap_ = new LinkedHashMap<String, int[]>();
			for (int i = 0; i < keyCount; i++) {
				String key = in.readUTF();
				int size = in.readInt();
				int[] ints = new int[size];
				for (int j = 0; j < size; j++) {
					ints[j] = in.readInt();
				}
				idMap_.put(key, ints);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		Map<String, int[]> map = getIdMap();
		synchronized (map) {
			Set<String> keys = map.keySet();
			out.writeInt(keys.size());
			for (String key : map.keySet()) {
				out.writeUTF(key);
				int[] ints = map.get(key);
				out.writeInt(ints.length);
				for (int i : ints) {
					out.writeInt(i);
				}
			}
		}
	}

	public int[] getNoteIds(final String key) {
		Map<String, int[]> map = getIdMap();
		synchronized (map) {
			return map.get(key);
		}
	}

	private Map<String, int[]> getIdMap() {
		if (idMap_ == null) {
			idMap_ = new LinkedHashMap<String, int[]>();
		}
		return idMap_;
	}
}
