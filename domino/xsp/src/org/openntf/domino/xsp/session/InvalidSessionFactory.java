package org.openntf.domino.xsp.session;

import org.openntf.domino.Session;
import org.openntf.domino.session.ISessionFactory;

public class InvalidSessionFactory implements ISessionFactory {
	private static final long serialVersionUID = 1L;

	@Override
	public Session createSession() {
		throw new IllegalStateException("Could not create session, because code is not properly signed");
	}

}
