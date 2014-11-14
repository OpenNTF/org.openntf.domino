package org.openntf.domino.testsuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.junit.TestProps;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

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
		assertEquals(TestProps.SESSION_USER, sess.getUserName());
		assertEquals(TestProps.SESSION_USER, sess.getEffectiveUserName());

		Database nab = sess.getDatabase(TestProps.REMOTE_TRUSTED_SERVER, "names.nsf");
		assertNotNull(nab);

		IconNote icn = nab.getDesign().getIconNote();
		assertNotNull(icn);
	}

	/**
	 * This test test if we have a correct trusted session by accessing a remote untrusted server
	 */
	@Test
	public void TestTrustedSession() {
		Session sess = Factory.getSession(SessionType.TRUSTED);
		assertTrue(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertFalse(sess.isRestricted());
		System.out.println("Native Session User name      " + sess.getUserName());
		System.out.println("Native Session Effective name " + sess.getEffectiveUserName());

		assertEquals(TestProps.SESSION_USER, sess.getUserName());
		assertEquals(TestProps.SESSION_USER, sess.getEffectiveUserName());

		Database nab = sess.getDatabase(TestProps.REMOTE_UNTRUSTED_SERVER, "names.nsf");
		assertNotNull(nab);

		IconNote icn = nab.getDesign().getIconNote();
		assertNotNull(icn);

	}

	@Test
	public void TestNamedSession() {
		Session sess = Factory.getNamedSession("CN=The Tester/OU=Test/O=FOCONIS", true);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		//assertFalse(sess.isRestricted());
		System.out.println("Named Session User name      " + sess.getUserName());
		System.out.println("Named Session Effective name " + sess.getEffectiveUserName());

		Database db = sess.getDatabase(TestProps.SESSION_USER, "Testdocuments.nsf");

		Document doc = db.createDocument();
		doc.replaceItemValue("Test", "Test");
		doc.replaceItemValue("$Leser", "[AllesLesen]").setReaders(true);
		doc.save();

	}

	@Test
	public void TestFullAccessSession() {
		//Session master = Factory.fromLotus(masterSession, Session.SCHEMA, null);
		Session normal = Factory.getSession(SessionType.CURRENT);
		Session sess = Factory.getNamedSession(TestProps.SESSION_USER, false);

		assertNotSame(normal, sess);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertEquals(TestProps.SESSION_USER, sess.getUserName());

		Database db = sess.getDatabase(TestProps.SESSION_USER, "names.nsf");
		assertNotNull(db);
		System.out.println("Documents in " + db + ": " + db.getAllDocuments().size());

		db = sess.getDatabase(TestProps.REMOTE_UNTRUSTED_SERVER, "names.nsf");
		assertNotNull(db);
		System.out.println("Documents in " + db + ": " + db.getAllDocuments().size());

	}
}
