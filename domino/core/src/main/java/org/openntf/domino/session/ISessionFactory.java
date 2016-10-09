package org.openntf.domino.session;

import java.io.Serializable;

import org.openntf.domino.Session;

public interface ISessionFactory extends Serializable {

	public Session createSession();

	// public String getName();
}
