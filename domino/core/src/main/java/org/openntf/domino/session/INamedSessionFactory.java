package org.openntf.domino.session;

import org.openntf.domino.Session;

public interface INamedSessionFactory {

	public Session createSession(String userName);

}
