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
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.impl.Base;
import org.openntf.domino.thread.AbstractDominoDaemon;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.thread.DominoFutureTask;
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
		 * @see org.openntf.domino.thread.DominoExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
		 */
		@Override
		protected void afterExecute(final Runnable r, final Throwable t) {
			super.afterExecute(r, t);
			if (r instanceof AbstractDominoRunnable) {
				if (((AbstractDominoRunnable) r).shouldRecycle()) {
					dispatcher_.queueJob((AbstractDominoRunnable) r);
				}
			}
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
		private final TrustedDispatcher dispatcher_;

		public TrustedFutureTask(final Callable callable, final TrustedDispatcher dispatcher) {
			super(callable);
			dispatcher_ = dispatcher;
		}

		/**
		 * @param runnable
		 * @param result
		 */
		public TrustedFutureTask(final Runnable runnable, final Object result, final TrustedDispatcher dispatcher) {
			super(runnable, result);
			dispatcher_ = dispatcher;
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoFutureTask#run()
		 */
		@Override
		public void run() {
			Session session = Factory.fromLotus(dispatcher_.getTrustedSession(), org.openntf.domino.Session.class, null);
			//			Factory.setClassLoader(Thread.currentThread().getContextClassLoader());
			if (session != null) {
				System.out.println("session: " + session.getEffectiveUserName());
				//				Factory.setSession(session);
			}
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

	public void queueJob(final AbstractDominoRunnable runnable) {
		synchronized (runQueue_) {
			runQueue_.add(runnable);
		}
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
			if (request instanceof AbstractDominoRunnable) {
				AbstractDominoRunnable adr = (AbstractDominoRunnable) request;
				if (adr.shouldRecycle()) {
					lotus.domino.Session s = (lotus.domino.Session) Base.getDelegate(adr.getSession());
					if (s != null)
						try {
							s.recycle();
						} catch (NotesException e) {
							e.printStackTrace();
						}
				} else {
					adr.setSession(getTrustedSession());
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
		try {
			Session session = Factory.getTrustedSession();
			System.out.println("Got a session with ident " + session.getEffectiveUserName());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		super.run();
	}

	public lotus.domino.Session getTrustedSession() {
		try {
			Object result = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					lotus.domino.Session s = lotus.domino.NotesFactory.createTrustedSession();
					return s;
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

}
