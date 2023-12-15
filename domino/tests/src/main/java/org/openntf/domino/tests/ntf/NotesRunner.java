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

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import lotus.domino.Database;
import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.DxlExporter;
import lotus.domino.Item;
import lotus.domino.Name;
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
public class NotesRunner implements Runnable {
	private static Method getCppMethod;
	@SuppressWarnings("unused")
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

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					String bitModeRaw = System.getProperty("com.ibm.vm.bitmode");
					try {
						int mode = Integer.valueOf(bitModeRaw);
						System.out.println("Set bitmode to " + mode);
					} catch (Exception e) {
						e.printStackTrace();
					}

					getCppMethod = lotus.domino.local.NotesBase.class.getDeclaredMethod("GetCppObj", (Class<?>[]) null);
					getCppMethod.setAccessible(true);

					wr_field = lotus.domino.local.NotesBase.class.getDeclaredField("weakObject");
					wr_field.setAccessible(true);
					Class<?> clazz = wr_field.getType();
					cpp_field = clazz.getDeclaredField("cpp_object");
					cpp_field.setAccessible(true);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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

	@SuppressWarnings("unused")
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
				NotesRunner run = new NotesRunner();
				NotesThread nt = new NotesThread(run, "Thread " + i);
				nt.start();
				Thread.sleep(500);
			}
		} finally {
			NotesThread.stermThread();
		}

	}

	public NotesRunner() {
	}

	public void run1(final Session session) throws NotesException {
		Long sessId = getLotusId(session);
		sessionid.set(sessId);
		Database db = session.getDatabase("", "names.nsf");
		System.out.println("Db id:" + getLotusId(db));
		Name name = null;
		int i = 0;
		try {
			for (i = 0; i <= 100000; i++) {
				name = session.createName(UUID.randomUUID().toString());
				getLotusId(name);
				DateTime dt = session.createDateTime(new Date());
				getLotusId(dt);
				DateTime end = session.createDateTime(new Date());
				getLotusId(end);
				DateRange dr = session.createDateRange(dt, end);
				getLotusId(dr);
				Document doc = db.createDocument();
				getLotusId(doc);
				Item i1 = doc.replaceItemValue("Foo", dr);
				getLotusId(i1);
				Item i2 = doc.replaceItemValue("Bar", dr.getText());
				getLotusId(i2);
				Item i3 = doc.replaceItemValue("Blah", dr.getStartDateTime().getLocalTime());
				getLotusId(i3);
				lotus.domino.ColorObject color = session.createColorObject();
				getLotusId(color);
				color.setRGB(128, 128, 128);
				Item i4 = doc.replaceItemValue("color", color.getNotesColor());
				getLotusId(i4);
				i1.recycle();
				i2.recycle();
				i3.recycle();
				i4.recycle();
				DateTime create = doc.getCreated();
				getLotusId(create);
				@SuppressWarnings("unused")
				String lc = create.getLocalTime();
				//					if (i % 10000 == 0) {
				//						System.out.println(Thread.currentThread().getName() + " Name " + i + " is " + name.getCommon() + " "
				//								+ "Local time is " + lc + "  " + dr.getText());
				//					}
				dr.recycle();
				doc.recycle();
				dt.recycle();
				end.recycle();
				create.recycle();
				color.recycle();
				name.recycle();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			System.out.println("Exception at loop point " + i);
		}
	}

	public void run2(final Session session) throws NotesException {
		Database db = session.getDatabase("", "log.nsf");
		Document doc = db.createDocument();
		Item names = doc.replaceItemValue("Names", "CN=Nathan T Freeman/O=REDPILL");
		names.setAuthors(true);
		doc.replaceItemValue("form", "test");
		doc.save(true);
		String nid = doc.getNoteID();
		doc.recycle();
		doc = db.getDocumentByID(nid);
		Vector<Double> numbers = new Vector<Double>();
		numbers.add(new Double(1));
		numbers.add(new Double(2));

		doc.replaceItemValue("Names", numbers);
		doc.save(true);
		doc.recycle();
		doc = db.getDocumentByID(nid);
		names = doc.getFirstItem("Names");
		System.out.println("Names is " + names.getType() + " with " + names.isNames() + " and " + names.isAuthors() + " and value "
				+ names.getText());
		doc.recycle();
		db.recycle();
	}

	public void run4(final Session session) throws NotesException {
		Database db = session.getDatabase("", "events4.nsf");
		NoteCollection cacheNC = db.createNoteCollection(false);
		cacheNC.setSelectDocuments(true);
		cacheNC.buildCollection();
		cacheNC.recycle();
		DocumentCollection cacheDc = db.getAllDocuments();
		Document cacheDoc = cacheDc.getFirstDocument();
		cacheDoc.recycle();
		cacheDc.recycle();
		db.recycle();

		db = session.getDatabase("", "events4.nsf");
		DocumentCollection dc = db.getAllDocuments();
		Document doc = dc.getFirstDocument();
		Document nextDoc = null;
		int dcCount = dc.getCount();
		int j = 0;
		String[] dcUnids = new String[dcCount];
		long dcStart = System.nanoTime();
		while (doc != null) {
			nextDoc = dc.getNextDocument(doc);
			dcUnids[j++] = doc.getUniversalID();
			doc.recycle();
			doc = nextDoc;
		}
		System.out.println("DocumentCollection strategy got UNIDs for " + dcCount + " docs in " + (System.nanoTime() - dcStart) / 1000
				+ "us");
		dc.recycle();
		db.recycle();

		db = session.getDatabase("", "events4.nsf");
		NoteCollection nc3 = db.createNoteCollection(false);
		nc3.setSelectDocuments(true);
		nc3.buildCollection();
		int nc3Count = nc3.getCount();
		String[] nc3Unids = new String[nc3Count];
		int[] nids = nc3.getNoteIDs();
		int k = 0;
		long nc3Start = System.nanoTime();
		for (int id : nids) {
			nc3Unids[k++] = nc3.getUNID(Integer.toHexString(id));
		}
		System.out.println("NoteCollection strategy ints got UNIDs for " + nc3Count + " notes in " + (System.nanoTime() - nc3Start) / 1000
				+ "us");
		nc3.recycle();
		db.recycle();

		db = session.getDatabase("", "events4.nsf");
		NoteCollection nc = db.createNoteCollection(false);
		nc.setSelectDocuments(true);
		nc.buildCollection();
		int ncCount = nc.getCount();
		String[] ncUnids = new String[ncCount];
		String nid = nc.getFirstNoteID();
		long ncStart = System.nanoTime();
		for (int i = 0; i < ncCount; i++) {
			ncUnids[i] = nc.getUNID(nid);
			nid = nc.getNextNoteID(nid);
		}
		System.out.println("NoteCollection strategy first/next got UNIDs for " + ncCount + " notes in " + (System.nanoTime() - ncStart)
				/ 1000 + "us");
		nc.recycle();
		db.recycle();

		db = session.getDatabase("", "events4.nsf");
		NoteCollection nc2 = db.createNoteCollection(false);
		nc2.setSelectDocuments(true);
		nc2.buildCollection();
		int nc2Count = nc2.getCount();
		String[] nc2Unids = new String[nc2Count];
		nid = nc2.getFirstNoteID();
		long nc2Start = System.nanoTime();
		for (int i = 0; i < nc2Count; i++) {
			Document nc2doc = db.getDocumentByID(nid);
			nc2Unids[i] = nc2doc.getUniversalID();
			nc2doc.recycle();
			nid = nc2.getNextNoteID(nid);
		}
		System.out.println("NoteCollection strategy doc got UNIDs for " + nc2Count + " notes in " + (System.nanoTime() - nc2Start) / 1000
				+ "us");
		nc2.recycle();
		db.recycle();

	}

	public void run3(final Session session) throws NotesException {
		Database db = session.getDatabase("", "index.ntf");
		NoteCollection nc = db.createNoteCollection(false);
		nc.setSelectIcon(true);
		nc.setSelectAcl(true);
		nc.selectAllDesignElements(true);
		nc.buildCollection();
		DxlExporter export = session.createDxlExporter();
		export.setForceNoteFormat(true);
		export.setRichTextOption(DxlExporter.DXLRICHTEXTOPTION_RAW);
		String dxl = export.exportDxl(nc);
		nc.recycle();
		export.recycle();
		db.recycle();
		try {
			PrintWriter out = new PrintWriter("c:\\data\\index.dxl");
			out.println(dxl);
			out.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("Starting NotesRunner");
			Session session = NotesFactory.createSession();
			run4(session);
			session.recycle();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.out.println("FINI!");
	}
}
