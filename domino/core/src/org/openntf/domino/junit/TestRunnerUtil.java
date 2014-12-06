package org.openntf.domino.junit;

import org.openntf.domino.AutoMime;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.session.NativeSessionFactory;
import org.openntf.domino.session.TrustedSessionFactory;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public enum TestRunnerUtil {
	;

	public static ISessionFactory NATIVE_SESSION = new NativeSessionFactory(Fixes.values(), AutoMime.WRAP_32K, null);
	public static ISessionFactory TRUSTED_SESSION = new TrustedSessionFactory(Fixes.values(), AutoMime.WRAP_32K, null);

	public static void runAsDominoThread(final Runnable r, final ISessionFactory sf) {
		Factory.startup();
		lotus.domino.NotesThread.sinitThread();
		Factory.initThread();
		Factory.setSessionFactory(sf, SessionType.CURRENT);

		Thread t = new DominoThread(r, "TestRunner");
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Factory.termThread();
		lotus.domino.NotesThread.stermThread();
		Factory.shutdown();
	}

	public static void runAsDominoThread(final Runnable r, final ISessionFactory sf, final int instances) {
		Factory.startup();
		lotus.domino.NotesThread.sinitThread();
		Factory.initThread();
		Factory.setSessionFactory(sf, SessionType.CURRENT);

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

		Factory.termThread();
		lotus.domino.NotesThread.stermThread();
		Factory.shutdown();
	}

	public static void runAsDominoThread(final Class<? extends Runnable> r, final ISessionFactory sf, final int instances) {
		Factory.startup();
		lotus.domino.NotesThread.sinitThread();
		Factory.initThread();
		Factory.setSessionFactory(sf, SessionType.CURRENT);

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

		Factory.termThread();
		lotus.domino.NotesThread.stermThread();
		Factory.shutdown();

	}

	public static void runAsNotesThread(final Class<? extends Runnable> r, final int instances) {
		lotus.domino.NotesThread.sinitThread();

		Thread[] t = new Thread[instances];
		for (int i = 0; i < instances; i++) {
			try {
				t[i] = new lotus.domino.NotesThread(r.newInstance(), "TestRunner-" + i);
				t[i].start();
				Thread.sleep(100); // sleep some millis, as the legacy notes API may crash
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
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

		lotus.domino.NotesThread.stermThread();

	}

}
