package org.openntf.domino.junit;

import lotus.domino.Session;

public enum TestProps {
	;
	/** The username of the master session */
	public static String SESSION_USER;

	/** The effective username of the master session */
	public static String EFFECTIVE_USER;

	public static boolean RUNS_ON_SERVER;

	public static String REMOTE_UNTRUSTED_SERVER = "CN=srv-01-nprod/O=FOCONIS";

	public static String REMOTE_TRUSTED_SERVER = "CN=srv-01-ntest85/OU=srv/O=FOCONIS";

	public static void setup(final Session sess) throws lotus.domino.NotesException {
		// TODO Auto-generated method stub
		SESSION_USER = sess.getUserName();
		EFFECTIVE_USER = sess.getEffectiveUserName();
		RUNS_ON_SERVER = sess.isOnServer();
		System.out.println("Test isOnServer = " + RUNS_ON_SERVER + ", user = " + SESSION_USER + " effective " + EFFECTIVE_USER);

	}
}
