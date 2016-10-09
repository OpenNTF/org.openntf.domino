package org.openntf.domino.session;

import org.openntf.domino.Session;

public class NamedSessionFactory extends AbstractSessionFactory implements INamedSessionFactory {
	private static final long serialVersionUID = 1L;
	final private String runAs_;

	public NamedSessionFactory(final String apiPath) {
		super(apiPath);
		runAs_ = null;
	}

	public NamedSessionFactory(final String apiPath, final String runAs) {
		super(apiPath);
		runAs_ = runAs;
	}


	@Override
	public Session createSession() {
		return createSession(runAs_);
	}

	@Override
	public Session createSession(final String userName) {
		if (userName == null)
			throw new NullPointerException();
		lotus.domino.Session raw = LotusSessionFactory.createSessionWithTokenEx(userName);
		return wrapSession(raw, true);
	}
}
