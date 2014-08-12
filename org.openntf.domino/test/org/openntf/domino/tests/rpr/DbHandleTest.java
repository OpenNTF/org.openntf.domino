package org.openntf.domino.tests.rpr;

import lotus.domino.NotesException;

import org.openntf.domino.impl.Base;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public class DbHandleTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new DbHandleTest(), "My thread");
		thread.start();
	}

	public DbHandleTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	FileCreationInfo
	@Override
	public void run() {
		try {
			lotus.domino.Session s = Factory.toLotus(Factory.getSession());
			lotus.domino.Database d1 = s.getDatabase("", "");
			lotus.domino.Database d2 = s.getDatabase("", "");
			System.out.println("d1.cppId: " + Base.getLotusId(d1));
			System.out.println("d2.cppId: " + Base.getLotusId(d2));

			d1.openWithFailover("", "names.nsf");
			d2.openWithFailover("", "names.nsf");
			System.out.println("d1.cppId: " + Base.getLotusId(d1));
			System.out.println("d2.cppId: " + Base.getLotusId(d2));

			System.out.println("D1:" + d1.getFilePath());
			d1.recycle();
			System.out.println("D2:" + d2.getFilePath());
			d2.recycle();
		} catch (NotesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Factory.terminate();
		System.out.println(Factory.dumpCounters(true));
	}

}
