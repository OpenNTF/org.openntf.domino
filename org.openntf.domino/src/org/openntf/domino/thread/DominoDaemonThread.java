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
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DominoDaemonThread.class.getName());

	public DominoDaemonThread(final AbstractDominoRunnable runnable) {
		super(runnable);
		setDaemon(true);
	}

	public DominoDaemonThread(final AbstractDominoRunnable runnable, final String threadName) {
		super(runnable, threadName);
		setDaemon(true);
	}

}
