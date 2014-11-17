package org.openntf.domino.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import lotus.domino.NotesException;
import lotus.domino.NotesThread;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.openntf.domino.impl.Base;
import org.openntf.domino.session.NamedSessionFactory;
import org.openntf.domino.session.NativeSessionFactory;
import org.openntf.domino.session.SessionFullAccessFactory;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.XotsDaemon;

/**
 * A Testrunner to run JUnit tests with proper set up of ODA.
 * 
 * Use <code>{@literal @}RunWith(DominoJUnitRunner.class)</code> in your test class.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class DominoJUnitRunner extends AbstractJUnitRunner {

	public DominoJUnitRunner(final Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	private lotus.domino.Session mastersession;
	private boolean factoryShutdown;

	@Override
	protected void startUp() {
		NotesThread.sinitThread();
		try {
			// keep alive one session over all test cases - this will improve execution speed
			mastersession = lotus.domino.NotesFactory.createSession();
			TestEnv.setup(mastersession);
		} catch (NotesException ne) {
			ne.printStackTrace();
		}
		if (!Factory.isStarted()) {

			Factory.startup();
			DominoExecutor executor = new DominoExecutor(50);
			XotsDaemon.start(executor);
			factoryShutdown = true;
		}
	}

	@Override
	protected void tearDown() {
		try {
			if (mastersession != null)
				mastersession.recycle();
		} catch (NotesException e) {
			e.printStackTrace();
		}
		if (factoryShutdown) {
			XotsDaemon.stop(600); // 10 minutes should be enough for tests
			Factory.shutdown();
		}
		NotesThread.stermThread();
	}

	@Override
	protected void beforeTest(final FrameworkMethod method) {
		try {
			String runAs = getRunAs(method);
			String db = getDatabase(method);
			Factory.initThread();
			if (runAs == null) {
				Factory.setSessionFactory(new NativeSessionFactory().apiPath(db), SessionType.CURRENT);
				Factory.setSessionFactory(new SessionFullAccessFactory().apiPath(db), SessionType.CURRENT_FULL_ACCESS);
			} else {
				Factory.setSessionFactory(new NamedSessionFactory(runAs).apiPath(db), SessionType.CURRENT);
				Factory.setSessionFactory(new SessionFullAccessFactory(runAs).apiPath(db), SessionType.CURRENT_FULL_ACCESS);
			}

			TestEnv.session = Factory.getSession(SessionType.CURRENT);
			TestEnv.database = TestEnv.session.getCurrentDatabase();
			if (db != null) {
				assertNotNull(TestEnv.database);
			}

			if (isRunLegacy(method)) {
				TestEnv.session = Base.toLotus(TestEnv.session);
				TestEnv.database = Base.toLotus(TestEnv.database);
			}

		} catch (NotesException ne) {
			ne.printStackTrace();
			Base.s_recycle(TestEnv.session);
			Base.s_recycle(TestEnv.database);
			fail(ne.getMessage());
		}
	}

	@Override
	protected void afterTest(final FrameworkMethod method) {
		// TODO Auto-generated method stub
		Base.s_recycle(TestEnv.database);
		Base.s_recycle(TestEnv.session);
		TestEnv.session = null;
		TestEnv.database = null;
		Factory.termThread();
	}

}
