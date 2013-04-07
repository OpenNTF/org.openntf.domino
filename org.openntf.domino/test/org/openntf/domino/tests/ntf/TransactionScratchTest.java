package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
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
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public enum TransactionScratchTest {
	INSTANCE;

	private TransactionScratchTest() {
		// TODO Auto-generated constructor stub
	}

	private static final int THREAD_COUNT = 1;
	private static final boolean INCLUDE_FORMS = false;
	private static final int delay = 1000;
	// private static final String server = "CN=DevilDog/O=REDPILL";
	private static final String server = "";
	private static final String dbPath = "events4.nsf";

	static class Doer implements Runnable {
		int nameCount = 0;
		int docCount = 0;
		int dateCount = 0;
		Database db;

		Set<Document> thirdReference = new HashSet<Document>();

		private void iterateForms(Database db) {
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

		private void iterateAllDocuments(Set<Document> secondReference) {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Documents");
			Session s = db.getParent();
			org.openntf.domino.transactions.DatabaseTransaction txn = db.startTransaction();
			DocumentCollection dc = db.getAllDocuments();
			for (Document doc : dc) {
				docCount++;

				if (docCount % 1000 == 0) {
					secondReference.add(db.getDocumentByID(doc.getNoteID()));
				}
				if (docCount % 2000 == 0) {
					thirdReference.add(doc);
				}

				doc.replaceItemValue("TxnTest", new Date());
				// doc.save();
			}
			System.out.println("ENDING ITERATION of Documents. Committing...");
			txn.rollback();
		}

		private void iterateSecondReferences(Set<Document> secondReference) {
			System.out.println("ITERATING Second reference set");
			org.openntf.domino.transactions.DatabaseTransaction txn = db.startTransaction();
			for (Document doc : secondReference) {
				DateTime created = doc.getCreated();
				if (created != null) {
					dateCount++;
					String busyWork3 = created.getDateOnly();
				} else {
					System.out.println("Created was null from document " + doc.getUniversalID());
				}
				doc.replaceItemValue("TxnTest2", new Date());
			}
			txn.commit();
		}

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
			RunContext rc = Factory.getRunContext();
			System.out.println("RunContext: " + rc.toString());
			Name sname = s.getUserNameObject();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			System.out.println(df.format(new Date()) + " Name: " + sname.getCanonical());
			db = s.getDatabase(server, dbPath);
			if (INCLUDE_FORMS) {
				iterateForms(db);
			}
			Set<Document> secondReference = new HashSet<Document>();
			iterateAllDocuments(secondReference);
			System.gc();
			// NoteCollection nc = db.createNoteCollection(false);
			// nc.buildCollection();
			iterateSecondReferences(secondReference);
			System.gc();
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
			// }
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DominoThread[] threads = new DominoThread[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			threads[i] = new DominoThread(new Doer(), "Scratch Test" + i);
		}

		for (DominoThread thread : threads) {
			thread.start();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e1) {
				DominoUtils.handleException(e1);

			}
		}

	}
}
