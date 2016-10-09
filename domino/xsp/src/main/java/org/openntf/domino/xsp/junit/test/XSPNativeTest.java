package org.openntf.domino.xsp.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import lotus.domino.NotesException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.junit.SessionDb;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.junit.ModuleJUnitRunner;
import org.openntf.junit4xpages.OsgiTest;

import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.Os;
import com.ibm.domino.napi.c.xsp.XSPNative;

@OsgiTest
@SessionDb("log.nsf")
@RunWith(ModuleJUnitRunner.class)
public class XSPNativeTest {

	public static void dummy4JUnit() throws Exception {
		assertFalse(false);
		assertTrue(true);
		assertEquals(1, 1);
		assertNull(null);
		assertNotNull(Boolean.TRUE);
		fail("???");
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		DominoUtils.setBubbleExceptions(true);
	}

	@After
	public void tearDown() throws Exception {
	}

	/*-------------------------------------------------------------------------*/
	@Test
	public void testGetDBHandle() throws NotesException {
		Database odaDB = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		long hOdaDB = XSPNative.getDBHandle(odaDB);
		System.out.println("HND: " + hOdaDB);
		assertTrue(hOdaDB > 0);
	}

	@Test
	public void testSetContextDatabase() throws NotesException {
		Database odaDB = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		long hOdaDB = XSPNative.getDBHandle(odaDB);
		Session sess = Factory.getSession(SessionType.NATIVE);
		assertNull(sess.getCurrentDatabase());
		System.out.println("HND2=" + hOdaDB);
		/*
		 * Has to be called with a Lotus Session object, since internally the Session is treated as a lotus.domino.local.Session
		 */
		lotus.domino.Database db = XSPNative.setContextDatabase(sess.getFactory().toLotus(sess), hOdaDB);
		assertEquals(db.getFileName(), "log.nsf");
		assertNotNull(sess.getCurrentDatabase());
		assertEquals(sess.getCurrentDatabase().getFileName(), "log.nsf");
	}

	@Test
	public void testCreateXPageSession() throws NException, NotesException {
		String userName = Factory.getLocalServerName();
		final long userHandle = NotesUtil.createUserNameList(userName);
		try {
			lotus.domino.Session xs = XSPNative.createXPageSession(userName, userHandle, false, true);
			assertEquals(userName, xs.getEffectiveUserName());
			xs.recycle();
		} finally {
			Os.OSMemFree(userHandle);
		}
	}

	@Test
	public void testCreateXPageSessionExt() throws NotesException, NException {
		String userName = Factory.getLocalServerName();
		final long userHandle = NotesUtil.createUserNameList(userName);
		try {
			lotus.domino.Session xs = XSPNative.createXPageSessionExt(userName, userHandle, false, true, false);
			assertEquals(userName, xs.getEffectiveUserName());
			xs.recycle();
		} finally {
			Os.OSMemFree(userHandle);
		}
	}

	@Test
	public void testDowngradeXPageRestrictions() throws NotesException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		System.out.println("Downgrade:" + XSPNative.downgradeXPageRestrictions(sess, sess.getEffectiveUserName(), null, 0L));
	}

	@Test
	public void testSetXPageRestrictions() throws NotesException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		XSPNative.setXPageRestrictions(sess, null, true);
		System.out.println("setXPageRestrictions passed");
	}

	@Test
	public void testIsRestrictedSigner() throws NotesException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		System.out.println("IsRestrictedSigner:" + XSPNative.isRestrictedSigner(sess, sess.getEffectiveUserName()));
	}

	@Test
	public void testIsDocEditable() throws NotesException {
		Database db = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		DocumentCollection dc = db.getAllDocuments();
		System.out.println("getAllDocuments=" + dc.size());
		int i = 1;
		for (Document doc : dc) {
			System.out.println("Doc " + i + " is editable: " + XSPNative.isDocEditable(doc));
			if (++i > 10)
				break;
		}
	}

	@Test
	public void testSetEclHandle() {
		Session sess = Factory.getSession(SessionType.CURRENT);
		System.out.println("setEclHandle=" + XSPNative.setEclHandle(sess, 512 | 4096));
	}

	@Test
	public void testXpgBEProfiler() throws NotesException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		XSPNative.startXpgBEProfiler(sess);
		testIsDocEditable();
		XSPNative.stopXpgBEProfiler(sess);
		System.out.println("BEProfiler was run");
		Database db = sess.getCurrentDatabase();
		Document doc = db.createDocument();
		XSPNative.getXpgBEProfilerResults(sess, doc, "XSPNative");
		System.out.println("BEProfiler results were fetched:");
		for (Item i : doc.getItems())
			System.out.println("    " + i.getName() + "=" + i.getValueString());
	}

}
