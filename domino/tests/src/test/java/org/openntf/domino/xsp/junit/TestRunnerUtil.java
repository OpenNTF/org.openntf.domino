package org.openntf.domino.xsp.junit;

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
}
