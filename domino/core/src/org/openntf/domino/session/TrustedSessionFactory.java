package org.openntf.domino.session;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;

public class TrustedSessionFactory extends AbstractSessionFactory {
	private static final long serialVersionUID = 1L;

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
	public Session createSession() {
		lotus.domino.Session raw = LotusSessionFactory.createTrustedSession();
		return wrapSession(raw, true);
	}

}