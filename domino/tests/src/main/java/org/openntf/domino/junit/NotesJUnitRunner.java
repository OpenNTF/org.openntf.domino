/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import lotus.domino.NotesException;
import lotus.domino.NotesThread;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.Factory;

@SuppressWarnings("nls")
public class NotesJUnitRunner extends AbstractJUnitRunner {
	private lotus.domino.Session mastersession;

	public NotesJUnitRunner(final Class<?> testClass) throws InitializationError {
		super(testClass);
	}

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

	}

	@Override
	protected void tearDown() {
		try {
			if (mastersession != null)
				mastersession.recycle();
		} catch (NotesException e) {
			e.printStackTrace();
		}
		NotesThread.stermThread();
	}

	@Override
	protected void beforeTest(final FrameworkMethod method) {
		try {
			String runAs = getRunAs(method);
			if (runAs == null) {
				TestEnv.session = lotus.domino.local.Session.createSession();
			} else {
				TestEnv.session = lotus.domino.local.Session.createSessionWithTokenEx(runAs);
			}
			assertNotNull(TestEnv.session);
			String db = getDatabase(method);
			if (db != null) {
				String server = "";
				String dbpath = db;
				int sep;
				if ((sep = db.indexOf("!!")) > -1) {
					server = db.substring(0, sep);
					dbpath = db.substring(sep + 2);
				}
				TestEnv.database = TestEnv.session.getDatabase(server, dbpath);
				assertNotNull(TestEnv.database);
			}

		} catch (NotesException ne) {
			ne.printStackTrace();
			fail(ne.toString());
		}
	}

	@Override
	protected void afterTest(final FrameworkMethod method) {
		// TODO Auto-generated method stub
		WrapperFactory wf = Factory.getWrapperFactory();
		wf.recycle(TestEnv.database);
		wf.recycle(TestEnv.session);
		TestEnv.session = null;
		TestEnv.database = null;
	}

}
