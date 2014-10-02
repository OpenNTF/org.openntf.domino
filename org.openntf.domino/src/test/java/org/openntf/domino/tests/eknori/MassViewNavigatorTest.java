package org.openntf.domino.tests.eknori;

/*
 -- START --
 1000000
 CacheSize: 1
 -- STOP --
 Thread MassViewEntryCollectionTest elapsed time: 170850ms
 Thread MassViewEntryCollectionTest auto-recycled 983424 lotus references during run. Then recycled 16579 lotus references on completion and had 0 recycle errors


 -- START --
 1000000
 CacheSize: 400
 -- STOP --
 Thread MassViewEntryCollectionTest elapsed time: 184252ms
 Thread MassViewEntryCollectionTest auto-recycled 946602 lotus references during run. Then recycled 53401 lotus references on completion and had 0 recycle errors


 */
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public enum MassViewNavigatorTest {
	INSTANCE;

	private MassViewNavigatorTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		DominoThread dt = new DominoThread(new Doer(), "MassViewEntryCollectionTest");
		dt.start();
	}

	static class Doer implements Runnable {
		private static final String TARGET = "target.nsf";
		private static final String VIEW = "Persons";

		@Override
		public void run() {

			Session s = Factory.getSession();
			Database source = s.getDatabase("", TARGET, true);
			View view = source.getView(VIEW);
			System.out.println("-- START --");
			long start = System.nanoTime();

			if (null != view) {
				view.setAutoUpdate(false);

				System.out.println(view.getEntryCount());

				ViewNavigator nav = view.createViewNav();
				nav.setCacheSize(400);
				System.out.println("CacheSize: " + nav.getCacheSize());

				view.setAutoUpdate(true);
				ViewEntry entry = null;
				entry = nav.getFirst();
				while (null != entry) {
					entry = nav.getNext(entry);
				}
			}

			long elapsed = System.nanoTime() - start;
			System.out.println("-- STOP --");
			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}
	}

}
