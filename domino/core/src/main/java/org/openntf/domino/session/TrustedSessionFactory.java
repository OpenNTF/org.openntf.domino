package org.openntf.domino.session;

import org.openntf.domino.Session;

public class TrustedSessionFactory extends AbstractSessionFactory {
	private static final long serialVersionUID = 1L;

	public TrustedSessionFactory(final String apiPath) {
		super(apiPath);
	}

	@Override
	public Session createSession() {
		lotus.domino.Session raw = LotusSessionFactory.createTrustedSession();
		return wrapSession(raw, true);
	}

}