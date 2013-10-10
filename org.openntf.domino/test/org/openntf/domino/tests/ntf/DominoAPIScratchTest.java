package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Form;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.Session.RunContext;
import org.openntf.domino.View;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;

public enum DominoAPIScratchTest {
	INSTANCE;

	private DominoAPIScratchTest() {
		// TODO Auto-generated constructor stub
	}

	private static final int THREAD_COUNT = 6;
	private static final boolean INCLUDE_FORMS = false;
	private static final int delay = 200;
	// private static final String server = "CN=DevilDog/O=REDPILL";
	private static final String server = "";
	private static final String dbPath = "events4.nsf";

	static class Doer extends AbstractDominoRunnable {
		int nameCount = 0;
		int docCount = 0;
		int dateCount = 0;

		Set<Document> thirdReference = new HashSet<Document>();

		private void iterateForms(final Database db) {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Forms");
			Vector<Form> forms = db.getForms();
			for (Form form : forms) {
				// System.out.println("Form : " + form.getName() + " (" + DominoUtils.getUnidFromNotesUrl(form.getNotesURL()) + ")");
				Document d = form.getDocument();
				Vector v = d.getItemValue("$UpdatedBy");

				Name n = db.getParent().createName((String) v.get(0));
				String cn = n.getCommon();
				nameCount++;
				docCount++;
				// System.out.println("Last Editor: " + n);
			}
			System.out.println("ENDING ITERATION of Forms");
		}

		/**
		 * Iterate all documents.
		 * 
		 * @param db
		 *            the db
		 * @param secondReference
		 *            the second reference
		 */
		private void iterateAllDocuments(final Database db, final Set<Document> secondReference) {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Documents");
			Session s = db.getParent();

			DocumentCollection dc = db.getAllDocuments();
			for (Document doc : dc) {
				docCount++;
				Vector v = doc.getItemValue("$UpdatedBy");
				for (Object o : v) {
					if (o instanceof String) {
						Name n = s.createName((String) o);
						String cn = n.getCommon();
						nameCount++;
					}
				}
				if (docCount % 1000 == 0) {
					secondReference.add(db.getDocumentByID(doc.getNoteID()));
				}
				if (docCount % 2000 == 0) {
					thirdReference.add(doc);
				}

				DateTime toxic = doc.getLastModified();
				String busyWork = toxic.getGMTTime();
				DateTime toxic2 = doc.getLastModified();
				String busyWork2 = toxic2.getDateOnly();
				// System.out.println("LastMod: " + toxic.getGMTTime());
				dateCount++;
			}
			System.out.println("REPEATING ITERATION of Documents");
			for (Document doc : dc) {
				docCount++;
				Vector v = doc.getItemValue("$UpdatedBy");
				for (Object o : v) {
					if (o instanceof String) {
						Name n = s.createName((String) o);
						String cn = n.getCommon();
						nameCount++;
					}
				}
				if (docCount % 1000 == 0) {
					secondReference.add(db.getDocumentByID(doc.getNoteID()));
				}
				if (docCount % 2000 == 0) {
					thirdReference.add(doc);
				}

				DateTime toxic = doc.getLastModified();
				String busyWork = toxic.getGMTTime();
				DateTime toxic2 = doc.getLastModified();
				String busyWork2 = toxic2.getDateOnly();
				// System.out.println("LastMod: " + toxic.getGMTTime());
				dateCount++;
			}

			System.out.println("ENDING ITERATION of Documents");
		}

		/**
		 * Iterate second references.
		 * 
		 * @param secondReference
		 *            the second reference
		 */
		private void iterateSecondReferences(final Set<Document> secondReference) {
			System.out.println("ITERATING Second reference set");
			for (Document doc : secondReference) {
				DateTime created = doc.getCreated();
				dateCount++;
				String busyWork3 = created.getDateOnly();
			}
		}

		/**
		 * Iterate third references.
		 */
		private void iterateThirdReferences() {
			System.out.println("ITERATING Third reference set");
			for (Document doc : thirdReference) {
				DateTime initMod = doc.getInitiallyModified();
				dateCount++;
				String busyWork4 = initMod.getDateOnly();
			}
		}

		@Override
		public void run() {
			// System.out.println("com class is loaded = " + String.valueOf(com.isLoaded()));

			// if (false) {
			long start = System.nanoTime();

			Session s = Factory.getSessionFullAccess();
			//			this.setup(s);
			RunContext rc = Factory.getRunContext();
			//			System.out.println("RunContext: " + rc.toString());
			Name sname = s.getUserNameObject();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			//			System.out.println(df.format(new Date()) + " Name: " + sname.getCanonical());
			Database db = s.getDatabase(server, dbPath);
			/*
			 * if (INCLUDE_FORMS) { iterateForms(db); } Set<Document> secondReference = new HashSet<Document>(); iterateAllDocuments(db,
			 * secondReference); System.gc(); NoteCollection nc = db.createNoteCollection(false); nc.buildCollection();
			 * iterateSecondReferences(secondReference); iterateThirdReferences();
			 */
			View view = db.getView("NameMessageEventMessages");
			List<String> keys = new ArrayList<String>();
			keys.add("Mail");
			keys.add("2");
			DocumentCollection dc = view.getAllDocumentsByKey(keys, false);
			System.out.println("dc: " + dc.getCount());
			StringBuilder sbc = new StringBuilder();
			for (Document doc : dc) {
				sbc.append(doc.getNoteID());
			}

			//			System.out.println("nids: " + sbc.toString());

			DocumentCollection allViewDocs = view.getAllDocuments();
			System.out.println("all view docs: " + allViewDocs.getCount());

			long elapsed = System.nanoTime() - start;
			StringBuilder sb = new StringBuilder();
			sb.append("Thread " + Thread.currentThread().getName());
			sb.append(" *** ALL OPERATIONS COMPLETE elapsed time: ");
			sb.append(elapsed / 1000000 + "ms: processed ");
			sb.append(nameCount + " names, ");
			sb.append(docCount + " docs, and ");
			sb.append(dateCount + " datetimes without recycling.");
			//			System.out.println(sb.toString());
		}

	}

	public static void main(final String[] args) {
		DominoExecutor de = new DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new Doer());
		}
		DominoExecutor de2 = new DominoExecutor(10);

		for (int i = 0; i < THREAD_COUNT; i++) {
			de2.execute(new Doer());
		}

		de.shutdown();
		de2.shutdown();

		//		DominoThread[] threads = new DominoThread[THREAD_COUNT];
		//		for (int i = 0; i < THREAD_COUNT; i++) {
		//			threads[i] = new DominoThread(new Doer(), "Scratch Test" + i);
		//		}
		//
		//		for (DominoThread thread : threads) {
		//			thread.start();
		//			try {
		//				Thread.sleep(delay);
		//			} catch (InterruptedException e1) {
		//				DominoUtils.handleException(e1);
		//
		//			}
		//		}

	}
}
