package org.openntf.domino.session;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;

public class NamedSessionFactory extends AbstractSessionFactory implements INamedSessionFactory {
	private static final long serialVersionUID = 1L;
	final private String runAs_;

	public NamedSessionFactory(final org.openntf.domino.Session source) {
		super(source);
		runAs_ = source.getEffectiveUserName();
	}

	public NamedSessionFactory(final Fixes[] fixes, final AutoMime autoMime, final String apiPath, final String runAs) {
		super(fixes, autoMime, apiPath);
		runAs_ = runAs;
	}

	public NamedSessionFactory(final ISessionFactory source, final String runAs) {
		super(source);
		runAs_ = runAs;
	}

	@Override
	public Session createSession() {
		return createSession(runAs_);
	}

	@Override
	public Session createSession(final String userName) {
		lotus.domino.Session raw = LotusSessionFactory.createSessionWithTokenEx(userName);
		return wrapSession(raw, true);
	}
}
