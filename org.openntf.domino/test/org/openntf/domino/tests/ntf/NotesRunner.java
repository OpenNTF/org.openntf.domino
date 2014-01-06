package org.openntf.domino.tests.ntf;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Date;
import java.util.UUID;

import lotus.domino.Database;
import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.Name;
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

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					Field wrField = lotus.domino.local.NotesBase.class.getDeclaredField("weakObject");
					Class clazz = wrField.getType();
					Field field = clazz.getDeclaredField("GCdisabled");
					field.setAccessible(true);
					// field.set(null, true);
					Field field2 = clazz.getDeclaredField("Debug");
					field2.setAccessible(true);
					// field2.set(null, true);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(final String[] args) throws InterruptedException {
		try {
			NotesThread.sinitThread();
			for (int i = 0; i < 20; i++) {
				NotesRunner run = new NotesRunner();
				NotesThread nt = new NotesThread(run, "thread" + i);
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
			Database db = session.getDatabase("", "log.nsf");
			Name name = null;
			int i = 0;
			try {
				for (i = 0; i <= 100000; i++) {
					name = session.createName(UUID.randomUUID().toString());
					DateTime dt = session.createDateTime(new Date());
					DateTime end = session.createDateTime(new Date());
					DateRange dr = session.createDateRange(dt, end);
					Document doc = db.createDocument();
					Item i1 = doc.replaceItemValue("Foo", dr);
					Item i2 = doc.replaceItemValue("Bar", dr.getText());
					Item i3 = doc.replaceItemValue("Blah", dr.getStartDateTime().getLocalTime());
					lotus.domino.ColorObject color = session.createColorObject();
					color.setRGB(128, 128, 128);
					Item i4 = doc.replaceItemValue("color", color.getNotesColor());
					// DxlExporter export = session.createDxlExporter();
					// String dxl = export.exportDxl(doc);
					// lotus.domino.Registration reg =
					// session.createRegistration();
					// reg.setExpiration(dt);
					// reg.setStoreIDInAddressBook(true);
					i1.recycle();
					i2.recycle();
					i3.recycle();
					i4.recycle();
					DateTime create = doc.getCreated();
					String lc = create.getLocalTime();
					if (i % 10000 == 0) {
						System.out.println(Thread.currentThread().getName() + " Name " + i + " is " + name.getCommon() + " "
								+ "Local time is " + lc + "  " + dr.getText());
					}
					dr.recycle();
					// doc.recycle();
					dt.recycle();
					end.recycle();
					create.recycle();
					// color.recycle();
					// name.recycle();
				}
			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println("Exception at loop point " + i);
			}
			session.recycle();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
