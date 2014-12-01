package org.openntf.domino.session;

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
	public Session createSession() {
		lotus.domino.Session raw = LotusSessionFactory.createSession();
		return wrapSession(raw, true);
	}
}
