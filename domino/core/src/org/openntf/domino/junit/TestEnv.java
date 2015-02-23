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
