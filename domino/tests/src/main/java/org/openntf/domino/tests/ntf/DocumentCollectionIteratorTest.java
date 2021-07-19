/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@SuppressWarnings("unused")
public class DocumentCollectionIteratorTest implements Runnable {
	private static int THREAD_COUNT = 1;

	public static void main(final String[] args) {
		org.openntf.domino.thread.DominoExecutor de = new org.openntf.domino.thread.DominoExecutor(10);
		for (int i = 0; i < THREAD_COUNT; i++) {
			de.execute(new DocumentCollectionIteratorTest());
		}
		// de.shutdown();
		//		for (int i = 0; i < 4; i++) {
		//			DominoThread thread = new DominoThread(new LargishSortedCollectionTest(), "My thread " + i);
		//			thread.start();
		//		}
		Factory.shutdown();
	}

	public DocumentCollectionIteratorTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		try {
			Session session = Factory.getSession(SessionType.CURRENT);
			Database db = session.getDatabase("", "events4.nsf");
			DocumentCollection coll = db.getAllDocuments();
			for (Document doc : coll) {
				System.out.println("nid: " + doc.getNoteID());
			}
			long endTime = System.nanoTime();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}
