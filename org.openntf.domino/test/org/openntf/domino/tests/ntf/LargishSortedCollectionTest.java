package org.openntf.domino.tests.ntf;

import java.util.ArrayList;
import java.util.List;

import lotus.domino.NotesFactory;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.big.impl.IndexDatabase;
import org.openntf.domino.helpers.DocumentSorter;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class LargishSortedCollectionTest implements Runnable {
	private static int THREAD_COUNT = 1;

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new LargishSortedCollectionTest());
		}
		de.shutdown();
		//		for (int i = 0; i < 4; i++) {
		//			DominoThread thread = new DominoThread(new LargishSortedCollectionTest(), "My thread " + i);
		//			thread.start();
		//		}
	}

	public LargishSortedCollectionTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		Session session = this.getSession();
		//		Database db = session.getDatabase("", "events4.nsf");
		Database db = session.getDatabase("", "imdb/movies.nsf");
		//		System.out.println("Starting build of byMod view");
		//		long vStartTime = System.nanoTime();
		//		View newView = db.createView("ModTest", "SELECT @All");
		//		ViewColumn modCol = newView.createColumn(1, "Modified", "@Modified");
		//		modCol.setSorted(true);
		//		newView.refresh();
		//		long vEndTime = System.nanoTime();
		//		System.out.println("Completed building byModView in " + ((vEndTime - vStartTime) / 1000000) + "ms");
		IndexDatabase index = new IndexDatabase(session.getDatabase("", "redpill/index.nsf"));
		Document indexDoc = index.getDbDocument(db.getReplicaID());

		//		System.out.println("UNSORTED");

		List<String> criteria = new ArrayList<String>();
		//		criteria.add("@doclength");
		criteria.add("@modifieddate");
		try {
			DocumentSorter sorter = null;

			if (indexDoc.hasItem("DocumentSorter")) {
				sorter = indexDoc.getItemValue("DocumentSorter", DocumentSorter.class);
				sorter.setDatabase(db);
				System.out.println("Starting resort of " + sorter.getCount() + " documents");
			} else {
				DocumentCollection coll = db.getAllDocuments();
				sorter = new DocumentSorter(coll, criteria);
				System.out.println("Starting resort of " + coll.getCount() + " documents");
			}
			//			System.out.println("SORTING...");

			long startTime = System.nanoTime();
			DocumentCollection sortedColl = sorter.sort();
			long endTime = System.nanoTime();
			System.out.println("Completed resort in " + ((endTime - startTime) / 1000000) + "ms");
			int count = 0;
			for (Document doc : sortedColl) {
				//				System.out.println(doc.getLastModifiedDate().getTime() + " " + doc.getNoteID());
				if (++count > 500) {
					break;
				}
			}

			indexDoc.replaceItemValue("DocumentList", sortedColl);
			indexDoc.replaceItemValue("DocumentSorter", sorter);
			indexDoc.save();
			System.out.println("Saved sorter and result list to document of length " + (indexDoc.getSize() / 1024) + "KB");
			//			System.out.println("SORTED");
			//			for (Document doc : sortedColl) {
			//				System.out.println(doc.getItemValueString("MainSortValue") + " " + doc.getLastModifiedDate().getTime());
			//			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000000) + "s");
		//		View modView = db.getView("AllByLengthMod");
		//		long startTime = System.nanoTime();
		//		if (modView != null) {
		//			modView.refresh();
		//		}
		//		long endTime = System.nanoTime();
		//		System.out.println("Completed view build in " + ((endTime - startTime) / 1000000) + "ms");

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
