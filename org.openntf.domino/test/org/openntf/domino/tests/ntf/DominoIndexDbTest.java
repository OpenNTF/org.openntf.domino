package org.openntf.domino.tests.ntf;

import lotus.domino.NotesFactory;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.big.impl.IndexDatabase;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

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
		Session session = this.getSession();
		session.setConvertMIME(false);
		session.setFixEnable(Fixes.APPEND_ITEM_VALUE, true);
		session.setFixEnable(Fixes.FORCE_JAVA_DATES, true);
		session.setFixEnable(Fixes.CREATE_DB, true);
		DbDirectory dir = session.getDbDirectory("");
		Database indexDb = dir.createDatabase("index.nsf", true);
		//		Database indexDb = session.getDatabase("", "index.nsf", true);
		//		indexDb.open();
		IndexDatabase index = new IndexDatabase(indexDb);
		index.setCaseSensitive(true);

		index.scanServer(session, "");
		System.out.println("Complete");
	}

	protected Session getSession() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.class, null);
			return session;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
}
