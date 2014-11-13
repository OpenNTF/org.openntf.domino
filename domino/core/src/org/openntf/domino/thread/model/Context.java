package org.openntf.domino.thread.model;

public enum Context {
	/**
	 * Run in XOTS Context
	 */
	XOTS,
	/**
	 * Run in XSP Context: with access to the Xsp dependencies, but without any context (faces or otherwise)
	 */
	XSPBARE,

	/**
	 * would be "it's running with access to the scoped variables within it's environment." So that would set up access to
	 * ApplicationScoped, ServerScope, IdentityScope in it
	 */
	XSPSCOPED,
	/**
	 * would force the Tasklet to run in the same NSFComponentModule context as any given Xpage this would force the Application to be
	 * activated, thus triggering ApplicationListeners, for instance
	 */
	XSPFORCE
}