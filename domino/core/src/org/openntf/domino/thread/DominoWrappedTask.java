package org.openntf.domino.thread;

import java.util.concurrent.Callable;

import lotus.domino.NotesThread;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class DominoWrappedTask extends AbstractWrappedTask {

	private Factory.ThreadConfig sourceThreadConfig = Factory.getThreadConfig();

	/**
	 * Common method that does setUp/tearDown before executing the wrapped object
	 * 
	 * @param callable
	 * @param runnable
	 * @return
	 * @throws Exception
	 */
	protected Object callOrRun() throws Exception {

		NotesThread.sinitThread();
		Factory.initThread(sourceThreadConfig);
		try {
			return invokeWrappedTask();
		} finally {
			Factory.termThread();
			NotesThread.stermThread();
		}
	}

	protected Object invokeWrappedTask() throws Exception {
		Thread thread = Thread.currentThread();
		ClassLoader oldCl = thread.getContextClassLoader();
		Object wrappedTask = getWrappedTask();
		ClassLoader runCl = wrappedTask.getClass().getClassLoader();
		thread.setContextClassLoader(runCl);

		DominoUtils.setBubbleExceptions(bubbleException);
		if (sessionFactory != null) {
			Factory.setSessionFactory(sessionFactory, SessionType.CURRENT);
		}
		try {
			if (wrappedTask instanceof Callable) {
				return ((Callable<?>) wrappedTask).call();
			} else {
				((Runnable) wrappedTask).run();
				return null;
			}
		} finally {
			thread.setContextClassLoader(oldCl);
		}
	}
}