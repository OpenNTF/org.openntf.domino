package org.openntf.domino.tests.eknori;

import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public class CreateOneMillion {
	private static final Logger log_ = Logger.getLogger(CreateOneMillion.class.getName());

	static class DocCreator implements Runnable {

		@Override
		public void run() {
			Document doc = null;
			System.out.println("START Creation of Documents");
			Session s = Factory.getSession();

			Database db = s.getDatabase("", "OneMillion.nsf", true);
			if (!db.isOpen()) {
				Database db2 = s.getDatabase("", "billing.ntf", true);
				db = db2.createCopy("", "OneMillion.nsf");
				if (!db.isOpen())
					db.open();
			}

			for (int i = 1; i < 1000000; i++) {

				doc = db.createDocument();
				doc.replaceItemValue("form", "doc");
				doc.replaceItemValue("Subject", String.valueOf(System.nanoTime()));
				doc.save();
				if (i % 5000 == 0) {
					// System.gc();
					System.out.println("Created " + i + " documents so far. Still going...");
				}
			}
			System.out.println("ENDING Creation of Documents");

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DominoThread dt = new DominoThread(new DocCreator(), "Create One Million Docs");
		dt.start();
	}

}
