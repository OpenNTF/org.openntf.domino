package org.openntf.domino.big.impl;

import static org.openntf.domino.big.NoteCoordinate.Utils.getLongFromReplid;
import static org.openntf.domino.big.NoteCoordinate.Utils.getLongsFromUnid;
import static org.openntf.domino.big.NoteCoordinate.Utils.getReplidFromLong;
import static org.openntf.domino.big.NoteCoordinate.Utils.getUnidFromLongs;
import static org.openntf.domino.big.NoteCoordinate.Utils.insertByteArray;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

import javolution.util.FastMap;

import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.types.Null;

import com.google.common.primitives.Longs;

/*
 * NTF This class stores information on where to find a note in a simple 3-long address
 * These can be easily converted to replicaids and UNIDs to find the exact document across
 * an entire enterprise
 */

public class NoteCoordinate implements org.openntf.domino.big.NoteCoordinate {
	private static ThreadLocal<byte[]> extreadbuffer_ = new ThreadLocal<byte[]>() {
		@Override
		protected byte[] initialValue() {
			return new byte[24];
		}
	};

	private static DbCache dbcache_ = new DbCache();

	public static void clearLocals() {
		extreadbuffer_.remove();
		dbcache_.remove();
	}

	private long db;
	private long x;
	private long y;
	transient private Map<String, Object> propertyCache;

	//TODO NTF we should probably have a factory that creates these instead of instantiating them directly

	public NoteCoordinate(final long db, final long x, final long y) {
		this.db = db;
		this.x = x;
		this.y = y;
	}

	public NoteCoordinate(final byte[] bytes) {
		if (bytes.length >= 24) {
			this.db = Longs.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
			this.x = Longs.fromBytes(bytes[8], bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]);
			this.y = Longs.fromBytes(bytes[16], bytes[17], bytes[18], bytes[19], bytes[20], bytes[21], bytes[22], bytes[23]);
		} else {
			throw new IllegalArgumentException("Can't construct new NoteCoordinate from byte length of " + bytes.length);
		}
	}

	public NoteCoordinate(final CharSequence replicaid, final CharSequence unid) {
		this.db = getLongFromReplid(replicaid);
		long[] unids = getLongsFromUnid(unid);
		this.x = unids[0];
		this.y = unids[1];
	}

	public NoteCoordinate(final Document doc) {
		this(doc.getAncestorDatabase().getReplicaID(), doc.getUniversalID());
	}

	public NoteCoordinate(final NoteCollection notecoll, final String nid) {
		this(notecoll.getAncestorDatabase().getReplicaID(), notecoll.getUNID(nid));
	}

	public static DbCache getDbCache() {
		return (DbCache) dbcache_.get();
	}

	public static void setDbCache(final DbCache cache) {
		dbcache_.set(cache);
	}

	@Override
	public String getReplicaId() {
		return getReplidFromLong(db);
	}

	protected long getDbid() {
		return db;
	}

	@Override
	public String getUNID() {
		return getUnidFromLongs(x, y);
	}

	@Override
	public Document getDocument() {
		return getDbCache().getDocument(this);
	}

	@Override
	public Document getDocument(final String serverName) {
		return getDbCache().getDocument(this, serverName);
	}

	private Map<String, Object> getPropertyCache() {
		if (propertyCache == null) {
			propertyCache = new FastMap<String, Object>();
		}
		return propertyCache;
	}

	@Override
	public Object get(final String key) {
		Object result = getPropertyCache().get(key);
		if (result == null) {
			result = getDocument().get(key);
			if (result == null) {
				getPropertyCache().put(key, Null.INSTANCE);
				return null;
			} else {
				getPropertyCache().put(key, result);
				return result;
			}
		} else if (result == Null.INSTANCE) {
			return null;
		} else {
			return result;
		}
	}

	@Override
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		byte[] bytes = extreadbuffer_.get();
		arg0.read(bytes);
		this.db = Longs.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
		this.x = Longs.fromBytes(bytes[8], bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]);
		this.y = Longs.fromBytes(bytes[16], bytes[17], bytes[18], bytes[19], bytes[20], bytes[21], bytes[22], bytes[23]);
	}

	@Override
	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.write(toByteArray());
	}

	public byte[] toByteArray() {
		byte[] result = new byte[24];
		insertByteArray(db, result, 0);
		insertByteArray(x, result, 8);
		insertByteArray(y, result, 16);
		return result;
	}

	public int insertToByteArray(final byte[] bytes, final int pos) {
		insertByteArray(db, bytes, pos + 0);
		insertByteArray(x, bytes, pos + 8);
		insertByteArray(y, bytes, pos + 16);
		return pos + 24;
	}

	@Override
	public int compareTo(final org.openntf.domino.big.NoteCoordinate arg0) {
		NoteCoordinate local = (NoteCoordinate) arg0;
		if (this.db > local.db)
			return 1;
		if (this.db < local.db)
			return -1;
		if (this.y > local.y)
			return 1;
		if (this.y < local.y)
			return -1;
		if (this.x > local.x)
			return 1;
		if (this.x < local.x)
			return -1;
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (db ^ (db >>> 32));
		result = prime * result + (int) (x ^ (x >>> 32));
		result = prime * result + (int) (y ^ (y >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoteCoordinate other = (NoteCoordinate) obj;
		if (db != other.db)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
