/**
 * 
 */
package org.openntf.domino.thread;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import lotus.domino.NotesThread;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.thread.AbstractWrapped.WrappedCallable;
import org.openntf.domino.thread.AbstractWrapped.WrappedRunnable;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

/**
 * A ThreadPoolExecutor for Domino runnables. It sets up a shutdown hook for proper termination. There should be maximum one instance of
 * DominoExecutor, otherwise concurrency won't work
 * 
 * @author Nathan T. Freeman
 */
@Incomplete
public class DominoExecutor extends AbstractDominoExecutor {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DominoExecutor.class.getName());

	/**
	 * The Callable - Wrapper for Domino Tasks
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 * @param <V>
	 */
	private static class DominoWrappedCallable<V> extends WrappedCallable<V> {

		public DominoWrappedCallable(final Callable<V> runnable) {
			init(runnable);
		}

		@Override
		public V call() throws Exception {
			return callOrRun(getWrappedTask(), null);
		}

	}

	/**
	 * The Runnable - Wrapper for Domino Tasks
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	private static class DominoWrappedRunnable extends WrappedRunnable {

		public DominoWrappedRunnable(final java.lang.Runnable runnable) {
			init(runnable);
		}

		@Override
		public void run() {
			try {
				callOrRun(null, getWrappedTask());
			} catch (Exception e) {
				DominoUtils.setBubbleExceptions(true);
				DominoUtils.handleException(e);
			}
		}

	}

	/**
	 * Common method that does setUp/tearDown before executing the wrapped object
	 * 
	 * @param callable
	 * @param runnable
	 * @return
	 * @throws Exception
	 */
	private static <V> V callOrRun(final Callable<V> callable, final Runnable runnable) throws Exception {
		NotesThread.sinitThread();
		DominoUtils.setBubbleExceptions(true); // RPr: true is always good (don't like suppressing errors at all)
		Factory.initThread();
		try {
			Thread thread = Thread.currentThread();
			ClassLoader oldCl = thread.getContextClassLoader();

			ClassLoader runCl = (callable != null ? callable : runnable).getClass().getClassLoader();
			thread.setContextClassLoader(runCl);
			try {
				if (callable != null) {
					return callable.call();
				} else {
					runnable.run();
					return null;
				}
			} finally {
				thread.setContextClassLoader(oldCl);
			}
		} finally {
			Factory.termThread();
			NotesThread.stermThread();
		}
	}

	// 		*********************************
	//		* Start of DominoExecutor class *
	//		*********************************

	/**
	 * Constructor of the DominoExecutor
	 * 
	 */
	public DominoExecutor(final int corePoolSize) {
		super(corePoolSize);
		setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
	}

	/**
	 * The wrap factory method for Callables
	 */
	@Override
	protected <V> WrappedCallable<V> wrap(final Callable<V> inner) {
		if (inner instanceof WrappedCallable)
			return (WrappedCallable<V>) inner;
		return new DominoWrappedCallable<V>(inner);
	}

	/**
	 * The wrap factory method for Runnables
	 */
	@Override
	protected WrappedRunnable wrap(final Runnable inner) {
		if (inner instanceof WrappedRunnable)
			return (WrappedRunnable) inner;
		return new DominoWrappedRunnable(inner);
	}

	@Override
	protected WrappedCallable<?> wrap(final String moduleName, final String className, final Object... ctorArgs) {
		throw new UnsupportedOperationException("Running tasklets is not supported (requires XPage-environment)");
		// TODO: maybe we can load the class with the design-class loader.
	}

}
