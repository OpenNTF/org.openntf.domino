package org.openntf.domino.junit;

import lotus.domino.NotesException;
import lotus.notes.NotesThread;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.XotsDaemon;

/**
 * A Testrunner to run JUnit tests with proper set up of ODA.
 * 
 * Use <code>{@literal @}RunWith(DominoJUnitRunner.class)</code> in your test class.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class DominoJUnitRunner extends BlockJUnit4ClassRunner {

	public DominoJUnitRunner(final Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	static {
		// TODO RPr: remove this code
		NotesThread.sinitThread();
		Factory.class.getName();
		// wait some millis until setup job is complete
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		NotesThread.stermThread();

	}

	protected void startUp() {
		System.out.println("Setting up the domino test environment");
		Factory.startup();
		NotesThread.sinitThread();
		DominoExecutor executor = new DominoExecutor(50);
		XotsDaemon.start(executor);
	}

	protected void tearDown() {
		XotsDaemon.stop(600); // 10 minutes should be enough for tests
		Factory.shutdown();
		NotesThread.stermThread();
	}

	/**
	 * Startup the ODA and the NotesThread once. Create one masterSession to keep alive the connection
	 */
	@Override
	public void run(final RunNotifier notifier) {

		startUp();

		try {
			lotus.domino.Session masterSession = lotus.domino.local.Session.createSession();
			TestProps.setup(masterSession);
			try {
				super.run(notifier);
			} finally {
				masterSession.recycle();
			}
		} catch (NotesException e) {
			e.printStackTrace();
		} finally {
			tearDown();

			System.out.println("Shut down the domino test environment");
		}
	}

	@Override
	protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
		// TODO Auto-generated method stub
		Factory.initThread();
		DominoUtils.setBubbleExceptions(true); // Test MUST bubble up exceptions!
		try {
			super.runChild(method, notifier);
		} finally {
			Factory.termThread();
		}
	}

}
