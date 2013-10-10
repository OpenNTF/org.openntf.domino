package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Form;
import org.openntf.domino.Name;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.Session.RunContext;
import org.openntf.domino.impl.Base;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.thread.deprecated.DominoChildThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public enum DominoChildScratchTest {
	INSTANCE;

	private DominoChildScratchTest() {
		// TODO Auto-generated constructor stub
	}

	private static final int THREAD_COUNT = 1;
	private static final boolean INCLUDE_FORMS = false;

	static class ParentDoer implements Runnable {

		@Override
		public void run() {
			long start = System.nanoTime();

			Session s = Factory.getSession();
			Database db = s.getDatabase("", "events4.nsf");
			Base.lock(s, db);

			int delay = 500;
			DominoChildThread[] threads = new DominoChildThread[THREAD_COUNT];
			Map<String, lotus.domino.Base> context = new HashMap<String, lotus.domino.Base>();
			context.put("session", s);
			context.put("database", db);
			for (int i = 0; i < THREAD_COUNT; i++) {
				threads[i] = new DominoChildThread(new Doer(), "Scratch Test " + i);
				threads[i].setContext(context);
			}

			for (DominoChildThread thread : threads) {
				thread.start();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e1) {
					DominoUtils.handleException(e1);

				}

			}

			for (DominoChildThread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					DominoUtils.handleException(e);

				}
			}

			for (DominoChildThread thread : threads) {
				thread.close();
			}

			Base.unlock(s, db);

			// boolean keepGoing = true;
			// while (keepGoing) {
			// for (Thread t : threads) {
			// keepGoing = (keepGoing || t.isAlive());
			// }
			// Thread.yield();
			// }

		}

	}

	static class Doer implements Runnable {
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			long start = System.nanoTime();

			org.openntf.domino.Session s = null;
			if (Thread.currentThread() instanceof DominoChildThread) {
				s = (org.openntf.domino.Session) ((DominoChildThread) Thread.currentThread()).getContextVar("session");
			}
			Database db = null;
			if (Thread.currentThread() instanceof DominoChildThread) {
				db = (org.openntf.domino.Database) ((DominoChildThread) Thread.currentThread()).getContextVar("database");
			}
			// Database db = s.getDatabase("", "events4.nsf");

			RunContext rc = Factory.getRunContext();
			System.out.println("RunContext: " + rc.toString());
			Name sname = s.getUserNameObject();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			System.out.println(df.format(new Date()) + " Name: " + sname.getCanonical());
			if (INCLUDE_FORMS) {
				iterateForms(db);
			}
			Set<Document> secondReference = new HashSet<Document>();
			iterateAllDocuments(db, secondReference);
			System.gc();
			NoteCollection nc = db.createNoteCollection(false);
			nc.buildCollection();
			iterateSecondReferences(secondReference);
			iterateThirdReferences();

			long elapsed = System.nanoTime() - start;
			StringBuilder sb = new StringBuilder();
			sb.append("Thread " + Thread.currentThread().getName());
			sb.append(" *** ALL OPERATIONS COMPLETE elapsed time: ");
			sb.append(elapsed / 1000000 + "ms: processed ");
			sb.append(nameCount + " names, ");
			sb.append(docCount + " docs, and ");
			sb.append(dateCount + " datetimes without recycling.");
			System.out.println(sb.toString());

		}
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		Thread t = new DominoThread(new ParentDoer());
		t.start();

	}
}
