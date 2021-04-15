/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.thread;

import java.util.logging.Logger;

import lotus.domino.NotesThread;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * The Class DominoThread extends the NotesThread and clones the current SessionFactory.
 */
@SuppressWarnings("nls")
public class DominoThread extends NotesThread {
	private static final Logger log_ = Logger.getLogger(DominoThread.class.getName());
	private transient Runnable runnable_;
	protected int nativeId_;
	private final ISessionFactory sessionFactory_;

	private Factory.ThreadConfig sourceThreadConfig = Factory.getThreadConfig();

	/**
	 * The shutdown hook is executed if the thread is still running before the Factory is shut down.
	 */
	private Runnable shutdownHook = new Runnable() {

		@Override
		public void run() {
			Factory.println(DominoThread.this, "Shutdown " + DominoThread.this);
			DominoThread.this.interrupt();
			try {
				DominoThread.this.join(30 * 1000); // give the thread 30 seconds to join after interrupt.
			} catch (InterruptedException e) {
			}
			if (DominoThread.this.isAlive()) {
				Factory.println(DominoThread.this, "WARNING " + DominoThread.this + " is still alive after 30 secs. Continuing anyway.");
			}
		}
	};

	/**
	 * Instantiates a new domino thread.
	 */
	public DominoThread() {
		sessionFactory_ = Factory.getSessionFactory(SessionType.CURRENT);
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param threadName
	 *            the thread name
	 */
	public DominoThread(final String threadName) {
		super(threadName);
		sessionFactory_ = Factory.getSessionFactory(SessionType.CURRENT);
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
		sessionFactory_ = Factory.getSessionFactory(SessionType.CURRENT);
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
		super(threadName);
		runnable_ = runnable;
		sessionFactory_ = Factory.getSessionFactory(SessionType.CURRENT);
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param runnable
	 *            the runnable
	 * @param threadName
	 *            the thread name
	 * @param sessionFactory
	 *            the session factory
	 */
	public DominoThread(final Runnable runnable, final String threadName, final ISessionFactory sessionFactory) {
		super(threadName);
		runnable_ = runnable;
		sessionFactory_ = sessionFactory;
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param threadName
	 *            the thread name
	 * @param sessionFactory
	 *            the session factory
	 */
	public DominoThread(final String threadName, final ISessionFactory sessionFactory) {
		super(threadName);
		sessionFactory_ = sessionFactory;
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param runnable
	 *            the runnable
	 * @param threadName
	 *            the thread name
	 * @param sessionFactory
	 *            the session factory
	 */
	public DominoThread(final ISessionFactory sessionFactory) {
		super();
		sessionFactory_ = sessionFactory;
	}

	/**
	 * Instantiates a new domino thread.
	 * 
	 * @param runnable
	 *            the runnable
	 * @param threadName
	 *            the thread name
	 * @param sessionFactory
	 *            the session factory
	 */
	public DominoThread(final Runnable runnable, final ISessionFactory sessionFactory) {
		super();
		runnable_ = runnable;
		sessionFactory_ = sessionFactory;
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
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void initThread() {
		super.initThread();
		nativeId_ = this.getNativeThreadID();
		log_.fine("DEBUG: Initializing a " + toString());
		Factory.initThread(sourceThreadConfig);
		Factory.setSessionFactory(sessionFactory_, SessionType.CURRENT);

		Factory.addShutdownHook(shutdownHook);
	}

	@Override
	public void termThread() {
		log_.fine("DEBUG: Terminating a " + toString());
		Factory.removeShutdownHook(shutdownHook);
		super.termThread();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		if (nativeId_ != 0) {
			sb.append(String.format(" [%04x]", nativeId_));
		}
		sb.append(": ");
		if (getRunnable() != null) {
			sb.append(getRunnable().getClass().getName());
		} else {
			sb.append(getClass().getName());
		}
		return sb.toString();
	}
}
