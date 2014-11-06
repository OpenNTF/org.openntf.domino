package org.openntf.domino.thread.model;

/**
 * The Scope determines the concurrency
 */
public enum Scope {
	/**
	 * One instance per server is allowed
	 */
	SERVER,

	/**
	 * One instance per template
	 */
	TEMPLATE,

	/**
	 * One instance per application
	 */
	APPLICATION,

	/**
	 * One instance per server and user is allowed
	 */
	SERVER_USER,
	/**
	 * One instance per template and user is allowed
	 */
	TEMPLATE_USER,
	/**
	 * One instance per application and user is allowed
	 */
	APPLICATION_USER,

	/**
	 * No scope, you can run as many instances as you have resource
	 */
	NONE
}