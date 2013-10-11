/**
 * 
 */
package org.openntf.domino.helpers;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Logger;

import org.openntf.domino.thread.AbstractDominoDaemon;
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
	private TrustedExecutor intimidator_ = new TrustedExecutor();
	private Queue<Object> runQueue_ = new ArrayDeque<Object>();

	protected static class TrustedExecutor extends DominoExecutor {
		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoExecutor#newTaskFor(java.util.concurrent.Callable)
		 */
		@Override
		protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
			return new TrustedFutureTask(callable);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoExecutor#newTaskFor(java.lang.Runnable, java.lang.Object)
		 */
		@Override
		protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
			return new TrustedFutureTask(runnable, value);
		}
	}

	protected static class TrustedFutureTask extends DominoFutureTask {
		public TrustedFutureTask(final Callable callable) {
			super(callable);
		}

		/**
		 * @param runnable
		 * @param result
		 */
		public TrustedFutureTask(final Runnable runnable, final Object result) {
			super(runnable, result);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoFutureTask#run()
		 */
		@Override
		public void run() {
			Factory.setClassLoader(Thread.currentThread().getContextClassLoader());
			Factory.setSession(Factory.getTrustedSession());
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.thread.AbstractDominoDaemon#process()
	 */
	@Override
	public Object process() {
		Object request = null;
		synchronized (runQueue_) {
			request = runQueue_.poll();
			while (request != null) {
				if (request instanceof Runnable) {
					intimidator_.execute((Runnable) request);

				}
				request = runQueue_.poll();
			}
		}
		return null;
	}
}
