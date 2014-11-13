package org.openntf.domino.thread;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * A Wrapper for runnables
 * 
 * @author Roland Praml, FOCONIS AG
 */
public class DominoRunner extends AbstractDominoRunner {
	private static final Logger log_ = Logger.getLogger(DominoRunner.class.getName());

	public <T> DominoRunner(final Callable<T> callable, final ISessionFactory sessionFactory) {
		super(callable, sessionFactory);
	}

	public DominoRunner(final Runnable runnable, final ISessionFactory sessionFactory) {
		super(runnable, sessionFactory);
	}

	public DominoRunner(final Runnable runnable) {
		super(runnable);
	}

	private AccessControlContext getAccessControlContext() {
		return null;
	}

	@Override
	public void runNotes() {
		final Thread thread = Thread.currentThread();

		ClassLoader runCl = getRunnable().getClass().getClassLoader();
		ClassLoader oldCl = thread.getContextClassLoader();
		thread.setContextClassLoader(runCl);

		try {
			Factory.setClassLoader(runCl);
			Factory.setSessionFactory(sessionFactory, SessionType.DEFAULT);
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
		} finally {
			thread.setContextClassLoader(oldCl);
		}

	}

}