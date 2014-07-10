/**
 * 
 */
package org.openntf.domino.helpers;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.thread.AbstractDominoDaemon;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.thread.DominoFutureTask;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
public class TrustedDispatcher extends AbstractDominoDaemon {
	private static final Logger log_ = Logger.getLogger(TrustedDispatcher.class.getName());
	private static final long serialVersionUID = 1L;
	private TrustedExecutor intimidator_;

	private Queue<Runnable> runQueue_ = new ArrayDeque<Runnable>();

	private TrustedExecutor getExecutor() {
		if (intimidator_ == null) {
			intimidator_ = new TrustedExecutor(this);
		}
		return intimidator_;
	}

	protected static class TrustedExecutor extends DominoExecutor {
		private final TrustedDispatcher dispatcher_;

		protected TrustedExecutor(final TrustedDispatcher dispatcher) {
			dispatcher_ = dispatcher;
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
		 */
		@Override
		public void shutdown() {
			//			System.out.println("Executor shutdown requested");
			super.shutdown();
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
		 */
		@Override
		protected void afterExecute(final Runnable r, final Throwable t) {
			//			System.out.println("afterExecute triggered on a " + r.getClass().getName());
			super.afterExecute(r, t);
			//			if (null != r) {
			//				Runnable target = r;
			//				if (r instanceof DominoFutureTask) {
			//					target = ((DominoFutureTask<?>) r).getRunnable();
			//				}
			//				if (target instanceof AbstractDominoRunnable) {
			//					AbstractDominoRunnable adr = (AbstractDominoRunnable) target;
			//					if (adr.shouldRecycle()) {
			//						//					System.out.println("Queuing a job for on-thread recycling...");
			//						dispatcher_.queueJob(adr);
			//					} else {
			//						System.out.println("ALERT: TrustedDispatcher has an afterExecute on a DominoRunnable that should not recycle. "
			//								+ target.getClass().getName());
			//					}
			//				} else {
			//					//					System.out.println("ALERT: TrustedDispatcher ran a process that isn't a DominoRunnable "
			//					//							+ (null == target ? "null" : target.getClass().getName()));
			//				}
			//			}
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoExecutor#newTaskFor(java.util.concurrent.Callable)
		 */
		@Override
		protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
			return new TrustedFutureTask(callable, dispatcher_);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoExecutor#newTaskFor(java.lang.Runnable, java.lang.Object)
		 */
		@Override
		protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
			return new TrustedFutureTask(runnable, value, dispatcher_);
		}
	}

	protected static class TrustedFutureTask extends DominoFutureTask {
		//		private final TrustedDispatcher dispatcher_;

		public TrustedFutureTask(final Callable callable, final TrustedDispatcher dispatcher) {
			super(callable);
			//			dispatcher_ = dispatcher;
		}

		/**
		 * @param runnable
		 * @param result
		 */
		public TrustedFutureTask(final Runnable runnable, final Object result, final TrustedDispatcher dispatcher) {
			super(runnable, result);
			//			dispatcher_ = dispatcher;
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoFutureTask#run()
		 */
		@Override
		public void run() {
			//			Session session = Factory.fromLotus(dispatcher_.getNewTrustedSession(), org.openntf.domino.Session.SCHEMA, null);
			super.run();
		}

	}

	/**
	 * 
	 */
	public TrustedDispatcher() {
		super(50l);
	}

	/**
	 * @param delay
	 */
	public TrustedDispatcher(final long delay) {
		super(delay);
	}

	public void queueJob(final Runnable runnable) {
		synchronized (runQueue_) {
			runQueue_.add(runnable);
		}
	}

	public Object process(final Runnable runnable) {
		//		System.out.println("Executing a " + runnable.getClass().getName());
		getExecutor().execute(runnable);
		return runnable;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.thread.AbstractDominoDaemon#process()
	 */
	@Override
	public Object process() {
		Runnable request = null;
		synchronized (runQueue_) {
			request = runQueue_.poll();
		}
		if (request != null) {
			//			System.out.println("Attempting auto-recycle of session from trusted runnable...");
			if (request instanceof AbstractDominoRunnable) {
				AbstractDominoRunnable adr = (AbstractDominoRunnable) request;
				if (adr.shouldRecycle()) {
					//					System.out.println("Attempting auto-recycle of session from trusted runnable...");
					lotus.domino.Session s = Factory.toLotus(adr.getSession());
					if (s != null)
						try {
							s.recycle();
							System.out.println(getClass().getName() + " recycled a session from a completed job: "
									+ adr.getClass().getName());
							log_.log(Level.INFO, getClass().getName() + " recycled a session from a completed job: "
									+ adr.getClass().getName());
						} catch (NotesException e) {
							e.printStackTrace();
						}
				} else {
					//					System.out.println("Executing a " + adr.getClass().getName() + " that's not ready to recycle...");
					adr.setSession(getNewTrustedSession());
					getExecutor().execute(request);
				}
			} else {
				getExecutor().execute(request);
			}
			return request;
		}
		return null;	//for future use
	}

	@Override
	public void run() {
		//		try {
		//			Session session = Factory.getTrustedSession();
		//			//			System.out.println("Got a session with ident " + session.getEffectiveUserName());
		//		} catch (Throwable t) {
		//			t.printStackTrace();
		//		}
		super.run();
	}

	public lotus.domino.Session getNewTrustedSession() {
		try {
			Object result = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@SuppressWarnings("deprecation")
				@Override
				public Object run() throws Exception {
					try {
						lotus.domino.Session s = lotus.domino.NotesFactory.createTrustedSession();
						return s;
					} catch (NotesException ne) {
						lotus.domino.Session s = lotus.domino.NotesFactory.createSessionWithFullAccess();
						return s;
					}
				}
			});
			if (result instanceof lotus.domino.Session) {
				return (lotus.domino.Session) result;
			}
		} catch (PrivilegedActionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.thread.AbstractDominoDaemon#stop()
	 */
	@Override
	public synchronized void stop() {
		log_.log(Level.INFO, "Stopping " + getClass().getName() + "...");
		try {
			if (intimidator_ != null)
				intimidator_.shutdown();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		try {
			lotus.domino.Session s = Factory.terminate();
			if (s != null) {
				try {
					s.recycle();
				} catch (NotesException e) {
					DominoUtils.handleException(e);
				}
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		try {
			super.stop();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		log_.log(Level.INFO, getClass().getName() + " stopped.");
	}

}
