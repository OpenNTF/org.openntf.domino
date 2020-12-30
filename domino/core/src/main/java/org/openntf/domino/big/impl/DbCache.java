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

import java.util.HashMap;
import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("nls")
public class DbCache {
	private final Map<Long, Database> dbMap_ = new HashMap<Long, Database>();	//note: not Thread-safe yet.
	private String defaultServerName_ = ""; //$NON-NLS-1$

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
