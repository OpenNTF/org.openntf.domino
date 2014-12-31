package org.openntf.domino.session;

import java.security.AccessControlContext;
import java.security.AccessController;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.utils.Factory;

public abstract class AbstractSessionFactory implements ISessionFactory {
	private static final long serialVersionUID = 1L;

	protected static final AccessControlContext acc_ = AccessController.getContext();

	protected String currentApiPath_;

	// No - we should respect the settings of the App wherever it is possible
	//	public AbstractSessionFactory() {
	//
	//		this(Fixes.values(), 		// it is always a good idea to enable ALL fixes
	//				AutoMime.WRAP_32K,	// CHECKME RPr: this is the best choice for FOCONIS. For others, too?
	//				null); 				// All the default sessionfactories do not have a contextDB
	//	}

	public AbstractSessionFactory(final String apiPath) {
		currentApiPath_ = apiPath;
	}

	protected Session wrapSession(final lotus.domino.Session raw, final boolean selfCreated) {
		WrapperFactory wf = Factory.getWrapperFactory();
		org.openntf.domino.Session sess = Factory.fromLotus(raw, Session.SCHEMA, null);
		sess.setNoRecycle(!selfCreated);
		((org.openntf.domino.impl.Session) sess).setSessionFactory(this);

		Fixes[] fixes = org.openntf.domino.ext.Session.Fixes.values();
		if (fixes != null) {
			for (Fixes fix : fixes) {
				sess.setFixEnable(fix, true);
			}
		}
		sess.setAutoMime(AutoMime.WRAP_ALL);

		sess.setConvertMIME(false);
		if (selfCreated && currentApiPath_ != null) {
			Database db = sess.getCurrentDatabase();
			if (db == null) {
				db = sess.getDatabase(currentApiPath_);
				setCurrentDatabase(sess, db);
			}
		}

		return sess;
	}

	/**
	 * This method may be overwritten by special XPage databases
	 * 
	 * @param sess
	 * @param db
	 */
	protected void setCurrentDatabase(final Session sess, final Database db) {
		sess.setCurrentDatabase(db);
	}
}
