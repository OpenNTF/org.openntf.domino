package org.openntf.domino.thread.model;

import java.io.Serializable;
import java.util.concurrent.Future;

/*
 * This is the layer responsible for setup & teardown of sessions
 */
public interface IDominoRunner extends Future<Serializable>, Serializable {

	public void setSession(lotus.domino.Session session);

	public org.openntf.domino.Session getSession();

	public void setClassLoader(ClassLoader cl);

	public ClassLoader getClassLoader();

}
