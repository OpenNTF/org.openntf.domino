package org.openntf.domino.big.impl;

import static org.openntf.domino.big.NoteCoordinate.Utils.getReplidFromLong;
import javolution.util.FastMap;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class DbCache extends ThreadLocal<Object> {
	private final FastMap<Long, Database> dbMap_ = new FastMap<Long, Database>();	//note: not Thread-safe yet.
	private String defaultServerName_ = "";

	public DbCache() {

	}

	public DbCache(final String defaultServer) {
		defaultServerName_ = defaultServer;
	}

	public void setDefaultServer(final String defaultServer) {
		defaultServerName_ = defaultServer;
	}

	public Database getDatabase(final long dbid) {
		return getDatabase(dbid, getDefaultServer());
	}

	public Database getDatabase(final long dbid, final String server) {
		return getDatabase(dbid, server, Factory.getSession(SessionType.CURRENT));
	}

	public Database getDatabase(final long dbid, final String server, final Session session) {
		Database result = dbMap_.get(dbid);
		if (result == null) {
			String replid = getReplidFromLong(dbid);
			DbDirectory dir = session.getDbDirectory(server);
			result = dir.openDatabaseByReplicaID(replid);
			dbMap_.put(dbid, result);
		}
		return result;
	}

	@SuppressWarnings("null")
	public Document getDocument(final NoteCoordinate nc) {
		Document result = null;
		long id = nc.getDbid();
		Database db = getDatabase(id);
		if (db == null) {
			System.out.println("DEBUG: Unable to find database with id " + id + " ("
					+ org.openntf.domino.big.NoteCoordinate.Utils.getReplidFromLong(id) + ")");
		}
		result = db.getDocumentByUNID(nc.getUNID(), true);
		return result;
	}

	public Document getDocument(final NoteCoordinate nc, final String server) {
		Document result = null;
		long id = nc.getDbid();
		Database db = getDatabase(id, server);
		result = db.getDocumentByUNID(nc.getUNID(), true);
		return result;
	}

	public String getDefaultServer() {
		return defaultServerName_;
	}
}
