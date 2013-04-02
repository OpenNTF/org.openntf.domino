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
 * -- START --
 1000000
 -- STOP --
 Thread MassViewEntryCollectionTest elapsed time: 293767ms
 Thread MassViewEntryCollectionTest auto-recycled 994806 lotus references during run. Then recycled 5197 lotus references on completion and had 0 recycle errors

 */
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Enum MassViewEntryCollectionTest.
 */
public enum MassViewEntryCollectionTest {
	
	/** The instance. */
	INSTANCE;

	/**
	 * Instantiates a new mass view entry collection test.
	 */
	private MassViewEntryCollectionTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		DominoThread dt = new DominoThread(new Doer(), "MassViewEntryCollectionTest");
		dt.start();
	}

	/**
	 * The Class Doer.
	 */
	static class Doer implements Runnable {
		
		/** The Constant TARGET. */
		private static final String TARGET = "target.nsf";
		
		/** The Constant VIEW. */
		private static final String VIEW = "Persons";

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			Session s = Factory.getSession();
			Database source = s.getDatabase("", TARGET, true);
			View view = source.getView(VIEW);
			System.out.println("-- START --");
			long start = System.nanoTime();

			if (null != view) {
				view.setAutoUpdate(false);

				System.out.println(view.getEntryCount());

				for (@SuppressWarnings("unused")
				ViewEntry entry : view.getAllEntries()) {

				}
				/* */
				view.setAutoUpdate(true);
			}

			long elapsed = System.nanoTime() - start;
			System.out.println("-- STOP --");
			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}
	}

}
