package org.openntf.domino.tests.jpg;

import static java.lang.Math.pow;

import java.util.Date;
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
public class ShardingDatabase {
	private final Session session_;

	private final String server_;
	private final HashingStrategy hashingStrategy_;
	private final ServerStrategy serverStrategy_;

	private final String baseName_;
	private boolean recalcOnSave_;
	private final Map<String, Database> dbCache_ = new HashMap<String, Database>();
	private final Map<String, DbDirectory> dbdirCache_ = new HashMap<String, DbDirectory>();
	private final int places_;

	public ShardingDatabase(final Session session, final HashingStrategy hashingStrategy, final String server, final String baseName,
			final int places) {
		if (session == null)
			throw new IllegalArgumentException("session cannot be null");
		if (baseName == null || baseName.length() == 0)
			throw new IllegalArgumentException("baseName cannot be null or zero-length");
		if (places < 0)
			throw new IllegalArgumentException("databases must be nonnegative");

		session_ = session;
		baseName_ = baseName.toLowerCase().endsWith(".nsf") ? baseName.substring(0, baseName.length() - 4) : baseName;
		places_ = places;

		hashingStrategy_ = hashingStrategy;
		server_ = server == null ? "" : server;
		dbdirCache_.put("only", session_.getDbDirectory(server_));
		serverStrategy_ = null;
	}

	public ShardingDatabase(final Session session, final HashingStrategy hashingStrategy, final ServerStrategy serverStrategy,
			final String baseName, final int places) {
		if (session == null)
			throw new IllegalArgumentException("session cannot be null");
		if (baseName == null || baseName.length() == 0)
			throw new IllegalArgumentException("baseName cannot be null or zero-length");
		if (places < 1)
			throw new IllegalArgumentException("places must be greater than zero");
		if (serverStrategy == null)
			throw new IllegalArgumentException("serverStrategy cannot be null");

		session_ = session;
		baseName_ = baseName.toLowerCase().endsWith(".nsf") ? baseName.substring(0, baseName.length() - 4) : baseName;
		places_ = places;

		hashingStrategy_ = hashingStrategy;
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

	public ShardingDocument createDocument() {
		return new ShardingDocument(getStagingDatabase().createDocument());
	}

	public ShardingDocument getDocumentByUNID(final String unid) {
		return new ShardingDocument(getDatabaseForHash(unid).getDocumentByUNID(unid));
	}

	private Database getStagingDatabase() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < places_; i++) {
			builder.append('0');
		}
		String hashChunk = builder.toString();
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

	private Database getDatabaseForHash(final String hash) {
		String hashChunk = hash.substring(0, places_);
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

	public interface ServerStrategy {
		String getServerForHashChunk(String hashChunk);
	}

	public interface HashingStrategy {
		String getHashForMap(Map<String, Object> doc);
	}

	public class ShardingDocument {
		private Document doc_;

		protected ShardingDocument(final Document origDoc) {
			doc_ = origDoc;
		}

		public Object getItemValue(String itemName) {
			return doc_.getItemValue(itemName);
		}

		public void replaceItemValue(String itemName, Object value) {
			doc_.replaceItemValue(itemName, value);
		}

		public Document getDoc() {
			return doc_;
		}

		public boolean save() {
			Database destDB;
			if (doc_.isNewNote() && hashingStrategy_ != null) {
				String hash = hashingStrategy_.getHashForMap(doc_);
				doc_.setUniversalID(hash);
				doc_.replaceItemValue("$Created", new Date());
				destDB = getDatabaseForHash(hash);
			} else {
				destDB = getDatabaseForHash(doc_.getUniversalID());
			}

			Database currentDB = doc_.getParentDatabase();
			if (!(currentDB.getFilePath().equalsIgnoreCase(destDB.getFilePath()) && currentDB.getServer().equalsIgnoreCase(
					destDB.getServer()))) {

				Document destDoc = destDB.createDocument();
				doc_.copyAllItems(destDoc, true);
				destDoc.replaceItemValue("$Created", doc_.getCreated());
				destDoc.setUniversalID(doc_.getUniversalID());
				doc_ = destDoc;
			}

			return doc_.save();
		}
	}

	public boolean isRecalcOnSave() {
		return recalcOnSave_;
	}

	public void setRecalcOnSave(boolean recalcOnSave) {
		this.recalcOnSave_ = recalcOnSave;
	}
}
