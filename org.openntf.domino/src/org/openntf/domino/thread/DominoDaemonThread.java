/**
 * 
 */
package org.openntf.domino.thread;

import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class DominoDaemonThread extends DominoThread {
	private static final Logger log_ = Logger.getLogger(DominoDaemonThread.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param runnable
	 */
	public DominoDaemonThread(final AbstractDominoRunnable runnable) {
		super(runnable);
		setDaemon(true);
	}

	/**
	 * @param runnable
	 * @param threadName
	 */
	public DominoDaemonThread(final AbstractDominoRunnable runnable, final String threadName) {
		super(runnable, threadName);
		setDaemon(true);
	}

}
