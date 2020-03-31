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

import lotus.domino.Session;

import com.ibm.commons.util.StringUtil;

public enum TestEnv {
	;
	/** The username of the master session */
	public static String SESSION_USER;

	/** The effective username of the master session */
	public static String EFFECTIVE_USER;

	public static boolean RUNS_ON_SERVER;

	// TODO RPR
	public static String REMOTE_UNTRUSTED_SERVER;

	public static String REMOTE_TRUSTED_SERVER;

	public static void setup(final Session sess) throws lotus.domino.NotesException {
		// TODO Auto-generated method stub
		SESSION_USER = sess.getUserName();
		EFFECTIVE_USER = sess.getEffectiveUserName();
		RUNS_ON_SERVER = sess.isOnServer();
		REMOTE_TRUSTED_SERVER = sess.getEnvironmentString("junit_remote_trusted_server", true);
		REMOTE_UNTRUSTED_SERVER = sess.getEnvironmentString("junit_remote_untrusted_server", true);
		if (StringUtil.isEmpty(REMOTE_TRUSTED_SERVER) || StringUtil.isEmpty(REMOTE_UNTRUSTED_SERVER)) {
			//			Assert.fail("Please set up the Notes.ini variables junit_remote_untrusted_server and junit_remote_trusted_server");
		}
	}

	public static lotus.domino.Session session;

	public static lotus.domino.Database database;
}
