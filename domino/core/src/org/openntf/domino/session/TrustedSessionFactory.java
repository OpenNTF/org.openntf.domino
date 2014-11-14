package org.openntf.domino.session;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;

public class TrustedSessionFactory extends AbstractSessionFactory {
	private static final long serialVersionUID = 1L;

	public TrustedSessionFactory() {
		super();
	}

	public TrustedSessionFactory(final Session source) {
		super(source);
	}

	public TrustedSessionFactory(final Fixes[] fixes, final AutoMime autoMime, final String apiPath) {
		super(fixes, autoMime, apiPath);
	}

	public TrustedSessionFactory(final ISessionFactory source) {
		super(source);
	}

	@Override
	public Session createSession() throws PrivilegedActionException {
		lotus.domino.Session raw = AccessController.doPrivileged(new PrivilegedExceptionAction<lotus.domino.Session>() {
			@Override
			public lotus.domino.Session run() throws Exception {
				synchronized (SecurityManager.class) {
					SecurityManager oldSm = System.getSecurityManager();
					System.setSecurityManager(null);
					try {
						return lotus.domino.local.Session.createTrustedSession();
					} finally {
						System.setSecurityManager(oldSm);
					}
				}
			}
		}, acc_);
		return wrapSession(raw);
	}

}