package org.openntf.domino.tests.ntf;

import lotus.domino.NotesFactory;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DominoDbDirectoryTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new DominoDbDirectoryTest(), "My thread");
		thread.start();
	}

	public DominoDbDirectoryTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = this.getSession();
		DbDirectory dir = session.getDbDirectory(null);
		dir.setDirectoryType(DbDirectory.Type.TEMPLATE);
		for (Database db : dir) {
			System.out.println(db.getApiPath() + ": " + db.getSize());
		}

		dir.setDirectoryType(DbDirectory.Type.DATABASE);
		for (Database db : dir) {
			System.out.println(db.getApiPath() + ": " + db.getSize());
		}
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
