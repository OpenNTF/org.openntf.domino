package org.openntf.domino.tests.eknori;

/*
 * -- START --
 -- STOP --
 Thread MassCreateDocumentsScratchTest elapsed time: 2456492ms
 Thread MassCreateDocumentsScratchTest auto-recycled 2975322 lotus references during run. Then recycled 24679 lotus references on completion and had 0 recycle errors

 */
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public enum MassCreateDocumentsScratchTest {
	INSTANCE;

	private MassCreateDocumentsScratchTest() {
		// TODO Auto-generated constructor stub
	}

	static class Doer implements Runnable {

		private static final String TEMPLATE = "simple.ntf";
		private static final String TARGET = "target.nsf";
		private static final String FORM = "person";
		private static final String ITEM = "subject";
		private static final int NUMBER_OF_DOCS = 1000000;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			Session s = Factory.getSession();

			Database db = s.getDatabase("", TARGET, true);

			if (!db.isOpen()) {
				Database db2 = s.getDatabase("", TEMPLATE, true);
				db = db2.createCopy("", TARGET);
				if (!db.isOpen())
					db.open();
			}

			Document doc = null;
			System.out.println("-- START --");
			long start = System.nanoTime();
			for (int i = 1; i < NUMBER_OF_DOCS + 1; i++) {

				doc = db.createDocument();
				doc.replaceItemValue("form", FORM);
				doc.replaceItemValue(ITEM, String.valueOf(System.nanoTime()));
				doc.computeWithForm(false, false);
				doc.save();
				/*
				 * if (i % 5000 == 0) { System.out.println("Created " + i + " documents so far. Still going..."); }
				 */
			}
			long elapsed = System.nanoTime() - start;
			System.out.println("-- STOP --");
			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DominoThread dt = new DominoThread(new Doer(), "MassCreateDocumentsScratchTest");
		dt.start();
	}

}
