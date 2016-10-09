package org.openntf.domino.xsp.session;

import org.openntf.domino.Session;
import org.openntf.domino.session.INamedSessionFactory;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.domino.napi.c.xsp.XSPNative;

public class XPageNamedSessionFactory extends AbstractXPageSessionFactory implements INamedSessionFactory {
	private static final long serialVersionUID = 1L;

	private boolean fullAccess_;
	private String runAs_;

	public XPageNamedSessionFactory(final String runAs, final boolean fullAccess) {
		super();
		fullAccess_ = fullAccess;
		runAs_ = runAs;
	}

	public XPageNamedSessionFactory(final boolean fullAccess) {
		super();
		fullAccess_ = fullAccess;
	}

	@Override
	public Session createSession() {
		if (runAs_ == null)
			throw new NullPointerException();
		return createSession(runAs_);
	}

	@Override
	public Session createSession(final String userName) {
		try {
			final long userHandle = createUserNameList(userName);
			lotus.domino.Session rawSession = XSPNative.createXPageSessionExt(userName, userHandle, false, true, fullAccess_);
			return wrapSession(rawSession, true);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

}
