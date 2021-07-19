/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public enum OpenNTFvLegacyBenchmark {
	INSTANCE;

	private OpenNTFvLegacyBenchmark() {
	}

	private static final int THREAD_COUNT = 1;
	private static final String server = "";
	private static final String dbPath = "events4.nsf";

	private static final String[] FIELDS_LIST = { "AddInName", "class", "Facility", "Form", "Filename", "Name", "OriginalText",
		"PossibleSolution", "ProbableCause", "TaskSubTypes", "Value", "UserText", "ui.Severity" };

	public static class Doer implements Runnable {
		int nameCount = 0;
		int docCount = 0;
		int dateCount = 0;
		int fieldCount = 0;
		private boolean useRawSession_;

		public Doer(final boolean useRawSession) {
			useRawSession_ = useRawSession;
		}

		@SuppressWarnings("unchecked")
		private void iterateForms(final lotus.domino.Database db) throws lotus.domino.NotesException {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Forms");

			Vector<lotus.domino.Form> forms = db.getForms();
			for (lotus.domino.Form form : forms) {
				lotus.domino.NoteCollection notes = form.getParent().createNoteCollection(false);
				notes.add(form);
				String unid = notes.getUNID(notes.getFirstNoteID());
				lotus.domino.Document d = form.getParent().getDocumentByUNID(unid);
				Vector<Object> v = d.getItemValue("$UpdatedBy");

				lotus.domino.Name n = db.getParent().createName((String) v.get(0));
				@SuppressWarnings("unused")
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

		@SuppressWarnings({ "unused", "unchecked" })
		private void iterateAllDocuments(final lotus.domino.Database db) throws lotus.domino.NotesException {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Documents");
			lotus.domino.Session s = db.getParent();

			lotus.domino.DocumentCollection dc = db.getAllDocuments();
			lotus.domino.Document doc = dc.getFirstDocument();
			lotus.domino.Document nextDoc = null;
			while (doc != null) {
				nextDoc = dc.getNextDocument(doc);
				docCount++;
				Vector<Object> v = doc.getItemValue("$UpdatedBy");
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
				Vector<Object> v = doc.getItemValue("$UpdatedBy");
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
				if (useRawSession_) {
					s = lotus.domino.NotesFactory.createSessionWithFullAccess();
				} else {
					s = Factory.getSession(SessionType.CURRENT);
				}
				sname = s.getUserNameObject();
				@SuppressWarnings("unused")
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
						if (dc != null)
							dc.recycle();
					} catch (Exception e) {
					}
					try {
						if (view != null)
							view.recycle();
					} catch (Exception e) {
					}
					try {
						if (nc != null)
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
						if (allViewEntries != null)
							allViewEntries.recycle();
					} catch (Exception e) {
					}
					try {
						if (view != null)
							view.recycle();
					} catch (Exception e) {
					}
				}
			} catch (lotus.domino.NotesException ne1) {
				ne1.printStackTrace();
			} finally {
				try {
					if (db != null)
						db.recycle();
				} catch (Exception e) {
				}
				try {
					if (sname != null)
						sname.recycle();
				} catch (Exception e) {
				}
				try {
					if (s != null)
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
			sb.append(dateCount + " datetimes");
			if (useRawSession_) {
				sb.append(" with legacy API.");
			} else {
				sb.append(" with legacy API - but with openntf Session");
			}
			System.out.println(sb.toString());
		}
	}

	public static class RawLegacyDoer extends Doer {

		public RawLegacyDoer() {
			super(true);
		}

	}

	public static class ODALegacyDoer extends Doer {

		public ODALegacyDoer() {
			super(false);
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
				Vector<Object> v = d.getItemValue("$UpdatedBy");
				org.openntf.domino.Name n = db.getParent().createName((String) v.get(0));
				@SuppressWarnings("unused")
				String cn = n.getCommon();
				nameCount++;
				docCount++;
			}
			System.out.println("ENDING ITERATION of Forms");
		}

		@SuppressWarnings("unused")
		private void iterateAllDocuments(final org.openntf.domino.Database db) {
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Documents");
			org.openntf.domino.Session s = db.getParent();

			org.openntf.domino.DocumentCollection dc = db.getAllDocuments();
			for (org.openntf.domino.Document doc : dc) {
				docCount++;
				Vector<Object> v = doc.getItemValue("$UpdatedBy");
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
				Vector<Object> v = doc.getItemValue("$UpdatedBy");
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

		@SuppressWarnings("unused")
		@Override
		public void run() {
			long start = System.nanoTime();
			org.openntf.domino.Session s = Factory.getSession(SessionType.CURRENT);
			org.openntf.domino.Name sname = s.getUserNameObject();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			org.openntf.domino.Database db = s.getDatabase(server, dbPath);

			iterateForms(db);
			iterateAllDocuments(db);
			//
			//			org.openntf.domino.NoteCollection nc = db.createNoteCollection(false);
			//			nc.buildCollection();
			//
			//			org.openntf.domino.View view = db.getView("NameMessageEventMessages");
			//			List<String> keys = new ArrayList<String>();
			//			keys.add("Mail");
			//			keys.add("2");
			//			org.openntf.domino.DocumentCollection dc = view.getAllDocumentsByKey(keys, false);
			//			System.out.println("dc: " + dc.getCount());
			//			StringBuilder sbc = new StringBuilder();
			//			for (org.openntf.domino.Document doc : dc) {
			//				sbc.append(doc.getNoteID());
			//			}
			//
			//			org.openntf.domino.ViewEntryCollection allViewEntries = view.getAllEntries();
			//			System.out.println("all view entries: " + allViewEntries.getCount());

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(final String[] args) {
		TestRunnerUtil.runAsNotesThread(RawLegacyDoer.class, THREAD_COUNT);

		Class[] classes = new Class[2];
		classes[0] = OpenNTFDoer.class;
		classes[1] = ODALegacyDoer.class;
		TestRunnerUtil.runAsDominoThread(classes, TestRunnerUtil.NATIVE_SESSION, 2);
		TestRunnerUtil.runAsNotesThread(RawLegacyDoer.class, 4);
		//		TestRunnerUtil.runAsDominoThread(ODALegacyDoer.class, TestRunnerUtil.NATIVE_SESSION, THREAD_COUNT);

		//		TestRunnerUtil.runAsDominoThread(OpenNTFDoer.class, TestRunnerUtil.NATIVE_SESSION, THREAD_COUNT);
		//		TestRunnerUtil.runAsDominoThread(ODALegacyDoer.class, TestRunnerUtil.NATIVE_SESSION, THREAD_COUNT);
		System.out.println("Main complete");
	}
}
