package org.openntf.domino.tests.ntf;

import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
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

	private static void doSomeLotusStuff() {
		Session s = Factory.getSession();
		System.out.println("Name: " + s.getEffectiveUserName());
		Database db = s.getDatabase("", "log.nsf");
		Vector<Form> forms = db.getForms();
		for (Form form : forms) {
			System.out.println("Form : " + form.getName() + " (" + DominoUtils.getUnidFromNotesUrl(form.getNotesURL()) + ")");

		}
	}

	static class Doer implements Runnable {

		@Override
		public void run() {
			Session s = Factory.getSession();
			Name sname = s.getUserNameObject();
			System.out.println("Name: " + sname.getCanonical());
			Database db = s.getDatabase("", "log.nsf");
			Vector<Form> forms = db.getForms();
			System.out.println("BEGINNING ITERATION of Forms");
			for (Form form : forms) {
				System.out.println("Form : " + form.getName() + " (" + DominoUtils.getUnidFromNotesUrl(form.getNotesURL()) + ")");
				Document d = form.getDocument();
				Vector v = d.getItemValue("$UpdatedBy");
				Name n = s.createName((String) v.get(0));
				System.out.println("Last Editor: " + n.getCommon());

			}
			System.out.println("ENDING ITERATION of Forms");
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DominoThread dt = new DominoThread(new Doer(), "Scratch Test");
		DominoThread dt2 = new DominoThread(new Doer(), "Scratch Test2");
		dt.start();
		dt2.start();

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

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			DominoUtils.handleException(e);

		}

		// NotesThread.stermThread();
	}
}
