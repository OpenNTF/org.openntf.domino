/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentScannerScratchTest.
 */
public class DocumentScannerScratchTest {
	
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DocumentScannerScratchTest.class.getName());

	/**
	 * Instantiates a new document scanner scratch test.
	 */
	public DocumentScannerScratchTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The Class Doer.
	 */
	static class Doer implements Runnable {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
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

	/** The Constant THREAD_COUNT. */
	private static final int THREAD_COUNT = 1;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
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
