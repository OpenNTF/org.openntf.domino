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
package org.openntf.domino.tests.paul;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class TimeSensitive implements Runnable {

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new TimeSensitive(), TestRunnerUtil.NATIVE_SESSION);
	}

	@Override
	public void run() {
		Session s = Factory.getSession(SessionType.NATIVE);
		DbDirectory dir = s.getDbDirectory(s.getServerName());
		for (Database db : dir) {
			if (null == db) {
				//System.out.println("Database " + db.getFilePath() + " is null");
			} else {
				try {
					for (View vw : db.getViews()) {
						if (vw.isTimeSensitive()) {
							System.out.println("TIME SENSITIVE: View " + vw.getName() + " in db " + db.getFilePath());
						}
					}
				} catch (Exception e) {
					System.out.println("Database " + db.getFilePath() + " cannot be opened");
				}
			}
		}
	}

}
