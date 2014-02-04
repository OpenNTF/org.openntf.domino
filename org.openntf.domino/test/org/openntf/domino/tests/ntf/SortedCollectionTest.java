package org.openntf.domino.tests.ntf;

import lotus.domino.NotesFactory;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class SortedCollectionTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new SortedCollectionTest(), "My thread");
		thread.start();
	}

	public SortedCollectionTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Session session = this.getSession();
		Database db = session.getDatabase("", "CollTest.nsf");
		DocumentCollection sortedColl = db.createSortedDocumentCollection();
		System.out.println("Is sorted: " + String.valueOf(sortedColl.isSorted()));
		View sorter = db.getView("Sorter");
		for (ViewEntry entry : sorter.getAllEntries()) {
			sortedColl.add(entry.getDocument());
		}
		for (Document doc : sortedColl) {
			System.out.println(doc.getItemValueString("MainSortValue"));
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
