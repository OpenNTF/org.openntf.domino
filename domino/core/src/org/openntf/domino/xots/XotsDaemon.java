package org.openntf.domino.xots;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openntf.domino.thread.AbstractDominoExecutor.DominoFutureTask;
import org.openntf.domino.thread.DominoExecutor;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
public class XotsDaemon {

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

	private XotsDaemon() {
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
	public static List<DominoFutureTask> getTasks(final boolean sortByExecDate) {
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
		System.out.println("[OpenNTF API] Starting XotsDaemon...");

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
			System.out.println("[OpenNTF API] Stopping XotsDaemon...");
			executor_.shutdown();
			long running;
			try {
				while ((running = executor_.getActiveCount()) > 0 && wait-- > 0) {
					System.out.println("[OpenNTF API] There are " + running + " tasks running... waiting " + wait + " seconds.");
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
			}

			if (executor_.getActiveCount() > 0) {
				System.out.println("[OpenNTF API] The following Threads did not terminate gracefully:");
				for (DominoFutureTask task : executor_.getTasks(false)) {
					System.out.println("[OpenNTF API] * " + task);
				}

			}

			try {
				for (int i = 60; i > 0; i -= 10) {
					if (executor_.getActiveCount() > 0) {
						System.out.println("[OpenNTF API] Trying to interrupt them and waiting again " + i + " seconds.");
						executor_.shutdownNow();
					}
					if (executor_.awaitTermination(10, TimeUnit.SECONDS)) {
						executor_ = null;
						System.out.println("[OpenNTF API] XotsDaemon stopped.");
						return;
					}
				}
			} catch (InterruptedException e) {
			}
			System.out.println("[OpenNTF API] WARNING: Could not stop XotsDaemon!");
		} else {
			System.out.println("[OpenNTF API] XotsDaemon not running");
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

	//	public synchronized static void publishEvent(final IDominoEvent event) {
	//		INSTANCE.fireEvent(event);
	//	}

	//
	//	@Override
	//	protected TrustedExecutor getExecutor() {
	//		if (intimidator_ == null) {
	//			intimidator_ = new XotsExecutor(this);
	//		}
	//		return intimidator_;
	//	}

	//	@Override
	//	protected TrustedScheduledExecutor getScheduledExecutor() {
	//		if (vengeance_ == null) {
	//			vengeance_ = new XotsScheduledExecutor(this);
	//		}
	//		return vengeance_;
	//	}

	//	public void scan(final String serverName) {
	//		XotsNsfScanner scanner = new XotsNsfScanner(serverName);
	//		scanner.addObserver(this);
	//		scanner.scan();
	//	}
	//
	//	public void schedule(final Class<? extends Runnable> taskClass) throws IllegalAccessException, InstantiationException {
	//		Schedule schedule = taskClass.getAnnotation(Schedule.class);
	//		if (schedule != null) {
	//			TimeUnit unit = schedule.timeunit();
	//			long freq = schedule.frequency();
	//			System.out.println("DEBUG: found a tasklet " + taskClass.getName() + " set to run every " + freq + " " + unit.name());
	//			Runnable runnable = taskClass.newInstance();
	//			getScheduledExecutor().schedule(runnable, freq, unit);
	//		}
	//	}

	//	public void schedule(final Runnable runnable) throws IllegalAccessException, InstantiationException {
	//		Class<? extends Runnable> taskClass = runnable.getClass();
	//		Schedule schedule = taskClass.getAnnotation(Schedule.class);
	//		if (schedule != null) {
	//			TimeUnit unit = schedule.timeunit();
	//			long freq = schedule.frequency();
	//			System.out.println("DEBUG: found a tasklet " + taskClass.getName() + " set to run every " + freq + " " + unit.name());
	//			getScheduledExecutor().schedule(runnable, freq, unit);
	//		}
	//	}
	//
	//	public static Future<?> queue(final Runnable runnable) {
	//		if (!isStarted()) {
	//			throw new IllegalStateException("XotsService is not started");
	//		}
	//		if (runnable instanceof Observable) {
	//			((Observable) runnable).addObserver(INSTANCE);
	//		}
	//		return INSTANCE.executor_.submit(runnable);
	//	}
	//
	//	public static <T> Future<T> queue(final Runnable runnable, final T result) {
	//		if (!isStarted()) {
	//			throw new IllegalStateException("XotsService is not started");
	//		}
	//		if (runnable instanceof Observable) {
	//			((Observable) runnable).addObserver(INSTANCE);
	//		}
	//		return INSTANCE.executor_.submit(runnable, result);
	//	}
	//
	//	public static <T> Future<T> queue(final Callable<T> callable) {
	//		if (!isStarted()) {
	//			throw new IllegalStateException("XotsService is not started");
	//		}
	//		if (callable instanceof Observable) {
	//			((Observable) callable).addObserver(INSTANCE);
	//		}
	//		return INSTANCE.executor_.submit(callable);
	//	}
	//
	//	public void fireEvent(final IDominoEvent event) {
	//		//		for (Class<?> clazz : XotsService.getInstance().getLoadedClasses()) {
	//		//			if (XotsITriggeredTasklet.class.isAssignableFrom(clazz) && Runnable.class.isAssignableFrom(clazz)) {
	//		//				// Then look to see if the annotation exists and matches this event
	//		//				Trigger trigger = clazz.getAnnotation(Trigger.class);
	//		//				if (trigger != null) {
	//		//					String value = trigger.value();
	//		//					if (value != null) {
	//		//						// TODO Handle DB events
	//		//						if (event instanceof CustomNamedEvent) {
	//		//							String eventName = ((CustomNamedEvent) event).getName();
	//		//							if (value.equals(eventName)) {
	//		//								try {
	//		//									Runnable runnable = (Runnable) clazz.newInstance();
	//		//									queue(new TriggerRunnable((XotsITriggeredTasklet) runnable, event), clazz.getClassLoader());
	//		//								} catch (InstantiationException e) {
	//		//									DominoUtils.handleException(e);
	//		//								} catch (IllegalAccessException e) {
	//		//									DominoUtils.handleException(e);
	//		//								}
	//		//							}
	//		//						}
	//		//					}
	//		//				}
	//		//			}
	//		//		}
	//	}
	//
	//	//	private static class TriggerRunnable extends AbstractDominoRunnable {
	//	//		private static final long serialVersionUID = 1L;
	//	//
	//	//		private final IDominoEvent event_;
	//	//		private final XotsITriggeredTasklet tasklet_;
	//	//
	//	//		public TriggerRunnable(final XotsITriggeredTasklet tasklet, final IDominoEvent event) {
	//	//			event_ = event;
	//	//			tasklet_ = tasklet;
	//	//		}
	//	//
	//	//		@Override
	//	//		public void run() {
	//	//			tasklet_.handleEvent(event_);
	//	//		}
	//	//
	//	//		@Override
	//	//		public DominoSessionType getSessionType() {
	//	//			return DominoSessionType.NATIVE;
	//	//		}
	//	//	}
	//
	//	@SuppressWarnings({ "unchecked", "rawtypes" })
	//	@Override
	//	public void update(final Observable arg0, final Object arg1) {
	//		System.out.println("Call from observable " + arg0);
	//		//		if (arg0 instanceof XotsNsfScanner) {
	//		//			if (arg1 instanceof Set) {
	//		//				taskletClasses_ = (Set) arg1;
	//		//			}
	//		//		}
	//	}

}
