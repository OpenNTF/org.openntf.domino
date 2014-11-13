package org.openntf.domino.thread;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import lotus.domino.AgentBase;
import lotus.domino.NotesException;
import lotus.domino.NotesThread;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.session.NativeSessionFactory;
import org.openntf.domino.thread.model.Context;
import org.openntf.domino.thread.model.Scope;
import org.openntf.domino.thread.model.SessionType;
import org.openntf.domino.thread.model.XotsTasklet;

/**
 * A Wrapper for runnables
 * 
 * @author Roland Praml, FOCONIS AG
 */
public class AbstractDominoRunner extends DominoThread {
	private static final Logger log_ = Logger.getLogger(AbstractDominoRunner.class.getName());

	/** the context when this object was created */
	protected ISessionFactory sessionFactory;

	protected String runAs;
	protected Scope scope;
	protected Context context;

	protected static class CallableToRunnable<T> implements Runnable {
		private final Callable<T> callable_;
		protected Object result;
		protected Exception exception;

		private CallableToRunnable(final Callable<T> callable) {
			callable_ = callable;
		}

		@Override
		public void run() {
			try {
				result = callable_.call();
			} catch (Exception e) {
				exception = e;
			}
		}
	}

	public AbstractDominoRunner(final Runnable runnable) {
		this(runnable, null); // CHECKME: should we use AccessController.getContext()
	}

	public AbstractDominoRunner(final Runnable runnable, final ISessionFactory sessionFactory) {
		super(runnable);
		if (runnable instanceof NotesThread) {
			throw new IllegalStateException("Cannot wrap the NotesThread " + runnable.getClass().getName() + " into a DominoRunner");
		}
		if (runnable instanceof AbstractDominoRunner) {
			throw new IllegalStateException("Cannot wrap the DominoRunner " + runnable.getClass().getName() + " into a DominoRunner");
		}
		this.sessionFactory = sessionFactory;
		init(runnable);
	}

	public <T> AbstractDominoRunner(final Callable<T> callable, final ISessionFactory sessionFactory) {
		super(new CallableToRunnable<T>(callable));
		if (callable instanceof NotesThread) {
			throw new IllegalStateException("Cannot wrap the NotesThread " + callable.getClass().getName() + " into a DominoRunner");
		}
		if (callable instanceof WrappedCallable) {
			// RPr: don't know if this is possible
			throw new IllegalStateException("Cannot wrap the WrappedCallable " + ((WrappedCallable) callable).wrapper.getClass().getName()
					+ " into a DominoRunner");
		}
		this.sessionFactory = sessionFactory;
		init(callable);
	}

	/**
	 * Determines the sessionType under which the current runnable should run. The first non-null value of the following list is returned
	 * <ul>
	 * <li>If the runnable implements <code>IDominoRunnable</code>: result of <code>getSessionType</code></li>
	 * <li>the value of {@link SessionType} Annotation</li>
	 * <li>DominoSessionType.DEFAULT</li>
	 * </ul>
	 * 
	 * @param runnable
	 *            the runnable to determine the DominoSessionType
	 * @return the DominoSessionType
	 */
	protected void init(final Object runnable) {

		if (runnable instanceof XotsTasklet.Interface) {
			XotsTasklet.Interface dominoRunnable = (XotsTasklet.Interface) runnable;
			ISessionFactory sf = dominoRunnable.getSessionFactory();
			if (sf != null) {
				sessionFactory = sf;
			}
			scope = dominoRunnable.getScope();
			runAs = dominoRunnable.getRunAs();
			context = dominoRunnable.getContext();
		}

		lotus.domino.Session currentSession = AgentBase.getAgentSession();
		if (currentSession != null) {
			if (runAs == null) {
				try {
					runAs = currentSession.getEffectiveUserName();
				} catch (NotesException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		XotsTasklet annot = runnable.getClass().getAnnotation(XotsTasklet.class);
		if (annot != null) {
			if (annot.session() == SessionType.NATIVE) {
				sessionFactory = new NativeSessionFactory(sessionFactory);
			}
			//			if (sessionType == null) {
			//				sessionType = annot.session();
			//			}
			//			if (sessionType == null) {
			//				sessionType = SessionType.DEFAULT;
			//			}

			if (context == null) {
				context = annot.context();
			}
			if (context == null) {
				context = Context.XOTS;
			}

			if (scope == null) {
				scope = annot.scope();
			}
			if (scope == null) {
				scope = Scope.NONE;
			}
		}
	}

	public class WrappedCallable<T> implements Callable<T> {
		private CallableToRunnable<T> wrapper;

		private WrappedCallable(final CallableToRunnable<T> wrapper) {
			this.wrapper = wrapper;
		}

		@Override
		public T call() throws Exception {
			AbstractDominoRunner.this.run();
			if (wrapper.exception != null)
				throw wrapper.exception;
			return (T) wrapper.result;
		}

	}

	public <T> Callable<T> asCallable(final Callable<T> typed) {
		// TODO Auto-generated method stub
		return new WrappedCallable<T>((CallableToRunnable<T>) getRunnable());
	}
}