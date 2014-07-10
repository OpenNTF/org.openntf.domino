package org.openntf.domino.xots;

import org.openntf.domino.helpers.TrustedDispatcher;
import org.openntf.domino.thread.DominoNativeRunner;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
public class XotsDaemon extends TrustedDispatcher {
	private static XotsDaemon INSTANCE;

	private XotsDaemon() {
		super();
	}

	public synchronized static XotsDaemon getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new XotsDaemon();
		}
		return INSTANCE;
	}

	public void queue(final Runnable runnable) {
		queue(runnable, runnable.getClass().getClassLoader());
	}

	public void queue(final Runnable runnable, final ClassLoader loader) {
		DominoNativeRunner runner = new DominoNativeRunner(runnable, loader);
		super.process(runner);
	}

}
