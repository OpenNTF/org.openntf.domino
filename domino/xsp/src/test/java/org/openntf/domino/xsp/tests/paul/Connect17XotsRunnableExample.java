package org.openntf.domino.xsp.tests.paul;

import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.AbstractXotsRunnable;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.Xots;
import org.openntf.domino.xots.XotsUtil;
import org.openntf.domino.xsp.xots.XotsDominoExecutor;

@Tasklet(session = Tasklet.Session.CLONE)
public class Connect17XotsRunnableExample implements Runnable {

	public Connect17XotsRunnableExample() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new Connect17XotsRunnableExample(), TestRunnerUtil.NATIVE_SESSION);
	}

	@Override
	public void run() {
		// Because we're in a TestRunner, Xots is not started, need to start it
		DominoExecutor executor = new XotsDominoExecutor(10);
		Xots.start(executor);
		for (int i = 0; i < 10; i++) {
			Xots.getService().submit(new DemoXotsRunnable("Hello", i));
		}
		Xots.stop(120);
	}

	@Tasklet(session = Tasklet.Session.CLONE)
	public class DemoXotsRunnable extends AbstractXotsRunnable {
		String greeting;
		int xotsThread;

		public DemoXotsRunnable(final String greeting, final int xotsThread) {
			this.greeting = greeting;
			this.xotsThread = xotsThread;
		}

		@Override
		public void run() {
			try {
				String name = Factory.getSession(SessionType.CURRENT).getEffectiveUserName();
				System.out.println(greeting + " " + name + " from thread " + xotsThread);
			} catch (Throwable t) {
				XotsUtil.handleException(t, getContext());
			}
		}
	}
}
