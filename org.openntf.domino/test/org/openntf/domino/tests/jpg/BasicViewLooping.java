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
package org.openntf.domino.tests.jpg;

import java.io.PrintWriter;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;

// TODO: Auto-generated Javadoc
/**
 * The Enum BasicViewLooping.
 */
public enum BasicViewLooping {
	
	/** The instance. */
	INSTANCE;

	/**
	 * Instantiates a new basic view looping.
	 */
	private BasicViewLooping() {
	}

	/**
	 * Prints the view entries.
	 * 
	 * @param out
	 *            the out
	 * @param view
	 *            the view
	 */
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
