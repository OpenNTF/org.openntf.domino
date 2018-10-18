package org.openntf.domino.tests.ntf;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
public class DocumentCollectionIteratorTest implements Runnable {
	private static int THREAD_COUNT = 1;

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new DocumentCollectionIteratorTest());
		}
		// de.shutdown();
		//		for (int i = 0; i < 4; i++) {
		//			DominoThread thread = new DominoThread(new LargishSortedCollectionTest(), "My thread " + i);
		//			thread.start();
		//		}
		Factory.shutdown();
	}

	public DocumentCollectionIteratorTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		try {
			Session session = Factory.getSession(SessionType.CURRENT);
			Database db = session.getDatabase("", "events4.nsf");
			DocumentCollection coll = db.getAllDocuments();
			for (Document doc : coll) {
				System.out.println("nid: " + doc.getNoteID());
			}
			long endTime = System.nanoTime();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}
