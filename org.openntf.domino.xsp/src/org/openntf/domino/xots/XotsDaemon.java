package org.openntf.domino.xots;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.openntf.domino.helpers.TrustedDispatcher;
import org.openntf.domino.thread.DominoManualRunner;
import org.openntf.domino.thread.DominoNativeRunner;
import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.thread.model.IDominoRunnable;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
public class XotsDaemon extends TrustedDispatcher implements Observer {
	private static XotsDaemon INSTANCE;
	private Set<Class<?>> taskletClasses_;
	private Set<XotsBaseTasklet> tasklets_;

	protected class XotsExecutor extends TrustedDispatcher.TrustedExecutor {

		protected XotsExecutor(final TrustedDispatcher dispatcher) {
			super(dispatcher);
		}

		@Override
		public void execute(final Runnable runnable) {
			super.execute(runnable);
		}

	}

	private XotsDaemon() {
		super();
	}

	public synchronized static void addToQueue(final Runnable runnable) {
		getInstance().queue(runnable, runnable.getClass().getClassLoader());
	}

	public synchronized static XotsDaemon getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new XotsDaemon();
		}
		return INSTANCE;
	}

	public void scan(final String serverName) {
		XotsNsfScanner scanner = new XotsNsfScanner(serverName);
		scanner.addObserver(this);
		taskletClasses_ = scanner.scan();
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
				DominoNativeRunner runner = new DominoNativeRunner(runnable, localLoader);
				super.process(runner);
			} else if (type == DominoSessionType.MANUAL) {
				DominoManualRunner runner = new DominoManualRunner(runnable, localLoader);
				super.process(runner);
			}
		} else {
			DominoNativeRunner runner = new DominoNativeRunner(runnable, loader);
			super.process(runner);
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
