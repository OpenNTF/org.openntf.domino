package org.openntf.domino.xsp.helpers;

import java.security.PrivilegedActionException;

import lotus.domino.NotesException;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.impl.Base;
import org.openntf.domino.session.AbstractSessionFactory;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.designer.runtime.domino.bootstrap.util.StringUtil;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.xsp.XSPNative;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XPageCurrentSessionFactory extends AbstractSessionFactory {
	private String runAs_;

	public XPageCurrentSessionFactory(final Fixes[] fixes, final AutoMime autoMime) {
		super(fixes, autoMime, null);

	}

	public XPageCurrentSessionFactory() {
		final lotus.domino.Session rawSession = NotesContext.getCurrent().getCurrentSession();
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

	}

	@Override
	public Session createSession() throws PrivilegedActionException {
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		lotus.domino.Session rawSession = ctx == null ? null : ctx.getCurrentSession();

		if (rawSession == null || Base.isDead(rawSession)) {
			try {
				long userHandle = NotesUtil.createUserNameList(runAs_);
				rawSession = XSPNative.createXPageSession(runAs_, userHandle, false, true);
				return wrapSession(rawSession, true);
			} catch (Exception e) {
				DominoUtils.handleException(e);
				return null;
			}
		} else {
			return wrapSession(rawSession, false);
		}
	}

}
