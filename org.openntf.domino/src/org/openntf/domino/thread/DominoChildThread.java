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
package org.openntf.domino.thread;

import org.openntf.domino.impl.Base;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoChildThread.
 */
public class DominoChildThread extends DominoThread {
	// This will be the Thread for executing Runnables that are given a Domino object starting point, such as a Session or Document or
	// Database.

	/**
	 * Instantiates a new domino child thread.
	 */
	public DominoChildThread() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino child thread.
	 * 
	 * @param runnable
	 *            the runnable
	 */
	public DominoChildThread(Runnable runnable) {
		super(runnable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino child thread.
	 * 
	 * @param threadName
	 *            the thread name
	 */
	public DominoChildThread(String threadName) {
		super(threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino child thread.
	 * 
	 * @param runnable
	 *            the runnable
	 * @param threadName
	 *            the thread name
	 */
	public DominoChildThread(Runnable runnable, String threadName) {
		super(runnable, threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino child thread.
	 * 
	 * @param group
	 *            the group
	 * @param runnable
	 *            the runnable
	 */
	public DominoChildThread(ThreadGroup group, Runnable runnable) {
		super(group, runnable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino child thread.
	 * 
	 * @param group
	 *            the group
	 * @param threadName
	 *            the thread name
	 */
	public DominoChildThread(ThreadGroup group, String threadName) {
		super(group, threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino child thread.
	 * 
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 */
	public DominoChildThread(ThreadGroup arg0, Runnable arg1, String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Sets the context.
	 * 
	 * @param bases
	 *            the new context
	 */
	public void setContext(lotus.domino.Base... bases) {
		Base.lock(bases);
	}
}
