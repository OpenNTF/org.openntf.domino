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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lotus.domino.Database;
import lotus.domino.DocumentCollection;
import lotus.domino.NoteCollection;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;
import lotus.domino.Session;

/*
 * @author NTF
 * This runnable class is designed to test the behavior of the lotus.domino API with regard to recycling objects.
 * If we fail to recycle the date objects and run multiple threads, we get crashes.
 * If we fail to recycle non-date objects and run multiple threads, we get Backend Out of Memory Handles messages
 * 
 */
@SuppressWarnings("unused")
public class LegacyCollectionPerfTest implements Runnable {
	private static Method getCppMethod;
	private static int bitMode;
	private static Field cpp_field;
	private static Field wr_field;

	private static ThreadLocal<Long> sessionid = new ThreadLocal<Long>() {
		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected Long initialValue() {
			return 0L;
		}

		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#set(java.lang.Object)
		 */
		@Override
		public void set(final Long value) {
			super.set(value);
			System.out.println("Session id: " + value);
		}
	};

	private static ThreadLocal<Long> minid = new ThreadLocal<Long>() {

		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected Long initialValue() {
			return Long.MAX_VALUE;
		}

		@Override
		public void set(final Long value) {
			if (value < super.get()) {
				//				System.out.println("New Min is " + value);
				super.set(value);
				if (sessionid.get() > 0) {
					long delta = (value - sessionid.get()) >> 3;	//difference divided by 8 (is a negative number)
					//					if (delta > 8192l) {
					System.out.println(Thread.currentThread().getName() + " is New min: " + value + " session diff: " + delta
							+ " session: " + sessionid.get());
					//					}
				} else {
					System.out.println("Setting up session id as min");
				}
			}
		};
	};

	private static ThreadLocal<Long> maxid = new ThreadLocal<Long>() {

		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected Long initialValue() {
			return 0L;
		}

		@Override
		public void set(final Long value) {
			//			if (value % 8 != 0) {
			//				System.out.println("Encountered a cppid that's not a multiple of 8!");
			//			}
			if (value > super.get()) {
				super.set(value);
				if (sessionid.get() > 0) {
					long delta = (value - sessionid.get()) >> 3;	//difference divided by 8
					if (delta > 8192l) {
						System.out.println(Thread.currentThread().getName() + " is New max: " + value + " session diff: " + delta
								+ " session: " + sessionid.get());
					}
				} else {
					System.out.println("Setting up session id as max");
				}
			}
		};
	};

	public static long getLotusId(final lotus.domino.Base base) {
		try {
			Object o = wr_field.get(base);
			long result = (Long) cpp_field.get(o);
			maxid.set(result);
			minid.set(result);
			return result;
		} catch (Exception e) {
			return 0L;
		}
	}

	public static void incinerate(final lotus.domino.Base base) {
		try {
			base.recycle();
		} catch (NotesException e) {

		}
	}

	private static Map<Long, Byte> idMap = new ConcurrentHashMap<Long, Byte>();

	public static void main(final String[] args) throws InterruptedException {
		try {
			//			Properties props = System.getProperties();
			//			for (Object key : props.keySet()) {
			//				Object value = props.get(key);
			//				System.out.println(String.valueOf(key) + " : " + String.valueOf(value));
			//			}
			NotesThread.sinitThread();
			for (int i = 0; i < 1; i++) {
				LegacyCollectionPerfTest run = new LegacyCollectionPerfTest();
				NotesThread nt = new NotesThread(run, "Thread " + i);
				nt.start();
				Thread.sleep(500);
			}
		} finally {
			NotesThread.stermThread();
		}

	}

	public LegacyCollectionPerfTest() {
	}

	@Override
	public void run() {
		try {
			System.out.println("Starting NotesRunner");
			Session session = NotesFactory.createSession();
			Long sessId = getLotusId(session);
			sessionid.set(sessId);
			Database db = session.getDatabase("", "events4.nsf");
			System.out.println("Db id:" + getLotusId(db));
			try {
				lotus.domino.Document doc = null;
				lotus.domino.Document nextDoc = null;

				lotus.domino.DocumentCollection allDocs = db.getAllDocuments();
				System.out.println("All Collection has " + allDocs.getCount() + " documents");
				int[] nids = new int[allDocs.getCount()];

				long walkStartTime = System.nanoTime();
				doc = allDocs.getFirstDocument();
				int i = 0;
				while (doc != null) {
					nextDoc = allDocs.getNextDocument(doc);
					nids[i++] = Integer.valueOf(doc.getNoteID(), 16);
					doc.recycle();
					doc = nextDoc;
				}
				long walkEndTime = System.nanoTime();

				System.out.println("DOCWALK: noteid array has " + allDocs.getCount() + " entries in " + (walkEndTime - walkStartTime)
						/ 1000 + "us");

				long ncStartTime = System.nanoTime();
				NoteCollection nc = db.createNoteCollection(false);
				nc.add(allDocs);
				nids = nc.getNoteIDs();
				long ncBuildTime = System.nanoTime();
				System.out
						.println("NOTECOLL: noteid array has " + nids.length + " entries in " + (ncBuildTime - ncStartTime) / 1000 + "us");
				//				for (int j = 0; j < nids.length; j++) {
				//					doc = db.getDocumentByID(Integer.toString(nids[j], 16));
				//				}
				//				long ncWalkTime = System.nanoTime();
				//				System.out.println("NOTECOLL: noteid array walked " + nids.length + " entries in " + (ncWalkTime - ncBuildTime) / 1000
				//						+ "us");

				long mergeStartTime = System.nanoTime();
				DocumentCollection mergeColl = db.search("@False", db.getLastModified(), 1);
				for (int j = 0; j < nids.length; j++) {
					mergeColl.merge(nids[j]);
				}
				long mergeBuildTime = System.nanoTime();
				System.out.println("MERGECOLL: mergeColl has " + mergeColl.getCount() + " entries in " + (mergeBuildTime - mergeStartTime)
						/ 1000 + "us");
				doc = mergeColl.getFirstDocument();
				while (doc != null) {
					nextDoc = mergeColl.getNextDocument(doc);
					int n = Integer.valueOf(doc.getNoteID(), 16);
					doc.recycle();
					doc = nextDoc;
				}
				long mergeWalkTime = System.nanoTime();
				System.out.println("MERGECOLL: mergeColl walked " + mergeColl.getCount() + " entries in "
						+ (mergeWalkTime - mergeBuildTime) / 1000 + "us");

				System.out.println("MERGECOLL: mergeColl total time " + mergeColl.getCount() + " entries in "
						+ (mergeWalkTime - ncStartTime) / 1000 + "us");

				walkStartTime = System.nanoTime();
				doc = allDocs.getFirstDocument();
				i = 0;
				while (doc != null) {
					nextDoc = allDocs.getNextDocument(doc);
					nids[i++] = Integer.valueOf(doc.getNoteID(), 16);
					doc.recycle();
					doc = nextDoc;
				}
				walkEndTime = System.nanoTime();

				System.out.println("DOCWALK: noteid array has " + allDocs.getCount() + " entries in " + (walkEndTime - walkStartTime)
						/ 1000 + "us");

				ncStartTime = System.nanoTime();
				nc = db.createNoteCollection(false);
				nc.add(allDocs);
				nids = nc.getNoteIDs();
				ncBuildTime = System.nanoTime();
				System.out
						.println("NOTECOLL: noteid array has " + nids.length + " entries in " + (ncBuildTime - ncStartTime) / 1000 + "us");
				//				for (int j = 0; j < nids.length; j++) {
				//					doc = db.getDocumentByID(Integer.toString(nids[j], 16));
				//				}
				//				long ncWalkTime = System.nanoTime();
				//				System.out.println("NOTECOLL: noteid array walked " + nids.length + " entries in " + (ncWalkTime - ncBuildTime) / 1000
				//						+ "us");

				mergeStartTime = System.nanoTime();
				mergeColl = db.search("@False", db.getLastModified(), 1);
				for (int j = 0; j < nids.length; j++) {
					mergeColl.merge(nids[j]);
				}
				mergeBuildTime = System.nanoTime();
				System.out.println("MERGECOLL: mergeColl has " + mergeColl.getCount() + " entries in " + (mergeBuildTime - mergeStartTime)
						/ 1000 + "us");
				doc = mergeColl.getFirstDocument();
				while (doc != null) {
					nextDoc = mergeColl.getNextDocument(doc);
					int n = Integer.valueOf(doc.getNoteID(), 16);
					doc.recycle();
					doc = nextDoc;
				}
				mergeWalkTime = System.nanoTime();
				System.out.println("MERGECOLL: mergeColl walked " + mergeColl.getCount() + " entries in "
						+ (mergeWalkTime - mergeBuildTime) / 1000 + "us");

				System.out.println("MERGECOLL: mergeColl total time " + mergeColl.getCount() + " entries in "
						+ (mergeWalkTime - ncStartTime) / 1000 + "us");

				nc.recycle();
				allDocs.recycle();
				mergeColl.recycle();

			} catch (Throwable t) {
				t.printStackTrace();

			}
			session.recycle();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.out.println("FINI!");
	}
}
