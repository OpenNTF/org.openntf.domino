package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Form;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public enum ScratchTest {
	INSTANCE;

	private ScratchTest() {
		// TODO Auto-generated constructor stub
	}

	static class Doer implements Runnable {

		@Override
		public void run() {
			long start = System.nanoTime();
			int nameCount = 0;
			int docCount = 0;
			Session s = Factory.getSession();
			Name sname = s.getUserNameObject();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			System.out.println(df.format(new Date()) + " Name: " + sname.getCanonical());
			Database db = s.getDatabase("", "events4.nsf");
			Vector<Form> forms = db.getForms();
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Forms");
			for (Form form : forms) {
				// System.out.println("Form : " + form.getName() + " (" + DominoUtils.getUnidFromNotesUrl(form.getNotesURL()) + ")");
				Document d = form.getDocument();
				Vector v = d.getItemValue("$UpdatedBy");
				Name n = s.createName((String) v.get(0));
				nameCount++;
				docCount++;
				// System.out.println("Last Editor: " + n);
			}
			System.out.println("ENDING ITERATION of Forms");
			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Documents");
			DocumentCollection dc = db.getAllDocuments();
			for (Document doc : dc) {
				docCount++;
				Vector v = doc.getItemValue("$UpdatedBy");
				for (Object o : v) {
					if (o instanceof String) {
						Name n = s.createName((String) o);
						nameCount++;
					}
				}
			}

			System.out.println("ENDING ITERATION of Documents");
			System.out.println("Thread " + Thread.currentThread().getName() + " processed " + nameCount + " names and " + docCount
					+ " docs without recycling.");
			long elapsed = System.nanoTime() - start;
			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int delay = 300;
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
		dt2.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		dt3.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		dt4.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		dt5.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		dt6.start();

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
