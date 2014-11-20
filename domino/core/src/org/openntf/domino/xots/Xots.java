package org.openntf.domino.xots;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openntf.domino.thread.AbstractDominoExecutor.DominoFutureTask;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
public class Xots {

	;

	//private Set<TaskletDefinition> tasklets_ = new HashSet<TaskletDefinition>();

	// This is our Threadpool that will execute all Runnables
	private static DominoExecutor executor_;

	//	public void addListener(final IDominoListener listener) {
	//		executor_.addListener(listener);
	//	}
	//
	//	public IDominoListener removeListener(final IDominoListener listener) {
	//		return executor_.removeListener(listener);
	//	}

	private Xots() {
		super();
	}

	public static ScheduledExecutorService getInstance() {
		if (!isStarted()) {
			throw new IllegalStateException("XotsService is not started");
		}
		return executor_;
	}

	/**
	 * 
	 * @param sortByExecDate
	 * @return
	 */
	public static List<DominoFutureTask<?>> getTasks(final boolean sortByExecDate) {
		if (!isStarted())
			return Collections.emptyList();
		return executor_.getTasks(sortByExecDate);
	}

	/**
	 * Start the XOTS with the given Executor
	 */
	public static synchronized void start(final DominoExecutor executor) {
		if (isStarted())
			throw new IllegalStateException("XotsDaemon is already started");
		Factory.println(Xots.class, "Starting XotsService with " + executor.getCorePoolSize() + " core threads.");

		executor_ = executor;
	}

	/**
	 * Tests if the XotsDaemon is started
	 * 
	 * @return
	 */
	public static synchronized boolean isStarted() {
		return executor_ != null;
	}

	public static synchronized void stop(int wait) {
		if (isStarted()) {
			Factory.println(Xots.class, "Stopping XotsDaemon...");

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
				Factory.println(Xots.class, "he following Threads did not terminate gracefully:");
				for (DominoFutureTask<?> task : executor_.getTasks(false)) {
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
						Factory.println(Xots.class, "XotsDaemon stopped.");
						return;
					}
				}
			} catch (InterruptedException e) {
			}
			Factory.println(Xots.class, "WARNING: Could not stop XotsDaemon!");
		} else {
			Factory.println(Xots.class, "XotsDaemon not running");
		}
	}

	// ---- delegate methods

	/**
	 * Submits the callable to the executor for immediate execution
	 * 
	 * @param callable
	 * @return
	 */
	public static <T> Future<T> queue(final Callable<T> callable) {
		return getInstance().submit(callable);
	}

	/**
	 * Submits the runnable to the executor for immediate execution
	 * 
	 * @param callable
	 * @return
	 */
	public static Future<?> queue(final Runnable callable) {
		return getInstance().submit(callable);
	}

/**
	 * Registers a new tasklet for periodic execution
	 * @param moduleName
	 *            the ModuleName (i.e. DatabaseName)
	 * @param className
	 *            the ClassName. (Must be annotated with {@link XotsTasklet
	 * @param onEvent
	 * String array with events
	 * @return
	 */
	public static Future<?> registerTasklet(final String moduleName, final String className, final String... cron) {
		//CronExpression
		return null;

	}
}
