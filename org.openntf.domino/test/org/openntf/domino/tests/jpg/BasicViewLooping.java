package org.openntf.domino.tests.jpg;

import java.io.PrintWriter;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;

public enum BasicViewLooping {
	INSTANCE;

	private BasicViewLooping() {
	}

	public static void printViewEntries(PrintWriter out, View view) {
		long start = System.nanoTime();
		for (ViewEntry entry : view.getAllEntries()) {
			out.println(entry.getPosition('.') + " - " + entry.getColumnValues());

			Document doc = entry.getDocument();
			out.println(doc.getNoteID());
		}
		long end = System.nanoTime();
		out.println("finished looping in " + ((end - start) / 1000 / 1000) + "ns");
		out.println("done");
	}

	/**
	 * Prints the docs.
	 * 
	 * @param out
	 *            the out
	 * @param view
	 *            the view
	 */
	public static void printDocs(PrintWriter out, View view) {
		long start = System.nanoTime();
		Document doc = view.getFirstDocument();
		while (doc != null) {
			out.println(doc.getNoteID() + " - " + doc.getColumnValues());

			doc = view.getNextDocument(doc);
		}
		long end = System.nanoTime();
		out.println("finished looping in " + ((end - start) / 1000 / 1000) + "ns");
		out.println("done");
	}
}
