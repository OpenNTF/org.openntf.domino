/**
 * 
 */
package org.openntf.domino.thread;

import java.io.Serializable;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.xots.Tasklet;

/**
 * An observable callable implementation. In your implementation you should check {@link #shouldStop()} periodically
 * 
 * @author Nathan T. Freeman
 */
public abstract class AbstractDominoCallable<T> extends Observable implements Tasklet.Interface, Callable<T>, Serializable {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDominoCallable.class.getName());
	private static final long serialVersionUID = 1L;

	private boolean shouldStop_ = false;
	private Thread runningThread_;

	@Override
	public ISessionFactory getSessionFactory() {
		return null;
	}

	@Override
	public Tasklet.Context getContext() {
		return null;
	}

	@Override
	public Tasklet.Scope getScope() {
		return null;
	}

	/**
	 * Returns true Method should be queried in loops to determine if we should stop
	 * 
	 * @return
	 */
	protected synchronized boolean shouldStop() {
		return shouldStop_;
	}

	@Override
	public synchronized void stop() {
		shouldStop_ = true;
	}
}
