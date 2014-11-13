package org.openntf.domino.thread.model;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * The session type for XOTS Tasklets: Note that CLONE, CLONE_FULL_ACCESS, SIGNER, SIGNER_FULL_ACCESS are XPage-Sessions and may be
 * restricted by maximumInternetAccess
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public enum XotsSessionType {

	/**
	 * Clones the {@link SessionType#CURRENT CURRENT} Session from the current context. This means if you are running in a XPage, you have
	 * the same user session in your XOTS Runnable. If you are running a scheduled servlet, you have a
	 * {@link Factory.SessionType#SESSION_AS_SIGNER SESSION_AS_SIGNER}.
	 * 
	 * This Session may be created with createXPageSession and may be restricted to maximum internet access!
	 */
	CLONE,

	/**
	 * @See {@link #CLONE} but with full access
	 */
	CLONE_FULL_ACCESS,

	/**
	 * Run as signer of that Runnable.class. If the Runnable is inside an NSF. It will run with a named session of the Signer. If the
	 * Runnable is a Plugin-Java object, it will run with the Server-ID
	 * 
	 * This Session may be created with createXPageSession and may be restricted to maximum internet access!
	 */
	SIGNER,

	/**
	 * @See {@link #SIGNER} but with full access
	 */
	SIGNER_FULL_ACCESS,

	/**
	 * Use the native session. Session is created with NotesFactory.createSession()
	 * 
	 * User and Effective user is the current server (or on client the current user) This Session should not be restricted by maximum
	 * internet access!
	 */
	NATIVE,

	/**
	 * Use a full access session. Session is created with NotesFactory.createSessionWithFullAccess()
	 * 
	 * Access is not restricted to readers/authors fields
	 */
	FULL_ACCESS,

	/**
	 * Use a trusted session. Session is created with NotesFactory.createTrustedSession()<br>
	 * 
	 * Applications running on a server installation that need to access databases on a remote server must have either a Trusted Server
	 * relationship, or a Trusted Session. The userID authority that the application is running under must be accounted for in the ACL of
	 * the remote database. That userID is often the serverID.
	 * 
	 * <font color=red>This does NOT yet work</font>
	 */
	TRUSTED,

	/**
	 * Do not create a session
	 */
	NONE
}