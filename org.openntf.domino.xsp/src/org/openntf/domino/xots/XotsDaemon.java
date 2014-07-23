package org.openntf.domino.xots;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.openntf.domino.helpers.TrustedDispatcher;
import org.openntf.domino.thread.DominoManualRunner;
import org.openntf.domino.thread.DominoNoneRunner;
import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.thread.DominoThreadFactory;
import org.openntf.domino.thread.model.IDominoRunnable;

import com.ibm.designer.runtime.domino.adapter.HttpService;
import com.ibm.designer.runtime.domino.adapter.LCDEnvironment;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

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
			if (r instanceof IXotsRunner) {
				ClassLoader loader = ((IXotsRunner) r).getContextClassLoader();
				if (loader != null) {
					t.setContextClassLoader(loader);
				}
				NSFComponentModule module = ((IXotsRunner) r).getModule();
				((XotsThread) t).initThread(module);
			}
		}

		@Override
		protected void afterExecute(final Runnable r, final Throwable t) {
			Thread thread = Thread.currentThread();
			if (thread instanceof lotus.domino.NotesThread) {
				((lotus.domino.NotesThread) thread).termThread();
			}
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

		public void initThread(final NSFComponentModule module) {
			if (module != null) {
				System.out.println("DEBUG: Initializing a thread using a module: " + module.getModuleName());
				NotesContext nc = new NotesContext(module);
				NotesContext.initThread(nc);
			} else {
				super.initThread();
			}
		}

		@Override
		public void termThread() {
			NotesContext nc = NotesContext.getCurrentUnchecked();
			if (nc == null) {
				super.termThread();
			} else {
				System.out.println("DEBUG: Terminating a thread using a module: " + nc.getModule().getModuleName());
				NotesContext.termThread();
			}
		}
	}

	private XotsDaemon() {
		super();
		LCDEnvironment env = LCDEnvironment.getInstance();
		List<HttpService> services = env.getServices();
		xotsService_ = new XotsService(env);
		services.add(xotsService_);
	}

	public synchronized static void stop() {
		if (null != INSTANCE) {
			INSTANCE.getExecutor().shutdownNow();
		}
	}

	public synchronized static void addToQueue(final Runnable runnable) {
		try {
			Object result = AccessController.doPrivileged(new PrivilegedAction<Object>() {
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

	@Override
	protected TrustedExecutor getExecutor() {
		if (intimidator_ == null) {
			intimidator_ = new XotsExecutor(this);
		}
		return intimidator_;
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
