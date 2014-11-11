package org.openntf.domino.tests.ntf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lotus.domino.NotesFactory;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.helpers.DocumentSorter;
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
		DocumentCollection coll = db.getAllDocuments();
		//		System.out.println("UNSORTED");
		for (Document doc : coll) {
			//			System.out.println(doc.getItemValueString("MainSortValue") + " " + doc.getLastModifiedDate().getTime());
		}

		List<String> criteria = new ArrayList<String>();
		criteria.add("MainSortValue");
		criteria.add("@modifieddate");
		try {
			DocumentSorter sorter = new DocumentSorter(coll, criteria);
			//			System.out.println("SORTING...");
			long startTime = System.nanoTime();
			DocumentCollection sortedColl = sorter.sort();
			long endTime = System.nanoTime();
			System.out.println("Completed resort in " + ((endTime - startTime) / 1000000) + "ms");
			//			System.out.println("SORTED");
			for (Document doc : sortedColl) {
				System.out.println(doc.getItemValueString("MainSortValue") + " " + doc.getLastModifiedDate().getTime() + " "
						+ Integer.valueOf(doc.getNoteID(), 16));
			}
			DocumentSorter.DocumentData[] dataset = sorter._debugGetDataset();
			for (DocumentSorter.DocumentData data : dataset) {
				StringBuilder sb = new StringBuilder();
				for (Serializable s : data._debugGetValues()) {
					sb.append(s);
					sb.append(',');
				}
				System.out.println(sb.toString());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	protected Session getSession() {
		try {
			Session session = Factory.fromLotus(NotesFactory.createSession(), Session.SCHEMA, null);
			return session;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}
}
