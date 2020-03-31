/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
import java.util.Arrays;
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
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public enum OneMillionScratchTest {
	INSTANCE;

	private OneMillionScratchTest() {
	}

	static class Doer implements Runnable {

		@SuppressWarnings("unused")
		@Override
		public void run() {
			long start = System.nanoTime();
			int nameCount = 0;
			int docCount = 0;
			int dateCount = 0;
			Session s = Factory.getSession(SessionType.CURRENT);
			Name sname = s.getUserNameObject();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			System.out.println(df.format(new Date()) + " Name: " + sname.getCanonical());
			Database db = s.getDatabase("", "events4.nsf");
			if (!db.isOpen()) {
				db.open();
			}
			Vector<Form> forms = db.getForms();
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Forms");
			for (Form form : forms) {
				// System.out.println("Form : " + form.getName() + " (" + DominoUtils.getUnidFromNotesUrl(form.getNotesURL()) + ")");
				Document d = form.getDocument();
				Vector<Object> v = d.getItemValue("$UpdatedBy");

				Name n = s.createName((String) v.get(0));
				nameCount++;
				docCount++;
				// System.out.println("Last Editor: " + n);
			}
			System.out.println("ENDING ITERATION of Forms");
			Set<Document> secondReference = new HashSet<Document>();
			Set<Document> thirdReference = new HashSet<Document>();
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Documents");
			DocumentCollection dc = db.getAllDocuments();
			for (Document doc : dc) {
				docCount++;
				Vector<Object> v = doc.getItemValue("$UpdatedBy");
				for (Object o : v) {
					if (o instanceof String) {
						Name n = s.createName((String) o);
						nameCount++;
					}
				}
				// if (docCount % 1000 == 0) {
				// secondReference.add(db.getDocumentByID(doc.getNoteID()));
				// }
				if (docCount % 1000 == 0) {
					secondReference.add(db.getDocumentByID(doc.getNoteID()));
					System.out.println("Created second reference for " + doc.getNoteID());
				}
				if (docCount % 2000 == 0) {
					thirdReference.add(db.getDocumentByUNID(doc.getUniversalID()));
					System.out.println("Created second reference for " + doc.getUniversalID());
				}
				DateTime toxic = doc.getLastModified();
				String busyWork = toxic.getGMTTime();
				DateTime toxic2 = doc.getLastModified();
				String busyWork2 = toxic2.getDateOnly();
				// System.out.println("LastMod: " + toxic.getGMTTime());
				dateCount++;
			}

			System.out.println("ENDING ITERATION of Documents");
			for (Document doc : secondReference) {
				DateTime created = doc.getCreated();
				String busyWork3 = created.getDateOnly();
			}

			System.out.println("ENDING ITERATION of Documents");
			for (Document doc : thirdReference) {
				DateTime initMod = doc.getInitiallyModified();
				String busyWork4 = initMod.getDateOnly();
				System.out.println(busyWork4);
			}

			System.out.println("Thread " + Thread.currentThread().getName() + " processed " + nameCount + " names, " + docCount
					+ " docs, and " + dateCount + " datetimes without recycling.");
			long elapsed = System.nanoTime() - start;
			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void Count(final Database db) {
			DocumentCollection dc = db.getAllDocuments();
			NoteCollection nc = db.createNoteCollection(false);
			nc.add(dc);

			Arrays.asList(nc.getNoteIDs());
			Set setAll = new HashSet(Arrays.asList(nc.getNoteIDs()));

			View allView = db.getView("All Documents");
			ViewEntryCollection vec = allView.getAllEntries();

			for (ViewEntry entry : vec) {
				setAll.remove(entry.getNoteIDAsInt());
			}
		}

	}

	/**
	 * The main method.
	 * 
	 */
	@SuppressWarnings("unused")
	public static void main(final String[] args) {
		int delay = 500;
		DominoThread dt = new DominoThread(new Doer(), "Scratch Test");
		DominoThread dt2 = new DominoThread(new Doer(), "Scratch Test2");
		DominoThread dt3 = new DominoThread(new Doer(), "Scratch Test3");
		DominoThread dt4 = new DominoThread(new Doer(), "Scratch Test4");
		DominoThread dt5 = new DominoThread(new Doer(), "Scratch Test5");

		DominoThread dt6 = new DominoThread(new Doer(), "Scratch Test6");
		dt.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		// dt2.start();
		// try {
		// Thread.sleep(delay);
		// } catch (InterruptedException e1) {
		// DominoUtils.handleException(e1);
		//
		// }
		// dt3.start();
		// try {
		// Thread.sleep(delay);
		// } catch (InterruptedException e1) {
		// DominoUtils.handleException(e1);
		//
		// }
		// dt4.start();
		// try {
		// Thread.sleep(delay);
		// } catch (InterruptedException e1) {
		// DominoUtils.handleException(e1);
		//
		// }
		// dt5.start();
		// try {
		// Thread.sleep(delay);
		// } catch (InterruptedException e1) {
		// DominoUtils.handleException(e1);
		//
		// }
		// dt6.start();

		// // NotesThread.sinitThread();
		//
		// doSomeLotusStuff();
		//
		// System.gc();
		// DominoReferenceQueue drq = Base._getRecycleQueue();
		// System.out.println("Found queue of type " + drq.getClass().getName());
		// Reference<?> ref = drq.poll();
		//
		// while (ref != null) {
		// if (ref instanceof DominoReference) {
		// System.out.println("Found a phantom reference of type " + ((DominoReference) ref).getType().getName());
		// ((DominoReference) ref).recycle();
		// }
		// ref = drq.poll();
		// }

		// NotesThread.stermThread();
	}
}
