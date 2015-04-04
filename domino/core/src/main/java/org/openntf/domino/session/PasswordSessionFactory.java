package org.openntf.domino.session;

import org.openntf.domino.Session;

public class PasswordSessionFactory extends AbstractSessionFactory implements INamedSessionFactory {
	private static final long serialVersionUID = 1L;
	final private String password_;

	public PasswordSessionFactory(final String apiPath) {
		super(apiPath);
		password_ = null;
	}

	public PasswordSessionFactory(final String apiPath, final String password) {
		super(apiPath);
		password_ = password;
	}

	@Override
	public Session createSession() {
		return createSession(password_);
	}

	@Override
	public Session createSession(final String password) {
		if (password == null)
			throw new NullPointerException();
		lotus.domino.Session raw = LotusSessionFactory.createSessionWithPassword(password);
		return wrapSession(raw, true);
	}
}
