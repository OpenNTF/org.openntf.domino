/**
 * 
 */
package org.openntf.domino.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.utils.Factory;

/**
 * A ThreadPoolExecutor for Domino runnables. It sets up a shutdown hook for proper termination.
 * 
 * 
 * The <code>DominoExecutor.schedule</code> schedules a Runnable or a Callable for execution. The Runnable/Callable is wrapped in a
 * {@link WrappedRunnable} (i.e. {@link WrappedCallable}) which is responsible for proper Thread setUp/tearDown.<br>
 * 
 * The Wrapped Runnable is wrapped again in a {@link DominoFutureTask} which observes the Runnable and keeps track of some status
 * information.<br>
 * 
 * 
 * <b>This class should not be used directly. Use XotsDaemon.getInstance() instead</b>
 * 
 * @author Nathan T. Freeman
 * @author Roland Praml
 */
@Incomplete
public abstract class AbstractDominoExecutor extends ScheduledThreadPoolExecutor implements XotsExecutorService {
	public enum TaskState {
		/** The Task is Queued and will be executed next */

		QUEUED,

		/** The Task is sleeping and will be executed further */
		SLEEPING,

		/** The Task is currently running */
		RUNNING,

		/** The Task is finished */
		DONE,

		ERROR
	}

	private static final Logger log_ = Logger.getLogger(AbstractDominoExecutor.class.getName());

	/** This list contains ALL tasks */
	protected Map<Long, DominoFutureTask<?>> tasks = new FastMap<Long, DominoFutureTask<?>>().atomic();

	private Set<IDominoListener> listeners_;

	private static final AtomicLong sequencer = new AtomicLong(0L);

	// the shutdown-hook for proper termination
	protected Runnable shutdownHook = new Runnable() {
		@Override
		public void run() {
			shutdownNow();
			Factory.removeShutdownHook(shutdownHook);
			try {
				for (int i = 5; i > 0; i--) {
					if (!awaitTermination(10, TimeUnit.SECONDS)) {
						if (i > 0) {
							Factory.println("Could not terminate java threads... Still waiting " + (i * 10) + " seconds");
						} else {
							Factory.println("Could not terminate java threads... giving up. Server may crash now.");
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * Creates a new {@link AbstractDominoExecutor}. Specify the
	 * 
	 * @param corePoolSize
	 */
	public AbstractDominoExecutor(final int corePoolSize) {
		super(corePoolSize);
		Factory.addShutdownHook(shutdownHook);
	}

	/**
	 * A FutureTask for {@link WrappedCallable}s and {@link WrappedRunnable}s. It is nearly identical with
	 * {@link ScheduledThreadPoolExecutor.ScheduledFutureTask} But ScheduledFutureTask is private, so that we cannot inherit
	 * 
	 * @author Roland Praml, FOCONIS AG
	 */
	public class DominoFutureTask<T> extends FutureTask<T> implements Delayed, RunnableScheduledFuture<T>, Observer {

		// the period. Values > 0: fixed rate. Values < 0: fixed delay
		private IWrappedTask wrappedTask;
		//		private long period;

		// The next runtime;
		//		private long time = 0;

		private Scheduler scheduler;
		public long sequenceNumber = sequencer.incrementAndGet();
		private TaskState state = TaskState.QUEUED;
		private Object objectState;
		private Thread runner;

		/**
		 * Sets the new state of this Thread
		 * 
		 * @param s
		 */
		synchronized void setState(final TaskState s) {
			state = s;
		}

		/**
		 * Returns the State of this task
		 * 
		 * @return the {@link TaskState}
		 */
		public synchronized TaskState getState() {
			return state;
		}

		/**
		 * Returns the ID for this task
		 * 
		 * @return the Id
		 */
		public long getId() {
			return sequenceNumber;
		}

		public DominoFutureTask(final IWrappedCallable<T> callable, final Scheduler scheduler) {
			super(callable);
			this.wrappedTask = callable;
			this.scheduler = scheduler;
			// the wrappedCallable itself is not observable, but maybe its wrapped object
			callable.addObserver(this);
		}

		public DominoFutureTask(final IWrappedRunnable runnable, final T result, final Scheduler scheduler) {
			super(runnable, result);
			this.wrappedTask = runnable;
			this.scheduler = scheduler;
			// the wrappedCallable itself is not observable, but maybe its wrapped object
			runnable.addObserver(this);
		}

		@Override
		public boolean isPeriodic() {
			return scheduler.isPeriodic();
		}

		private void runPeriodic() {
			boolean success = super.runAndReset();

			if (success && (!isShutdown() || ((getContinueExistingPeriodicTasksAfterShutdownPolicy()) && (!isTerminating())))) {
				scheduler.computeNextExecutionTime();
				getQueue().add(this);
			}
		}

		@Override
		public final void run() {
			this.runner = Thread.currentThread();
			try {
				if (isPeriodic()) {
					runPeriodic();
				} else {
					super.run();
				}
			} finally {
				try {
					this.get();
				} catch (Throwable t) {
					log_.log(Level.WARNING, getWrappedTask().getDescription(), t);
				}
				synchronized (this) {
					this.runner = null;
				}
			}
		}

		/**
		 * Cancels the FutureTask. Tries also to cancel the inner task. If this is a periodic task, it will stop
		 */
		@Override
		public boolean cancel(final boolean mayInterruptIfRunning) {
			wrappedTask.stop();
			if (super.cancel(mayInterruptIfRunning)) {
				return true;
			}
			if (mayInterruptIfRunning) {
				// unfortunately subsequent calls to cancel will return false.
				synchronized (this) {
					if (runner != null)
						runner.interrupt();
					return true;
				}
			}
			return false;
		}

		@Override
		public long getDelay(final TimeUnit timeUnit) {
			return scheduler.getDelay(timeUnit);
		}

		public long getNextExecutionTime(final TimeUnit timeUnit) {
			return scheduler.getNextExecutionTime(timeUnit);
		}

		@Override
		public int compareTo(final Delayed other) {
			long delta = getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS);

			if (delta < 0L)
				return -1;
			if (delta > 0L)
				return 1;
			//delta == 0 - compare sequence numbers
			if ((other instanceof DominoFutureTask)) {
				if (this.sequenceNumber < ((DominoFutureTask<?>) other).sequenceNumber) {
					return -1;
				}
				return 1;
			}
			return 0;
		}

		@Override
		public String toString() {
			// TODO increment Period/Time 
			return sequenceNumber + "State: " + getState() + " Task: " + wrappedTask + " objectState: " + objectState;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void update(final Observable arg0, final Object arg1) {
			objectState = arg1;
		}

		public IWrappedTask getWrappedTask() {
			return wrappedTask;
		}

	}

	// Oracle/SUN made this package private

	protected final long now() {
		return System.nanoTime();
	}

	protected long triggerTime(final long delay) {
		return now() + (delay < 4611686018427387903L ? delay : overflowFree(delay));
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
	 * Returns a list of all tasks sortet by next execution time or Sequence number
	 * 
	 * @return
	 */
	@Override
	public List<DominoFutureTask<?>> getTasks(final Comparator<DominoFutureTask<?>> comparator) {
		ArrayList<DominoFutureTask<?>> ret = new ArrayList<DominoFutureTask<?>>();

		for (DominoFutureTask<?> task : tasks.values()) {
			ret.add(task);
		}
		if (comparator == null) {
			Collections.sort(ret);
		} else {
			Collections.sort(ret, comparator);
		}
		return ret;
	}

	/**
	 * Retrun a Task with the given ID. May return null if the task is no longer in queue.
	 * 
	 * @param id
	 * @return
	 */
	public DominoFutureTask<?> getTask(final long id) {
		return tasks.get(id);
	}

	@Override
	protected void beforeExecute(final Thread thread, final Runnable runnable) {
		super.beforeExecute(thread, runnable);
		if (runnable instanceof DominoFutureTask) {
			DominoFutureTask<?> task = (DominoFutureTask<?>) runnable;
			thread.setName("XOTS: " + task.getWrappedTask().getDescription() + " - " + new Date());
			task.setState(TaskState.RUNNING);
		} else {
			thread.setName("XOTS: #" + thread.getId());
		}
	}

	@Override
	protected void afterExecute(final Runnable runnable, final Throwable error) {
		super.afterExecute(runnable, error);

		if (runnable instanceof DominoFutureTask) {
			DominoFutureTask<?> task = (DominoFutureTask<?>) runnable;
			if (task.isDone()) {
				if (error == null) {
					task.setState(TaskState.DONE);
				} else {
					task.setState(TaskState.ERROR);
				}
				tasks.remove(task.sequenceNumber);
			}
		}

	}

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

	//	@Override
	//	public void shutdown() {
	//		//Factory.removeShutdownHook(shutdownHook);
	//		super.shutdown();
	//	}
	//
	//	@Override
	//	public List<Runnable> shutdownNow() {
	//		List<Runnable> ret = super.shutdownNow();
	//		//Factory.removeShutdownHook(shutdownHook);
	//		return ret;
	//	}

	protected <V> RunnableScheduledFuture<V> queue(final RunnableScheduledFuture<V> future) {
		if (isShutdown()) {
			throw new RejectedExecutionException();
		}
		if (getPoolSize() < getCorePoolSize()) {
			prestartCoreThread();
		}

		if (future instanceof DominoFutureTask) {
			DominoFutureTask<?> dft = (DominoFutureTask<?>) future;
			tasks.put(dft.sequenceNumber, dft);
			if (dft.getDelay(TimeUnit.NANOSECONDS) > 0) {
				dft.setState(TaskState.SLEEPING);
			}
		}
		super.getQueue().add(future);
		return future;
	}

	@Override
	public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit timeUnit) {
		return queue(new DominoFutureTask<V>(wrap(callable), new PeriodicScheduler(delay, 0L, timeUnit)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ScheduledFuture<?> schedule(final Runnable runnable, final long delay, final TimeUnit timeUnit) {
		return queue(new DominoFutureTask(wrap(runnable), null, new PeriodicScheduler(delay, 0L, timeUnit)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(final Runnable runnable, final long delay, final long period, final TimeUnit timeUnit) {
		if (period <= 0) {
			throw new IllegalStateException("period must be > 0");
		}
		return queue(new DominoFutureTask(wrap(runnable), null, new PeriodicScheduler(delay, period, timeUnit)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable runnable, final long delay, final long period, final TimeUnit timeUnit) {
		if (period <= 0) {
			throw new IllegalStateException("period must be > 0");
		}
		return queue(new DominoFutureTask(wrap(runnable), null, new PeriodicScheduler(delay, -period, timeUnit)));
	}

	protected abstract <V> IWrappedCallable<V> wrap(Callable<V> inner);

	protected abstract IWrappedRunnable wrap(Runnable inner);

	protected abstract IWrappedCallable<?> wrap(final String moduleName, final String className, final Object... ctorArgs);

	@Override
	public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final Scheduler scheduler) {
		return queue(new DominoFutureTask<V>(wrap(callable), scheduler));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ScheduledFuture<?> schedule(final Runnable runnable, final Scheduler scheduler) {
		return queue(new DominoFutureTask(wrap(runnable), null, scheduler));
	}

	@Override
	public ScheduledFuture<?> scheduleTasklet(final String moduleName, final String className, final Scheduler scheduler,
			final Object... ctorArgs) {
		return queue(new DominoFutureTask(wrap(moduleName, className, ctorArgs), scheduler));
	}

	@Override
	public ScheduledFuture<?> runTasklet(final String moduleName, final String className, final Object... ctorArgs) {
		return queue(new DominoFutureTask(wrap(moduleName, className, ctorArgs), new PeriodicScheduler(0L, 0L, TimeUnit.NANOSECONDS)));
	}

}
