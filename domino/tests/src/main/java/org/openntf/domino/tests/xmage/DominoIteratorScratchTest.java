/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.tests.xmage;

import org.openntf.domino.ACLEntry;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public enum DominoIteratorScratchTest {
	INSTANCE;

	private DominoIteratorScratchTest() {
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		int delay = 500;
		DominoThread dt = new DominoThread(new Doer(), "Scratch Test");
		DominoThread dt2 = new DominoThread(new Doer(), "Scratch Test2");
		DominoThread dt3 = new DominoThread(new Doer(), "Scratch Test3");
		DominoThread dt4 = new DominoThread(new Doer(), "Scratch Test4");
		DominoThread dt5 = new DominoThread(new Doer(), "Scratch Test5");

		DominoThread dt6 = new DominoThread(new Doer(), "Scratch Test6");
		dt.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		dt2.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		dt3.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		dt4.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		dt5.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
		dt6.start();

	}

	static class Doer implements Runnable {

		@Override
		public void run() {
			long start = System.nanoTime();
			Session s = Factory.getSession(SessionType.CURRENT);
			if (s == null) {
				s = Factory.getSession(SessionType.TRUSTED);
			}
			String server = s.getServerName();
			DbDirectory dbDirectory = s.getDbDirectory(server);

			int dateCount = 0;
			int nameCount = 0;
			int docCount = 0;

			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Databases");
			int dbCount = 0;
			for (Database database : dbDirectory) {
				if (database != null) {
					if (!database.isOpen()) {
						database.open();
					}

					DateTime toxic = database.getCreated();
					if (toxic != null)
						dateCount++;

					for (ACLEntry entry : database.getACL()) {
						Name entryName = entry.getNameObject();
						if (entryName != null)
							nameCount++;
					}

					// if (database.getAllDocuments().getCount() > 0) {
					for (Document doc : database.getAllDocuments()) {
						if (doc != null)
							docCount++;
					}
					// }

				}
				dbCount++;
			}
			System.out.println("ENDING ITERATION of Databases");

			/*
			 * System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Templates"); int templateCount =
			 * 0; for (Database template : dbDirectory.getTemplates()) { if (template != null) { if (!template.isOpen()) { template.open();
			 * }
			 * 
			 * DateTime toxic = template.getCreated(); dateCount++;
			 * 
			 * for (ACLEntry entry : template.getACL()) { Name entryName = entry.getNameObject(); nameCount++; }
			 * 
			 * templateCount++; } } System.out.println("ENDING ITERATION of Templates");
			 */

			System.out
			.println("Thread " + Thread.currentThread().getName() + " processed " + (dbCount /* + templateCount */)
					+ " databases, " + docCount + " documents, " + nameCount + " names, and " + dateCount
					+ " datetimes without recycling.");
			long elapsed = System.nanoTime() - start;
			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}

	}

}
