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

/*
 -- START --
 DocumentIterator set up idArray of 2780192
 Thread MassCopyDocumentsScratchTest elapsed time: 4104398ms
 -- STOP --
 Thread MassCopyDocumentsScratchTest auto-recycled 5523389 lotus references during run. 
 Then recycled 36999 lotus references on completion and had 0 recycle errors
 */
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Enum MassCopyDocumentsScratchTest.
 */
public enum MassCopyDocumentsScratchTest {
	
	/** The instance. */
	INSTANCE;

	/**
	 * Instantiates a new mass copy documents scratch test.
	 */
	private MassCopyDocumentsScratchTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		DominoThread dt = new DominoThread(new Doer(), "MassCopyDocumentsScratchTest");
		dt.start();

	}

	/**
	 * The Class Doer.
	 */
	static class Doer implements Runnable {
		
		/** The Constant SOURCE. */
		private static final String SOURCE = "OneMillion.nsf";
		
		/** The Constant TARGET. */
		private static final String TARGET = "target.nsf";

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			long start = System.nanoTime();
			Session s = Factory.getSession();
			Database source = s.getDatabase("", SOURCE, true);
			Database target = s.getDatabase("", TARGET, true);

			System.out.println("-- START --");

			for (Document doc : source.getAllDocuments()) {
				doc.copyToDatabase(target);
			}

			System.out.println("-- STOP --");

			long elapsed = System.nanoTime() - start;
			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}

	}

}
