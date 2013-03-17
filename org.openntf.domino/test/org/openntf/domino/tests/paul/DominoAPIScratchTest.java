package org.openntf.domino.tests.paul;

import java.util.Date;

import org.openntf.domino.Session;
import org.openntf.domino.impl.DateTime;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public enum DominoAPIScratchTest {
	INSTANCE;

	private DominoAPIScratchTest() {

	}

	static class Doer implements Runnable {

		@Override
		public void run() {
			long start = System.nanoTime();
			Session s = Factory.getSession();
			Date d = new Date();
			DateTime dt = (DateTime) s.createDateTime(d);
			DateTime dt2 = (DateTime) s.createDateTime(d);
			dt.adjustHour(1);
			System.out.println(dt.getDateOnly());
			System.out.println(dt2.getDateOnly());
			System.out.println(dt.equalsIgnoreTime(dt2));
			System.out.println(dt.getDateOnly());
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int delay = 500;
		DominoThread dt = new DominoThread(new Doer(), "Scratch Test");
		dt.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
	}
}
