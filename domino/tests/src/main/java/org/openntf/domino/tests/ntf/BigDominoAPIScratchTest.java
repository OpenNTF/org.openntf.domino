/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.Session.RunContext;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public enum BigDominoAPIScratchTest {
	INSTANCE;

	private BigDominoAPIScratchTest() {
	}

	private static final int THREAD_COUNT = 10;
	private static final boolean INCLUDE_FORMS = false;

	public static class Doer implements Runnable {
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
				Vector<?> v = d.getItemValue("$UpdatedBy");

				Name n = db.getParent().createName((String) v.get(0));
				String cn = n.getCommon();
				if (cn != null) {
					nameCount++;
					docCount++;
				}
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
				Vector<?> v = doc.getItemValue("$UpdatedBy");
				for (Object o : v) {
					if (o instanceof String) {
						Name n = s.createName((String) o);
						String cn = n.getCommon();
						if (cn != null)
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
				if (busyWork != null && busyWork2 != null)
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
				String busyWork3 = created.getDateOnly();
				if (busyWork3 != null)
					dateCount++;
			}
		}

		/**
		 * Iterate third references.
		 */
		private void iterateThirdReferences() {
			System.out.println("ITERATING Third reference set");
			for (Document doc : thirdReference) {
				DateTime initMod = doc.getInitiallyModified();
				String busyWork4 = initMod.getDateOnly();
				if (busyWork4 != null)
					dateCount++;
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

			Session s = Factory.getSession(SessionType.CURRENT);
			RunContext rc = Factory.getRunContext();
			System.out.println("RunContext: " + rc.toString());
			Name sname = s.getUserNameObject();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			System.out.println(df.format(new Date()) + " Name: " + sname.getCanonical());
			Database db = s.getDatabase("", "OneMillion.nsf");
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
		TestRunnerUtil.runAsDominoThread(Doer.class, TestRunnerUtil.NATIVE_SESSION, THREAD_COUNT);
		//		int delay = 500;
		//		
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
