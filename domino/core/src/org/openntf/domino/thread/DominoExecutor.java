/**
 * 
 */
package org.openntf.domino.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javolution.util.FastMap;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.utils.Factory;

/**
 * A ThreadPoolExecutor for Domino runnables. It sets up a shutdown hook for proper termination. There should be maximum one instance of
 * DominoExecutor, otherwise concurrency won't work
 * 
 * @author Nathan T. Freeman
 */
@Incomplete
public class DominoExecutor extends ThreadPoolExecutor { // do not inherit from ScheduledThreadPoolExecutor

	private static final Logger log_ = Logger.getLogger(DominoExecutor.class.getName());

	protected List<Runnable> deferred = new ArrayList<Runnable>();
	protected Map<Runnable, String> running = new FastMap<Runnable, String>().shared();
	private Set<IDominoListener> listeners_;

	// the shutdown-hook for proper termination
	protected Runnable shutdownHook = new Runnable() {
		@Override
		public void run() {
			shutdownNow();
		}
	};

	private static BlockingQueue<Runnable> getBlockingQueue(final int size) {
		return new LinkedBlockingQueue<Runnable>(size);
	}

	protected void init() {
		Factory.addShutdownHook(shutdownHook);
	}

	public DominoExecutor(final int corePoolSize) {
		super(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
		init();
	}

	/**
	 * Returns a list of all
	 * 
	 * @return
	 */
	public List<Runnable> getRunningTasks() {
		System.out.println("Running: " + running);
		List<Runnable> ret = new ArrayList<Runnable>();
		ret.addAll(getQueue());
		ret.addAll(deferred);
		return ret;
	}

	//
	//	/* (non-Javadoc)
	//	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	//	 */
	//	@Override
	//	public void execute(final Runnable runnable) {
	//		//		DominoRunnableWrapper wrappedRunnable;
	//		//		Runnable realRunnable;
	//		//
	//		//		if (runnable instanceof DominoRunnableWrapper) {
	//		//			wrappedRunnable = (DominoRunnableWrapper) runnable;
	//		//			realRunnable = wrappedRunnable.getRunnable();
	//		//		} else {
	//		//			realRunnable = runnable;
	//		//			wrappedRunnable = wrap(runnable);
	//		//		}
	//		//		System.out.println("DominoExecutor: EXEC: " + runnable.getClass().getName());
	//		//		super.execute(wrappedRunnable);
	//		//	return runnable;
	//
	//		//			Class<? extends Runnable> cls = runnable.getClass();
	//		//			boolean isSingleton = cls.isAnnotationPresent(Singleton.class);
	//		//			boolean isRejectingRunnable = runnable instanceof RejectingRunnable;
	//		//	
	//		//			if (isSingleton || isRejectingRunnable) {
	//		//				synchronized (this) {
	//		//					for (Runnable running : getQueue()) {
	//		//						// If the runnable is declared as singleton, we must not enqueue it, if it is already present
	//		//						if (isSingleton && running.getClass().equals(cls)) {
	//		//							throw new RejectedExecutionException();
	//		//						}
	//		//	
	//		//						// if the runnable implements RejectingRunnable, we check if it may run with others
	//		//						if (isRejectingRunnable) {
	//		//							if (!((RejectingRunnable) runnable).checkReject(unwrap(running))) {
	//		//								throw new RejectedExecutionException();
	//		//							}
	//		//						}
	//		//					}
	//		//					super.execute(wrap(runnable));
	//		//				}
	//		//			} else {
	//		//				super.execute(wrap(runnable));
	//		//			}
	//	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	 */

	@Override
	protected void beforeExecute(final Thread paramThread, final Runnable paramRunnable) {
		super.beforeExecute(paramThread, paramRunnable);
		running.put(paramRunnable, "Started at: " + new Date());
	}

	@Override
	protected void afterExecute(final Runnable runnable, final Throwable paramThrowable) {
		running.remove(runnable);
		super.afterExecute(runnable, paramThrowable);

		synchronized (deferred) {
			if (!deferred.isEmpty() && !isShutdown()) {
				Iterator<Runnable> it = deferred.iterator();
				while (it.hasNext()) {
					try {
						// try to execute the next possible deferred
						execute(it.next());
						it.remove();
					} catch (RejectedExecutionException ex) {

					}
				}
			}
		}
	}

	//	@Override
	//	public void execute(final Runnable task) {
	//		super.execute(wrap(task));
	//	}
	//
	//	@Override
	//	public boolean remove(final Runnable task) {
	//		return super.remove(wrap(task));
	//	}

	//
	//	@Override
	//	public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
	//		// TODO Auto-generated method stub
	//		return super.schedule(callable, delay, unit);
	//	}
	//
	//	@Override
	//	public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
	//		return super.schedule(wrap(command), delay, unit);
	//	}
	//
	//	@Override
	//	public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
	//		// TODO Auto-generated method stub
	//		return super.scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
	//	}
	//
	//	@Override
	//	public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
	//		// TODO Auto-generated method stub
	//		return super.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
	//	}
	//
	//	@Override
	//	public <T> Future<T> submit(final Callable<T> task) {
	//		// TODO Auto-generated method stub
	//		return super.submit(wrap(task));
	//	}
	//
	//	@Override
	//	public <T> Future<T> submit(final Runnable task, final T result) {
	//		// TODO Auto-generated method stub
	//		return super.submit(wrap(task), result);
	//	}
	//
	//	@Override
	//	public Future<?> submit(final Runnable task) {
	//		// TODO Auto-generated method stub
	//		return super.submit(wrap(task));
	//	}

	//	/**
	//	 * Queue the given runnable. In contrast to "execute", queue will ensure that the runnable will run at a later time
	//	 * 
	//	 * @param runnable
	//	 * @return
	//	 */
	//	public boolean queue(final Runnable runnable) {
	//		try {
	//			execute(runnable);
	//			//schedule(runnable, 30, TimeUnit.SECONDS);
	//			return true;
	//		} catch (RejectedExecutionException ex) {
	//			// if we are shutting down, don't queue anything
	//			if (isShutdown()) {
	//				throw ex;
	//			}
	//			// otherwise we will add the runnable to the deferred list
	//			synchronized (deferred) {
	//				deferred.add(runnable);
	//			}
	//		}
	//		return false;
	//	}

	//	/* (non-Javadoc)
	//	 * @see java.util.concurrent.AbstractExecutorService#newTaskFor(java.util.concurrent.Callable)
	//	 */
	//	@Override
	//	protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
	//		return new DominoFutureTask<T>(callable);
	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see java.util.concurrent.AbstractExecutorService#newTaskFor(java.lang.Runnable, java.lang.Object)
	//	 */
	//	@Override
	//	protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
	//		return new DominoFutureTask<T>(runnable, value);
	//	}

	//	/* (non-Javadoc)
	//	 * @see java.util.concurrent.ThreadPoolExecutor#awaitTermination(long, java.util.concurrent.TimeUnit)
	//	 */
	//	@Override
	//	public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
	//		return super.awaitTermination(timeout, unit);
	//	}

	@Incomplete
	//these can't use the IDominoListener interface. Will need to put something new together for that.
	public void addListener(final IDominoListener listener) {
		if (listeners_ == null) {
			listeners_ = new LinkedHashSet<IDominoListener>();
		}
		listeners_.add(listener);
	}

	@Incomplete
	public IDominoListener removeListener(final IDominoListener listener) {
		if (listeners_ != null) {
			listeners_.remove(listener);
		}
		return listener;
	}

	@Override
	public void shutdown() {
		Factory.removeShutdownHook(shutdownHook);
		super.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		List<Runnable> ret = super.shutdownNow();
		Factory.removeShutdownHook(shutdownHook);
		return ret;
	}

	//	protected Runnable wrap(final Runnable runnable) {
	//		if (runnable instanceof AbstractDominoRunner) {
	//			return runnable;
	//		}
	//		return new DominoRunner(runnable);
	//	}
	//
	//	protected <T> Callable<T> wrap(final Callable<T> callable) {
	//		if (callable instanceof AbstractDominoRunner.WrappedCallable) {
	//			return callable;
	//		}
	//		return new DominoRunner(callable).asCallable(callable);
	//	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
		return new DominoFutureTask<T>(callable);
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T result) {
		return new DominoFutureTask<T>(runnable, result);
	}
}
