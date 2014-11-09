package org.openntf.domino.thread;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
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
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionMode;

/**
 * A Wrapper for runnables
 * 
 * @author Roland Praml, FOCONIS AG
 */
public class DominoRunner extends DominoThread {
	private static final Logger log_ = Logger.getLogger(DominoRunner.class.getName());

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

	public DominoRunner(final Runnable runnable) {
		this(runnable, null); // CHECKME: should we use AccessController.getContext()
	}

	public DominoRunner(final Runnable runnable, final ISessionFactory sessionFactory) {
		super(runnable);
		if (runnable instanceof NotesThread) {
			throw new IllegalStateException("Cannot wrap the NotesThread " + runnable.getClass().getName() + " into a DominoRunner");
		}
		this.sessionFactory = sessionFactory;
		init(runnable);
	}

	public <T> DominoRunner(final Callable<T> callable, final ISessionFactory sessionFactory) {
		super(new CallableToRunnable<T>(callable));
		if (callable instanceof NotesThread) {
			throw new IllegalStateException("Cannot wrap the NotesThread " + callable.getClass().getName() + " into a DominoRunner");
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

	@Override
	public ClassLoader getContextClassLoader() {
		return getRunnable().getClass().getClassLoader();
	}

	private AccessControlContext getAccessControlContext() {
		return null;
	}

	//	@Override
	//	public String toString() {
	//		StringBuilder sb = new StringBuilder();
	//		try {
	//			sb.append("RunnableData.runAs =\t" + runAs + "\n");
	//
	//			if (session != null) {
	//				sb.append("RunnableData.Session =\t" + session.getClass().getName() + "\n");
	//				sb.append("RunnableData.Session.UserName =\t" + session.getUserName() + "\n");
	//				sb.append("RunnableData.Session.EffectiveUserName =\t" + session.getUserName() + "\n");
	//				sb.append("RunnableData.Session.isTrusted =\t" + session.isTrustedSession() + "\n");
	//				sb.append("RunnableData.Session.isRestricted =\t" + session.isRestricted() + "\n");
	//				lotus.domino.Database db = session.getCurrentDatabase();
	//				if (db != null) {
	//					sb.append("RunnableData.Session.Database =\t" + db.getFilePath() + "\n");
	//				}
	//			}
	//		} catch (NotesException ex) {
	//
	//		}
	//		sb.append("RunnableData.OldClassLoader =\t" + oldClassLoader == null ? null : oldClassLoader.getClass().getName());
	//		sb.append("RunnableData.ClassLoader =\t" + getContextClassLoader() == null ? null : getContextClassLoader().getClass().getName());
	//		return sb.toString();
	//	}

	@Override
	public void runNotes() {
		//Factory.initThread(); initThread not needed. The thread is already initialized
		//try {
		final ClassLoader oldClassLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
			@Override
			public ClassLoader run() {
				ClassLoader ret = Thread.currentThread().getContextClassLoader();
				ClassLoader contextClassLoader = getContextClassLoader();
				Thread.currentThread().setContextClassLoader(contextClassLoader);
				Factory.setClassLoader(contextClassLoader);
				return ret;
			}
		});

		try {
			runWithSession();
		} finally {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					Thread.currentThread().setContextClassLoader(oldClassLoader);
					return null;
				}
			});
		}
		//		} finally {
		//			Factory.termThread();
		//		}
	}

	protected void runWithSession() {
		Factory.setSessionFactory(sessionFactory, SessionMode.DEFAULT);
		runWithXspContext();
	}

	protected void runWithXspContext() {
		runRunnable();
	}

	protected void runRunnable() {
		// If we have a RunContext, we run the runnable in a privileged section with this context
		AccessControlContext runContext = getAccessControlContext();
		if (runContext == null) {
			super.runNotes();
		} else {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					DominoRunner.super.runNotes();
					return null;
				}
			}, runContext);
		}
	}

	public <T> Callable<T> asCallable(final Callable<T> dummy) {
		// TODO Auto-generated method stub
		final CallableToRunnable<T> wrapper = (CallableToRunnable<T>) getRunnable();
		return new Callable<T>() {

			@Override
			public T call() throws Exception {
				DominoRunner.this.run();
				if (wrapper.exception != null)
					throw wrapper.exception;
				return (T) wrapper.result;
			}

		};
	}

}