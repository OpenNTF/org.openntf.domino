/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
