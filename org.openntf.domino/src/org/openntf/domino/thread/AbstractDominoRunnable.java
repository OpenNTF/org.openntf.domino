/**
 * 
 */
package org.openntf.domino.thread;

import java.security.AccessControlContext;
import java.util.Observable;
import java.util.logging.Logger;

import org.openntf.domino.thread.model.IDominoRunnable;
import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
@SuppressWarnings("restriction")
public abstract class AbstractDominoRunnable extends Observable implements IDominoRunnable {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDominoRunnable.class.getName());
	private static final long serialVersionUID = 1L;
	private transient org.openntf.domino.Session session_;
	protected DominoSessionType sessionType_;	//type of session to use for this runnable (may require security context override)
	protected ClassLoader classLoader_;	//optional context class loader to be used by executing thread
	protected AccessControlContext accessContext_;
	protected Runnable runnable_;
	protected String runAs_;

	public AbstractDominoRunnable() {

	}

	public AbstractDominoRunnable(final DominoSessionType sessionType) {
		sessionType_ = sessionType;
	}

	public AbstractDominoRunnable(final DominoSessionType sessionType, final ClassLoader classLoader) {
		sessionType_ = sessionType;
		classLoader_ = classLoader;
	}

	public AbstractDominoRunnable(final ClassLoader classLoader) {
		classLoader_ = classLoader;
	}

	public AbstractDominoRunnable(final DominoSessionType sessionType, final ClassLoader classLoader, final Runnable runnable) {
		sessionType_ = sessionType;
		classLoader_ = classLoader;
		runnable_ = runnable;
	}

	public abstract boolean shouldStop();

	private transient boolean recycle_ = false;

	public boolean shouldRecycle() {
		return recycle_;
	}

	@Override
	public void setSessionType(final DominoSessionType type) {
		sessionType_ = type;
	}

	@Override
	public DominoSessionType getSessionType() {
		if (sessionType_ == null) {
			sessionType_ = DominoSessionType.DEFAULT;
		}
		return sessionType_;
	}

	public void setClassLoader(final ClassLoader classLoader) {
		classLoader_ = classLoader;
	}

	public ClassLoader getClassLoader() {
		return classLoader_;
	}

	@Override
	public void setSession(final lotus.domino.Session session) {
		session_ = Factory.fromLotus(session, org.openntf.domino.Session.SCHEMA, null);
		Factory.setSession(session);
	}

	@Override
	public org.openntf.domino.Session getSession() {
		if (session_ == null) {
			session_ = Factory.getSession();
		}
		return session_;
	}

	public void clean() {
		recycle_ = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContextClassLoader(final ClassLoader classLoader) {
		classLoader_ = classLoader;
	}

	@Override
	public ClassLoader getContextClassLoader() {
		return classLoader_;
	}

	@Override
	public void setAccessControlContext(final AccessControlContext context) {
		accessContext_ = context;
	}

	@Override
	public AccessControlContext getAccessControlContext() {
		return accessContext_;
	}

	@Override
	public String getRunAs() {
		return runAs_;
	}

	@Override
	public void setRunAs(final String username) {
		runAs_ = username;
	}

}
