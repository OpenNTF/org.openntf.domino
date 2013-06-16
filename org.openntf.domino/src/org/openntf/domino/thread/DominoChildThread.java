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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.impl.Base;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoChildThread.
 */
@Incomplete
public class DominoChildThread extends DominoThread {
	// This will be the Thread for executing Runnables that are given a Domino object starting point, such as a Session or Document or
	// Database.
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DominoChildThread.class.getName());

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
	public DominoChildThread(final Runnable runnable) {
		super(runnable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new domino child thread.
	 * 
	 * @param threadName
	 *            the thread name
	 */
	public DominoChildThread(final String threadName) {
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
	public DominoChildThread(final Runnable runnable, final String threadName) {
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
	public DominoChildThread(final ThreadGroup group, final Runnable runnable) {
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
	public DominoChildThread(final ThreadGroup group, final String threadName) {
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
	public DominoChildThread(final ThreadGroup arg0, final Runnable arg1, final String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	/** The context variables_. */
	private final Map<String, lotus.domino.Base> contextVariables_ = new HashMap<String, lotus.domino.Base>();

	/**
	 * Sets the context.
	 * 
	 * @param bases
	 *            the new context
	 */
	public void setContext(final lotus.domino.Base... bases) {
		Base.lock(bases);
	}

	/**
	 * Sets the context.
	 * 
	 * @param context
	 *            the new context
	 */
	public void setContext(final Map<String, lotus.domino.Base> context) {
		System.out.println("Setting up context variables...");

		for (Map.Entry<String, lotus.domino.Base> entry : context.entrySet()) {
			System.out.println("Putting an " + entry.getValue().getClass().getSimpleName() + " in entry called " + entry.getKey());
			contextVariables_.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Gets the context var.
	 * 
	 * @param name
	 *            the name
	 * @return the context var
	 */
	public lotus.domino.Base getContextVar(final String name) {
		System.out.println("Looking for context variable " + name);

		if (!contextVariables_.containsKey(name))
			System.out.println("No variable found for name: " + name);
		if (contextVariables_.containsKey(name)) {
			return contextVariables_.get(name);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.thread.DominoThread#run()
	 */
	@Override
	public void run() {
		super.runChild();
	}

	/**
	 * Close.
	 */
	public void close() {
		int drCount = 0;
		try {
			drCount = Base.finalizeQueue();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		int runRecycleCount = Factory.getAutoRecycleCount();
		log_.log(
				Level.INFO,
				"Thread " + Thread.currentThread().getName() + " auto-recycled " + runRecycleCount
						+ " lotus references during run. Then recycled " + drCount + " lotus references on completion and had "
						+ Factory.getRecycleErrorCount() + " recycle errors");
		lotus.domino.NotesThread.stermThread();
	}

}
