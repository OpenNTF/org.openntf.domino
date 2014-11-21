package org.openntf.domino.helpers;

import java.io.Serializable;
import java.security.PrivilegedActionException;

import org.openntf.domino.Session;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.DominoUtils;

public class SessionHolder implements Serializable {
	private static final long serialVersionUID = 1L;

	private transient ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
	private ISessionFactory factory_;

	public SessionHolder(final Session delegate, final ISessionFactory sessionFactory) {
		threadSession.set(delegate);
		factory_ = sessionFactory;
	}

	public Session getSession() {

		synchronized (this) {
			if (threadSession == null) {
				threadSession = new ThreadLocal<Session>();
			}
		}

		Session ret = threadSession.get();
		if (ret == null || ret.isDead()) {
			try {
				ret = factory_.createSession();
				threadSession.set(ret);
				return ret;
			} catch (PrivilegedActionException e) {
				DominoUtils.handleException(e);
				return null;
			}
		} else {
			return ret;
		}
	}
}
