package org.openntf.domino.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

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
		try {
			startUp();
		} catch (UnsatisfiedLinkError e) {
			System.err.println();
			System.err.println();
			System.err.println("        #==========================================================================#");
			System.err.println("        # Did not find notes-binaries. This means you cannot access Domino-Objects #");
			System.err.println("        # Please read the instructions on                                          #");
			System.err.println("        # https://github.com/OpenNTF/org.openntf.domino/wiki/Configuring-JUnit     #");
			System.err.println("        #==========================================================================#");
			System.err.println();
			System.err.println("Search path for binaries: " + System.getProperty("java.library.path"));
			System.err.println("Waiting 30 seconds, so you can read the message.");
			try {
				Thread.sleep(30000); // wait 30 seconds, so hopefully everyone should notice that message
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.err.println("Please add '-Djava.library.path=/your/domino/dir' as VM argument");
			Description desc = Description.createSuiteDescription(getClass());
			notifier.fireTestFailure(new Failure(desc, e));
			return;
		}
		try {
			super.run(notifier);
		} finally {
			tearDown();
		}
	}

	@Override
	protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
		beforeTest(method);
		try {
			super.runChild(method, notifier);
		} finally {
			afterTest(method);
		}
	}

	protected abstract void beforeTest(FrameworkMethod method);

	protected abstract void afterTest(FrameworkMethod method);
}
