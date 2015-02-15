package org.openntf.domino.tests.paul;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;

import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class Create200KLotus {

	public Create200KLotus() {

	}

	static class DocCreator implements Runnable {

		@Override
		public void run() {
			try {
				Document doc = null;
				System.out.println("START Creation of Documents:" + new Date().toString());
				Session s = Factory.getSession(SessionType.CURRENT);
				Set<Document> docset = new HashSet<Document>();
				Database db = s.getDatabase("", "OneMillionLotus.nsf", true);
				if (!db.isOpen()) {
					Database db2 = s.getDatabase("", "billing.ntf", true);
					db = db2.createCopy("", "OneMillionLotus.nsf");
					if (!db.isOpen())
						db.open();
				}

				for (int i = 1; i < 200000; i++) {
					doc = db.createDocument();
					doc.replaceItemValue("form", "doc");
					doc.replaceItemValue("Subject", String.valueOf(System.nanoTime()));
					doc.save();
					doc.recycle();
					if (i % 5000 == 0) {
						System.out.println("Created " + i + " documents so far. Still going...");
					}
				}
				System.out.println("ENDING Creation of Documents: " + new Date().toString());
			} catch (lotus.domino.NotesException e) {
				System.out.println(e.toString());
			}
		}
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DocCreator(), TestRunnerUtil.NATIVE_SESSION);
	}
}
