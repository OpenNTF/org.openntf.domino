package org.openntf.domino.thread.model;

import java.io.Serializable;
import java.security.AccessControlContext;

import org.openntf.domino.thread.DominoSessionType;

/*
 * Implement this interface for all your Domino concurrency needs. You aren't required to set a classloader, 
 * but it's generally a good idea if you want to make sure that your Runnable process has access to 
 * the context from your OSGi plugin, XPage app or general NSF.
 * 
 * NTF Why Serializable?
 * To make sure you declare any Domino objects that aren't themselves Serializable as transient
 * Code-checkers and compiler warnings will track this stuff down for you.
 */
public interface IDominoRunnable extends Runnable, Serializable {
	public void setSessionType(final DominoSessionType type);

	public DominoSessionType getSessionType();

	public void setContextClassLoader(final ClassLoader classLoader);

	public ClassLoader getContextClassLoader();

	public void setAccessControlContext(AccessControlContext context);

	public AccessControlContext getAccessControlContext();

	public void setSession(final lotus.domino.Session session);

	public org.openntf.domino.Session getSession();

	public String getRunAs();

	public void setRunAs(String username);
}
