/**
 * 
 */
package org.openntf.domino.tests.jpg;

import static java.lang.Math.pow;
import static org.openntf.domino.utils.DominoUtils.checksum;

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
public class KVTest {
	private KVTest() {
	}

	// e.g. KVStore.testKVStore(session, "Telamon/Frost", "tests/b");
	public static void testKVStore(final Session session, final String server, final String baseName) {
		KeyValueStore store = new KeyValueStore(session, server, baseName);
		long initStart = System.nanoTime();
		store.initializeDatabases();
		long initEnd = System.nanoTime();

		long now = (new Date()).getTime();

		long saveStart = System.nanoTime();
		for (int i = 0; i < 5000; i++) {
			store.set(i + "_" + now, i);
		}
		long saveEnd = System.nanoTime();

		long readStart = System.nanoTime();
		for (int i = 0; i < 5000; i++) {
			store.get(i + "_" + now);
		}
		long readEnd = System.nanoTime();

		System.out.println("Initialization took " + ((initEnd - initStart) / 1000 / 1000) + "ms");
		System.out.println("Storage took " + ((saveEnd - saveStart) / 1000 / 1000) + "ms");
		System.out.println("Reading took " + ((readEnd - readStart) / 1000 / 1000) + "ms");
	}
}

class KeyValueStore {
	private final Session session_;
	private final String server_;
	private final String baseName_;
	private final Map<String, Database> dbCache_ = new HashMap<String, Database>();
	private final int places = 2;

	public KeyValueStore(final Session session, final String server, final String baseName) {
		session_ = session;
		server_ = server == null ? "" : server;
		baseName_ = baseName.toLowerCase().endsWith(".nsf") ? baseName.substring(0, baseName.length() - 4) : baseName;
	}

	public void initializeDatabases() {
		DbDirectory dbdir = session_.getDbDirectory(server_);
		for (int i = 0; i < pow(16, places); i++) {
			String key = Integer.toString(i, 16).toLowerCase();
			while (key.length() < places)
				key = "0" + key;
			String dbName = baseName_ + "-" + key + ".nsf";
			Database database = session_.getDatabase(server_, dbName, true);
			if (!database.isOpen()) {
				database = dbdir.createDatabase(dbName, true);
				database.createView();
				database.setOption(Database.DBOption.LZ1, true);
				database.setOption(Database.DBOption.COMPRESSDESIGN, true);
				database.setOption(Database.DBOption.COMPRESSDOCUMENTS, true);
				database.setOption(Database.DBOption.NOUNREAD, true);
			}
			dbCache_.put(dbName, database);
		}
	}

	public Object get(final String key) {
		String hashKey = checksum(key, "MD5");
		Database keyDB = getDatabaseForKey(hashKey);

		Document keyDoc = keyDB.getDocumentByKey(key);
		return keyDoc == null ? null : keyDoc.get("Value");
	}

	public void set(final String key, final Object value) {
		String hashKey = checksum(key, "MD5");
		Database keyDB = getDatabaseForKey(hashKey);

		Document keyDoc = keyDB.getDocumentByKey(key, true);

		keyDoc.replaceItemValue("Value", value);
		keyDoc.save();
	}

	private Database getDatabaseForKey(final String hashKey) {
		String dbName = baseName_ + "-" + hashKey.substring(0, 2) + ".nsf";

		if (!dbCache_.containsKey(dbName)) {
			Database database = session_.getDatabase(server_, dbName, true);
			if (!database.isOpen()) {
				DbDirectory dbdir = session_.getDbDirectory(server_);
				database = dbdir.createDatabase(dbName, true);
				database.createView();
				database.setOption(Database.DBOption.LZ1, true);
				database.setOption(Database.DBOption.COMPRESSDESIGN, true);
				database.setOption(Database.DBOption.COMPRESSDOCUMENTS, true);
				database.setOption(Database.DBOption.NOUNREAD, true);
			}
			dbCache_.put(dbName, database);
		}

		return dbCache_.get(dbName);
	}
}