package org.openntf.domino.thread.model;

public enum SessionType {

	/**
	 * Use the native session. Session is created with NotesFactory.createSession()
	 * 
	 * User and Effective user is the current server (or on client the current user)
	 */
	NATIVE,

	/**
	 * Use a trusted session. Session is created with NotesFactory.createTrustedSession()
	 * 
	 * Applications running on a server installation that need to access databases on a remote server must have either a Trusted Server
	 * relationship, or a Trusted Session. The userID authority that the application is running under must be accounted for in the ACL of
	 * the remote database. That userID is often the serverID.
	 */
	TRUSTED,

	/**
	 * Use a full access session. Session is created with NotesFactory.createSessionWithFullAccess()
	 * 
	 * Access is not restricted to readers/authors fields
	 */
	FULL_ACCESS,

	/**
	 * Use a named Session is created with NotesFactory.createSessionWithFullAccess()
	 * 
	 * Access is not restricted to readers/authors fields
	 */
	NAMED,

	/**
	 * A Named XSP session is created. This type works only in XSP-environment. (Fallback to NATIVE)
	 * 
	 * This is done by NotesContext.initRequest(HttpServletRequest). It also intialize session.currentDatabase with the module's database
	 * and some other important things in the NotesContext.
	 * 
	 */
	XSP_NAMED,

	/**
	 * The Session as signer is created. NotesContext.getSessionAsSigner(false) is used.
	 * 
	 * Note that this type of session works ONLY if the loaded classes are properly signed.
	 */
	XSP_SIGNER,

	/**
	 * The Session as signer is created. NotesContext.getSessionAsSigner(true) is used.
	 * 
	 * Note that this type of session works ONLY if the loaded classes are properly signed.
	 */
	XSP_SIGNER_FULL_ACCESS,

	/**
	 * Clone the current session
	 */
	XSP_NAMED_CLONE,

	/**
	 * Use session as signer
	 */
	SIGNER,
	/**
	 * Use session as signer (with full access)
	 */
	SIGNER_FULL_ACCESS,
	/**
	 * ???
	 */
	MANUAL,

	NAMED_FULL_ACCESS,

	/**
	 * ???
	 */
	DEFAULT,
	/**
	 * Do not create a session
	 */
	NONE
}