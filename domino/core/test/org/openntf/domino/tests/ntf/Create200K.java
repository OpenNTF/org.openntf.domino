package org.openntf.domino.tests.ntf;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public class Create200K {
	private static final Logger log_ = Logger.getLogger(Create200K.class.getName());

	static class DocCreator implements Runnable {

		@Override
		public void run() {
			Document doc = null;
			System.out.println("START Creation of Documents");
			Session s = Factory.getSession();
			Set<Document> docset = new HashSet<Document>();
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
					docset.add(doc);
					System.out.println("Created " + i + " documents so far. Still going...");
				}
			}
			System.out.println("ENDING Creation of Documents");
			System.out.println("START Extra-processing of retained docs");
			for (Document d : docset) {
				DateTime dt = d.getCreated();
				d.replaceItemValue("$Created", dt);
				d.save();
			}
			System.out.println("ENDED Extra-processing of retained docs");
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		DominoThread dt = new DominoThread(new DocCreator(), "Create One Million Docs");
		dt.start();
	}

}
