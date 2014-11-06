package org.openntf.domino.xots;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.xots.builtin.XotsNsfScanner;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
public class XotsDaemon implements Observer {
	private static XotsDaemon INSTANCE = new XotsDaemon();
	private Set<TaskletDefinition> tasklets_ = new HashSet<TaskletDefinition>();

	// This is our Threadpool that will execute all Runnables
	private DominoExecutor executor = new XotsDominoExecutor(50);

	private XotsDaemon() {
		super();
		XotsNsfScanner scanner = new XotsNsfScanner("");
		scanner.addObserver(this);
		tasklets_.add(new TaskletDefinition(scanner));
	}

	public static XotsDaemon getInstance() {
		return INSTANCE;
	}

	public List<Runnable> getRunningTasks() {
		return executor.getRunningTasks();
	}

	public void start() {
		System.out.println("DEBUG: Starting XotsService...");
		queue(new XotsNsfScanner(""));
	}

	public void stop() {
		System.out.println("DEBUG: Destroying XotsService...");
	}

	public synchronized static void addToQueue(final Runnable runnable) {
		getInstance().queue(runnable);
	}

	public synchronized static void publishEvent(final IDominoEvent event) {
		getInstance().fireEvent(event);
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

	public boolean queue(final Runnable runnable) {
		return executor.queue(runnable);
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
