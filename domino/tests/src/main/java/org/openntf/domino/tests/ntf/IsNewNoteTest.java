package org.openntf.domino.tests.ntf;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class IsNewNoteTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new IsNewNoteTest(), "My thread");
		thread.start();
	}

	public IsNewNoteTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		Database db = session.getDatabase("", "log.nsf");
		int i = 0;
		try {
			for (i = 0; i < 5; i++) {
				Document doc = db.createDocument();
				System.out
						.println("doc " + i + " " + doc.getUniversalID() + ": " + doc.getNoteID() + " " + String.valueOf(doc.isNewNote()));
				//				doc.replaceItemValue("form", "junk");
				doc.save();
				System.out
						.println("doc " + i + " " + doc.getUniversalID() + ": " + doc.getNoteID() + " " + String.valueOf(doc.isNewNote()));
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}
