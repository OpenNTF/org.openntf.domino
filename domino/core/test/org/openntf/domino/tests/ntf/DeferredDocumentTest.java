package org.openntf.domino.tests.ntf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import lotus.domino.NotesException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.Xots;

@RunWith(DominoJUnitRunner.class)
@Tasklet(session = Tasklet.Session.NATIVE)
public class DeferredDocumentTest extends AbstractDominoRunnable {
	private static int THREAD_COUNT = 1;

	//	public static void main(final String[] args) {
	//		Factory.startup();
	//
	//		DominoExecutor executor = new DominoExecutor(50);
	//		XotsDaemon.start(executor);
	//
	//		for (int i = 0; i < THREAD_COUNT; i++) {
	//			XotsDaemon.queue(new DeferredDocumentTest());
	//		}
	//
	//		Factory.shutdown();
	//	}

	/**
	 * Test if deferred will not recycle each other.
	 * 
	 * @throws NotesException
	 */
	@Test
	public void testIdentity() throws NotesException {
		Session session = Factory.getSession(SessionType.CURRENT);
		assertNotNull(session);
		Database db = session.getDatabase("", "log.nsf");
		assertNotNull(db);

		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		int[] nids = nc.getNoteIDs();
		int nid1 = nids[0];
		int nid2 = nids[1];
		Document doc1 = db.getDocumentByID(nid1, true);
		Document doc2 = db.getDocumentByID(nid1, true);
		Document doc3 = db.getDocumentByID(nid2, true);
		Document doc4 = db.getDocumentByID(nid2, true);

		doc2.replaceItemValue("testfield", "testvalue");
		doc4.replaceItemValue("testfield", "testvalue2");

		assertEquals(doc1, doc2);
		assertFalse(doc1 == doc2);

		assertEquals(doc3, doc4);
		assertFalse(doc3 == doc4);

		assertEquals("testvalue", doc1.getItemValueString("testfield"));
		assertEquals("testvalue", doc2.getItemValueString("testfield"));
		assertEquals("testvalue2", doc3.getItemValueString("testfield"));
		assertEquals("testvalue2", doc4.getItemValueString("testfield"));
		//doc2 = null;
		for (int i = 0; i < 1000; i++) {
			doc1 = null;
			doc3 = null;
			System.gc();
			doc1 = db.getDocumentByID(Integer.toHexString(nid1));
			doc3 = db.getDocumentByID(Integer.toHexString(nid2));
			if (i % 100 == 0)
				System.out.println("Test complete: " + i / 10 + "%");
			//doc2.recycle();
			//assertEquals("testvalue", doc2.getItemValueString("testfield"));
			doc4 = db.getDocumentByID(nid2, true);
			assertEquals("testvalue", doc1.getItemValueString("testfield"));
			assertEquals("testvalue", doc2.getItemValueString("testfield"));
			assertEquals("testvalue2", doc3.getItemValueString("testfield"));
			assertEquals("testvalue2", doc4.getItemValueString("testfield"));
		}
	}

	@Test
	public void testDeferredDocuments() throws InterruptedException, ExecutionException {
		Session session = Factory.getSession(SessionType.CURRENT);
		assertNotNull(session);
		List<Future<?>> tasks = new ArrayList<Future<?>>();
		for (int i = 0; i < THREAD_COUNT; i++) {
			tasks.add(Xots.getService().submit(new DeferredDocumentTest()));
		}
		for (Future<?> f : tasks) {
			f.get(); // to catch the exceptions!
		}
		System.out.println("DONE");
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		assertNotNull(session);
		long collectionBuildNS = 0l;
		long documentBuildNS = 0l;
		long documentPageNS = 0l;
		int collectionSize = 0;
		int dbDocs = 0;

		Database db = session.getDatabase("", "imdb/movies.bak");
		assertNotNull(db);
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
			documents[i] = db.getDocumentByID(nids[i], true); /*false would mean full load of the Document*/
		}
		long documentTime = System.nanoTime();
		documentBuildNS = documentTime - collectionTime;
		int pageSize = 1000;
		int pageStart = 2014;

		assertTrue("found too few documents to perform test", documents.length > (pageStart + pageSize));

		for (int i = pageStart; i < (pageStart + pageSize); i++) {
			assertTrue(documents[i].getItemValueString("Title").length() > 0);
			assertEquals(Integer.toHexString(nids[i]).toUpperCase(Locale.ENGLISH), documents[i].getNoteID());
		}
		documentPageNS = System.nanoTime() - documentTime;

		System.out.println("Done with document details for " + pageSize + " out of " + collectionSize + " (" + dbDocs
				+ " total in db.) NC build took " + (collectionBuildNS / 1000000) + "ms, Document build took "
				+ (documentBuildNS / 1000000) + "ms, Document page took " + (documentPageNS / 1000000) + "ms");

		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName());
	}

	@Override
	public boolean shouldStop() {
		return true;
	}

}
