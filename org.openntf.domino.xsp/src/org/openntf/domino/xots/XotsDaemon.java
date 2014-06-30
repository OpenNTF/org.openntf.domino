package org.openntf.domino.xots;

import org.openntf.domino.helpers.TrustedDispatcher;

/*
 * This class and package is intended to become the space for the XPages implementation
 * of IBM's DOTS. Except it will use modern thread management instead of acting like it was
 * written in Java 1.1
 */
public class XotsDaemon extends TrustedDispatcher {

	public XotsDaemon() {
		// TODO Auto-generated constructor stub
	}

	public XotsDaemon(final long delay) {
		super(delay);
		// TODO Auto-generated constructor stub
	}

}
