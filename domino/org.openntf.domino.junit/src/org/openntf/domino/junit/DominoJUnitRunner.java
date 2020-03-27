/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.logging.LogManager;

import lotus.domino.NotesException;
import lotus.domino.NotesThread;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.session.NamedSessionFactory;
import org.openntf.domino.session.NativeSessionFactory;
import org.openntf.domino.session.SessionFullAccessFactory;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Xots;

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

	//private boolean ownSM;

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
		LogManager.getLogManager().reset();
		// RPr: Did not figure out, how to set up a proper SM
		//		if (System.getSecurityManager() == null) {
		//			new lotus.notes.AgentSecurityManager();
		//			ownSM = true;
		//		}
		if (!Factory.isStarted()) {

			Factory.startup();
			DominoExecutor executor = new DominoExecutor(50);
			Xots.start(executor);
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
			Xots.stop(120); // 10 minutes should be enough for tests
			Factory.shutdown();
		}
		//		if (ownSM) {
		//			AccessController.doPrivileged(new PrivilegedAction<Object>() {
		//
		//				@Override
		//				public Object run() {
		//					System.setSecurityManager(null);
		//					return null;
		//				}
		//			});
		//		}
		NotesThread.stermThread();
	}

	@Override
	protected void beforeTest(final FrameworkMethod method) {
		try {
			String runAs = getRunAs(method);
			String db = getDatabase(method);
			Factory.initThread(Factory.STRICT_THREAD_CONFIG);
			if (runAs == null) {
				Factory.setSessionFactory(new NativeSessionFactory(db), SessionType.CURRENT);
				Factory.setSessionFactory(new SessionFullAccessFactory(db), SessionType.CURRENT_FULL_ACCESS);

			} else {
				Factory.setSessionFactory(new NamedSessionFactory(db, runAs), SessionType.CURRENT);
				Factory.setSessionFactory(new SessionFullAccessFactory(db, runAs), SessionType.CURRENT_FULL_ACCESS);
			}

			TestEnv.session = Factory.getSession(SessionType.CURRENT);
			TestEnv.database = TestEnv.session.getCurrentDatabase();
			if (db != null) {
				assertNotNull(TestEnv.database);
			}

			if (isRunLegacy(method)) {
				WrapperFactory wf = Factory.getWrapperFactory();
				TestEnv.session = wf.toLotus(TestEnv.session);
				TestEnv.database = wf.toLotus(TestEnv.database);
			}

		} catch (NotesException ne) {
			ne.printStackTrace();
			fail(ne.toString());
		}
	}

	@Override
	protected void afterTest(final FrameworkMethod method) {
		WrapperFactory wf = Factory.getWrapperFactory();
		wf.recycle(TestEnv.database);
		wf.recycle(TestEnv.session);
		TestEnv.session = null;
		TestEnv.database = null;
		Factory.termThread();
	}

}
