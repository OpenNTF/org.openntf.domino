package org.openntf.domino.session;

import org.openntf.domino.Session;

public class SessionFullAccessFactory extends AbstractSessionFactory implements INamedSessionFactory {
	private static final long serialVersionUID = 1L;
	final private String runAs_;

	public SessionFullAccessFactory(final String apiPath, final String runAs) {
		super(apiPath);
		runAs_ = runAs;
	}

	public SessionFullAccessFactory(final String apiPath) {
		this(apiPath, null);
	}

	@Override
	public Session createSession() {
		return createSession(runAs_);
	}

	@Override
	public Session createSession(final String userName) {
		if (userName != null) {
			//			Thread.currentThread().interrupt();
			throw new UnsupportedOperationException("SessionType FULL_ACCESS as user " + String.valueOf(userName)
					+ " is not (yet)supported in Domino environment: ");
		}
		lotus.domino.Session raw = LotusSessionFactory.createSessionWithFullAccess(userName);
		return wrapSession(raw, true);
	}

}
