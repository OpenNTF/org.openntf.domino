/*
 * Copyright 2013
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
package org.openntf.domino.thread;

import org.openntf.domino.impl.Base;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoThread.
 */
public class DominoThread extends Thread {
	// lotus.domino.NotesThread temp_;
	// This will be the Thread for executing Runnables that need Domino objects created from scratch

	/**
	 * Instantiates a new domino thread.
	 */
	public DominoThread() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param runnable
	 *            the runnable
	 */
	public DominoThread(final Runnable runnable) {
		super(runnable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param threadName
	 *            the thread name
	 */
	public DominoThread(final String threadName) {
		super(threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param runnable
	 *            the runnable
	 * @param threadName
	 *            the thread name
	 */
	public DominoThread(final Runnable runnable, final String threadName) {
		super(runnable, threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param group
	 *            the group
	 * @param runnable
	 *            the runnable
	 */
	public DominoThread(final ThreadGroup group, final Runnable runnable) {
		super(group, runnable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param group
	 *            the group
	 * @param threadName
	 *            the thread name
	 */
	public DominoThread(final ThreadGroup group, final String threadName) {
		super(group, threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 */
	public DominoThread(final ThreadGroup arg0, final Runnable arg1, final String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			lotus.domino.NotesThread.sinitThread();
			super.run();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		} finally {
			System.gc();
			try {
				sleep(1000);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			int drCount = 0;
			try {
				drCount = Base.drainQueue(0l);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			int runRecycleCount = Factory.getAutoRecycleCount();
			System.out.println("Thread " + Thread.currentThread().getName() + " auto-recycled " + runRecycleCount
					+ " lotus references during run. Then recycled " + drCount + " lotus references on completion and had "
					+ Factory.getRecycleErrorCount() + " recycle errors");
			lotus.domino.NotesThread.stermThread();
		}
	}

	/**
	 * Run child.
	 */
	public void runChild() {
		try {
			lotus.domino.NotesThread.sinitThread();
			super.run();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		} finally {
			System.gc();
		}

		// deliberately don't close out the thread access...
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
	}
}
