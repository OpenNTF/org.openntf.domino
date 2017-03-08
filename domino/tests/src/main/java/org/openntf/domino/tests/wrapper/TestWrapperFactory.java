package org.openntf.domino.tests.wrapper;

import org.junit.Test;
import org.openntf.domino.Session;
import org.openntf.domino.tests.AllTests;
import org.openntf.domino.utils.Factory;

import static org.junit.Assert.*;

public class TestWrapperFactory {

	@Test
	public void testWrapSession() {
		AllTests.session = Factory.getWrapperFactory().fromLotus(AllTests.lotusSession, Session.SCHEMA, null);
		assertNotEquals("Wrapped session should not be null", null, AllTests.session);
	}
}
