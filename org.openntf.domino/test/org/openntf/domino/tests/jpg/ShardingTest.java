/**
 * 
 */
package org.openntf.domino.tests.jpg;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;

/**
 * @author jgallagher
 * 
 */
public class ShardingTest {
	private ShardingTest() {
	}

	public static void normalTest(final Session session, final String server, final String baseName, final int places) {
		ShardingDatabase database = new ShardingDatabase(session, null, server, baseName, places);
		database.initializeDatabases();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				ShardingDatabase.ShardingDocument doc = database.createDocument();
				doc.replaceItemValue("Form", "Round" + i);
				doc.replaceItemValue("i", i);
				doc.replaceItemValue("j", j);
				doc.save();
				Document savedDoc = doc.getDoc();
				Database savedDB = savedDoc.getParentDatabase();

				System.out.println("Stored doc in " + savedDB.getServer() + "!!" + savedDB.getFilePath());
			}
		}
	}

	public static void strategyTest(final Session session, final ShardingDatabase.HashingStrategy hashingStrategy,
			final ShardingDatabase.ServerStrategy serverStrategy, final String baseName, final int places) {
		ShardingDatabase database = new ShardingDatabase(session, hashingStrategy, serverStrategy, baseName, places);
		database.initializeDatabases();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				ShardingDatabase.ShardingDocument doc = database.createDocument();
				doc.replaceItemValue("Form", "Round" + i);
				doc.replaceItemValue("i", i);
				doc.replaceItemValue("j", j);
				doc.save();
				Document savedDoc = doc.getDoc();
				Database savedDB = savedDoc.getParentDatabase();

				System.out.println("Stored doc in " + savedDB.getServer() + "!!" + savedDB.getFilePath());
			}
		}
	}
}
