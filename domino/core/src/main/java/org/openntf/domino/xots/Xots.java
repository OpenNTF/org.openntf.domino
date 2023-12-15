/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.xots;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
@SuppressWarnings("nls")
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

	public static XotsExecutorService getService() {
		if (!isStarted()) {
			throw new IllegalStateException("Xots is not started");
		}
		return executor_;
	}

	public static void execute(final Runnable r) {
		if (isStarted()) {
			getService().execute(r);
		} else {
			runnableCache_.add(r);
		}
	}

	public static int getActiveThreadCount() {
		return executor_.getActiveCount();
	}

	public static BlockingQueue<Runnable> getQueue() {
		return executor_.getQueue();
	}

	public static void remove(final Runnable task) {
		if (isStarted()) {
			executor_.remove(task);
		} else {
			runnableCache_.remove(task);
		}
	}

	private static Map<Callable<?>, Scheduler> scheduleCallableCache_ = new LinkedHashMap<Callable<?>, Scheduler>();

	public static <T> Future<T> schedule(final Callable<T> task, final Scheduler scheduler) {
		if (isStarted()) {
			return getService().schedule(task, scheduler);
		} else {
			scheduleCallableCache_.put(task, scheduler);
			return null;
		}
	}

	private static Map<Runnable, Scheduler> scheduleRunnableCache_ = new LinkedHashMap<Runnable, Scheduler>();

	public static Future<?> schedule(final Runnable task, final Scheduler scheduler) {
		if (isStarted()) {
			return getService().schedule(task, scheduler);
		} else {
			scheduleRunnableCache_.put(task, scheduler);
			return null;
		}
	}

	private static Map<Callable<?>, Long> scheduleCallableDelayCache_ = new LinkedHashMap<Callable<?>, Long>();
	private static Map<Callable<?>, TimeUnit> scheduleCallableTimeUnitCache_ = new LinkedHashMap<Callable<?>, TimeUnit>();

	public static <T> Future<T> schedule(final Callable<T> task, final long delay, final TimeUnit unit) {
		if (isStarted()) {
			return getService().schedule(task, delay, unit);
		} else {
			scheduleCallableDelayCache_.put(task, delay);
			scheduleCallableTimeUnitCache_.put(task, unit);
			return null;
		}
	}

	private static Map<Runnable, Long> scheduleRunnableDelayCache_ = new LinkedHashMap<Runnable, Long>();
	private static Map<Runnable, TimeUnit> scheduleRunnableTimeUnitCache_ = new LinkedHashMap<Runnable, TimeUnit>();

	public static Future<?> schedule(final Runnable task, final long delay, final TimeUnit unit) {
		if (isStarted()) {
			return getService().schedule(task, delay, unit);
		} else {
			scheduleRunnableDelayCache_.put(task, delay);
			scheduleRunnableTimeUnitCache_.put(task, unit);
			return null;
		}
	}

	private static List<Runnable> runnableCache_ = new ArrayList<Runnable>();

	public static Future<?> submit(final Runnable task) {
		if (isStarted()) {
			return getService().submit(task);
		} else {
			runnableCache_.add(task);
			return null;
		}
	}

	private static List<Callable<?>> callableCache_ = new ArrayList<Callable<?>>();

	public static <T> Future<T> submit(final Callable<T> task) {
		if (isStarted()) {
			return getService().submit(task);
		} else {
			callableCache_.add(task);
			return null;
		}
	}

	private static Map<Runnable, Object> runnableResultCache_ = new LinkedHashMap<Runnable, Object>();

	public static <T> Future<T> submit(final Runnable task, final T result) {
		if (isStarted()) {
			return getService().submit(task, result);
		} else {
			runnableResultCache_.put(task, result);
			return null;
		}
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
		return getService().getTasks(comparator);
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
			if (!scheduleCallableCache_.isEmpty()) {
				for (Callable<?> task : scheduleCallableCache_.keySet()) {
					Scheduler scheduler = scheduleCallableCache_.get(task);
					schedule(task, scheduler);
				}
			}
			if (!scheduleCallableDelayCache_.isEmpty()) {
				for (Callable<?> task : scheduleCallableDelayCache_.keySet()) {
					Long delay = scheduleCallableDelayCache_.get(task);
					TimeUnit unit = scheduleCallableTimeUnitCache_.get(task);
					schedule(task, delay, unit);
				}
			}
			if (!scheduleRunnableCache_.isEmpty()) {
				for (Runnable task : scheduleRunnableCache_.keySet()) {
					Scheduler scheduler = scheduleRunnableCache_.get(task);
					schedule(task, scheduler);
				}
			}
			if (!scheduleRunnableDelayCache_.isEmpty()) {
				for (Runnable task : scheduleRunnableDelayCache_.keySet()) {
					Long delay = scheduleRunnableDelayCache_.get(task);
					TimeUnit unit = scheduleRunnableTimeUnitCache_.get(task);
					schedule(task, delay, unit);
				}
			}
			if (!callableCache_.isEmpty()) {
				for (Callable<?> task : callableCache_) {
					submit(task);
				}
			}
			if (!runnableCache_.isEmpty()) {
				for (Runnable task : runnableCache_) {
					submit(task);
				}
			}
			if (!runnableResultCache_.isEmpty()) {
				for (Runnable task : runnableResultCache_.keySet()) {
					Object result = runnableResultCache_.get(task);
					submit(task, result);
				}
			}
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
