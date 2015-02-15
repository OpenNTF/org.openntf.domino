package org.openntf.domino.tests.paul;

import java.util.Date;

import org.openntf.domino.DateTime;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public enum DominoAPIScratchTest {
	INSTANCE;

	private DominoAPIScratchTest() {

	}

	static class DateTimeTest implements Runnable {
		@Override
		public void run() {
			long start = System.nanoTime();
			Session s = Factory.getSession(SessionType.CURRENT);
			Date d = new Date();
			DateTime dt = s.createDateTime(d);
			DateTime dt2 = s.createDateTime(d);
			doChecks(dt, dt2);
			dt.adjustHour(1);
			doChecks(dt, dt2);
			dt.adjustDay(-1);
			doChecks(dt, dt2);
			dt.adjustHour(-1);
			doChecks(dt, dt2);
			start = System.nanoTime() - start;
			System.out.println("Time: " + start + "ns");
		}
	}

	/**
	 * Do checks.
	 * 
	 * @param dt1
	 *            the dt1
	 * @param dt2
	 *            the dt2
	 */
	private static void doChecks(final DateTime dt1, final DateTime dt2) {
		System.out.println("Comparing Date 1 " + dt1.toJavaDate().toString() + " and Date 2 " + dt2.toJavaDate().toString());
		if (dt1.isBefore(dt2)) {
			System.out.println("Date 1 before Date 2");
		} else {
			System.out.println("Date 1 not before Date 2");
		}
		if (dt1.isAfter(dt2)) {
			System.out.println("Date 1 after Date 2");
		} else {
			System.out.println("Date 1 not after Date 2");
		}
		if (dt1.equals(dt2)) {
			System.out.println("Date 1 equals Date 2");
		} else {
			System.out.println("Date 1 not equal to Date 2");
		}
		if (dt1.equalsIgnoreDate(dt2)) {
			System.out.println("Date 1 same time as Date 2");
		} else {
			System.out.println("Date 1 not same time as Date 2");
		}
		if (dt1.equalsIgnoreTime(dt2)) {
			System.out.println("Date 1 same date as Date 2");
		} else {
			System.out.println("Date 1 not same date as Date 2");
		}
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		int delay = 500;
		DominoThread dt = new DominoThread(new DateTimeTest(), "DateTime Test");
		dt.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
	}
}
