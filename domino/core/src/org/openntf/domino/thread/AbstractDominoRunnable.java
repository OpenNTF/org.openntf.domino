/**
 * 
 */
package org.openntf.domino.thread;

import java.io.Serializable;
import java.util.Observable;
import java.util.logging.Logger;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.model.Context;
import org.openntf.domino.thread.model.Scope;
import org.openntf.domino.thread.model.XotsTasklet;

/**
 * @author Nathan T. Freeman
 * 
 * 
 */
public abstract class AbstractDominoRunnable extends Observable implements XotsTasklet.Interface, Runnable, Serializable {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDominoRunnable.class.getName());
	private static final long serialVersionUID = 1L;

	private volatile boolean shouldStop_ = false;
	private Thread runningThread_;
	private ISessionFactory sessionFactory_;

	@Override
	public ISessionFactory getSessionFactory() {
		return sessionFactory_;
	}

	@Override
	public Context getContext() {
		return null;
	}

	@Override
	public Scope getScope() {
		return null;
	}

	/**
	 * Method should be queried in loops to determine if we should stop
	 * 
	 * @return
	 */
	protected synchronized boolean shouldStop() {
		return shouldStop_;
	}

	@Override
	public void setCurrentThread(final Thread thread) {
		runningThread_ = thread;
	}

	@Override
	public synchronized void stop(final boolean force) {
		shouldStop_ = true;
		if (force && runningThread_ != null) {
			runningThread_.interrupt();
		}
	}
}
