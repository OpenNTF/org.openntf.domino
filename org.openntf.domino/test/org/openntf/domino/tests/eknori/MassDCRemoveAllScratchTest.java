package org.openntf.domino.tests.eknori;

/*
 * 2.780.192 documents in source application
 -- START --
 -- STOP --
 Thread DCRemoveAllScratchTest elapsed time: 316095ms
 Thread DCRemoveAllScratchTest auto-recycled 0 lotus references during run. Then recycled 2 lotus references on completion and had 0 recycle errors

 */
import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public enum MassDCRemoveAllScratchTest {
	INSTANCE;

	private MassDCRemoveAllScratchTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		DominoThread dt = new DominoThread(new Doer(), "DCRemoveAllScratchTest");
		dt.start();
	}

	static class Doer implements Runnable {
		private static final String SOURCE = "target.nsf";

		@Override
		public void run() {

			Session s = Factory.getSession();
			Database source = s.getDatabase("", SOURCE, true);

			System.out.println("-- START --");
			long start = System.nanoTime();
			DocumentCollection dc = source.getAllDocuments();
			dc.removeAll(true);
			long elapsed = System.nanoTime() - start;
			System.out.println("-- STOP --");

			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}

	}

}
