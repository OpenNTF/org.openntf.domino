package org.openntf.domino.tests.ntf;

import lotus.domino.NotesFactory;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewColumn;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.big.impl.DbCache;
import org.openntf.domino.big.impl.NoteCoordinate;
import org.openntf.domino.big.impl.NoteList;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class NoteListTest implements Runnable {
	private static int THREAD_COUNT = 1;
	private long marktime;

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new NoteListTest());
		}
		de.shutdown();
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

	public NoteListTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	public void run2() {
		long testStartTime = System.nanoTime();
		Session session = this.getSession();
		try {
			Database db = session.getDatabase("", "imdb/movies.nsf");
			//			db.setDelayUpdates(true);
			marktime = System.nanoTime();
			timelog("Beginning view build...");
			View byLength = db.getView("byLength");
			View btitles = db.createView("BTitles", "@Begins(Title; \"B\")", byLength);
			//			View btitles = db.createView("BTitles", "@Begins(Title; \"B\")");
			ViewColumn length = btitles.createColumn(1, "Title", "Title");
			length.setSorted(true);
			timelog("View defined.");
			btitles.refresh();
			timelog("Completed view build.");
			ViewEntryCollection vec = btitles.getAllEntries();
			int count = vec.getCount();
			timelog("Found " + count + " documents in the view");
			btitles.remove();
			timelog("Removed view.");
			btitles.recycle();
			db.recycle();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			long testEndTime = System.nanoTime();
			System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
		}
	}

	@Override
	public void run() {
		run2();
		run1();
	}

	public void run1() {
		long testStartTime = System.nanoTime();
		Session session = this.getSession();
		Database log = session.getDatabase("", "log.nsf");
		Document storeTest = log.createDocument();
		storeTest.replaceItemValue("form", "BinaryTest");
		Database db = session.getDatabase("", "imdb/movies.nsf");

		try {
			NoteList notelist = new NoteList();
			marktime = System.nanoTime();
			timelog("Beginning first notelist...");
			NoteCollection notecoll = db.createNoteCollection(false);
			notecoll.setSelectDocuments(true);
			notecoll.setSelectionFormula("@Begins(Title; \"B\")");
			notecoll.buildCollection();
			//			DocumentCollection coll = db.search("@Begins(Title; \"B\")");
			timelog("Starting note coordinates of " + notecoll.getCount() + " documents");
			//			for (Document doc : notecoll) {
			for (String nid : notecoll) {
				NoteCoordinate nc = new NoteCoordinate(notecoll, nid);
				notelist.add(nc);
			}

			//			Database eventDb = session.getDatabase("", "events4.nsf");
			//			NoteCollection eventNotecoll = eventDb.createNoteCollection(false);
			//			eventNotecoll.setSelectDocuments(true);
			//			eventNotecoll.buildCollection();
			//			timelog("Continuing note coordinates of " + eventNotecoll.getCount() + " documents");
			//			for (String nid : eventNotecoll) {
			//				NoteCoordinate nc = new NoteCoordinate(eventNotecoll, nid);
			//				notelist.add(nc);
			//			}
			//			Database xspextDb = session.getDatabase("", "openntf/xpagesext.nsf");
			//			NoteCollection xspextNotecoll = xspextDb.createNoteCollection(false);
			//			xspextNotecoll.setSelectDocuments(true);
			//			xspextNotecoll.buildCollection();
			//			timelog("Continuing note coordinates of " + xspextNotecoll.getCount() + " documents");
			//			for (String nid : xspextNotecoll) {
			//				NoteCoordinate nc = new NoteCoordinate(xspextNotecoll, nid);
			//				notelist.add(nc);
			//			}

			byte[] bytes = notelist.toByteArray();
			int byteSize = bytes.length;
			timelog("Resulting bytearray is " + bytes.length + " so we expect " + (bytes.length / (2500 * 24)) + " items");
			//			File file = File.createTempFile("foo", "bar");
			//			FileOutputStream fos = new FileOutputStream(file);
			//			fos.write(bytes);
			//			fos.close();

			storeTest.writeBinary("imdbNoteList", bytes, 2500 * 24);
			storeTest.save();
			String storeId = storeTest.getUniversalID();
			storeTest.recycle();
			storeTest = null;

			//			coll.recycle();
			//			coll = null;

			notecoll.recycle();
			notecoll = null;
			//			eventNotecoll.recycle();
			//			eventNotecoll = null;
			//			eventDb.recycle();
			//			eventDb = null;
			//			xspextNotecoll.recycle();
			//			xspextNotecoll = null;
			//			xspextDb.recycle();
			//			xspextDb = null;
			db.recycle();
			db = null;
			notelist = null;
			System.gc();

			timelog("Binary data serialized out. Reloading...");
			storeTest = log.getDocumentByUNID(storeId);
			//			FileInputStream fis = new FileInputStream(file);

			DbCache cache = new DbCache();
			NoteCoordinate.setDbCache(cache);
			NoteList notelist2 = new NoteList(cache, NoteList.getComparator("Title"));
			byte[] loaded = storeTest.readBinary("imdbNoteList");

			//			byte[] loaded = new byte[byteSize];
			//			fis.read(loaded);
			notelist2.loadByteArray(loaded);
			timelog("Done reloading " + notelist2.size() + " sorted notes. Iterating...");
			int notecount = 0;
			try {
				for (NoteCoordinate nc : notelist2) {
					Document doc = nc.getDocument();
					notecount++;
					//					System.out.println("doc " + doc.getNoteID() + " " + doc.getItemValue("$UpdatedBy", String.class));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			timelog("iterated over " + notecount + " notes out of " + notelist2.size());
			long endTime = System.nanoTime();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
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
