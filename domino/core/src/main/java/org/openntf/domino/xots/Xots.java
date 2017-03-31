package org.openntf.domino.xots;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.openntf.domino.extmgr.EMBridgeMessageQueue;
import org.openntf.domino.thread.AbstractDominoExecutor;
import org.openntf.domino.thread.AbstractDominoExecutor.DominoFutureTask;
import org.openntf.domino.thread.Scheduler;
import org.openntf.domino.thread.XotsExecutorService;
import org.openntf.domino.utils.Factory;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
public class Xots {
	;
	public static Comparator<DominoFutureTask<?>> TASKS_BY_ID = new Comparator<DominoFutureTask<?>>() {
		@Override
		public int compare(final DominoFutureTask<?> o1, final DominoFutureTask<?> o2) {
			if (o1.sequenceNumber < o2.sequenceNumber) {
				return -1;
			} else if (o1.sequenceNumber == o2.sequenceNumber) {
				return 0;
			} else {
				return 1;
			}
		}
	};
	//private Set<TaskletDefinition> tasklets_ = new HashSet<TaskletDefinition>();

	// This is our Threadpool that will execute all Runnables
	private static AbstractDominoExecutor executor_;

	//	public void addListener(final IDominoListener listener) {
	//		executor_.addListener(listener);
	//	}
	//
	//	public IDominoListener removeListener(final IDominoListener listener) {
	//		return executor_.removeListener(listener);
	//	}

	private Xots() {

	}

	private static XotsExecutorService getService() {
		if (!isStarted()) {
			throw new IllegalStateException("Xots is not started");
		}
		return executor_;
	}

	public static void execute(final Runnable r) {
		getService().execute(r);
	}

	public static int getActiveThreadCount() {
		return executor_.getActiveCount();
	}

	public static BlockingQueue<Runnable> getQueue() {
		return executor_.getQueue();
	}

	public static void remove(final Runnable task) {
		executor_.remove(task);
	}

	public static <T> Future<T> schedule(final Callable<T> task, final Scheduler scheduler) {
		return executor_.schedule(task, scheduler);
	}

	public static Future schedule(final Runnable task, final Scheduler scheduler) {
		return executor_.schedule(task, scheduler);
	}

	public static <T> Future<T> schedule(final Callable<T> task, final long delay, final TimeUnit unit) {
		return getService().schedule(task, delay, unit);
	}

	public static Future schedule(final Runnable task, final long delay, final TimeUnit unit) {
		return getService().schedule(task, delay, unit);
	}

	public static Future submit(final Runnable task) {
		return getService().submit(task);
	}

	public static <T> Future<T> submit(final Callable<T> task) {
		return getService().submit(task);
	}

	public static <T> Future<T> submit(final Runnable task, final T result) {
		return getService().submit(task, result);
	}

	public static <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return getService().invokeAll(tasks);
	}

	public static void runTasklet(final String moduleName, final String className, final Object... ctorArgs) {
		getService().runTasklet(moduleName, className, ctorArgs);
	}

	public static List<DominoFutureTask<?>> getTasks(final Comparator<DominoFutureTask<?>> comparator) {
		if (!isStarted()) {
			return Collections.emptyList();
		}
		return executor_.getTasks(comparator);
	}

	/**
	 * Start the XOTS with the given Executor
	 */
	public static synchronized void start(final AbstractDominoExecutor executor) throws IllegalStateException {
		if (isStarted()) {
			throw new IllegalStateException("XotsDaemon is already started");
		}
		try {
			executor_ = executor;
			EMBridgeMessageQueue.start();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	/**
	 * Tests if the XotsDaemon is started
	 * 
	 */
	public static synchronized boolean isStarted() {
		return executor_ != null;
	}

	public static synchronized void stop(int wait) {
		if (isStarted()) {
			//			System.out.println("Stopping XPages OSGi Tasklet Service...");
			//TODO Re-enable post release
			EMBridgeMessageQueue.stop();
			executor_.shutdown();
			long running;
			try {
				while ((running = executor_.getActiveCount()) > 0 && wait-- > 0) {
					Factory.println(Xots.class, "There are " + running + " tasks running... waiting " + wait + " seconds.");
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
			}

			if (executor_.getActiveCount() > 0) {
				Factory.println(Xots.class, "The following Threads did not terminate gracefully:");
				for (DominoFutureTask<?> task : executor_.getTasks(null)) {
					Factory.println(Xots.class, "* " + task);
				}

			}

			try {
				for (int i = 60; i > 0; i -= 10) {
					if (executor_.getActiveCount() > 0) {
						Factory.println(Xots.class, "Trying to interrupt them and waiting again " + i + " seconds.");
						executor_.shutdownNow();
					}
					if (executor_.awaitTermination(10, TimeUnit.SECONDS)) {
						executor_ = null;
						Factory.println(Xots.class, " XPages OSGi Tasklet Service stopped.");
						return;
					}
				}
			} catch (InterruptedException e) {
			}
			Factory.println(Xots.class, "WARNING: Could not stop  XPages OSGi Tasklet Service!");
		} else {
			Factory.println(Xots.class, " XPages OSGi Tasklet Service not running");
		}
	}

	//
	//	// ---- delegate methods
	//
	///**
	//	 * Registers a new tasklet for periodic execution
	//	 * @param moduleName
	//	 *            the ModuleName (i.e. DatabaseName)
	//	 * @param className
	//	 *            the ClassName. (Must be annotated with {@link XotsTasklet
	//	 * @param onEvent
	//	 * String array with events
	//	 * @return
	//	 */
	//	public static Future<?> registerTasklet(final String moduleName, final String className, final String... cron) {
	//		return executor_.registerTasklet(moduleName, className, cron);
	//
	//	}
}
