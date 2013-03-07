package org.openntf.domino.tests.ntf;

import java.lang.ref.Reference;
import java.util.Vector;

import lotus.notes.NotesThread;

import org.openntf.domino.Database;
import org.openntf.domino.Form;
import org.openntf.domino.Session;
import org.openntf.domino.impl.Base;
import org.openntf.domino.thread.DominoReference;
import org.openntf.domino.thread.DominoReferenceQueue;
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NotesThread.sinitThread();

		doSomeLotusStuff();

		System.gc();
		DominoReferenceQueue drq = Base._getRecycleQueue();
		System.out.println("Found queue of type " + drq.getClass().getName());
		Reference<?> ref = drq.poll();

		while (ref != null) {
			if (ref instanceof DominoReference) {
				System.out.println("Found a phantom reference of type " + ((DominoReference) ref).getType().getName());
				((DominoReference) ref).recycle();
			}
			ref = drq.poll();
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			DominoUtils.handleException(e);

		}

		NotesThread.stermThread();
	}
}
