package org.openntf.domino.tests.wrapper;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.tests.AllTests;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class TestWrapperFactory {

	@Test
	public void testWrapSession() throws Exception {
		AllTests.session = Factory.getWrapperFactory().fromLotus(AllTests.lotusSession, Session.SCHEMA, null);
		assertNotEquals("Wrapped session should not be null", null, AllTests.session);
		System.out.println("Path is " + AllTests.EMPTY_DB);
		Database db = AllTests.session.getDatabase(AllTests.EMPTY_DB);
		assertNotNull(db);
		
		Factory.setSessionFactory(() -> AllTests.session, SessionType.CURRENT);
	}
}
