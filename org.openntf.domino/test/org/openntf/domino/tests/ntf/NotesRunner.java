package org.openntf.domino.tests.ntf;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lotus.domino.Database;
import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.Name;
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
			for (int i = 0; i < 8; i++) {
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
			session.recycle();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.out.println("FINI!");
	}
}
