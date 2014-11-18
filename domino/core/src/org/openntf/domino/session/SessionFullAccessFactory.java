package org.openntf.domino.session;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;

public class SessionFullAccessFactory extends AbstractSessionFactory implements INamedSessionFactory {
	private static final long serialVersionUID = 1L;
	final private String runAs_;

	public SessionFullAccessFactory() {
		super();
		runAs_ = null;
	}

	public SessionFullAccessFactory(final String runAs) {
		super();
		runAs_ = runAs;
	}

	public SessionFullAccessFactory(final org.openntf.domino.Session source) {
		super(source);
		runAs_ = source.getEffectiveUserName();
	}

	public SessionFullAccessFactory(final Fixes[] fixes, final AutoMime autoMime, final String apiPath, final String runAs) {
		super(fixes, autoMime, apiPath);
		runAs_ = runAs;
	}

	public SessionFullAccessFactory(final Fixes[] fixes, final AutoMime autoMime, final String apiPath) {
		this(fixes, autoMime, apiPath, null);
	}

	public SessionFullAccessFactory(final ISessionFactory source, final String runAs) {
		super(source);
		runAs_ = runAs;
	}

	@Override
	public Session createSession() throws PrivilegedActionException {
		return createSession(runAs_);
	}

	@Override
	public Session createSession(final String userName) throws PrivilegedActionException {
		if (userName != null) {
			throw new UnsupportedOperationException("This SessionType is not supported in Domino environment");
		}
		lotus.domino.Session raw = AccessController.doPrivileged(new PrivilegedExceptionAction<lotus.domino.Session>() {

			@Override
			public lotus.domino.Session run() throws Exception {
				return lotus.domino.local.Session.createSessionWithFullAccess(userName);
			}
		}, acc_);
		return wrapSession(raw);
	}

}
