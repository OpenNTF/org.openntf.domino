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

import static org.openntf.domino.big.NoteCoordinate.Utils.getLongFromReplid;
import static org.openntf.domino.big.NoteCoordinate.Utils.getLongsFromUnid;
import static org.openntf.domino.big.NoteCoordinate.Utils.getReplidFromLong;
import static org.openntf.domino.big.NoteCoordinate.Utils.getUnidFromLongs;
import static org.openntf.domino.big.NoteCoordinate.Utils.insertByteArray;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.design.impl.DesignFactory;
import org.openntf.domino.types.Null;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.google.common.primitives.Longs;

import javolution.util.FastMap;

/*
 * NTF This class stores information on where to find a note in a simple 3-long address
 * These can be easily converted to replicaids and UNIDs to find the exact document across
 * an entire enterprise
 */

@SuppressWarnings("nls")
public class NoteCoordinate implements org.openntf.domino.big.NoteCoordinate {
	private static ThreadLocal<byte[]> extreadbuffer_ = new ThreadLocal<byte[]>() {
		@Override
		protected byte[] initialValue() {
			return new byte[24];
		}
	};

	long db;
	long x;
	long y;
	transient Boolean isView_;
	transient Boolean isIcon_;
	transient private Map<String, Object> propertyCache;

	//	transient private Database database_;
	//	transient private Document document_;

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

	public NoteCoordinate(final CharSequence metaversalid) {
		try {
			this.db = getLongFromReplid(metaversalid.subSequence(0, 16));
			long[] unids = getLongsFromUnid(metaversalid.subSequence(16, 48));
			this.x = unids[0];
			this.y = unids[1];
		} catch (Throwable t) {
			System.err.println("Unable to create a NoteCoordinate from character sequence: " + metaversalid);
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}

	public NoteCoordinate(final CharSequence replicaid, final CharSequence unid) {
		this.db = getLongFromReplid(replicaid);
		long[] unids = getLongsFromUnid(unid);
		this.x = unids[0];
		this.y = unids[1];
	}

	public NoteCoordinate(final Document doc) {
		this(doc.getAncestorDatabase().getReplicaID().toLowerCase(), doc.getUniversalID().toLowerCase());
	}

	public NoteCoordinate(final View view) {
		this(view.getAncestorDatabase().getReplicaID().toLowerCase(), view.getUniversalID().toLowerCase());
		isView_ = true;
	}

	public NoteCoordinate(final NoteCollection notecoll, final String nid) {
		this(notecoll.getAncestorDatabase().getReplicaID().toLowerCase(), notecoll.getUNID(nid).toLowerCase());
	}

	@Override
	public String getReplicaId() {
		return getReplidFromLong(db).toLowerCase();
	}

	@Override
	public Long getReplicaLong() {
		return db;
	}

	@Override
	public long getX() {
		return x;
	}

	@Override
	public long getY() {
		return y;
	}

	protected long getDbid() {
		return db;
	}

	@Override
	public String getUNID() {
		return getUnidFromLongs(x, y).toLowerCase();
	}

	protected Database getDatabase(final String server) {
		String replid = getReplidFromLong(db);
		Session session = Factory.getSession(SessionType.CURRENT);
		DbDirectory dir = session.getDbDirectory(server);
		return dir.openDatabaseByReplicaID(replid);
	}

	@Override
	public Document getDocument() {
		return getDocument("");
	}

	@Override
	public Document getDocument(final String serverName) {
		return getDatabase(serverName).getDocumentByUNID(getUNID(), true);
	}

	public View getView() {
		Database database = getDatabase("");
		Document doc = getDocument("");
		return database.getView(doc);
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
	public boolean isView() {
		if (isView_ == null) {
			Document doc = getDocument();
			if (!doc.isNewNote()) {
				try {
					isView_ = DesignFactory.isView(doc);
				} catch (Exception e) {
					isView_ = false;
					//					System.err.println("Exception thrown while checking isView for a document: " + e.getMessage() + " on notecoordinate "
					//							+ toString());
				}
			} else {
				isView_ = false;
			}
		}
		return isView_;
	}

	@Override
	public boolean isIcon() {
		if (isIcon_ == null) {
			isIcon_ = false;
			if (x == 0l && y == 0l) {
				isIcon_ = true;
			} else {
				Document doc = getDocument();
				if (doc != null && !doc.isNewNote()) {
					isIcon_ = DesignFactory.isIcon(doc);
				}
			}
		}
		return isIcon_;
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

	@Override
	public byte[] toByteArray() {
		byte[] result = new byte[24];
		insertByteArray(db, result, 0);
		insertByteArray(x, result, 8);
		insertByteArray(y, result, 16);
		return result;
	}

	@Override
	public int insertToByteArray(final byte[] bytes, final int pos) {
		insertByteArray(db, bytes, pos + 0);
		insertByteArray(x, bytes, pos + 8);
		insertByteArray(y, bytes, pos + 16);
		return pos + 24;
	}

	@Override
	public String toString() {
		return getReplicaId() + getUNID();
	}

	@Override
	public int compareTo(final org.openntf.domino.big.NoteCoordinate arg0) {
		NoteCoordinate local = (NoteCoordinate) arg0;
		if (this.db > local.db) {
			return 1;
		}
		if (this.db < local.db) {
			return -1;
		}
		if (this.y > local.y) {
			return 1;
		}
		if (this.y < local.y) {
			return -1;
		}
		if (this.x > local.x) {
			return 1;
		}
		if (this.x < local.x) {
			return -1;
		}
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		NoteCoordinate other = (NoteCoordinate) obj;
		if (db != other.db) {
			return false;
		}
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

}
