package org.openntf.domino.junit;

import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public enum TestRunnerUtil {
	;
	public static void runAsDominoThread(final Runnable r) {
		Factory.startup();
		Thread t = new DominoThread(r, "TestRunner");
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Factory.shutdown();
	}

	public static void runAsDominoThread(final Runnable r, final int instances) {
		//	NotesThread.sinitThread(); // you must keep the thread open if you want to spawn more than one child thread
		Factory.startup();
		//	NotesThread.stermThread();

		Thread[] t = new Thread[instances];
		for (int i = 0; i < instances; i++) {
			t[i] = new DominoThread(r, "TestRunner-" + i);
			//System.out.println("Starting Thread " + t[i].getName());
			t[i].start();
		}

		try {
			for (int i = 0; i < instances; i++) {
				t[i].join();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Factory.shutdown();

	}

	public static void runAsDominoThread(final Class<? extends Runnable> r, final int instances) {
		//	NotesThread.sinitThread(); // you must keep the thread open if you want to spawn more than one child thread
		Factory.startup();
		//	NotesThread.stermThread();

		Thread[] t = new Thread[instances];
		for (int i = 0; i < instances; i++) {
			try {
				t[i] = new DominoThread(r.newInstance(), "TestRunner-" + i);
				t[i].start();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}

		try {
			for (int i = 0; i < instances; i++) {
				t[i].join();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Factory.shutdown();

	}

	public static void runAsNotesThread(final Class<? extends Runnable> r, final int instances) {
		lotus.domino.NotesThread.sinitThread(); // you must keep the thread open if you want to spawn more than one child thread
		//		Factory.startup();

		Thread[] t = new Thread[instances];
		for (int i = 0; i < instances; i++) {
			try {
				t[i] = new lotus.domino.NotesThread(r.newInstance(), "TestRunner-" + i);
				t[i].start();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}

		try {
			for (int i = 0; i < instances; i++) {
				t[i].join();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//		Factory.shutdown();
		lotus.domino.NotesThread.stermThread();

	}

}
