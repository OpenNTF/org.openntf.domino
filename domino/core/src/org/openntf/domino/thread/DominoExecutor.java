/**
 * 
 */
package org.openntf.domino.thread;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import lotus.domino.NotesThread;

import org.openntf.domino.annotations.Incomplete;
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

	public DominoExecutor(final int corePoolSize) {
		super(corePoolSize);
		// TODO Auto-generated constructor stub
	}

	private static final Logger log_ = Logger.getLogger(DominoExecutor.class.getName());

	@Override
	protected <V> WrappedCallable<V> wrap(final Callable<V> inner) {
		// TODO Auto-generated method stub
		return new WrappedCallable<V>(inner) {

			@Override
			public V call() throws Exception {
				NotesThread.sinitThread();
				DominoUtils.setBubbleExceptions(bubbleException);
				Factory.initThread();
				try {
					Thread thread = Thread.currentThread();
					ClassLoader oldCl = thread.getContextClassLoader();

					ClassLoader runCl = wrappedObject.getClass().getClassLoader();
					thread.setContextClassLoader(runCl);
					try {
						return getWrappedObject().call();
					} finally {
						thread.setContextClassLoader(oldCl);
					}
				} finally {
					Factory.termThread();
					NotesThread.stermThread();
				}
			}

		};
	}

	@Override
	protected WrappedRunnable wrap(final Runnable inner) {
		return new WrappedRunnable(inner) {

			@Override
			public void run() {
				NotesThread.sinitThread();
				Factory.initThread();
				try {
					Thread thread = Thread.currentThread();
					ClassLoader oldCl = thread.getContextClassLoader();

					ClassLoader runCl = wrappedObject.getClass().getClassLoader();
					thread.setContextClassLoader(runCl);
					try {
						getWrappedObject().run();
					} finally {
						thread.setContextClassLoader(oldCl);
					}
				} finally {
					Factory.termThread();
					NotesThread.stermThread();
				}

			}

		};
	}

	//	public class DominoFutureTask<T> extends AbstractDominoFutureTask<T> {
	//
	//		public DominoFutureTask(final Callable<T> callable, final long time) {
	//			super(wrap(callable), time);
	//		}
	//
	//		public DominoFutureTask(final Runnable runnable, final T result, final long time, final long period) {
	//			super(wrap(runnable), result, time, period);
	//		}
	//
	//		public DominoFutureTask(final Runnable runnable, final T result, final long time) {
	//			super(wrap(runnable), result, time);
	//		}
	//
	//		private static Runnable wrap(final Runnable runnable) {
	//			// TODO Auto-generated method stub
	//			return null;
	//
	//		}
	//
	//		private static Callable<T> wrap(Callable<T> callable) {
	//			// TODO Auto-generated method stub
	//			return null;
	//		}
	//
	//		@Override
	//		public void run() {
	//			NotesThread.sinitThread();
	//			Factory.initThread();
	//			try {
	//				Thread thread = Thread.currentThread();
	//				ClassLoader oldCl = thread.getContextClassLoader();
	//
	//				ClassLoader runCl = wrappedObject.getClass().getClassLoader();
	//				thread.setContextClassLoader(runCl);
	//				try {
	//					super.run();
	//				} finally {
	//					thread.setContextClassLoader(oldCl);
	//				}
	//			} finally {
	//				Factory.termThread();
	//				NotesThread.stermThread();
	//			}
	//		}
	//	}

	//	protected List<Runnable> deferred = new ArrayList<Runnable>();
	//	protected Map<Runnable, String> running = new FastMap<Runnable, String>().shared();
	//	private Set<IDominoListener> listeners_;
	//
	//	// the shutdown-hook for proper termination
	//	protected Runnable shutdownHook = new Runnable() {
	//		@Override
	//		public void run() {
	//			shutdownNow();
	//		}
	//	};
	//
	//	/**
	//	 * A Wrapper for runnables
	//	 * 
	//	 * @author Roland Praml, FOCONIS AG
	//	 */
	//	protected abstract class AbstractDominoFutureTask<T> extends FutureTask<T> implements Delayed, RunnableScheduledFuture<T> {
	//
	//		/** the context when this object was created */
	//		//protected ISessionFactory sessionFactory;
	//
	//		protected Scope scope;
	//		protected Context context;
	//		protected ISessionFactory sessionFactory;
	//		protected boolean bubbleException;
	//		protected Object wrappedObject;
	//
	//		// the period
	//		private long period;
	//
	//		// The next runtime;
	//		private long time = 0;
	//
	//		private TimeUnit timeUnit = TimeUnit.NANOSECONDS;
	//
	//		public AbstractDominoFutureTask(final DominoExecutor executor, final Callable<T> callable, final long delay, final TimeUnit timeUnit) {
	//			super(callable);
	//			this.parent = executor;
	//			this.time = executor.now();
	//			init(callable);
	//			if (delay != 0) {
	//				this.time = time;
	//				this.timeUnit = timeUnit;
	//			}
	//		}
	//
	//		public AbstractDominoFutureTask(final DominoExecutor executor, final Runnable runnable, final T result, final long time,
	//				final TimeUnit timeUnit) {
	//			super(runnable, result);
	//			this.parent = executor;
	//			init(runnable);
	//			if (time != 0) {
	//				this.time = time;
	//				this.timeUnit = timeUnit;
	//			}
	//
	//		}
	//
	//		public AbstractDominoFutureTask(final DominoExecutor executor, final Runnable runnable, final T result, final long delay,
	//				final long period, final TimeUnit timeUnit, final boolean fixedRate) {
	//			// TODO Auto-generated constructor stub
	//			super(runnable, result);
	//			this.parent = executor;
	//			init(runnable);
	//			if (time != 0) {
	//				this.time = time;
	//				this.timeUnit = timeUnit;
	//			}
	//		}
	//
	//		//	protected static class CallableToRunnable<T> implements Runnable {
	//		//		private final Callable<T> callable_;
	//		//		protected Object result;
	//		//		protected Exception exception;
	//		//
	//		//		private CallableToRunnable(final Callable<T> callable) {
	//		//			callable_ = callable;
	//		//		}
	//		//
	//		//		@Override
	//		//		public void run() {
	//		//			try {
	//		//				result = callable_.call();
	//		//			} catch (Exception e) {
	//		//				exception = e;
	//		//			}
	//		//		}
	//		//	}
	//		//
	//		//	public AbstractDominoRunner(final Runnable runnable) {
	//		//		super(runnable);
	//		//		if (runnable instanceof NotesThread) {
	//		//			throw new IllegalStateException("Cannot wrap the NotesThread " + runnable.getClass().getName() + " into a DominoRunner");
	//		//		}
	//		//		if (runnable instanceof AbstractDominoRunner) {
	//		//			throw new IllegalStateException("Cannot wrap the DominoRunner " + runnable.getClass().getName() + " into a DominoRunner");
	//		//		}
	//		//	}
	//		//
	//		//	public <T> AbstractDominoRunner(final Callable<T> callable) {
	//		//		super(new CallableToRunnable<T>(callable));
	//		//		init(callable);
	//		//	}
	//
	//		/**
	//		 * Determines the sessionType under which the current runnable should run. The first non-null value of the following list is
	//		 * returned
	//		 * <ul>
	//		 * <li>If the runnable implements <code>IDominoRunnable</code>: result of <code>getSessionType</code></li>
	//		 * <li>the value of {@link SessionType} Annotation</li>
	//		 * <li>DominoSessionType.DEFAULT</li>
	//		 * </ul>
	//		 * 
	//		 * @param runnable
	//		 *            the runnable to determine the DominoSessionType
	//		 * @return the DominoSessionType
	//		 */
	//		protected void init(final Object runnable) {
	//			wrappedObject = runnable;
	//			if (runnable instanceof NotesThread) {
	//				throw new IllegalStateException("Cannot wrap the NotesThread " + runnable.getClass().getName() + " into a DominoRunner");
	//			}
	//			if (runnable instanceof AbstractDominoFutureTask) {
	//				// RPr: don't know if this is possible
	//				throw new IllegalStateException("Cannot wrap the WrappedCallable " + runnable.getClass().getName() + " into a DominoRunner");
	//			}
	//
	//			if (runnable instanceof XotsTasklet.Interface) {
	//				XotsTasklet.Interface dominoRunnable = (XotsTasklet.Interface) runnable;
	//				sessionFactory = dominoRunnable.getSessionFactory();
	//				scope = dominoRunnable.getScope();
	//				context = dominoRunnable.getContext();
	//			}
	//			bubbleException = DominoUtils.getBubbleExceptions();
	//			XotsTasklet annot = runnable.getClass().getAnnotation(XotsTasklet.class);
	//			if (annot != null) {
	//				System.out.println("XotsTasklet.session " + annot.session());
	//				if (sessionFactory == null) {
	//					switch (annot.session()) {
	//					case CLONE:
	//						sessionFactory = Factory.getSessionFactory(SessionType.CURRENT);
	//						break;
	//					case CLONE_FULL_ACCESS:
	//						sessionFactory = Factory.getSessionFactory(SessionType.CURRENT_FULL_ACCESS);
	//						break;
	//
	//					case FULL_ACCESS:
	//						sessionFactory = Factory.getSessionFactory(SessionType.FULL_ACCESS);
	//						break;
	//
	//					case NATIVE:
	//						sessionFactory = Factory.getSessionFactory(SessionType.NATIVE);
	//						break;
	//
	//					case NONE:
	//						sessionFactory = null;
	//						break;
	//
	//					case SIGNER:
	//						sessionFactory = Factory.getSessionFactory(SessionType.SIGNER);
	//						break;
	//
	//					case SIGNER_FULL_ACCESS:
	//						sessionFactory = Factory.getSessionFactory(SessionType.SIGNER_FULL_ACCESS);
	//						break;
	//
	//					case TRUSTED:
	//						sessionFactory = Factory.getSessionFactory(SessionType.TRUSTED);
	//						break;
	//
	//					default:
	//						break;
	//					}
	//					if ((annot.session() != XotsSessionType.NONE) && sessionFactory == null) {
	//						throw new IllegalStateException("Could not create a Fatory for " + annot.session());
	//					}
	//				}
	//
	//				if (context == null) {
	//					context = annot.context();
	//				}
	//				if (context == null) {
	//					context = Context.XOTS;
	//				}
	//
	//				if (scope == null) {
	//					scope = annot.scope();
	//				}
	//				if (scope == null) {
	//					scope = Scope.NONE;
	//				}
	//			}
	//		}
	//
	//		@Override
	//		public final void run() {
	//			initThread();
	//			try {
	//				runNotes();
	//			} finally {
	//				termThread();
	//			}
	//		}
	//
	//		protected void runNotes() {
	//			super.run();
	//		}
	//
	//		protected void termThread() {
	//			Factory.termThread();
	//			lotus.domino.NotesThread.stermThread();
	//		}
	//
	//		protected void initThread() {
	//			lotus.domino.NotesThread.sinitThread();
	//			DominoUtils.setBubbleExceptions(bubbleException);
	//			Factory.initThread();
	//		}
	//
	//		@Override
	//		public boolean isPeriodic() {
	//			return period != 0L;
	//		}
	//
	//		@Override
	//		public long getDelay(final TimeUnit timeUnit) {
	//			return timeUnit.convert(this.time - parent.now(), TimeUnit.NANOSECONDS);
	//		}
	//
	//		@Override
	//		public int compareTo(final Delayed paramT) {
	//			// TODO Auto-generated method stub
	//			return 0;
	//		}
	//
	//		//	public class WrappedCallable<T> implements Callable<T> {
	//		//		private CallableToRunnable<T> wrapper;
	//		//
	//		//		private WrappedCallable(final CallableToRunnable<T> wrapper) {
	//		//			this.wrapper = wrapper;
	//		//		}
	//		//
	//		//		@Override
	//		//		public T call() throws Exception {
	//		//			AbstractDominoRunner.this.run();
	//		//			if (wrapper.exception != null)
	//		//				throw wrapper.exception;
	//		//			return (T) wrapper.result;
	//		//		}
	//		//
	//		//	}
	//		//
	//		//	public <T> Callable<T> asCallable(final Callable<T> typed) {
	//		//		// TODO Auto-generated method stub
	//		//		return new WrappedCallable<T>((CallableToRunnable<T>) getRunnable());
	//		//	}
	//	}
	//
	//	// Oracle/SUN made this package private
	//	private static final long NANO_ORIGIN = System.nanoTime();
	//
	//	public final long now() {
	//		return System.nanoTime() - NANO_ORIGIN;
	//	}
	//
	//	public DominoExecutor(final int corePoolSize) {
	//		super(corePoolSize);//new WorkQueue());
	//		Factory.addShutdownHook(shutdownHook);
	//	}
	//
	//	long triggerTime(final long delay) {
	//		return now() + (delay < 4611686018427387903L ? delay : overflowFree(delay));
	//	}
	//
	//	/**
	//	 * Returns a list of all
	//	 * 
	//	 * @return
	//	 */
	//	public List<Runnable> getRunningTasks() {
	//		System.out.println("Running: " + running);
	//		List<Runnable> ret = new ArrayList<Runnable>();
	//		ret.addAll(getQueue());
	//		ret.addAll(deferred);
	//		return ret;
	//	}
	//
	//	//
	//	//	/* (non-Javadoc)
	//	//	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	//	//	 */
	//	//	@Override
	//	//	public void execute(final Runnable runnable) {
	//	//		//		DominoRunnableWrapper wrappedRunnable;
	//	//		//		Runnable realRunnable;
	//	//		//
	//	//		//		if (runnable instanceof DominoRunnableWrapper) {
	//	//		//			wrappedRunnable = (DominoRunnableWrapper) runnable;
	//	//		//			realRunnable = wrappedRunnable.getRunnable();
	//	//		//		} else {
	//	//		//			realRunnable = runnable;
	//	//		//			wrappedRunnable = wrap(runnable);
	//	//		//		}
	//	//		//		System.out.println("DominoExecutor: EXEC: " + runnable.getClass().getName());
	//	//		//		super.execute(wrappedRunnable);
	//	//		//	return runnable;
	//	//
	//	//		//			Class<? extends Runnable> cls = runnable.getClass();
	//	//		//			boolean isSingleton = cls.isAnnotationPresent(Singleton.class);
	//	//		//			boolean isRejectingRunnable = runnable instanceof RejectingRunnable;
	//	//		//	
	//	//		//			if (isSingleton || isRejectingRunnable) {
	//	//		//				synchronized (this) {
	//	//		//					for (Runnable running : getQueue()) {
	//	//		//						// If the runnable is declared as singleton, we must not enqueue it, if it is already present
	//	//		//						if (isSingleton && running.getClass().equals(cls)) {
	//	//		//							throw new RejectedExecutionException();
	//	//		//						}
	//	//		//	
	//	//		//						// if the runnable implements RejectingRunnable, we check if it may run with others
	//	//		//						if (isRejectingRunnable) {
	//	//		//							if (!((RejectingRunnable) runnable).checkReject(unwrap(running))) {
	//	//		//								throw new RejectedExecutionException();
	//	//		//							}
	//	//		//						}
	//	//		//					}
	//	//		//					super.execute(wrap(runnable));
	//	//		//				}
	//	//		//			} else {
	//	//		//				super.execute(wrap(runnable));
	//	//		//			}
	//	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	//	 */
	//
	//	@Override
	//	protected void beforeExecute(final Thread paramThread, final Runnable paramRunnable) {
	//		super.beforeExecute(paramThread, paramRunnable);
	//		running.put(paramRunnable, "Started at: " + new Date());
	//	}
	//
	//	@Override
	//	protected void afterExecute(final Runnable runnable, final Throwable paramThrowable) {
	//		running.remove(runnable);
	//		super.afterExecute(runnable, paramThrowable);
	//
	//		synchronized (deferred) {
	//			if (!deferred.isEmpty() && !isShutdown()) {
	//				Iterator<Runnable> it = deferred.iterator();
	//				while (it.hasNext()) {
	//					try {
	//						// try to execute the next possible deferred
	//						execute(it.next());
	//						it.remove();
	//					} catch (RejectedExecutionException ex) {
	//
	//					}
	//				}
	//			}
	//		}
	//	}
	//
	//	//	@Override
	//	//	public void execute(final Runnable task) {
	//	//		super.execute(wrap(task));
	//	//	}
	//	//
	//	//	@Override
	//	//	public boolean remove(final Runnable task) {
	//	//		return super.remove(wrap(task));
	//	//	}
	//
	//	//
	//	//	@Override
	//	//	public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
	//	//		// TODO Auto-generated method stub
	//	//		return super.schedule(callable, delay, unit);
	//	//	}
	//	//
	//	//	@Override
	//	//	public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
	//	//		return super.schedule(wrap(command), delay, unit);
	//	//	}
	//	//
	//	//	@Override
	//	//	public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
	//	//		// TODO Auto-generated method stub
	//	//		return super.scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
	//	//	}
	//	//
	//	//	@Override
	//	//	public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
	//	//		// TODO Auto-generated method stub
	//	//		return super.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
	//	//	}
	//	//
	//	//	@Override
	//	//	public <T> Future<T> submit(final Callable<T> task) {
	//	//		// TODO Auto-generated method stub
	//	//		return super.submit(wrap(task));
	//	//	}
	//	//
	//	//	@Override
	//	//	public <T> Future<T> submit(final Runnable task, final T result) {
	//	//		// TODO Auto-generated method stub
	//	//		return super.submit(wrap(task), result);
	//	//	}
	//	//
	//	//	@Override
	//	//	public Future<?> submit(final Runnable task) {
	//	//		// TODO Auto-generated method stub
	//	//		return super.submit(wrap(task));
	//	//	}
	//
	//	//	/**
	//	//	 * Queue the given runnable. In contrast to "execute", queue will ensure that the runnable will run at a later time
	//	//	 * 
	//	//	 * @param runnable
	//	//	 * @return
	//	//	 */
	//	//	public boolean queue(final Runnable runnable) {
	//	//		try {
	//	//			execute(runnable);
	//	//			//schedule(runnable, 30, TimeUnit.SECONDS);
	//	//			return true;
	//	//		} catch (RejectedExecutionException ex) {
	//	//			// if we are shutting down, don't queue anything
	//	//			if (isShutdown()) {
	//	//				throw ex;
	//	//			}
	//	//			// otherwise we will add the runnable to the deferred list
	//	//			synchronized (deferred) {
	//	//				deferred.add(runnable);
	//	//			}
	//	//		}
	//	//		return false;
	//	//	}
	//
	//	//	/* (non-Javadoc)
	//	//	 * @see java.util.concurrent.AbstractExecutorService#newTaskFor(java.util.concurrent.Callable)
	//	//	 */
	//	//	@Override
	//	//	protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
	//	//		return new DominoFutureTask<T>(callable);
	//	//	}
	//	//
	//	//	/* (non-Javadoc)
	//	//	 * @see java.util.concurrent.AbstractExecutorService#newTaskFor(java.lang.Runnable, java.lang.Object)
	//	//	 */
	//	//	@Override
	//	//	protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
	//	//		return new DominoFutureTask<T>(runnable, value);
	//	//	}
	//
	//	//	/* (non-Javadoc)
	//	//	 * @see java.util.concurrent.ThreadPoolExecutor#awaitTermination(long, java.util.concurrent.TimeUnit)
	//	//	 */
	//	//	@Override
	//	//	public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
	//	//		return super.awaitTermination(timeout, unit);
	//	//	}
	//
	//	@Incomplete
	//	//these can't use the IDominoListener interface. Will need to put something new together for that.
	//	public void addListener(final IDominoListener listener) {
	//		if (listeners_ == null) {
	//			listeners_ = new LinkedHashSet<IDominoListener>();
	//		}
	//		listeners_.add(listener);
	//	}
	//
	//	@Incomplete
	//	public IDominoListener removeListener(final IDominoListener listener) {
	//		if (listeners_ != null) {
	//			listeners_.remove(listener);
	//		}
	//		return listener;
	//	}
	//
	//	@Override
	//	public void shutdown() {
	//		Factory.removeShutdownHook(shutdownHook);
	//		super.shutdown();
	//	}
	//
	//	@Override
	//	public List<Runnable> shutdownNow() {
	//		List<Runnable> ret = super.shutdownNow();
	//		Factory.removeShutdownHook(shutdownHook);
	//		return ret;
	//	}
	//
	//	//	protected Runnable wrap(final Runnable runnable) {
	//	//		if (runnable instanceof AbstractDominoRunner) {
	//	//			return runnable;
	//	//		}
	//	//		return new DominoRunner(runnable);
	//	//	}
	//	//
	//	//	protected <T> Callable<T> wrap(final Callable<T> callable) {
	//	//		if (callable instanceof AbstractDominoRunner.WrappedCallable) {
	//	//			return callable;
	//	//		}
	//	//		return new DominoRunner(callable).asCallable(callable);
	//	//	}
	//
	//	protected <V> RunnableScheduledFuture<V> queue(final RunnableScheduledFuture<V> future) {
	//		if (isShutdown()) {
	//			throw new RejectedExecutionException();
	//		}
	//		if (getPoolSize() < getCorePoolSize()) {
	//			prestartCoreThread();
	//		}
	//		super.getQueue().add(future);
	//		return future;
	//	}
	//
	//	@Override
	//	public <V extends Object> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit timeUnit) {
	//		return queue(new DominoFutureTask<V>(callable, delay, timeUnit));
	//
	//	};
	//
	//	@Override
	//	public ScheduledFuture<?> schedule(final Runnable runnable, final long delay, final TimeUnit timeUnit) {
	//		return queue(new DominoFutureTask(runnable, null, delay, timeUnit));
	//	};
	//
	//	@Override
	//	public ScheduledFuture<?> scheduleAtFixedRate(final Runnable runnable, final long delay, final long period, final TimeUnit timeUnit) {
	//		return queue(new DominoFutureTask(runnable, null, delay, period, timeUnit, true));
	//	};
	//
	//	@Override
	//	public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable runnable, final long delay, final long period, final TimeUnit timeUnit) {
	//		return queue(new DominoFutureTask(runnable, null, delay, period, timeUnit, false));
	//	};

}
