package org.openntf.domino.tests.ntf;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lotus.domino.Database;
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
public class LegacyCollectionTest implements Runnable {
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
					Class clazz = wr_field.getType();
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
				LegacyCollectionTest run = new LegacyCollectionTest();
				NotesThread nt = new NotesThread(run, "Thread " + i);
				nt.start();
				Thread.sleep(500);
			}
		} finally {
			NotesThread.stermThread();
		}

	}

	public LegacyCollectionTest() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			System.out.println("Starting NotesRunner");
			Session session = NotesFactory.createSession();
			Long sessId = getLotusId(session);
			sessionid.set(sessId);
			Database db = session.getDatabase("", "names.nsf");
			System.out.println("Db id:" + getLotusId(db));
			int i = 0;
			try {
				lotus.domino.DocumentCollection allDocs = db.getAllDocuments();
				System.out.println("All Collection has " + allDocs.getCount() + " documents");
				int[] nids = new int[allDocs.getCount()];
				lotus.domino.Document doc = allDocs.getFirstDocument();
				lotus.domino.Document nextDoc = null;
				while (doc != null) {
					nextDoc = allDocs.getNextDocument(doc);
					nids[i++] = Integer.valueOf(doc.getNoteID(), 16);
					doc.recycle();
					doc = nextDoc;
				}
				System.out.println("noteid array has " + nids.length + " entries");
				allDocs.recycle();

				//				lotus.domino.DocumentCollection newColl = db.getModifiedDocuments(session.createDateTime(new java.util.Date()));
				lotus.domino.DocumentCollection newColl = db.getAllUnreadDocuments();

				System.out.println("New Coll has " + newColl.getCount() + " documents");

				//				lotus.domino.DocumentCollection emptyColl = db.createDocumentCollection();
				newColl.intersect(nids[0]);
				System.out.println("New Coll has " + newColl.getCount() + " documents");

				for (int nid : nids) {
					newColl.merge(nid);
				}
				System.out.println("Merged Collection has " + newColl.getCount() + " documents");
				newColl.recycle();
			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println("Exception at loop point " + i);
			}
			session.recycle();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.out.println("FINI!");
	}
}
