package org.openntf.domino.tests.ntf;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.big.IndexDatabase;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class DominoIndexDbTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new DominoIndexDbTest(), "Index Thread");
		thread.start();
	}

	public DominoIndexDbTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		session.setConvertMIME(false);
		session.setFixEnable(Fixes.APPEND_ITEM_VALUE, true);
		session.setFixEnable(Fixes.FORCE_JAVA_DATES, true);
		session.setFixEnable(Fixes.CREATE_DB, true);
		DbDirectory dir = session.getDbDirectory("");
		Database indexDb = dir.createDatabase("index.nsf", true);
		//		Database indexDb = session.getDatabase("", "index.nsf", true);
		//		indexDb.open();
		IndexDatabase index = new org.openntf.domino.big.impl.IndexDatabase(indexDb);
		index.setCaseSensitive(true);

		index.scanServer(session, "");
		System.out.println("Complete");
	}

}
