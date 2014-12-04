package org.openntf.domino.xsp.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Vector;

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
import org.openntf.domino.DxlExporter;
import org.openntf.domino.DxlImporter;
import org.openntf.domino.Form;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.exceptions.BackendBridgeSanityCheckException;
import org.openntf.domino.impl.Base;
import org.openntf.domino.junit.SessionDb;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.junit.ModuleJUnitRunner;
import org.openntf.junit4xpages.OsgiTest;

import com.ibm.domino.napi.c.BackendBridge;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.Os;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;

@OsgiTest
@SessionDb("log.nsf")
@RunWith(ModuleJUnitRunner.class)
public class BackendBridgeTest {

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
	public void testGetDatabaseHandleRO() {
		System.out.println("Test: getDatabaseHandleRO");
		Database odaDB = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		long hOdaDB = BackendBridge.getDatabaseHandleRO(odaDB);
		System.out.println("    HDB-RO=" + hOdaDB);
	}

	@Test
	public void testGetDocumentHandleRW() {
		System.out.println("Test: getDocumentHandleRW");
		Database db = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		DocumentCollection dc = db.getAllDocuments();
		int sz = dc.size();
		System.out.println("    getAllDocuments=" + sz);
		if (sz == 0)
			return;
		int i = sz >>> 1;
		Document found = null;
		for (Document doc : dc)
			if (i-- == 0) {
				found = doc;
				break;
			}
		long hDoc = BackendBridge.getDocumentHandleRW(found);
		System.out.println("    HDoc-RW=" + hDoc);
	}

	@Test
	public void testDetachDatabaseHandle() {
		System.out.println("Test: detachDatabaseHandle");
		Database odaDB = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		long hOdaDB = BackendBridge.getDatabaseHandleRO(odaDB);
		System.out.println("    HDB-RO=" + hOdaDB);
		hOdaDB = BackendBridge.detachDatabaseHandle(odaDB);
		System.out.println("    detachDatabaseHandle=" + hOdaDB);
	}

	@Test
	public void testDetachDocumentHandle() {
		System.out.println("Test: detachDocumentHandle");
		Database db = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		DocumentCollection dc = db.getAllDocuments();
		int sz = dc.size();
		System.out.println("    getAllDocuments=" + sz);
		if (sz == 0)
			return;
		int i = sz >>> 1;
		Document found = null;
		for (Document doc : dc)
			if (i-- == 0) {
				found = doc;
				break;
			}
		long hDoc = BackendBridge.getDocumentHandleRW(found);
		System.out.println("    HDoc-RW=" + hDoc);
		hDoc = BackendBridge.detachDocumentHandle(found);
		System.out.println("    detachDocumentHandle=" + hDoc);
	}

	@Test
	public void testCreateDatabase() throws NotesException {
		System.out.println("Test: createDatabase");
		Session sess = Factory.getSession(SessionType.CURRENT);
		lotus.domino.Database db = BackendBridge.createDatabase(sess, 1);
		System.out.println("    createDatabase: " + db);
	}

	@Test
	public void testCreateDocument() {
		System.out.println("Test: createDocument");
		Database db = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		lotus.domino.Document doc = BackendBridge.createDocument(db, 1);
		System.out.println("    createDocument: " + doc);
	}

	@Test
	public void testDxlExporterSetExtendedOptions() {
		System.out.println("Test: dxlExporterSetExtendedOptions");
		Session sess = Factory.getSession(SessionType.CURRENT);
		DxlExporter dxlExp = sess.createDxlExporter();
		BackendBridge.dxlExporterSetExtendedOptions(dxlExp, 4);
		System.out.println("    Set to 4");
	}

	@Test
	public void testDxlImporterSetExtendedOptions() {
		System.out.println("Test: dxlImporterSetExtendedOptions");
		Session sess = Factory.getSession(SessionType.CURRENT);
		DxlImporter dxlImp = sess.createDxlImporter();
		BackendBridge.dxlImporterSetExtendedOptions(dxlImp, 2, "OpenNTF");
		System.out.println("    Set to 2/OpenNTF");
	}

	@Test
	public void testSetContextDatabaseContext2() throws NotesException {
		System.out.println("Test: setContextDatabaseContext2");
		Session sess = Factory.getSession(SessionType.CURRENT);
		long hOdaDB = BackendBridge.getDatabaseHandleRO(sess.getCurrentDatabase());
		System.out.println("    HDB-RO=" + hOdaDB);
		/*
		 * Again, it is necessary to pass a Lotus Session as 1st parameter, otherwise you run into a NullPointerException
		 */
		NSFComponentModule.XPagesDatabase xdb = (NSFComponentModule.XPagesDatabase) BackendBridge.setContextDatabaseContext2(
				Base.toLotus(sess), hOdaDB, NSFComponentModule.XPagesDatabase.class);
		System.out.println("    setContextDatabaseContext2=" + xdb);
	}

	@Test
	public void testComputeWithForm() throws NotesException {
		System.out.println("Test: computeWithForm");
		Database db = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		DocumentCollection dc = db.getAllDocuments();
		int sz = dc.size();
		System.out.println("    getAllDocuments=" + sz);
		if (sz == 0)
			return;
		int i = sz >>> 1;
		Document found = null;
		for (Document doc : dc)
			if (i-- == 0) {
				found = doc;
				break;
			}
		boolean res = BackendBridge.computeWithForm(found, true, true);
		System.out.println("    Result=" + res);
	}

	@Test
	public void testComputeWithFormExt() throws NotesException {
		System.out.println("Test: computeWithFormExt");
		Database db = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		DocumentCollection dc = db.getAllDocuments();
		int sz = dc.size();
		System.out.println("    getAllDocuments=" + sz);
		if (sz == 0)
			return;
		int i = sz >>> 2;
		Document found = null;
		for (Document doc : dc)
			if (i-- == 0) {
				found = doc;
				break;
			}
		Form f = db.getForm("Events");
		System.out.println("    Found Form for 'Events': " + f);
		boolean res = BackendBridge.computeWithFormExt(found, f, true, true);
		System.out.println("    Result=" + res);
	}

	@Test
	public void testPassThroughSecurityErrors() {
		System.out.println("Test: passThroughSecurityErrors");
		Database db = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
		BackendBridge.passThroughSecurityErrors(db, true);
		System.out.println("    Done.");
	}

	@Test
	public void testGetViewEntryByKeyWithOptions() throws NotesException {
		Session sess = Factory.getSession(SessionType.CURRENT);
		Database db = sess.getCurrentDatabase();
		Vector<View> views = db.getViews();

		View dummyView = views.get(0);
		try {
			BackendBridge.getViewEntryByKeyWithOptions(dummyView, null, 42);
			fail("BackendBridge.getViewEntryByKeyWithOptions did not throw expected exceptions");
		} catch (BackendBridgeSanityCheckException allGood) {

		}
		Vector<String> key = new Vector<String>();
		key.add("a");
		Object e = BackendBridge.getViewEntryByKeyWithOptions(dummyView, key, 2243);
		assertTrue(e instanceof org.openntf.domino.ViewEntry);

	}

	@Test
	public void testSetNoRecycle() {
		System.out.println("Test: setNoRecyle");
		Session sess = Factory.getSession(SessionType.CURRENT);
		Database db = sess.getCurrentDatabase();
		DocumentCollection dc = db.getAllDocuments();
		int sz = dc.size();
		System.out.println("    getAllDocuments=" + sz);
		if (sz == 0)
			return;
		int i = sz >>> 3;
		Document found = null;
		for (Document doc : dc)
			if (i-- == 0) {
				found = doc;
				break;
			}
		BackendBridge.setNoRecycle(sess, found, true);
		System.out.println("    Done.");
	}

	@Test
	public void testForceRecycle() {
		System.out.println("Test: forceRecyle");
		Session sess = Factory.getSession(SessionType.CURRENT);
		Database db = sess.getCurrentDatabase();
		DocumentCollection dc = db.getAllDocuments();
		int sz = dc.size();
		System.out.println("    getAllDocuments=" + sz);
		if (sz == 0)
			return;
		int i = sz >>> 4;
		Document found = null;
		for (Document doc : dc)
			if (i-- == 0) {
				found = doc;
				break;
			}
		BackendBridge.forceRecycle(sess, found);
		System.out.println("    Done.");
	}

	@Test(expected = UnsatisfiedLinkError.class)
	public void testCreateXPageSession() throws Throwable {
		String userName = Factory.getLocalServerName();
		final long userHandle = NotesUtil.createUserNameList(userName);
		try {
			lotus.domino.Session xs = BackendBridge.createXPageSession(userName, userHandle, true, true, true, false);
		} finally {
			Os.OSMemFree(userHandle);
		}
	}

}
