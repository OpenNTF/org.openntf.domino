package org.openntf.domino.tests.eknori;

/*
 * 2.780.192 documents in source application
 Thread MassDeleteDocumentsScratchTest elapsed time: 528132ms
 Thread MassDeleteDocumentsScratchTest auto-recycled 2774889 lotus references during run. Then recycled 5306 lotus references on completion and had 0 recycle errors
 with Base line 165: There's already a reference to Document (...). The current call stack is ...

 after commenting out the print statement

 -- START --
 DocumentIterator set up idArray of 2780192
 -- STOP --
 Thread MassDeleteDocumentsScratchTest elapsed time: 451921ms
 Thread MassDeleteDocumentsScratchTest auto-recycled 2779616 lotus references during run. Then recycled 579 lotus references on completion and had 0 recycle errors

 */
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public enum MassDeleteDocumentsScratchTest {
	INSTANCE;

	private MassDeleteDocumentsScratchTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		DominoThread dt = new DominoThread(new Doer(), "MassDeleteDocumentsScratchTest");
		dt.start();
	}

	static class Doer implements Runnable {
		private static final String TARGET = "target.nsf";

		@Override
		public void run() {

			Session s = Factory.getSession();
			Database source = s.getDatabase("", TARGET, true);

			System.out.println("-- START --");
			long start = System.nanoTime();
			for (Document doc : source.getAllDocuments()) {
				doc.removePermanently(true);
			}
			long elapsed = System.nanoTime() - start;
			System.out.println("-- STOP --");

			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}

	}

}
