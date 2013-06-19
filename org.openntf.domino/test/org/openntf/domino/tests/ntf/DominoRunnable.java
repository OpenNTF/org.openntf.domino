package org.openntf.domino.tests.ntf;

import lotus.domino.NotesFactory;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public class DominoRunnable implements Runnable {
	public static void main(String[] args) {
		DominoThread thread = new DominoThread(new DominoRunnable(), "My thread");
		thread.start();
	}

	public DominoRunnable() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.class, null);
			Database db = session.getDatabase("", "names.nsf");
			// whatever you're gonna do, do it fast!
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
