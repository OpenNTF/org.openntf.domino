package org.openntf.domino.xsp.tests.rpr;


public class XspSessionTestEx {
	//	//private static String LOCAL_SERVER = masterSession.getUserName();
	//
	//	private static String REMOTE_UNTRUSTED_SERVER = "CN=srv-01-nprod/O=FOCONIS";
	//
	//	private static String REMOTE_TRUSTED_SERVER = "CN=srv-01-ntest85/OU=srv/O=FOCONIS";
	//
	//	//	@Test
	//	public void TestNativeSession() {
	//		Session sess = Factory.getSession();
	//		assertFalse(sess.isTrustedSession());
	//		assertFalse(sess.isAnonymous());
	//		assertFalse(sess.isRestricted());
	//		assertEquals(getCurrentId(), sess.getUserName());
	//		assertEquals(getCurrentId(), sess.getEffectiveUserName());
	//
	//		Database nab = sess.getDatabase(REMOTE_TRUSTED_SERVER, "names.nsf");
	//		assertNotNull(nab);
	//
	//		IconNote icn = nab.getDesign().getIconNote();
	//		assertNotNull(icn);
	//
	//	}
	//
	//	//@Test
	//	public void TestTrustedSession() {
	//		Session sess = Factory.getTrustedSession();
	//		assertTrue(sess.isTrustedSession());
	//		assertFalse(sess.isAnonymous());
	//		assertFalse(sess.isRestricted());
	//		System.out.println("Native Session User name      " + sess.getUserName());
	//		System.out.println("Native Session Effective name " + sess.getEffectiveUserName());
	//
	//		assertEquals(getCurrentId(), sess.getUserName());
	//		assertEquals(getCurrentId(), sess.getEffectiveUserName());
	//
	//		Database nab = sess.getDatabase(REMOTE_UNTRUSTED_SERVER, "names.nsf");
	//		assertNotNull(nab);
	//
	//		IconNote icn = nab.getDesign().getIconNote();
	//		assertNotNull(icn);
	//
	//	}
	//
	//	//	@Test
	//	public void TestNamedSession() {
	//		Session sess = Factory.getNamedSession("CN=The Tester/OU=Test/O=FOCONIS");
	//		//		assertFalse(sess.isTrustedSession());
	//		assertFalse(sess.isAnonymous());
	//		//assertFalse(sess.isRestricted());
	//		System.out.println("Named Session User name      " + sess.getUserName());
	//		System.out.println("Named Session Effective name " + sess.getEffectiveUserName());
	//
	//		Database db = sess.getDatabase(getCurrentId(), "Testdocuments.nsf");
	//
	//		Document doc = db.createDocument();
	//		doc.replaceItemValue("Test", "Test");
	//		doc.replaceItemValue("$Leser", "[AllesLesen]").setReaders(true);
	//		doc.save();
	//
	//	}
	//
	//	@Test
	//	public void TestFullAccessSession() throws NotesException, NException {
	//		//String username = "CN=Roland Praml/OU=01/OU=int/O=FOCONIS";
	//		String username = "CN=Theo Tester/OU=test/O=FOCONIS";
	//		long hList = com.ibm.domino.napi.c.NotesUtil.createUserNameList(username);
	//		// enforce access - preview server
	//		lotus.domino.Session raw = XSPNative.createXPageSessionExt(username, hList, false, false, true);
	//		Factory.setSession(raw);
	//		Session sess = Factory.getSession(); //"CN=The Tester/OU=Test/O=FOCONIS")
	//		assertFalse(sess.isTrustedSession());
	//		assertFalse(sess.isAnonymous());
	//		System.out.println("Named Session User name      " + sess.getUserName());
	//		System.out.println("Named Session Effective name " + sess.getEffectiveUserName());
	//		//assertFalse(sess.isRestricted());
	//
	//		Database db = sess.getDatabase(getCurrentId(), "Testdocuments.nsf");
	//
	//		for (Document doc : db.getAllDocuments()) {
	//			System.out.println("ID:" + doc.getUniversalID());
	//		}
	//		Document doc = db.getDocumentByUNID("D4E1E39F32A051A7C1257D89005940C6");
	//		doc.remove(true);
	//	}
}
