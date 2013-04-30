package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DocumentScannerScratchTest {
	private static final Logger log_ = Logger.getLogger(DocumentScannerScratchTest.class.getName());

	public DocumentScannerScratchTest() {
		// TODO Auto-generated constructor stub
	}

	static class Doer implements Runnable {

		@Override
		public void run() {
			long start = System.nanoTime();
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			int docCount = 0;

			DocumentScanner scanner = new DocumentScanner();

			Session s = Factory.getSession();
			Database db = s.getDatabase("", "events4.nsf");
			for (Document doc : db.getAllDocuments()) {
				scanner.processDocument(doc);
				docCount++;
			}

			log_.info("Built field token map of " + scanner.getFieldTokenMap().size() + " entries");
			log_.info("Built token frequency map of " + scanner.getTokenFreqMap().size() + " entries");

			long elapsed = System.nanoTime() - start;
			StringBuilder sb = new StringBuilder();
			sb.append("Thread " + Thread.currentThread().getName());
			sb.append(" *** ALL OPERATIONS COMPLETE elapsed time: ");
			sb.append(elapsed / 1000000 + "ms: processed ");
			sb.append(docCount + " docs without recycling.");
			log_.info(sb.toString());
		}

	}

	private static final int THREAD_COUNT = 1;

	public static void main(String[] args) {
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
