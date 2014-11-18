/**
 * 
 */
package org.openntf.domino.thread;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javolution.util.FastSet;

import lotus.domino.NotesThread;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.model.Context;
import org.openntf.domino.thread.model.Scope;
import org.openntf.domino.thread.model.XotsSessionType;
import org.openntf.domino.thread.model.XotsTasklet;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * A ThreadPoolExecutor for Domino runnables. It sets up a shutdown hook for proper termination. There should be maximum one instance of
 * DominoExecutor, otherwise concurrency won't work
 * 
 * @author Nathan T. Freeman
 */
@Incomplete
public abstract class AbstractDominoExecutor extends ScheduledThreadPoolExecutor {
	public enum TaskState {
		QUEUED, RUNNING, DONE
	}

	private static final Logger log_ = Logger.getLogger(AbstractDominoExecutor.class.getName());

	protected Set<DominoFutureTask<?>> tasks = new FastSet<DominoFutureTask<?>>().atomic();

	private Set<IDominoListener> listeners_;

	private static final AtomicLong sequencer = new AtomicLong(0L);

	// the shutdown-hook for proper termination
	protected Runnable shutdownHook = new Runnable() {
		@Override
		public void run() {
			shutdownNow();
		}
	};

	public AbstractDominoExecutor(final int corePoolSize) {
		super(corePoolSize);//new WorkQueue());
		Factory.addShutdownHook(shutdownHook);
	}

	/**
	 * A Wrapper for runnables
	 * 
	 * @author Roland Praml, FOCONIS AG
	 */
	public class DominoFutureTask<T> extends FutureTask<T> implements Delayed, RunnableScheduledFuture<T> {

		// the period. Values > 0: fixed rate. Values < 0: fixed delay
		private Object wrappedObject;
		private long period;

		// The next runtime;
		private long time = 0;

		private long sequenceNumber = sequencer.incrementAndGet();
		private TaskState state = TaskState.QUEUED;

		public synchronized void setState(final TaskState s) {
			state = s;
		}

		public synchronized TaskState getState() {
			return state;
		}

		public DominoFutureTask(final WrappedCallable<T> callable, final long time) {
			super(callable);
			this.period = 0;
			this.time = time;

			wrappedObject = callable.getWrappedObject();
		}

		public DominoFutureTask(final WrappedRunnable runnable, final T result, final long time) {
			super(runnable, result);
			this.period = 0;
			this.time = time;

			wrappedObject = runnable.getWrappedObject();
		}

		public DominoFutureTask(final WrappedRunnable runnable, final T result, final long time, final long period) {
			super(runnable, result);
			this.period = period;
			this.time = time;

			wrappedObject = runnable.getWrappedObject();
		}

		@Override
		public boolean isPeriodic() {
			return period != 0L;
		}

		private void runPeriodic() {
			boolean success = super.runAndReset();

			if (success && (!isShutdown() || ((getContinueExistingPeriodicTasksAfterShutdownPolicy()) && (!isTerminating())))) {
				long l = this.period;
				if (l > 0L) {
					this.time += l;
				} else
					this.time = triggerTime(-l);
				getQueue().add(this);
			}
		}

		@Override
		public final void run() {
			if (isPeriodic()) {
				runPeriodic();
			} else {
				super.run();
			}
		}

		@Override
		public long getDelay(final TimeUnit timeUnit) {
			return timeUnit.convert(this.time - now(), TimeUnit.NANOSECONDS);
		}

		@Override
		public int compareTo(final Delayed paramT) {
			if (paramT == this)
				return 0;
			if ((paramT instanceof DominoFutureTask)) {
				DominoFutureTask<?> localScheduledFutureTask = (DominoFutureTask<?>) paramT;
				long l2 = this.time - localScheduledFutureTask.time;
				if (l2 < 0L)
					return -1;
				if (l2 > 0L)
					return 1;
				if (this.sequenceNumber < localScheduledFutureTask.sequenceNumber) {
					return -1;
				}
				return 1;
			}
			long l1 = getDelay(TimeUnit.NANOSECONDS) - paramT.getDelay(TimeUnit.NANOSECONDS);

			return l1 < 0L ? -1 : l1 == 0L ? 0 : 1;
		}

		@Override
		public String toString() {
			// TODO increment Period/Time 
			return sequenceNumber + "State: " + getState() + " Task: " + wrappedObject;
		}

	}

	protected abstract class WrappedAbstract<T> {
		protected T wrappedObject;

		protected Scope scope;
		protected Context context;
		protected ISessionFactory sessionFactory;
		protected boolean bubbleException;

		/**
		 * Determines the sessionType under which the current runnable should run. The first non-null value of the following list is
		 * returned
		 * <ul>
		 * <li>If the runnable implements <code>IDominoRunnable</code>: result of <code>getSessionType</code></li>
		 * <li>the value of {@link SessionType} Annotation</li>
		 * <li>DominoSessionType.DEFAULT</li>
		 * </ul>
		 * 
		 * @param runnable
		 *            the runnable to determine the DominoSessionType
		 * @return the DominoSessionType
		 */
		protected WrappedAbstract(final T runnable) {
			wrappedObject = runnable;
			// some security checks...
			if (runnable instanceof NotesThread) {
				// RPr: I'm not sure if this should be allowed anyway...
				throw new IllegalStateException("Cannot wrap the NotesThread " + runnable.getClass().getName() + " into a DominoRunner");
			}
			if (runnable instanceof DominoFutureTask) {
				// RPr: don't know if this is possible
				throw new IllegalStateException("Cannot wrap the WrappedCallable " + runnable.getClass().getName() + " into a DominoRunner");
			}
			if (runnable instanceof WrappedAbstract) {
				// RPr: don't know if this is possible
				throw new IllegalStateException("Cannot wrap the WrappedCallable " + runnable.getClass().getName() + " into a DominoRunner");
			}
			if (runnable instanceof XotsTasklet.Interface) {
				XotsTasklet.Interface dominoRunnable = (XotsTasklet.Interface) runnable;
				sessionFactory = dominoRunnable.getSessionFactory();
				scope = dominoRunnable.getScope();
				context = dominoRunnable.getContext();
			}
			bubbleException = DominoUtils.getBubbleExceptions();
			XotsTasklet annot = runnable.getClass().getAnnotation(XotsTasklet.class);
			if (annot != null) {
				System.out.println("XotsTasklet.session " + annot.session());
				if (sessionFactory == null) {
					switch (annot.session()) {
					case CLONE:
						sessionFactory = Factory.getSessionFactory(SessionType.CURRENT);
						break;
					case CLONE_FULL_ACCESS:
						sessionFactory = Factory.getSessionFactory(SessionType.CURRENT_FULL_ACCESS);
						break;

					case FULL_ACCESS:
						sessionFactory = Factory.getSessionFactory(SessionType.FULL_ACCESS);
						break;

					case NATIVE:
						sessionFactory = Factory.getSessionFactory(SessionType.NATIVE);
						break;

					case NONE:
						sessionFactory = null;
						break;

					case SIGNER:
						sessionFactory = Factory.getSessionFactory(SessionType.SIGNER);
						break;

					case SIGNER_FULL_ACCESS:
						sessionFactory = Factory.getSessionFactory(SessionType.SIGNER_FULL_ACCESS);
						break;

					case TRUSTED:
						sessionFactory = Factory.getSessionFactory(SessionType.TRUSTED);
						break;

					default:
						break;
					}
					if ((annot.session() != XotsSessionType.NONE) && sessionFactory == null) {
						throw new IllegalStateException("Could not create a Fatory for " + annot.session());
					}
				}

				if (context == null) {
					context = annot.context();
				}
				if (context == null) {
					context = Context.XOTS;
				}

				if (scope == null) {
					scope = annot.scope();
				}
				if (scope == null) {
					scope = Scope.NONE;
				}
			}
		}

		public T getWrappedObject() {
			return wrappedObject;
		}
	}

	protected abstract class WrappedCallable<V> extends WrappedAbstract<Callable<V>> implements Callable<V> {

		public WrappedCallable(final Callable<V> runnable) {
			super(runnable);
			this.wrappedObject = runnable;
		}

	}

	protected abstract class WrappedRunnable extends WrappedAbstract<Runnable> implements Runnable {

		public WrappedRunnable(final Runnable wrappedObject) {
			super(wrappedObject);
		}

	}

	// Oracle/SUN made this package private
	private static final long NANO_ORIGIN = System.nanoTime();

	protected final long now() {
		return System.nanoTime() - NANO_ORIGIN;
	}

	protected long triggerTime(final long delay) {
		return now() + (delay < 4611686018427387903L ? delay : overflowFree(delay));
	}

	protected long triggerTime(final long delay, final TimeUnit timeUnit) {
		return triggerTime(timeUnit.toNanos(delay < 0L ? 0L : delay));
	}

	private long overflowFree(long paramLong) {
		Delayed localDelayed = (Delayed) super.getQueue().peek();
		if (localDelayed != null) {
			long l = localDelayed.getDelay(TimeUnit.NANOSECONDS);
			if ((l < 0L) && (paramLong - l < 0L))
				paramLong = Long.MAX_VALUE + l;
		}
		return paramLong;
	}

	// --- end duplicate stuff

	/**
	 * Returns a list of all
	 * 
	 * @return
	 */
	public List<DominoFutureTask> getRunningTasks() {
		ArrayList<DominoFutureTask> ret = new ArrayList<DominoFutureTask>();

		for (DominoFutureTask task : tasks) {
			ret.add(task);
		}
		return ret;
	}

	@Override
	protected void beforeExecute(final Thread paramThread, final Runnable runnable) {
		super.beforeExecute(paramThread, runnable);
		//running.add((RunnableScheduledFuture<?>) runnable);
		//running.put(paramRunnable, "Started at: " + new Date());
		if (runnable instanceof DominoFutureTask) {
			DominoFutureTask<?> task = (DominoFutureTask<?>) runnable;
			task.setState(TaskState.RUNNING);
		}
	}

	@Override
	protected void afterExecute(final Runnable runnable, final Throwable paramThrowable) {
		super.afterExecute(runnable, paramThrowable);

		if (runnable instanceof DominoFutureTask) {
			DominoFutureTask<?> task = (DominoFutureTask<?>) runnable;
			if (task.isDone()) {
				task.setState(TaskState.DONE);
				tasks.remove(task);
			} else {
				task.setState(TaskState.QUEUED);
			}
		}

	}

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

	protected <V> RunnableScheduledFuture<V> queue(final RunnableScheduledFuture<V> future) {
		if (isShutdown()) {
			throw new RejectedExecutionException();
		}
		if (getPoolSize() < getCorePoolSize()) {
			prestartCoreThread();
		}
		if (future instanceof DominoFutureTask) {
			tasks.add((DominoFutureTask) future);
		}
		super.getQueue().add(future);
		return future;
	}

	@Override
	public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit timeUnit) {
		return queue(new DominoFutureTask<V>(wrap(callable), triggerTime(delay, timeUnit)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ScheduledFuture<?> schedule(final Runnable runnable, final long delay, final TimeUnit timeUnit) {
		return queue(new DominoFutureTask(wrap(runnable), null, triggerTime(delay, timeUnit)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(final Runnable runnable, final long delay, final long period, final TimeUnit timeUnit) {
		return queue(new DominoFutureTask(wrap(runnable), null, triggerTime(delay, timeUnit), triggerTime(period, timeUnit)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable runnable, final long delay, final long period, final TimeUnit timeUnit) {
		return queue(new DominoFutureTask(wrap(runnable), null, triggerTime(delay, timeUnit), triggerTime(-period, timeUnit)));
	}

	protected abstract <V> WrappedCallable<V> wrap(Callable<V> inner);

	protected abstract WrappedRunnable wrap(Runnable inner);
}
