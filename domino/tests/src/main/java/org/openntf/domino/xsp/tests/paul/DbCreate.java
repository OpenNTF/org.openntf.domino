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
package org.openntf.domino.xsp.tests.paul;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class DbCreate implements Runnable {

	public DbCreate() {

	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DbCreate(), TestRunnerUtil.NATIVE_SESSION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Session sess = Factory.getSession(SessionType.NATIVE);
			Database db3 = sess.createBlankDatabase("temp\\demo1.nsf");
			System.out.println(db3.getApiPath());
			Database db2 = sess.createBlankDatabaseAbsolutePath("C:/PaulTemp/ODATest/oda2/demo1.nsf");
			System.out.println(db2.getApiPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
