package org.openntf.domino.tests.xmage;

import org.openntf.domino.ACLEntry;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;

public enum DominoIteratorScratchTest {
	INSTANCE;

	private DominoIteratorScratchTest() {
		// TODO Auto-generated constructor stub
	}

	static class Doer implements Runnable {

		@Override
		public void run() {
			long start = System.nanoTime();
			Session s = Factory.getSession();
			String server = s.getServerName();
			DbDirectory dbDirectory = s.getDbDirectory(server);

			int dateCount = 0;
			int nameCount = 0;

			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Databases");
			int dbCount = 0;
			for (Database database : dbDirectory) {
				if (!database.isOpen()) {
					database.open();
				}

				DateTime toxic = database.getCreated();
				dateCount++;

				for (ACLEntry entry : database.getACL()) {
					Name entryName = entry.getNameObject();
					nameCount++;
				}

				dbCount++;
			}
			System.out.println("ENDING ITERATION of Databases");

			System.out.println("Thread " + Thread.currentThread().getName() + " BEGINNING ITERATION of Templates");
			int templateCount = 0;
			for (Database template : dbDirectory.getTemplates()) {
				if (!template.isOpen()) {
					template.open();
				}

				DateTime toxic = template.getCreated();
				dateCount++;

				for (ACLEntry entry : template.getACL()) {
					Name entryName = entry.getNameObject();
					nameCount++;
				}

				templateCount++;
			}
			System.out.println("ENDING ITERATION of Templates");

			System.out.println("Thread " + Thread.currentThread().getName() + " processed " + (dbCount + templateCount) + " databases, "
					+ nameCount + " names, and " + dateCount + " datetimes without recycling.");
			long elapsed = System.nanoTime() - start;
			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}

	}

}
