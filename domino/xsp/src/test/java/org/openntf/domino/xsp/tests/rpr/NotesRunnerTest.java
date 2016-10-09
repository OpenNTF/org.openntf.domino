package org.openntf.domino.xsp.tests.rpr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.openntf.domino.junit.TestEnv.database;
import static org.openntf.domino.junit.TestEnv.session;

import lotus.domino.Database;
import lotus.domino.NotesException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.junit.NotesJUnitRunner;
import org.openntf.domino.junit.SessionUser;
import org.openntf.domino.junit.SessionDb;

/**
 * Tests if the NotesJUnitRunner works
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
@RunWith(NotesJUnitRunner.class)
//@RunWith(DominoJUnitRunner.class)
public class NotesRunnerTest {

	@Test
	public void testSession() throws NotesException {
		assertNotNull(session);
		assertNotNull(session.getEffectiveUserName());
		assertNull(database);
	}

	@Test
	@SessionUser("CN=Tony Stark/O=Avengers")
	public void testSessionNamed() throws NotesException {
		assertNotNull(session);
		System.out.println("Session class: " + session.getClass());
		assertEquals("CN=Tony Stark/O=Avengers", session.getEffectiveUserName());
	}

	@Test
	public void testSessionNamedEx1() throws NotesException {
		assertNotNull(session);
		Database db = session.getDatabase("", "names.nsf");
		assertNotNull(db);
	}

	@Test(expected = NotesException.class)
	@SessionUser("CN=Tony Stark/O=Avengers")
	public void testSessionNamedEx2() throws NotesException {
		assertNotNull(session);
		assertEquals("CN=Tony Stark/O=Avengers", session.getEffectiveUserName());
		session.getDatabase("", "names.nsf"); // Should throw a UAC - unless you have Tony Stark in your ACL
	}

	@Test
	@SessionDb("names.nsf")
	public void testDatabase() throws NotesException {
		assertNotNull(session);
		assertNotNull(session.getEffectiveUserName());
		assertNotNull(database);
		assertNotNull("names.nsf", database.getFilePath());
	}
}
