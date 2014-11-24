package org.openntf.domino.xsp.helpers;

import java.security.PrivilegedActionException;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.impl.Base;
import org.openntf.domino.session.AbstractSessionFactory;
import org.openntf.domino.session.INamedSessionFactory;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.designer.runtime.domino.bootstrap.util.StringUtil;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.xsp.XSPNative;

public class XPageSessionFactory extends AbstractSessionFactory implements INamedSessionFactory {
	ThreadLocal<lotus.domino.Session> session = new ThreadLocal<lotus.domino.Session>();
	private boolean fullAccess_;
	private String runAs_;

	public XPageSessionFactory(final lotus.domino.Session rawSession, final boolean fullAccess) {
		session.set(rawSession);
		try {
			runAs_ = rawSession.getEffectiveUserName();
			lotus.domino.Database rawDb = rawSession.getCurrentDatabase();
			if (rawDb != null) {
				if (StringUtil.isEmpty(rawDb.getServer())) {
					currentApiPath_ = rawDb.getFilePath();
				} else {
					currentApiPath_ = rawDb.getServer() + "!!" + rawDb.getFilePath();
				}
			}
		} catch (NotesException e) {
			e.printStackTrace();
		}
		fullAccess_ = fullAccess;
	}

	public XPageSessionFactory(final boolean fullAccess) {
		fullAccess_ = fullAccess;
	}

	public XPageSessionFactory(final String runAs, final boolean fullAccess) {
		fullAccess_ = fullAccess;
		runAs_ = runAs;
	}

	@Override
	public Session createSession() throws PrivilegedActionException {
		lotus.domino.Session rawSession = session.get();
		if (rawSession == null || Base.isDead(rawSession)) {
			try {
				long userHandle = NotesUtil.createUserNameList(runAs_);
				rawSession = XSPNative.createXPageSessionExt(runAs_, userHandle, false, true, fullAccess_);
				return wrapSession(rawSession, true);
			} catch (Exception e) {
				DominoUtils.handleException(e);
			}
		} else {
			return wrapSession(rawSession, false);
		}
		return null;
	}

	@Override
	public Session createSession(final String userName) throws PrivilegedActionException {
		try {
			long userHandle = NotesUtil.createUserNameList(userName);
			lotus.domino.Session rawSession = XSPNative.createXPageSessionExt(userName, userHandle, false, true, fullAccess_);
			return wrapSession(rawSession, true);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

}
