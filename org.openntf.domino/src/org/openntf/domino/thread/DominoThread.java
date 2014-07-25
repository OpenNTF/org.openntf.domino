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

import lotus.domino.NotesThread;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoThread.
 */
public class DominoThread extends NotesThread {
	private transient Runnable runnable_;

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
		super();
		runnable_ = runnable;
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param runnable
	 *            the runnable
	 */
	public DominoThread(final Runnable runnable, final String name) {
		super(name);
		runnable_ = runnable;
	}

	public DominoThread(final AbstractDominoRunnable runnable) {
		super();
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
		super(threadName);
		runnable_ = runnable;
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
	public void runNotes() {
		try {
			getRunnable().run();	//NTF: we're already inside the NotesThread.run();
			//Note: if the runnable is a ThreadPoolExecutor.Worker, then this process will not end
			//until the Executor is shutdown or the keep alive expires.
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			termThread();
		}
	}

	//	public synchronized void start(final ClassLoader loader) {
	//		setContextClassLoader(loader);
	//		start();
	//	}

}
