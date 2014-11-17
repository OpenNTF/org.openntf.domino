package org.openntf.domino.tests.rpr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.openntf.domino.junit.TestEnv.REMOTE_TRUSTED_SERVER;
import static org.openntf.domino.junit.TestEnv.REMOTE_UNTRUSTED_SERVER;
import static org.openntf.domino.junit.TestEnv.SESSION_USER;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.design.IconNote;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class SessionTestEx {

	@Test
	public void TestNativeSession() {
		Session sess = Factory.getSession(SessionType.CURRENT);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertFalse(sess.isRestricted());
		assertEquals(SESSION_USER, sess.getUserName());
		assertEquals(SESSION_USER, sess.getEffectiveUserName());

		Database nab = sess.getDatabase(REMOTE_TRUSTED_SERVER, "names.nsf");
		assertNotNull(nab);

		IconNote icn = nab.getDesign().getIconNote();
		assertNotNull(icn);

	}

	@Test
	public void TestTrustedSession() {
		Session sess = Factory.getSession(SessionType.TRUSTED);
		assertTrue(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		assertFalse(sess.isRestricted());
		System.out.println("Native Session User name      " + sess.getUserName());
		System.out.println("Native Session Effective name " + sess.getEffectiveUserName());

		assertEquals(SESSION_USER, sess.getUserName());
		assertEquals(SESSION_USER, sess.getEffectiveUserName());

		Database nab = sess.getDatabase(REMOTE_UNTRUSTED_SERVER, "names.nsf");
		assertNotNull(nab);

		IconNote icn = nab.getDesign().getIconNote();
		assertNotNull(icn);

	}

	@Test
	public void TestNamedSession() {
		Session sess = Factory.getNamedSession("CN=The Tester/OU=Test/O=FOCONIS", false);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		//assertFalse(sess.isRestricted());
		System.out.println("Named Session User name      " + sess.getUserName());
		System.out.println("Named Session Effective name " + sess.getEffectiveUserName());

		Database db = sess.getDatabase(SESSION_USER, "Testdocuments.nsf");

		Document doc = db.createDocument();
		doc.replaceItemValue("Test", "Test");
		doc.replaceItemValue("$Leser", "[AllesLesen]").setReaders(true);
		doc.save();

	}

	@Test
	public void TestFullAccessSession() {
		//Session master = Factory.fromLotus(masterSession, Session.SCHEMA, null);
		Session normal = Factory.getSession(SessionType.CURRENT);
		//Session sess = Factory.getNamedSession("CN=Theo Tester/OU=test/O=FOCONIS", true);
		Session sess = Factory.getNamedSession(SESSION_USER, false);
		//assertNotSame(master, sess);
		//assertNotSame(masterSession, normal);
		assertNotSame(normal, sess);
		assertFalse(sess.isTrustedSession());
		assertFalse(sess.isAnonymous());
		//assertFalse(sess.isRestricted());

		Database db = sess.getDatabase(SESSION_USER, "Testdocuments.nsf");

		for (Document doc : db.getAllDocuments()) {
			System.out.println("ID:" + doc.getUniversalID());
		}

	}
}
