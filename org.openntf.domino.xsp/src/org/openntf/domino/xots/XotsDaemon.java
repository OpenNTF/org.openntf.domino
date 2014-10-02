package org.openntf.domino.xots;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.helpers.TrustedDispatcher;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.thread.DominoExecutor.ThreadCleaner;
import org.openntf.domino.thread.DominoManualRunner;
import org.openntf.domino.thread.DominoNoneRunner;
import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.thread.DominoThreadFactory;
import org.openntf.domino.thread.model.IDominoRunnable;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.xots.annotations.Schedule;
import org.openntf.domino.xots.annotations.Trigger;
import org.openntf.domino.xots.builtin.XotsNsfScanner;
import org.openntf.domino.xots.events.CustomNamedEvent;

import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
public class XotsDaemon extends TrustedDispatcher implements Observer {
	private static XotsDaemon INSTANCE;
	private XotsService xotsService_;
	private Set<Class<?>> taskletClasses_;
	private Set<XotsBaseTasklet> tasklets_;

	protected class XotsScheduledExecutor extends TrustedDispatcher.TrustedScheduledExecutor {

		protected XotsScheduledExecutor(final TrustedDispatcher dispatcher) {
			super(dispatcher, new XotsThreadFactory());
		}

		@Override
		public void execute(final Runnable runnable) {
			if (!(runnable instanceof IXotsRunner)) {
				System.out.println("DEBUG: XotsExecutor has been asked to execute a " + runnable.getClass().getName());
			}
			super.execute(runnable);
		}

		@Override
		public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
			System.out.println("DEBUG: Scheduling a runnable " + command.getClass().getName() + " to run in " + delay + " " + unit.name());
			return super.schedule(command, delay, unit);
		}

		@Override
		protected void beforeExecute(final Thread t, final Runnable r) {
			Runnable run = r;
			if (r instanceof TrustedRunnable) {
				run = ((TrustedRunnable) r).getRunnable();
			}
			if (run instanceof IXotsRunner) {
				ClassLoader loader = ((IXotsRunner) run).getContextClassLoader();
				if (loader != null) {
					t.setContextClassLoader(loader);
				}
			} else {
				System.out.println("DEBUG: XotsDaemon is running a " + run.getClass().getName());
			}
		}

		@Override
		protected void afterExecute(final Runnable r, final Throwable t) {

		}
	}

	protected class XotsExecutor extends TrustedDispatcher.TrustedExecutor {
		protected XotsExecutor(final TrustedDispatcher dispatcher) {
			super(dispatcher, new XotsThreadFactory());
		}

		@Override
		public void execute(final Runnable runnable) {
			if (!(runnable instanceof IXotsRunner)) {
				System.out.println("DEBUG: XotsExecutor has been asked to execute a " + runnable.getClass().getName());
			}
			super.execute(runnable);
		}

		@Override
		protected void beforeExecute(final Thread t, final Runnable r) {
			Runnable run = r;
			if (r instanceof TrustedRunnable) {
				run = ((TrustedRunnable) r).getRunnable();
			}
			if (run instanceof IXotsRunner) {
				ClassLoader loader = ((IXotsRunner) run).getContextClassLoader();
				if (loader != null) {
					t.setContextClassLoader(loader);
				}
			} else {
				System.out.println("DEBUG: XotsDaemon is running a " + run.getClass().getName());
			}
		}

		@Override
		protected void afterExecute(final Runnable r, final Throwable t) {

		}
	}

	protected class XotsThreadFactory extends DominoThreadFactory {
		public XotsThreadFactory() {

		}

		@Override
		protected DominoThread makeThread(final Runnable runnable) {
			return new XotsThread(runnable);
		}
	}

	protected class XotsThread extends DominoThread {
		public XotsThread(final Runnable runnable) {
			super(runnable);
		}
	}

	private XotsDaemon() {
		super();
		LCDEnvironment env = LCDEnvironment.getInstance();
		List<HttpService> services = env.getServices();
		xotsService_ = new XotsService(env);
		services.add(xotsService_);

		XotsNsfScanner scanner = new XotsNsfScanner("");
		scanner.addObserver(this);
		Thread t = new lotus.domino.NotesThread(scanner);
		t.start();
	}

	public synchronized static void stop() {
		ThreadCleaner.INSTANCE.clean();
	}

	public synchronized static void addToQueue(final Runnable runnable) {
		try {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					getInstance().queue(runnable, runnable.getClass().getClassLoader());
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static XotsDaemon getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new XotsDaemon();
		}
		return INSTANCE;
	}

	public synchronized static void publishEvent(final IDominoEvent event) {
		getInstance().fireEvent(event);
	}

	@Override
	protected TrustedExecutor getExecutor() {
		if (intimidator_ == null) {
			intimidator_ = new XotsExecutor(this);
		}
		return intimidator_;
	}

	@Override
	protected TrustedScheduledExecutor getScheduledExecutor() {
		if (vengeance_ == null) {
			vengeance_ = new XotsScheduledExecutor(this);
		}
		return vengeance_;
	}

	public void scan(final String serverName) {
		XotsNsfScanner scanner = new XotsNsfScanner(serverName);
		scanner.addObserver(this);
		scanner.scan();
	}

	public void schedule(final Class<? extends Runnable> taskClass) throws IllegalAccessException, InstantiationException {
		Schedule schedule = taskClass.getAnnotation(Schedule.class);
		if (schedule != null) {
			TimeUnit unit = schedule.timeunit();
			long freq = schedule.frequency();
			System.out.println("DEBUG: found a tasklet " + taskClass.getName() + " set to run every " + freq + " " + unit.name());
			Runnable runnable = taskClass.newInstance();
			getScheduledExecutor().schedule(runnable, freq, unit);
		}
	}

	public void schedule(final Runnable runnable) throws IllegalAccessException, InstantiationException {
		Class<? extends Runnable> taskClass = runnable.getClass();
		Schedule schedule = taskClass.getAnnotation(Schedule.class);
		if (schedule != null) {
			TimeUnit unit = schedule.timeunit();
			long freq = schedule.frequency();
			System.out.println("DEBUG: found a tasklet " + taskClass.getName() + " set to run every " + freq + " " + unit.name());
			getScheduledExecutor().schedule(runnable, freq, unit);
		}
	}

	public void queue(final Runnable runnable) {
		queue(runnable, runnable.getClass().getClassLoader());
	}

	public void queue(final Runnable runnable, final ClassLoader loader) {
		ClassLoader localLoader = loader == null ? runnable.getClass().getClassLoader() : loader;
		if (runnable instanceof IDominoRunnable) {
			DominoSessionType type = ((IDominoRunnable) runnable).getSessionType();
			if (type == DominoSessionType.NAMED) {
				XotsNamedRunner runner = new XotsNamedRunner(runnable, localLoader);
				super.process(runner);
			} else if (type == DominoSessionType.NATIVE) {
				XotsNativeRunner runner = new XotsNativeRunner(runnable, localLoader);
				super.process(runner);
			} else if (type == DominoSessionType.MANUAL) {
				DominoManualRunner runner = new DominoManualRunner(runnable, localLoader);
				super.process(runner);
			} else if (type == DominoSessionType.NONE) {
				DominoNoneRunner runner = new DominoNoneRunner(runnable, localLoader);
				super.process(runner);
			}
		} else {
			DominoNoneRunner runner = new DominoNoneRunner(runnable, loader);
			super.process(runner);
		}
	}

	public void fireEvent(final IDominoEvent event) {
		for (Class<?> clazz : XotsService.getInstance().getLoadedClasses()) {
			if (XotsITriggeredTasklet.class.isAssignableFrom(clazz) && Runnable.class.isAssignableFrom(clazz)) {
				// Then look to see if the annotation exists and matches this event
				Trigger trigger = clazz.getAnnotation(Trigger.class);
				if (trigger != null) {
					String value = trigger.value();
					if (value != null) {
						// TODO Handle DB events
						if (event instanceof CustomNamedEvent) {
							String eventName = ((CustomNamedEvent) event).getName();
							if (value.equals(eventName)) {
								try {
									Runnable runnable = (Runnable) clazz.newInstance();
									queue(new TriggerRunnable((XotsITriggeredTasklet) runnable, event), clazz.getClassLoader());
								} catch (InstantiationException e) {
									DominoUtils.handleException(e);
								} catch (IllegalAccessException e) {
									DominoUtils.handleException(e);
								}
							}
						}
					}
				}
			}
		}
	}

	private static class TriggerRunnable extends AbstractDominoRunnable {
		private static final long serialVersionUID = 1L;

		private final IDominoEvent event_;
		private final XotsITriggeredTasklet tasklet_;
		private boolean done_ = false;

		public TriggerRunnable(final XotsITriggeredTasklet tasklet, final IDominoEvent event) {
			event_ = event;
			tasklet_ = tasklet;
		}

		@Override
		public void run() {
			tasklet_.handleEvent(event_);
			done_ = true;
		}

		@Override
		public boolean shouldStop() {
			return done_;
		}

		@Override
		public DominoSessionType getSessionType() {
			return DominoSessionType.NATIVE;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void update(final Observable arg0, final Object arg1) {
		if (arg0 instanceof XotsNsfScanner) {
			if (arg1 instanceof Set) {
				taskletClasses_ = (Set) arg1;
			}
		}
	}
}
