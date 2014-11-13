package org.openntf.domino.session;

import java.io.Serializable;
import java.security.PrivilegedActionException;

import org.openntf.domino.Session;

public interface ISessionFactory extends Serializable {

	public Session createSession() throws PrivilegedActionException;

}
