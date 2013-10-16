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

import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoThread.
 */
public class DominoThread extends Thread {
	private long starttime_ = 0l;
	private Runnable runnable_;

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
		runnable_ = runnable;
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param runnable
	 *            the runnable
	 */
	public DominoThread(final Runnable runnable, final String name) {
		super(runnable, name);
		runnable_ = runnable;
	}

	public DominoThread(final AbstractDominoRunnable runnable) {
		super(runnable);
		runnable_ = runnable;
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param runnable
	 *            the runnable
	 * @param threadName
	 *            the thread name
	 */
	public DominoThread(final AbstractDominoRunnable runnable, final String threadName) {
		super(runnable, threadName);
		runnable_ = runnable;
	}

	public long getStartTime() {
		return starttime_;
	}

	public Runnable getRunnable() {
		return runnable_;
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
			Factory.setClassLoader(this.getContextClassLoader());
			super.run();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		} finally {
			Factory.terminate();
			lotus.domino.NotesThread.stermThread();
			//						clean();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		super.start();

		starttime_ = System.currentTimeMillis();
	}

	public synchronized void start(final ClassLoader loader) {
		setContextClassLoader(loader);
		start();
	}

	@Override
	public void setContextClassLoader(final ClassLoader loader) {
		super.setContextClassLoader(loader);
	}
}
