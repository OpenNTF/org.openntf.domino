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
 * 2.780.192 documents in source application
 -- START --
 -- STOP --
 Thread DCRemoveAllScratchTest elapsed time: 316095ms
 Thread DCRemoveAllScratchTest auto-recycled 0 lotus references during run. Then recycled 2 lotus references on completion and had 0 recycle errors

 */
import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Enum MassDCRemoveAllScratchTest.
 */
public enum MassDCRemoveAllScratchTest {
	
	/** The instance. */
	INSTANCE;

	/**
	 * Instantiates a new mass dc remove all scratch test.
	 */
	private MassDCRemoveAllScratchTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		DominoThread dt = new DominoThread(new Doer(), "DCRemoveAllScratchTest");
		dt.start();
	}

	/**
	 * The Class Doer.
	 */
	static class Doer implements Runnable {
		
		/** The Constant SOURCE. */
		private static final String SOURCE = "target.nsf";

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			Session s = Factory.getSession();
			Database source = s.getDatabase("", SOURCE, true);

			System.out.println("-- START --");
			long start = System.nanoTime();
			DocumentCollection dc = source.getAllDocuments();
			dc.removeAll(true);
			long elapsed = System.nanoTime() - start;
			System.out.println("-- STOP --");

			System.out.println("Thread " + Thread.currentThread().getName() + " elapsed time: " + elapsed / 1000000 + "ms");

		}

	}

}
