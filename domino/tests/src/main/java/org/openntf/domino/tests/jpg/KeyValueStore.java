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
/**
 * 
 */
package org.openntf.domino.tests.jpg;

import static java.lang.Math.pow;
import static org.openntf.domino.utils.DominoUtils.checksum;

import java.util.HashMap;
import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.Session;

/**
 * @author jgallagher
 * 
 */
public class KeyValueStore {
	public interface ServerStrategy {
		public String getServerForHashChunk(final String hashChunk);
	}

	private final Session session_;

	private final String server_;
	private final ServerStrategy serverStrategy_;

	private final String baseName_;
	private final Map<String, Database> dbCache_ = new HashMap<String, Database>();
	private final Map<String, DbDirectory> dbdirCache_ = new HashMap<String, DbDirectory>();
	private final int places_;

	public KeyValueStore(final Session session, final String server, final String baseName, final int places) {
		if (session == null)
			throw new NullPointerException("session cannot be null");
		if (baseName == null || baseName.length() == 0)
			throw new IllegalArgumentException("baseName cannot be null or zero-length");
		if (places < 0)
			throw new IllegalArgumentException("places must be nonnegative");

		session_ = session;
		baseName_ = baseName.toLowerCase().endsWith(".nsf") ? baseName.substring(0, baseName.length() - 4) : baseName;
		places_ = places;

		server_ = server == null ? "" : server;
		dbdirCache_.put("only", session_.getDbDirectory(server_));
		serverStrategy_ = null;
	}

	public KeyValueStore(final Session session, final ServerStrategy serverStrategy, final String baseName, final int places) {
		if (session == null)
			throw new NullPointerException("session cannot be null");
		if (baseName == null || baseName.length() == 0)
			throw new IllegalArgumentException("baseName cannot be null or zero-length");
		if (places < 1)
			throw new IllegalArgumentException("places must be greater than zero");
		if (serverStrategy == null)
			throw new NullPointerException("serverStrategy cannot be null");

		session_ = session;
		baseName_ = baseName.toLowerCase().endsWith(".nsf") ? baseName.substring(0, baseName.length() - 4) : baseName;
		places_ = places;

		server_ = null;
		serverStrategy_ = serverStrategy;
	}

	public void initializeDatabases() {
		if (places_ > 0) {
			for (int i = 0; i < pow(16, places_); i++) {
				String hashChunk = Integer.toString(i, 16).toLowerCase();
				while (hashChunk.length() < places_)
					hashChunk = "0" + hashChunk;
				String server = serverStrategy_ == null ? server_ : serverStrategy_.getServerForHashChunk(hashChunk);
				DbDirectory dbdir = getDbDirectoryForHashChunk(hashChunk);
				String dbName = baseName_ + "-" + hashChunk + ".nsf";
				Database database = session_.getDatabase(server, dbName, true);
				if (!database.isOpen()) {
					database = createDatabase(dbdir, dbName);
				}
				dbCache_.put(dbName, database);
			}
		} else {
			Database database = session_.getDatabase(server_, baseName_ + ".nsf");
			if (!database.isOpen()) {
				database = createDatabase(getDbDirectoryForHashChunk(null), baseName_ + ".nsf");
			}
			dbCache_.put(baseName_ + ".nsf", database);
		}
	}

	public Object get(final String key) {
		String hashKey = checksum(key, "MD5");
		Database keyDB = getDatabaseForKey(hashKey);

		Document keyDoc = keyDB.getDocumentWithKey(key);
		return keyDoc == null ? null : keyDoc.get("Value");
	}

	public void put(final String key, final Object value) {
		String hashKey = checksum(key, "MD5");
		Database keyDB = getDatabaseForKey(hashKey);

		Document keyDoc = keyDB.getDocumentWithKey(key, true);

		keyDoc.replaceItemValue("Value", value);
		keyDoc.save();
	}

	private Database getDatabaseForKey(final String hashKey) {
		String hashChunk = hashKey.substring(0, places_);
		String dbName = baseName_ + "-" + hashChunk + ".nsf";

		if (!dbCache_.containsKey(dbName)) {
			String server = serverStrategy_ == null ? server_ : serverStrategy_.getServerForHashChunk(hashChunk);
			Database database = session_.getDatabase(server, dbName, true);
			if (!database.isOpen()) {
				DbDirectory dbdir = getDbDirectoryForHashChunk(hashChunk);
				database = createDatabase(dbdir, dbName);
			}
			dbCache_.put(dbName, database);
		}

		return dbCache_.get(dbName);
	}

	private DbDirectory getDbDirectoryForHashChunk(final String hashChunk) {
		if (server_ != null) {
			return dbdirCache_.get("only");
		} else {
			if (!dbdirCache_.containsKey(hashChunk)) {
				String server = serverStrategy_ == null ? server_ : serverStrategy_.getServerForHashChunk(hashChunk);
				dbdirCache_.put(hashChunk, session_.getDbDirectory(server));
			}
			return dbdirCache_.get(hashChunk);
		}
	}

	private Database createDatabase(final DbDirectory dbdir, final String dbName) {
		Database database = dbdir.createDatabase(dbName, true);
		database.createView();
		database.setOption(Database.DBOption.LZ1, true);
		database.setOption(Database.DBOption.COMPRESSDESIGN, true);
		database.setOption(Database.DBOption.COMPRESSDOCUMENTS, true);
		database.setOption(Database.DBOption.NOUNREAD, true);
		return database;
	}
}