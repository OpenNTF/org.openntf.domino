package org.openntf.domino.tests.ntf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.XotsDaemon;

@RunWith(DominoJUnitRunner.class)
public class DeferredDocumentTest extends AbstractDominoRunnable {
	private static int THREAD_COUNT = 1;

	public static void main(final String[] args) {
		Factory.startup();

		DominoExecutor executor = new DominoExecutor(50);
		XotsDaemon.start(executor);

		for (int i = 0; i < THREAD_COUNT; i++) {
			XotsDaemon.queue(new DeferredDocumentTest());
		}

		Factory.shutdown();
	}

	@Test
	public void testDeferredDocuments() {
		for (int i = 0; i < THREAD_COUNT; i++) {
			XotsDaemon.queue(new DeferredDocumentTest());
		}
	}

	@Override
	public void run() {
		Session session = Factory.getSession();
		long collectionBuildNS = 0l;
		long documentBuildNS = 0l;
		long documentPageNS = 0l;
		int collectionSize = 0;
		int dbDocs = 0;
		try {
			Database db = session.getDatabase("", "imdb/movies.bak");
			dbDocs = db.getAllDocuments().getCount();
			long testStartTime = System.nanoTime();
			NoteCollection nc = db.createNoteCollection(false);
			nc.selectAllDataNotes(true);
			nc.setSelectionFormula("@Begins(Title; \"B\")"); //NTF comment or uncomment this to toggle testing selection formula impact
			nc.buildCollection();
			int[] nids = nc.getNoteIDs();
			long collectionTime = System.nanoTime();
			collectionBuildNS = collectionTime - testStartTime;
			collectionSize = nids.length;
			Document[] documents = new Document[collectionSize];
			for (int i = 0; i < collectionSize; i++) {
				documents[i] = db.getDocumentByID(nids[i], true/*false would mean full load of the Document*/);
			}
			long documentTime = System.nanoTime();
			documentBuildNS = documentTime - collectionTime;
			int pageSize = 100;
			int pageStart = 2014;
			for (int i = pageStart; i < (pageStart + pageSize); i++) {
				String report = documents[i].getNoteID() + " is a " + documents[i].getItemValueString("Title");
			}
			documentPageNS = System.nanoTime() - documentTime;

			System.out.println("Done with document details for " + pageSize + " out of " + collectionSize + " (" + dbDocs
					+ " total in db.) NC build took " + (collectionBuildNS / 1000000) + "ms, Document build took "
					+ (documentBuildNS / 1000000) + "ms, Document page took " + (documentPageNS / 1000000) + "ms");
		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName());
	}

	@Override
	public boolean shouldStop() {
		return true;
	}

}
