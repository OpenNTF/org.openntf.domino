package org.openntf.domino.tests.rpr;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;

public class SimpleTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new SimpleTest(), "My thread");
		thread.start();
	}

	public SimpleTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		try {
			lotus.domino.Session session = NotesFactory.createSessionWithFullAccess();
			lotus.domino.Database d = session.getDatabase("", "names.nsf");
			System.out.println(d.getClass().getName());
			System.out.println(d.getTitle());
			System.out.println(d.getCurrentAccessLevel());
			System.out.println(d.getSize());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}
