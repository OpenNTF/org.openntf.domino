package org.openntf.domino.tests.ntf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.helpers.DocumentScanner;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class DocumentScannerScratchTest {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DocumentScannerScratchTest.class.getName());

	public DocumentScannerScratchTest() {
	}

	static class Doer implements Runnable {

		@Override
		public void run() {
			long start = System.nanoTime();
			@SuppressWarnings("unused")
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			int docCount = 0;

			DocumentScanner scanner = new DocumentScanner();
			scanner.setTrackTokenLocation(true);
			scanner.setTrackFieldTokens(true);
			scanner.setTrackFieldTypes(false);
			scanner.setTrackFieldValues(false);
			scanner.setTrackTokenFreq(false);

			Session s = Factory.getSession(SessionType.CURRENT);
			Database db = s.getDatabase("", "help/help9_admin.nsf");
			System.out.println("Beginning scan of " + db.getApiPath());
			for (Document doc : db.getAllDocuments()) {
				scanner.processDocument(doc);
				docCount++;
			}

			System.out.println("Scanner reports processing " + scanner.getDocCount() + " documents, " + scanner.getItemCount()
					+ " items, and " + scanner.getTokenCount() + " tokens.");
			System.out.println("Built field token map of " + scanner.getFieldTokenMap().size() + " entries");
			Map<?, ?> tfmap = scanner.getTokenLocationMap();
			System.out.println("Built token location map of " + tfmap.size() + " entries");
			CaseInsensitiveString dom = new CaseInsensitiveString("domino");
			if (tfmap.get(dom) != null) {
				int hitCount = 0;
				Map<?, ?> tlvalue = (Map<?, ?>) tfmap.get(dom);
				for (Object key : tlvalue.keySet()) {
					Set<?> hits = (Set<?>) tlvalue.get(key);
					hitCount += hits.size();
				}
				System.out.println("Found " + hitCount + " hits for 'domino'");
			}

			long elapsed = System.nanoTime() - start;
			StringBuilder sb = new StringBuilder();
			sb.append("Thread " + Thread.currentThread().getName());
			sb.append(" *** ALL OPERATIONS COMPLETE elapsed time: ");
			sb.append(elapsed / 1000000 + "ms: processed ");
			sb.append(docCount + " docs without recycling.");
			System.out.println(sb.toString());
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
