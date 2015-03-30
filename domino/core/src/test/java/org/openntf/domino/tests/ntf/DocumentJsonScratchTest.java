package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
public class DocumentJsonScratchTest {
	private static final Logger log_ = Logger.getLogger(DocumentJsonScratchTest.class.getName());

	public DocumentJsonScratchTest() {
		// TODO Auto-generated constructor stub
	}

	static class Doer implements Runnable {

		@Override
		public void run() {
			long start = System.nanoTime();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			int docCount = 0;

			Session s = Factory.getSession(SessionType.CURRENT);
			Database db = s.getDatabase("", "names.nsf");
			for (Document doc : db.getAllDocuments()) {
				String json = doc.toJson(false);
				System.out.println(json);
				docCount++;
				if (docCount > 30) {
					break;
				}
			}

			long elapsed = System.nanoTime() - start;
			StringBuilder sb = new StringBuilder();
			sb.append("Thread " + Thread.currentThread().getName());
			sb.append(" *** ALL OPERATIONS COMPLETE elapsed time: ");
			sb.append(elapsed / 1000000 + "ms: processed ");
			sb.append(docCount + " docs without recycling.");
			log_.info(sb.toString());
		}

	}

	/** The Constant THREAD_COUNT. */
	private static final int THREAD_COUNT = 1;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		int delay = 500;
		DominoThread[] threads = new DominoThread[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			threads[i] = new DominoThread(new Doer(), "Scratch Test" + i);
		}

		for (DominoThread thread : threads) {
			thread.start();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e1) {
				DominoUtils.handleException(e1);

			}
		}

	}
}
