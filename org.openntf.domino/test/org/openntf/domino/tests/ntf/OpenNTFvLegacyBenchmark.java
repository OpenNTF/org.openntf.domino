package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public enum OpenNTFvLegacyBenchmark {
	INSTANCE;

	private OpenNTFvLegacyBenchmark() {
		// TODO Auto-generated constructor stub
	}

	private static final int THREAD_COUNT = 3;
	private static final int delay = 250;
	private static final String server = "";
	private static final String dbPath = "events4.nsf";

	private static final String[] FIELDS_LIST = { "AddInName", "class", "Facility", "Form", "Filename", "Name", "OriginalText",
			"PossibleSolution", "ProbableCause", "TaskSubTypes", "Value", "UserText", "ui.Severity" };

	public static class LegacyDoer implements Runnable {
		private static final long serialVersionUID = 1L;
		int nameCount = 0;
		int docCount = 0;
		int dateCount = 0;
		int fieldCount = 0;

		private void iterateForms(final lotus.domino.Database db) throws lotus.domino.NotesException {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Forms");

			Vector<lotus.domino.Form> forms = db.getForms();
			for (lotus.domino.Form form : forms) {
				lotus.domino.NoteCollection notes = form.getParent().createNoteCollection(false);
				notes.add(form);
				String unid = notes.getUNID(notes.getFirstNoteID());
				lotus.domino.Document d = form.getParent().getDocumentByUNID(unid);
				Vector v = d.getItemValue("$UpdatedBy");

				lotus.domino.Name n = db.getParent().createName((String) v.get(0));
				String cn = n.getCommon();
				nameCount++;
				docCount++;
				notes.recycle();
				d.recycle(v);
				n.recycle();
				d.recycle();
			}
			db.recycle(forms);
			System.out.println("ENDING ITERATION of Forms");
		}

		private void iterateAllDocuments(final lotus.domino.Database db) throws lotus.domino.NotesException {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Documents");
			lotus.domino.Session s = db.getParent();

			lotus.domino.DocumentCollection dc = db.getAllDocuments();
			lotus.domino.Document doc = dc.getFirstDocument();
			lotus.domino.Document nextDoc = null;
			while (doc != null) {
				nextDoc = dc.getNextDocument(doc);
				docCount++;
				Vector v = doc.getItemValue("$UpdatedBy");
				for (Object o : v) {
					if (o instanceof String) {
						lotus.domino.Name n = s.createName((String) o);
						String cn = n.getCommon();
						n.recycle();
						nameCount++;
					}
				}

				Map<String, String> dataContents = new HashMap<String, String>();
				for (String fieldName : FIELDS_LIST) {
					dataContents.put(fieldName, doc.getItemValueString(fieldName));
					fieldCount++;
				}

				lotus.domino.DateTime toxic = doc.getLastModified();
				String busyWork = toxic.getGMTTime();
				lotus.domino.DateTime toxic2 = doc.getCreated();
				String busyWork2 = toxic2.getDateOnly();
				dateCount++;
				doc.recycle(v);
				toxic.recycle();
				toxic2.recycle();
				doc.recycle();
				doc = nextDoc;
			}

			System.out.println("REPEATING ITERATION of Documents");
			doc = dc.getFirstDocument();
			nextDoc = null;
			while (doc != null) {
				nextDoc = dc.getNextDocument(doc);
				docCount++;
				Vector v = doc.getItemValue("$UpdatedBy");
				for (Object o : v) {
					if (o instanceof String) {
						lotus.domino.Name n = s.createName((String) o);
						String cn = n.getCommon();
						n.recycle();
						nameCount++;
					}
				}
				Map<String, String> dataContents = new HashMap<String, String>();
				for (String fieldName : FIELDS_LIST) {
					dataContents.put(fieldName, doc.getItemValueString(fieldName));
					fieldCount++;
				}
				lotus.domino.DateTime toxic = doc.getLastModified();
				String busyWork = toxic.getGMTTime();
				lotus.domino.DateTime toxic2 = doc.getCreated();
				String busyWork2 = toxic2.getDateOnly();
				dateCount++;
				doc.recycle(v);
				toxic.recycle();
				toxic2.recycle();
				doc.recycle();
				doc = nextDoc;
			}
			System.out.println("ENDING ITERATION of Documents");
		}

		@Override
		public void run() {
			long start = System.nanoTime();
			lotus.domino.Session s = null;
			lotus.domino.Name sname = null;
			lotus.domino.Database db = null;

			try {
				lotus.domino.NotesThread.sinitThread();
				s = lotus.domino.NotesFactory.createSessionWithFullAccess();
				sname = s.getUserNameObject();
				DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
				db = s.getDatabase(server, dbPath);

				try {
					iterateForms(db);
				} catch (lotus.domino.NotesException ne) {
					ne.printStackTrace();
				}
				try {
					iterateAllDocuments(db);
				} catch (lotus.domino.NotesException ne) {
					ne.printStackTrace();
				}

				lotus.domino.NoteCollection nc = null;
				lotus.domino.View view = null;
				lotus.domino.DocumentCollection dc = null;
				try {
					nc = db.createNoteCollection(false);
					nc.buildCollection();
					view = db.getView("NameMessageEventMessages");
					Vector<String> keys = new Vector<String>();
					keys.add("Mail");
					keys.add("2");
					dc = view.getAllDocumentsByKey(keys, false);
					System.out.println("dc: " + dc.getCount());
					StringBuilder sbc = new StringBuilder();
					lotus.domino.Document doc = dc.getFirstDocument();
					lotus.domino.Document nextDoc = null;
					while (doc != null) {
						nextDoc = dc.getNextDocument(doc);
						sbc.append(doc.getNoteID());
						doc.recycle();
						doc = nextDoc;
					}

				} catch (lotus.domino.NotesException ne) {
					ne.printStackTrace();
				} finally {
					try {
						dc.recycle();
					} catch (Exception e) {
					}
					try {
						view.recycle();
					} catch (Exception e) {
					}
					try {
						nc.recycle();
					} catch (Exception e) {
					}
				}

				lotus.domino.ViewEntryCollection allViewEntries = null;
				try {
					view = db.getView("NameMessageEventMessages");
					allViewEntries = view.getAllEntries();
					System.out.println("all view entries: " + allViewEntries.getCount());
					allViewEntries.recycle();
					view.recycle();
				} catch (lotus.domino.NotesException ne) {
					ne.printStackTrace();
				} finally {
					try {
						allViewEntries.recycle();
					} catch (Exception e) {
					}
					try {
						view.recycle();
					} catch (Exception e) {
					}
				}
			} catch (lotus.domino.NotesException ne1) {
				ne1.printStackTrace();
			} finally {
				try {
					db.recycle();
				} catch (Exception e) {
				}
				try {
					sname.recycle();
				} catch (Exception e) {
				}
				try {
					s.recycle();
				} catch (Exception e) {
				}
				lotus.domino.NotesThread.stermThread();
			}

			long elapsed = System.nanoTime() - start;
			StringBuilder sb = new StringBuilder();
			sb.append("Thread " + Thread.currentThread().getName());
			sb.append(" *** ALL OPERATIONS COMPLETE elapsed time: ");
			sb.append(elapsed / 1000000 + "ms: processed ");
			sb.append(nameCount + " names, ");
			sb.append(fieldCount + " fields, ");
			sb.append(docCount + " docs, and ");
			sb.append(dateCount + " datetimes with legacy API.");
			System.out.println(sb.toString());
		}

	}

	public static class OpenNTFDoer extends org.openntf.domino.thread.AbstractDominoRunnable {
		private static final long serialVersionUID = 1L;
		int nameCount = 0;
		int docCount = 0;
		int dateCount = 0;
		int fieldCount = 0;

		private void iterateForms(final org.openntf.domino.Database db) {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Forms");
			Vector<org.openntf.domino.Form> forms = db.getForms();
			for (org.openntf.domino.Form form : forms) {
				org.openntf.domino.Document d = form.getDocument();
				Vector v = d.getItemValue("$UpdatedBy");
				org.openntf.domino.Name n = db.getParent().createName((String) v.get(0));
				String cn = n.getCommon();
				nameCount++;
				docCount++;
			}
			System.out.println("ENDING ITERATION of Forms");
		}

		private void iterateAllDocuments(final org.openntf.domino.Database db) {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Documents");
			org.openntf.domino.Session s = db.getParent();

			org.openntf.domino.DocumentCollection dc = db.getAllDocuments();
			for (org.openntf.domino.Document doc : dc) {
				docCount++;
				Vector v = doc.getItemValue("$UpdatedBy");
				for (Object o : v) {
					if (o instanceof String) {
						org.openntf.domino.Name n = s.createName((String) o);
						String cn = n.getCommon();
						nameCount++;
					}
				}

				Map<String, String> dataContents = new HashMap<String, String>();
				for (String fieldName : FIELDS_LIST) {
					dataContents.put(fieldName, doc.getItemValueString(fieldName));
					fieldCount++;
				}

				org.openntf.domino.DateTime toxic = doc.getLastModified();
				String busyWork = toxic.getGMTTime();
				org.openntf.domino.DateTime toxic2 = doc.getCreated();
				String busyWork2 = toxic2.getDateOnly();
				dateCount++;
			}
			System.out.println("REPEATING ITERATION of Documents");
			for (org.openntf.domino.Document doc : dc) {
				docCount++;
				Vector v = doc.getItemValue("$UpdatedBy");
				for (Object o : v) {
					if (o instanceof String) {
						org.openntf.domino.Name n = s.createName((String) o);
						String cn = n.getCommon();
						nameCount++;
					}
				}
				Map<String, String> dataContents = new HashMap<String, String>();
				for (String fieldName : FIELDS_LIST) {
					dataContents.put(fieldName, doc.getItemValueString(fieldName));
					fieldCount++;
				}
				org.openntf.domino.DateTime toxic = doc.getLastModified();
				String busyWork = toxic.getGMTTime();
				org.openntf.domino.DateTime toxic2 = doc.getCreated();
				String busyWork2 = toxic2.getDateOnly();
				dateCount++;
			}

			System.out.println("ENDING ITERATION of Documents");
		}

		@Override
		public void run() {
			long start = System.nanoTime();
			org.openntf.domino.Session s = org.openntf.domino.utils.Factory.getSessionFullAccess();
			org.openntf.domino.Name sname = s.getUserNameObject();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			org.openntf.domino.Database db = s.getDatabase(server, dbPath);

			iterateForms(db);
			iterateAllDocuments(db);

			org.openntf.domino.NoteCollection nc = db.createNoteCollection(false);
			nc.buildCollection();

			org.openntf.domino.View view = db.getView("NameMessageEventMessages");
			List<String> keys = new ArrayList<String>();
			keys.add("Mail");
			keys.add("2");
			org.openntf.domino.DocumentCollection dc = view.getAllDocumentsByKey(keys, false);
			System.out.println("dc: " + dc.getCount());
			StringBuilder sbc = new StringBuilder();
			for (org.openntf.domino.Document doc : dc) {
				sbc.append(doc.getNoteID());
			}

			org.openntf.domino.ViewEntryCollection allViewEntries = view.getAllEntries();
			System.out.println("all view entries: " + allViewEntries.getCount());

			long elapsed = System.nanoTime() - start;
			StringBuilder sb = new StringBuilder();
			sb.append("Thread " + Thread.currentThread().getName());
			sb.append(" *** ALL OPERATIONS COMPLETE elapsed time: ");
			sb.append(elapsed / 1000000 + "ms: processed ");
			sb.append(nameCount + " names, ");
			sb.append(fieldCount + " fields, ");
			sb.append(docCount + " docs, and ");
			sb.append(dateCount + " datetimes without recycling.");
			System.out.println(sb.toString());
		}

		@Override
		public boolean shouldStop() {
			return false;
		}
	}

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new OpenNTFDoer());
		}
		de.shutdown();

		for (int i = 0; i < THREAD_COUNT; i++) {
			lotus.domino.NotesThread nthread = new lotus.domino.NotesThread(new LegacyDoer(), "Legacy " + i);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nthread.start();
		}

		System.out.println("Main complete");
	}
}
