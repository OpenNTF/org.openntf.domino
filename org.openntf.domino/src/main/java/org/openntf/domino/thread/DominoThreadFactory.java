/**
 * 
 */
package org.openntf.domino.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.utils.Factory;

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
			System.out.println("ALERT: Uncaught exception in DominoThread " + paramThread.getClass().getName());
			paramThrowable.printStackTrace();
			//			if (paramThread instanceof DominoThread) {
			//				Runnable runnable = ((DominoThread) paramThread).getRunnable();
			//				if (runnable instanceof IDominoRunnable) {
			//					lotus.domino.Session s = ((IDominoRunnable) runnable).getSession();
			//					if (s != null) {
			//						try {
			//							s.recycle();
			//						} catch (Throwable t) {
			//							t.printStackTrace();
			//						}
			//					}
			//				}
			//			}
			if (paramThread instanceof lotus.domino.NotesThread) {
				lotus.domino.Session session = Factory.terminate();
				if (session != null) {
					try {
						session.recycle();
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
				((lotus.domino.NotesThread) paramThread).termThread();
			}
		}
	}

	private static final Logger log_ = Logger.getLogger(DominoThreadFactory.class.getName());
	private static final long serialVersionUID = 1L;
	private AtomicInteger count_ = new AtomicInteger();
	private long lastThread_ = 0;
	private ClassLoader factoryClassLoader_;
	private AccessControlContext factoryAccessController_;
	private UncaughtExceptionHandler factoryExceptionHandler_ = new DominoUncaughtExceptionHandler();

	public DominoThreadFactory() {
		factoryClassLoader_ = Thread.currentThread().getContextClassLoader();
		factoryAccessController_ = AccessController.getContext();
	}

	private void stall() {
		long time = System.currentTimeMillis() - lastThread_;
		if (time < THREAD_DELAY) {
			long sleeptime = THREAD_DELAY - time;
			log_.log(Level.INFO, getClass().getName() + " autodelaying for " + sleeptime);
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected DominoThread makeThread(final Runnable runnable) {
		return new DominoThread(runnable);
	}

	@Override
	public Thread newThread(final Runnable paramRunnable) {
		DominoThread result = null;
		result = makeThread(paramRunnable);
		lastThread_ = System.currentTimeMillis();
		count_.incrementAndGet();
		result.setUncaughtExceptionHandler(factoryExceptionHandler_);
		//		System.out.println("DEBUG: Constructing a " + result.getClass().getSimpleName() + ": " + result.getId() + " ("
		//				+ System.identityHashCode(result) + ")");
		return result;
	}

}
