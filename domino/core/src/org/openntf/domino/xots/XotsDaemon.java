package org.openntf.domino.xots;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.thread.DominoExecutor;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
public class XotsDaemon implements Observer {
	private static XotsDaemon INSTANCE = new XotsDaemon();
	private Set<TaskletDefinition> tasklets_ = new HashSet<TaskletDefinition>();

	// This is our Threadpool that will execute all Runnables
	private DominoExecutor executor_;

	private XotsDaemon() {
		super();
	}

	public static List<Runnable> getRunningTasks() {
		if (!isStarted())
			return Collections.emptyList();
		return INSTANCE.executor_.getRunningTasks();
	}

	public static void start() {
		start(new DominoExecutor(10));
	}

	public static void start(final DominoExecutor executor) {
		synchronized (INSTANCE) {
			if (isStarted())
				throw new IllegalStateException("XotsService is already started");
			System.out.println("DEBUG: Starting XotsService... with executor " + executor.getClass().getName());
			INSTANCE.executor_ = executor;
		}
	}

	public static boolean isStarted() {
		synchronized (INSTANCE) {
			return INSTANCE.executor_ != null;
		}
	}

	public static void stop(int wait) {
		synchronized (INSTANCE) {
			System.out.println("DEBUG: Destroying XotsService...");
			if (isStarted()) {
				long running;
				try {
					while ((running = INSTANCE.executor_.getActiveCount()) > 0 && wait-- > 0) {
						System.out.println("There are " + running + " tasks... waiting: " + wait);
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
				}
				INSTANCE.executor_.shutdownNow();
				try {
					INSTANCE.executor_.awaitTermination(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
				}
			}
			System.out.println("DEBUG: ... Done");
			INSTANCE.executor_ = null;
		}
	}

	public synchronized static void publishEvent(final IDominoEvent event) {
		INSTANCE.fireEvent(event);
	}

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

	public static boolean queue(final Runnable runnable) {
		if (!isStarted()) {
			throw new IllegalStateException("XotsService is not started");
		}
		if (runnable instanceof Observable) {
			((Observable) runnable).addObserver(INSTANCE);
		}
		return INSTANCE.executor_.queue(runnable);
	}

	public void fireEvent(final IDominoEvent event) {
		//		for (Class<?> clazz : XotsService.getInstance().getLoadedClasses()) {
		//			if (XotsITriggeredTasklet.class.isAssignableFrom(clazz) && Runnable.class.isAssignableFrom(clazz)) {
		//				// Then look to see if the annotation exists and matches this event
		//				Trigger trigger = clazz.getAnnotation(Trigger.class);
		//				if (trigger != null) {
		//					String value = trigger.value();
		//					if (value != null) {
		//						// TODO Handle DB events
		//						if (event instanceof CustomNamedEvent) {
		//							String eventName = ((CustomNamedEvent) event).getName();
		//							if (value.equals(eventName)) {
		//								try {
		//									Runnable runnable = (Runnable) clazz.newInstance();
		//									queue(new TriggerRunnable((XotsITriggeredTasklet) runnable, event), clazz.getClassLoader());
		//								} catch (InstantiationException e) {
		//									DominoUtils.handleException(e);
		//								} catch (IllegalAccessException e) {
		//									DominoUtils.handleException(e);
		//								}
		//							}
		//						}
		//					}
		//				}
		//			}
		//		}
	}

	//	private static class TriggerRunnable extends AbstractDominoRunnable {
	//		private static final long serialVersionUID = 1L;
	//
	//		private final IDominoEvent event_;
	//		private final XotsITriggeredTasklet tasklet_;
	//
	//		public TriggerRunnable(final XotsITriggeredTasklet tasklet, final IDominoEvent event) {
	//			event_ = event;
	//			tasklet_ = tasklet;
	//		}
	//
	//		@Override
	//		public void run() {
	//			tasklet_.handleEvent(event_);
	//		}
	//
	//		@Override
	//		public DominoSessionType getSessionType() {
	//			return DominoSessionType.NATIVE;
	//		}
	//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void update(final Observable arg0, final Object arg1) {
		System.out.println("Call from observable " + arg0);
		//		if (arg0 instanceof XotsNsfScanner) {
		//			if (arg1 instanceof Set) {
		//				taskletClasses_ = (Set) arg1;
		//			}
		//		}
	}
}
