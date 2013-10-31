/**
 * 
 */
package org.openntf.domino.thread;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Logger;

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
