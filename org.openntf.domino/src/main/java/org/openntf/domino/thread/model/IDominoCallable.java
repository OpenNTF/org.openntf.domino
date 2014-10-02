package org.openntf.domino.thread.model;

import java.io.Serializable;
import java.security.AccessControlContext;
import java.util.concurrent.Callable;

import org.openntf.domino.thread.DominoSessionType;

/*
 * NTF Why Serializable?
 * To make sure you declare any Domino objects that aren't themselves Serializable as transient
 * Code-checkers and compiler warnings will track this stuff down for you.
 * Also we want to make sure you don't try to return a Domino object. Returning a Document, even an OpenNTF one,
 * is likely to have nasty side-effects unless you know exactly what you're doing, in which case you can write
 * your own concurrency model. :-)
 */
public interface IDominoCallable<V> extends Callable<Serializable>, Serializable {
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
