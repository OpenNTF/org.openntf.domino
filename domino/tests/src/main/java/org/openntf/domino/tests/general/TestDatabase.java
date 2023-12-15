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
package org.openntf.domino.tests.general;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.tests.AllTests;
import org.openntf.domino.utils.Factory;

public class TestDatabase {
	@Test
	public void testCopyDatabase() throws IOException {
		Session session = Factory.getSession();
		Database database = session.getDatabase(AllTests.EMPTY_DB);
		
		Path tempPath = Files.createTempFile(getClass().getSimpleName(), ".nsf");
		Files.deleteIfExists(tempPath);
		
		Database copy = database.createCopy("", tempPath.toString());
		assertNotEquals("copy should not be null", null, copy);
		assertNotEquals("Replica IDs should differ", database.getReplicaID(), copy.getReplicaID());
		
		AllTests.EMPTY_DB_COPY = tempPath.toString();
	}
	
	/**
	 * Test for https://github.com/OpenNTF/org.openntf.domino/issues/164
	 */
	@Test
	public void testGetForm() {
		Session session = Factory.getSession();
		Database database = session.getDatabase(AllTests.EMPTY_DB);
		database.getForm(null);
		database.getForm("");
		database.getForm("fakeform");
	}
}
