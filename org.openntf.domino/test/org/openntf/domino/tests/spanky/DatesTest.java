package org.openntf.domino.tests.spanky;

import java.util.Date;

import org.openntf.domino.utils.Dates;

public class DatesTest implements Runnable {
	private static int THREAD_COUNT = 1;

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new DatesTest());
		}

		de.shutdown();
	}

	public DatesTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {

		try {
			String datepart = "09/11/2014";
			String timepart = "0630PM";

			Date date = Dates.getDate(datepart, timepart);

			System.out.println("*");
			System.out.println("*");
			System.out.println("*");
			System.out.println("*");
			System.out.println("datepart: \"" + datepart + "\"");
			System.out.println("timepart: \"" + timepart + "\"");
			System.out.println("date: " + date);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
