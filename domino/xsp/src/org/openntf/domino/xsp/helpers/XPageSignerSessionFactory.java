package org.openntf.domino.xsp.helpers;

import java.security.PrivilegedActionException;

import org.openntf.domino.Session;
import org.openntf.domino.session.AbstractSessionFactory;

import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XPageSignerSessionFactory extends AbstractSessionFactory {
	ThreadLocal<lotus.domino.Session> session = new ThreadLocal<lotus.domino.Session>();
	private boolean fullAccess_;
	private NotesContext notesContext_;

	public XPageSignerSessionFactory(final NotesContext notesContext, final boolean fullAccess) {
		notesContext_ = notesContext;
		fullAccess_ = fullAccess;
	}

	@Override
	public Session createSession() throws PrivilegedActionException {
		lotus.domino.Session rawSession = notesContext_.getSessionAsSigner(fullAccess_);
		return wrapSession(rawSession);
	}

}
