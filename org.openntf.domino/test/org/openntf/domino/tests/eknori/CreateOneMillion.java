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
package org.openntf.domino.tests.eknori;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateOneMillion.
 */
public class CreateOneMillion {
	
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(CreateOneMillion.class.getName());

	/**
	 * The Class DocCreator.
	 */
	static class DocCreator implements Runnable {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
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
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		DominoThread dt = new DominoThread(new DocCreator(), "Create One Million Docs");
		dt.start();
	}

}
