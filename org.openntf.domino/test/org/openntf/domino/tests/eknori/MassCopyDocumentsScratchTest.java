package org.openntf.domino.tests.eknori;

/*
 -- START --
 DocumentIterator set up idArray of 2780192
 Thread MassCopyDocumentsScratchTest elapsed time: 4104398ms
 -- STOP --
 Thread MassCopyDocumentsScratchTest auto-recycled 5523389 lotus references during run. 
 Then recycled 36999 lotus references on completion and had 0 recycle errors
 */
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public enum MassCopyDocumentsScratchTest {
	INSTANCE;

	private MassCopyDocumentsScratchTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		DominoThread dt = new DominoThread(new Doer(), "MassCopyDocumentsScratchTest");
		dt.start();

	}

	static class Doer implements Runnable {
		private static final String SOURCE = "OneMillion.nsf";
		private static final String TARGET = "target.nsf";

		@Override
		public void run() {

			long start = System.nanoTime();
			Session s = Factory.getSession();
			Database source = s.getDatabase("", SOURCE, true);
			Database target = s.getDatabase("", TARGET, true);

			System.out.println("-- START --");

			for (Document doc : source.getAllDocuments()) {
				doc.copyToDatabase(target);
			}

			System.out.println("-- STOP --");

			long elapsed = System.nanoTime() - start;
			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}

	}

}
