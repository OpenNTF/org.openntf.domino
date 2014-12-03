package org.openntf.domino.xsp.session;

import java.security.PrivilegedActionException;

import org.openntf.domino.Session;

import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XPageSignerSessionFactory extends AbstractXPageSessionFactory {
	private static final long serialVersionUID = 1L;
	private boolean fullAccess_;

	public XPageSignerSessionFactory(final boolean fullAccess) {
		fullAccess_ = fullAccess;
	}

	@Override
	public Session createSession() throws PrivilegedActionException {
		lotus.domino.Session rawSession = NotesContext.getCurrent().getSessionAsSigner(fullAccess_);
		return wrapSession(rawSession, false); //contextDB is set in notesContext
	}

}
