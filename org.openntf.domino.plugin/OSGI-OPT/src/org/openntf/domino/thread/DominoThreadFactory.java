/**
 * 
 */
package org.openntf.domino.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author Nathan T. Freeman
 * 
 */
@Incomplete
public class DominoThreadFactory implements ThreadFactory {
	private static final long THREAD_DELAY = 400l;

	public static class DominoUncaughtExceptionHandler implements UncaughtExceptionHandler {
		@Override
		public void uncaughtException(final Thread paramThread, final Throwable paramThrowable) {
			DominoUtils.handleException(paramThrowable);
			if (paramThread instanceof DominoThread) {
				Runnable runnable = ((DominoThread) paramThread).getRunnable();
				if (runnable instanceof AbstractDominoRunnable) {
					((AbstractDominoRunnable) runnable).clean();
				}
			}
		}

	}

	private static final Logger log_ = Logger.getLogger(DominoThreadFactory.class.getName());
	private static final long serialVersionUID = 1L;
	private AtomicInteger count_ = new AtomicInteger();
	private long lastThread_ = 0;

	//	private final Deque<DominoThread> activeThreads_ = new ArrayDeque<DominoThread>();
	//private final Deque<DominoThread> idleThreads_ = new ArrayDeque<DominoThread>();

	public DominoThreadFactory() {
		//		System.out.println("Created a new DominoThreadFactory");
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(final Runnable paramRunnable) {
		DominoThread result = null;
		long time = System.currentTimeMillis() - lastThread_;
		if (time < THREAD_DELAY) {
			long sleeptime = THREAD_DELAY - time;
			System.out.println("ThreadFactory autodelaying for " + sleeptime);
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (paramRunnable instanceof AbstractDominoDaemon) {
			result = new DominoDaemonThread((AbstractDominoDaemon) paramRunnable);
		} else {
			result = new DominoThread(paramRunnable);
		}
		lastThread_ = System.currentTimeMillis();
		int count = count_.incrementAndGet();
		result.setUncaughtExceptionHandler(new DominoUncaughtExceptionHandler());
		return result;
	}

}
