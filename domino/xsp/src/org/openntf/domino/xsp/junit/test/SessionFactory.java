package org.openntf.domino.xsp.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.ServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.junit.TestEnv;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.thread.ModuleLoader;
import org.openntf.domino.xsp.xots.FakeHttpRequest;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

@RunWith(DominoJUnitRunner.class)
public class SessionFactory {

	/**
	 * This test tests if we have a correct (untrusted) session.
	 */
	@Test
	public void TestNativeSession() {
		Session sess = Factory.getSession(SessionType.CURRENT);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertFalse(sess.isRestricted());
		assertEquals(TestEnv.SESSION_USER, sess.getUserName());
		assertEquals(TestEnv.SESSION_USER, sess.getEffectiveUserName());

		// now check if we can access a database on a trusted server (this should work)
		Database nab = sess.getDatabase(TestEnv.REMOTE_TRUSTED_SERVER, "names.nsf");
		assertNotNull(nab);

		IconNote icn = nab.getDesign().getIconNote();
		assertNotNull(icn);

		// now check if we can access a database on an untrusted server (this should not work)
		// RPr TODO: Behavior inconsistent, see #testNamedSession 
		nab = sess.getDatabase(TestEnv.REMOTE_UNTRUSTED_SERVER, "names.nsf"); // Returns null
		assertNull(nab);
	}

	/**
	 * This test test if we have a correct trusted session by accessing a remote untrusted server.
	 * 
	 * Attention: Creating trusted session does not work in Domino-server environment (due AgentSecurityManager)
	 */
	@Test
	public void TestTrustedSession() {
		Session sess = Factory.getSession(SessionType.TRUSTED);
		assertTrue(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertFalse(sess.isRestricted());
		System.out.println("Native Session User name      " + sess.getUserName());
		System.out.println("Native Session Effective name " + sess.getEffectiveUserName());

		assertEquals(TestEnv.SESSION_USER, sess.getUserName());
		assertEquals(TestEnv.SESSION_USER, sess.getEffectiveUserName());

		// now check if we can access a database on a trusted server (this should work)
		Database nab = sess.getDatabase(TestEnv.REMOTE_TRUSTED_SERVER, "names.nsf");
		assertNotNull(nab);

		IconNote icn = nab.getDesign().getIconNote();
		assertNotNull(icn);

		// now check if we can access a database on an untrusted server (this should work, because we have a trusted session)
		nab = sess.getDatabase(TestEnv.REMOTE_UNTRUSTED_SERVER, "names.nsf");
		assertNotNull(nab);

	}

	@Test
	public void TestTrustedSessionWNotesContext() throws ServletException {
		NSFComponentModule module = ModuleLoader.loadModule("names.nsf", false);
		NotesContext ctx = new NotesContext(module);
		NotesContext.initThread(ctx);
		try {
			ctx.initRequest(new FakeHttpRequest("CN=Roland Praml/OU=01/OU=int/O=FOCONIS"));
			Session sess = Factory.getSession(SessionType.TRUSTED);
			assertTrue(sess.isTrustedSession());
			assertFalse(sess.isAnonymous());
			assertFalse(sess.isRestricted());
			System.out.println("Native Session User name      " + sess.getUserName());
			System.out.println("Native Session Effective name " + sess.getEffectiveUserName());

			assertEquals(TestEnv.SESSION_USER, sess.getUserName());
			assertEquals(TestEnv.SESSION_USER, sess.getEffectiveUserName());

			// now check if we can access a database on a trusted server (this should work)
			Database nab = sess.getDatabase(TestEnv.REMOTE_TRUSTED_SERVER, "names.nsf");
			assertNotNull(nab);

			IconNote icn = nab.getDesign().getIconNote();
			assertNotNull(icn);

			// now check if we can access a database on an untrusted server (this should work, because we have a trusted session)
			nab = sess.getDatabase(TestEnv.REMOTE_UNTRUSTED_SERVER, "names.nsf");
			assertNotNull(nab);
		} finally {
			NotesContext.termThread();
		}

	}

	/**
	 * This test checks if we can create named sessions
	 */
	@Test(expected = UserAccessException.class)
	public void TestNamedSession() {
		Session sess = Factory.getNamedSession("CN=Tony Stark/O=Avengers", false);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertTrue(sess.isRestricted()); //- the XPage sessions is not restricted, the native named sessions are
		System.out.println("Named Session User name      " + sess.getUserName());
		System.out.println("Named Session Effective name " + sess.getEffectiveUserName());

		assertEquals("CN=Tony Stark/O=Avengers", sess.getEffectiveUserName());

		// RPr TODO: Behavior inconsistent, see #testNativeSession
		Database db = sess.getDatabase(TestEnv.SESSION_USER, "names.nsf"); // Throws UAC
		assertNotNull(db);

	}

	/**
	 * Checks full access session (cannot really test if it will work)
	 */
	@Test
	public void TestFullAccessSession() {
		//Session master = Factory.fromLotus(masterSession, Session.SCHEMA, null);
		Session normal = Factory.getSession(SessionType.CURRENT);
		Session sess = Factory.getNamedSession(TestEnv.SESSION_USER, false);

		assertNotSame(normal, sess);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertEquals(TestEnv.SESSION_USER, sess.getUserName());

		Database db = sess.getDatabase(TestEnv.SESSION_USER, "names.nsf");
		assertNotNull(db);

		db = sess.getDatabase(TestEnv.REMOTE_UNTRUSTED_SERVER, "names.nsf");
		// RPr TODO: Behavior inconsistent, see #testNativeSession
		assertNull(db);
	}

	@Test
	public void TestSignerSession() {
		//Session master = Factory.fromLotus(masterSession, Session.SCHEMA, null);
		Session normal = Factory.getSession(SessionType.SIGNER);
		Session sess = Factory.getNamedSession(TestEnv.SESSION_USER, false);

		assertNotSame(normal, sess);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertEquals(TestEnv.SESSION_USER, sess.getUserName());

		Database db = sess.getDatabase(TestEnv.SESSION_USER, "names.nsf");
		assertNotNull(db);

		db = sess.getDatabase(TestEnv.REMOTE_UNTRUSTED_SERVER, "names.nsf");
		// RPr TODO: Behavior inconsistent, see #testNativeSession
		assertNull(db);
	}

	@Test
	public void TestSignerFullAccessSession() {
		//Session master = Factory.fromLotus(masterSession, Session.SCHEMA, null);
		Session normal = Factory.getSession(SessionType.SIGNER_FULL_ACCESS);
		Session sess = Factory.getNamedSession(TestEnv.SESSION_USER, false);

		assertNotSame(normal, sess);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertEquals(TestEnv.SESSION_USER, sess.getUserName());

		Database db = sess.getDatabase(TestEnv.SESSION_USER, "names.nsf");
		assertNotNull(db);

		db = sess.getDatabase(TestEnv.REMOTE_UNTRUSTED_SERVER, "names.nsf");
		// RPr TODO: Behavior inconsistent, see #testNativeSession
		assertNull(db);
	}

	@Test
	public void TestCurrentFullAccessSession() {
		//Session master = Factory.fromLotus(masterSession, Session.SCHEMA, null);
		Session normal = Factory.getSession(SessionType.CURRENT_FULL_ACCESS);
		Session sess = Factory.getNamedSession(TestEnv.SESSION_USER, false);

		assertNotSame(normal, sess);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertEquals(TestEnv.SESSION_USER, sess.getUserName());

		Database db = sess.getDatabase(TestEnv.SESSION_USER, "names.nsf");
		assertNotNull(db);

		db = sess.getDatabase(TestEnv.REMOTE_UNTRUSTED_SERVER, "names.nsf");
		// RPr TODO: Behavior inconsistent, see #testNativeSession
		assertNull(db);
	}

}
