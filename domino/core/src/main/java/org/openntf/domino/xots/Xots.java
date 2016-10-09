package org.openntf.domino.xots;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openntf.domino.extmgr.EMBridgeMessageQueue;
import org.openntf.domino.thread.AbstractDominoExecutor;
import org.openntf.domino.thread.AbstractDominoExecutor.DominoFutureTask;
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
	private static EMBridgeMessageQueue eventListener_;

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

	public static XotsExecutorService getService() {
		if (!isStarted()) {
			throw new IllegalStateException("Xots is not started");
		}
		return executor_;
	}

	public static List<DominoFutureTask<?>> getTasks(final Comparator<DominoFutureTask<?>> comparator) {
		if (!isStarted())
			return Collections.emptyList();
		return executor_.getTasks(comparator);
	}

	/**
	 * Start the XOTS with the given Executor
	 */
	public static synchronized void start(final AbstractDominoExecutor executor) throws IllegalStateException {
		if (isStarted())
			throw new IllegalStateException("XotsDaemon is already started");
		Factory.println(Xots.class, "Starting XPages OSGi Tasklet Service with " + executor.getCorePoolSize() + " core threads.");

		executor_ = executor;
		eventListener_ = new EMBridgeMessageQueue();
		executor_.execute(eventListener_);

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
			Factory.println(Xots.class, "Stopping XPages OSGi Tasklet Service...");
			eventListener_.stop();
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
