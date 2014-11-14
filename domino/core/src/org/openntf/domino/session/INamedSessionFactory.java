package org.openntf.domino.session;

import java.security.PrivilegedActionException;

import org.openntf.domino.Session;

public interface INamedSessionFactory {

	public Session createSession(String userName) throws PrivilegedActionException;

}
