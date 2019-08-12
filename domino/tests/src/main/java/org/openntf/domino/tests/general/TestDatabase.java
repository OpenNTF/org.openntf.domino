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
}
