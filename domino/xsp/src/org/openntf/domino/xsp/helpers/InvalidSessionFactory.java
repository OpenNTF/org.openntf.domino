package org.openntf.domino.xsp.helpers;

import org.openntf.domino.Session;
import org.openntf.domino.session.ISessionFactory;

public class InvalidSessionFactory implements ISessionFactory {

	@Override
	public Session createSession() {
		throw new IllegalStateException("Could not create session, because code is not properly signed");
	}

}
