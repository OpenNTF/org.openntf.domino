/**
 * 
 */
package org.openntf.domino.thread;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Logger;

import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
public abstract class AbstractDominoDaemon extends AbstractDominoRunnable {
	private static final Logger log_ = Logger.getLogger(AbstractDominoDaemon.class.getName());
	private static final long serialVersionUID = 1L;
	private volatile boolean shouldStop_ = false;
	private long delay_ = 100l;	//default to 100ms delay cycle
	private transient org.openntf.domino.Session session_;
	private boolean running_ = false;

	/**
	 * 
	 */
	public AbstractDominoDaemon() {

	}

	public AbstractDominoDaemon(final long delay) {
		delay_ = delay;
	}

	public void setThreadDelay(final long delay) {
		delay_ = delay;
	}

	public Session getSession() {
		if (session_ == null) {
			session_ = Factory.getSession();
		}
		return session_;
	}

	protected void setSession(final lotus.domino.Session session) {
		if (session instanceof org.openntf.domino.Session) {
			session_ = (org.openntf.domino.Session) session;
		} else {
			session_ = Factory.fromLotus(session, org.openntf.domino.Session.class, null);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!shouldStop()) {
			try {
				Object result = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
					@Override
					public Object run() throws Exception {
						Object result = process();
						return result;
					}
				});
				if (result != null) {
					setChanged();
					notifyObservers(result);
				}
				Thread.sleep(delay_);
			} catch (InterruptedException ie) {
				stop();
			} catch (PrivilegedActionException e) {
				stop();
			}
		}
		setChanged();
		notifyObservers();
		clean();
		running_ = false;
	}

	@Override
	public void clean() {
		deleteObservers();
		super.clean();
	}

	public abstract Object process();

	/* (non-Javadoc)
	 * @see org.openntf.domino.thread.AbstractDominoRunnable#shouldStop()
	 */
	@Override
	public synchronized boolean shouldStop() {
		return shouldStop_;
	}

	public synchronized void stop() {
		shouldStop_ = true;
	}

	public synchronized void start() {
		if (!running_) {
			new DominoDaemonThread(this).start();
		}
	}
}
