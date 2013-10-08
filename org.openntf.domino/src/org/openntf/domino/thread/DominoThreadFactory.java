/**
 * 
 */
package org.openntf.domino.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.openntf.domino.annotations.Incomplete;

/**
 * @author Nathan T. Freeman
 * 
 */
@Incomplete
public class DominoThreadFactory implements ThreadFactory {
	private static final Logger log_ = Logger.getLogger(DominoThreadFactory.class.getName());
	private static final long serialVersionUID = 1L;
	private AtomicInteger count_ = new AtomicInteger();
	private long lastThread_ = 0;

	//	private final Deque<DominoThread> activeThreads_ = new ArrayDeque<DominoThread>();
	//private final Deque<DominoThread> idleThreads_ = new ArrayDeque<DominoThread>();

	public DominoThreadFactory() {
		System.out.println("Created a new DominoThreadFactory");
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(final Runnable paramRunnable) {
		System.out.println("Maing a new DominoThread...");
		DominoThread result = null;
		long time = System.currentTimeMillis() - lastThread_;
		if (time < 100) {
			try {
				Thread.sleep(100 - time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		result = new DominoThread(paramRunnable);
		lastThread_ = System.currentTimeMillis();
		int count = count_.incrementAndGet();
		System.out.println("Returning a new DominoThread...");
		return result;
	}

}
