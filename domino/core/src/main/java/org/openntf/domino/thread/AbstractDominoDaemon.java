/**
 * 
 */
package org.openntf.domino.thread;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Daemon implementation. Must be queued to XOTS if you want to run this periodically
 * 
 * @author Nathan T. Freeman
 * 
 */
// Not yet ready!
@Deprecated
public abstract class AbstractDominoDaemon<T> extends AbstractDominoRunnable {
	private static final Logger log_ = Logger.getLogger(AbstractDominoDaemon.class.getName());
	private static final long serialVersionUID = 1L;
	private long delay_ = 100l;	//default to 100ms delay cycle
	private AccessControlContext acc_;

	/**
	 * 
	 */
	public AbstractDominoDaemon() {

	}

	public AbstractDominoDaemon(final AccessControlContext acc) {
		acc_ = acc;
	}

	public AbstractDominoDaemon(final long delay, final AccessControlContext acc) {
		delay_ = delay;
		acc_ = acc;
	}

	public void setThreadDelay(final long delay) {
		delay_ = delay;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (!shouldStop()) {
				try {
					Object result = null;
					if (acc_ != null) {
						result = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
							@Override
							public Object run() throws Exception {
								return process();
							}
						}, acc_);
					} else {
						result = process();
					}
					if (result != null) {
						setChanged();
						notifyObservers(result);
					}
					Thread.sleep(delay_);
				} catch (InterruptedException ie) {
					stop();
				} catch (PrivilegedActionException e) {
					stop();
					log_.log(Level.SEVERE, "Error in " + this.getClass().getName(), e);
				}
			}
			setChanged();
			notifyObservers();
			deleteObservers();
		} finally {

		}
	}

	public abstract Object process();

}
