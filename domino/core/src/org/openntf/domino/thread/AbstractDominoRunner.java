package org.openntf.domino.thread;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import lotus.domino.NotesThread;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.model.Context;
import org.openntf.domino.thread.model.Scope;
import org.openntf.domino.thread.model.XotsSessionType;
import org.openntf.domino.thread.model.XotsTasklet;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * A Wrapper for runnables
 * 
 * @author Roland Praml, FOCONIS AG
 */
public class AbstractDominoRunner extends DominoThread {
	private static final Logger log_ = Logger.getLogger(AbstractDominoRunner.class.getName());

	/** the context when this object was created */
	//protected ISessionFactory sessionFactory;

	protected Scope scope;
	protected Context context;
	protected ISessionFactory sessionFactory;

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
		super(runnable);
		if (runnable instanceof NotesThread) {
			throw new IllegalStateException("Cannot wrap the NotesThread " + runnable.getClass().getName() + " into a DominoRunner");
		}
		if (runnable instanceof AbstractDominoRunner) {
			throw new IllegalStateException("Cannot wrap the DominoRunner " + runnable.getClass().getName() + " into a DominoRunner");
		}
		init(runnable);
	}

	public <T> AbstractDominoRunner(final Callable<T> callable) {
		super(new CallableToRunnable<T>(callable));
		if (callable instanceof NotesThread) {
			throw new IllegalStateException("Cannot wrap the NotesThread " + callable.getClass().getName() + " into a DominoRunner");
		}
		if (callable instanceof WrappedCallable) {
			// RPr: don't know if this is possible
			throw new IllegalStateException("Cannot wrap the WrappedCallable " + ((WrappedCallable) callable).wrapper.getClass().getName()
					+ " into a DominoRunner");
		}
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
			sessionFactory = dominoRunnable.getSessionFactory();
			scope = dominoRunnable.getScope();
			context = dominoRunnable.getContext();
		}

		XotsTasklet annot = runnable.getClass().getAnnotation(XotsTasklet.class);
		if (annot != null) {
			if (sessionFactory == null) {
				switch (annot.session()) {
				case CLONE:
					sessionFactory = Factory.getSessionFactory(SessionType.CURRENT);
					break;
				case CLONE_FULL_ACCESS:
					sessionFactory = Factory.getSessionFactory(SessionType.CURRENT_FULL_ACCESS);
					break;

				case FULL_ACCESS:
					sessionFactory = Factory.getSessionFactory(SessionType.FULL_ACCESS);
					break;

				case NATIVE:
					sessionFactory = Factory.getSessionFactory(SessionType.NATIVE);
					break;

				case NONE:
					sessionFactory = null;
					break;

				case SIGNER:
					sessionFactory = Factory.getSessionFactory(SessionType.SIGNER);
					break;

				case SIGNER_FULL_ACCESS:
					sessionFactory = Factory.getSessionFactory(SessionType.SIGNER_FULL_ACCESS);
					break;

				case TRUSTED:
					sessionFactory = Factory.getSessionFactory(SessionType.TRUSTED);
					break;

				default:
					break;
				}
				if ((annot.session() != XotsSessionType.NONE) && sessionFactory == null) {
					throw new IllegalStateException("Could not create a Fatory for " + annot.session());
				}
			}

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