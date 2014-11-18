package org.openntf.domino.thread;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * A Wrapper for runnables
 * 
 * @author Roland Praml, FOCONIS AG
 */
public class DominoFutureTask<T> extends AbstractDominoFutureTask<T> {
	private static final Logger log_ = Logger.getLogger(DominoFutureTask.class.getName());

	public DominoFutureTask(final Callable<T> callable) {
		super(callable);
	}

	public DominoFutureTask(final Runnable runnable, final T result) {
		super(runnable, result);
	}

	//	private AccessControlContext getAccessControlContext() {
	//		return null; // TODO
	//	}

	@Override
	protected void runNotes() {
		// TODO Auto-generated method stub
		Thread thread = Thread.currentThread();
		ClassLoader oldCl = thread.getContextClassLoader();

		ClassLoader runCl = wrappedObject.getClass().getClassLoader();
		thread.setContextClassLoader(runCl);
		try {
			super.runNotes();
		} finally {
			thread.setContextClassLoader(oldCl);
		}
	}
	//	@Override
	//	public void runNotes() {
	//
	//		try {
	//			Factory.setClassLoader(runCl);
	//			Factory.setSessionFactory(sessionFactory, SessionType.CURRENT);
	//			AccessControlContext runContext = getAccessControlContext();
	//			if (runContext == null) {
	//				super.runNotes();
	//			} else {
	//				AccessController.doPrivileged(new PrivilegedAction<Object>() {
	//					@Override
	//					public Object run() {
	//						DominoRunner.super.runNotes();
	//						return null;
	//					}
	//				}, runContext);
	//			}
	//		} finally {
	//
	//		}
	//
	//	}

}