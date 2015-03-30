/**
 * 
 */
package org.openntf.domino.thread;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.annotations.Incomplete;

/**
 * A ThreadPoolExecutor for Domino runnables. It sets up a shutdown hook for proper termination. There should be maximum one instance of
 * DominoExecutor, otherwise concurrency won't work
 * 
 * @author Nathan T. Freeman
 */
@Incomplete
public class DominoExecutor extends AbstractDominoExecutor {
	private static final Logger log_ = Logger.getLogger(DominoExecutor.class.getName());

	/**
	 * The Callable - Wrapper for Domino Tasks
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 * @param <V>
	 */
	private static class DominoWrappedCallable<V> extends AbstractWrappedTask implements IWrappedCallable<V> {

		public DominoWrappedCallable(final Callable<V> runnable) {
			setWrappedTask(runnable);
		}

		@SuppressWarnings("unchecked")
		@Override
		public V call() throws Exception {
			return (V) callOrRun();
		}

	}

	/**
	 * The Runnable - Wrapper for Domino Tasks
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	private static class DominoWrappedRunnable extends AbstractWrappedTask implements IWrappedRunnable {

		public DominoWrappedRunnable(final java.lang.Runnable runnable) {
			setWrappedTask(runnable);
		}

		@Override
		public void run() {
			try {
				callOrRun();
			} catch (Throwable t) {
				log_.log(Level.WARNING, getDescription() + " caused an error: " + t.toString(), t);
				throw new RuntimeException(t);
			}
		}
	}

	// 		*********************************
	//		* Start of DominoExecutor class *
	//		*********************************

	/**
	 * Constructor of the DominoExecutor
	 * 
	 */
	public DominoExecutor(final int corePoolSize, final String executorName) {
		super(corePoolSize, executorName);
		setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
	}

	public DominoExecutor(final int corePoolSize) {
		this(corePoolSize, "Unnamed");
	}

	/**
	 * The wrap factory method for Callables
	 */
	@Override
	protected <V> IWrappedCallable<V> wrap(final Callable<V> inner) {
		if (inner instanceof IWrappedCallable)
			return (IWrappedCallable<V>) inner;
		return new DominoWrappedCallable<V>(inner);
	}

	/**
	 * The wrap factory method for Runnables
	 */
	@Override
	protected IWrappedRunnable wrap(final Runnable inner) {
		if (inner instanceof IWrappedRunnable)
			return (IWrappedRunnable) inner;
		return new DominoWrappedRunnable(inner);
	}

	@Override
	protected IWrappedCallable<?> wrap(final String moduleName, final String className, final Object... ctorArgs) {
		throw new UnsupportedOperationException("Running tasklets is not supported (requires XPage-environment)");
		// TODO: maybe we can load the class with the design-class loader.
	}

}
