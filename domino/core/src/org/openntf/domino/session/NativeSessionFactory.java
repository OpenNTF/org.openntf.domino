package org.openntf.domino.session;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;

public class NativeSessionFactory extends AbstractSessionFactory {
	private static final long serialVersionUID = 1L;

	public NativeSessionFactory(final Session source) {
		super(source);
	}

	public NativeSessionFactory(final Fixes[] fixes, final AutoMime autoMime, final String apiPath) {
		super(fixes, autoMime, apiPath);
	}

	public NativeSessionFactory(final ISessionFactory source) {
		super(source);
	}

	@Override
	public Session createSession() throws PrivilegedActionException {
		lotus.domino.Session raw = AccessController.doPrivileged(new PrivilegedExceptionAction<lotus.domino.Session>() {
			@Override
			public lotus.domino.Session run() throws Exception {
				//				return lotus.domino.local.Session.createSession();
				// it is too dangerous to disable the SM temporary.
				// it might happen that an other thread runs while we create the session
				// and queries the SM -> bang.
				synchronized (SecurityManager.class) {
					SecurityManager oldSm = System.getSecurityManager();
					System.setSecurityManager(null);
					try {
						return lotus.domino.local.Session.createSession();
					} finally {
						System.setSecurityManager(oldSm);
					}
				}
			}
		}, acc_);
		return wrapSession(raw, true);
	}
}
