package org.openntf.domino.xsp.helpers;

import org.openntf.domino.xsp.Activator;

public abstract class AbstractListener {
	private final static boolean _debug = Activator.isDebug();

	protected void _debugOut(String message) {
		if (_debug)
			System.out.println(getClass().getName() + " " + message);
	}
}
