package org.openntf.domino.xsp.helpers;

import org.openntf.domino.xsp.Activator;

/**
 * AbstractListener class
 */
public abstract class AbstractListener {
	private final static boolean _debug = Activator.isDebug();

	/**
	 * Prints a message to the server console
	 * 
	 * @param message
	 *            String to print
	 * @since org.openntf.domino.xsp 2.5.0
	 */
	protected void _debugOut(final String message) {
		if (_debug)
			System.out.println(getClass().getName() + " " + message);
	}
}
