package org.openntf.domino.xsp.helpers;

import java.security.PrivilegedActionException;

import org.openntf.domino.Session;
import org.openntf.domino.session.ISessionFactory;

public class InvalidSessionFactory implements ISessionFactory {

	@Override
	public Session createSession() throws PrivilegedActionException {
		throw new IllegalStateException("Could not create session, because code is not properly signed");
	}

}
