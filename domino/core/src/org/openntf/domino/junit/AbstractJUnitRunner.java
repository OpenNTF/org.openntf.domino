package org.openntf.domino.junit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.openntf.domino.utils.DominoUtils;

/**
 * A Testrunner to run JUnit tests with proper set up of ODA.
 * 
 * Use <code>{@literal @}RunWith(DominoJUnitRunner.class)</code> in your test class.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public abstract class AbstractJUnitRunner extends BlockJUnit4ClassRunner {

	private String runAs;
	private String db;

	private boolean runLegacy = false;

	public AbstractJUnitRunner(final Class<?> testClass) throws InitializationError {
		super(testClass);
		SessionUser runTestAs = testClass.getAnnotation(SessionUser.class);
		if (runTestAs != null)
			runAs = runTestAs.value();
		SessionDb runTestDb = testClass.getAnnotation(SessionDb.class);
		if (runTestDb != null)
			db = runTestDb.value();
		runLegacy = testClass.isAnnotationPresent(RunLegacy.class);
	}

	protected String getRunAs(final FrameworkMethod method) {
		SessionUser runTestAs = method.getAnnotation(SessionUser.class);
		String ret = null;
		if (runTestAs != null) {
			ret = runTestAs.value();
		}
		if (ret == null) {
			ret = runAs;
		}
		return ret;
	}

	protected String getDatabase(final FrameworkMethod method) {
		SessionDb runTestDb = method.getAnnotation(SessionDb.class);
		String ret = null;
		if (runTestDb != null) {
			ret = runTestDb.value();
		}
		if (ret == null) {
			ret = db;
		}
		return ret;
	}

	protected boolean isRunLegacy(final FrameworkMethod method) {
		if (runLegacy)
			return true;
		return method.getAnnotation(RunLegacy.class) != null;
	}

	protected abstract void startUp();

	protected abstract void tearDown();

	/**
	 * Startup the ODA and the NotesThread once. Create one masterSession to keep alive the connection
	 */
	@Override
	public void run(final RunNotifier notifier) {
		startUp();
		try {
			super.run(notifier);
		} finally {
			tearDown();
		}
	}

	@Override
	protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
		beforeTest(method);
		DominoUtils.setBubbleExceptions(true); // Test MUST bubble up exceptions!
		try {
			super.runChild(method, notifier);
		} finally {
			afterTest(method);
		}
	}

	protected abstract void beforeTest(FrameworkMethod method);

	protected abstract void afterTest(FrameworkMethod method);
}
