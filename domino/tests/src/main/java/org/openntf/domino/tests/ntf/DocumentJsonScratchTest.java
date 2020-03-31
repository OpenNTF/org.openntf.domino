/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
