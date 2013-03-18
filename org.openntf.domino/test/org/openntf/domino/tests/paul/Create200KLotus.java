package org.openntf.domino.tests.paul;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesFactory;
import lotus.domino.Session;

import org.openntf.domino.thread.DominoThread;

public class Create200KLotus {

	public Create200KLotus() {
		// TODO Auto-generated constructor stub
	}

	static class DocCreator implements Runnable {

		@Override
		public void run() {
			try {
				Document doc = null;
				System.out.println("START Creation of Documents:" + new Date().toString());
				Session s = NotesFactory.createSession();
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
	 * @param args
	 */
	public static void main(String[] args) {
		DominoThread dt = new DominoThread(new DocCreator(), "Create One Million Docs");
		dt.start();
	}
}
