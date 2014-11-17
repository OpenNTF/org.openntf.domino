package org.openntf.domino.session;

import java.security.AccessControlContext;
import java.security.AccessController;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.utils.Factory;

public abstract class AbstractSessionFactory implements ISessionFactory {
	private static final long serialVersionUID = 1L;

	protected static final AccessControlContext acc_ = AccessController.getContext();

	private Fixes[] fixes_;

	private AutoMime autoMime_;

	protected String currentApiPath_;

	public AbstractSessionFactory() {

		this(Fixes.values(), 		// it is always a good idea to enable ALL fixes
				AutoMime.WRAP_32K,	// CHECKME RPr: this is the best choice for FOCONIS. For others, too?
				null); 				// All the default sessionfactories do not have a contextDB
	}

	public AbstractSessionFactory(final Fixes[] fixes, final AutoMime autoMime, final String apiPath) {
		fixes_ = fixes;
		autoMime_ = autoMime;
		currentApiPath_ = apiPath;
	}

	public AbstractSessionFactory(final Session source) {
		fixes_ = source.getEnabledFixes();
		autoMime_ = source.getAutoMime();
		// TODO Should we clone event factory and so on also?
		Database currDb = source.getCurrentDatabase();
		currentApiPath_ = currDb == null ? null : currDb.getApiPath();
	}

	public AbstractSessionFactory(final ISessionFactory source) {
		AbstractSessionFactory sf = (AbstractSessionFactory) source;
		fixes_ = sf.fixes_;
		autoMime_ = sf.autoMime_;
		currentApiPath_ = sf.currentApiPath_;
	}

	public AbstractSessionFactory apiPath(final String apiPath) {
		currentApiPath_ = apiPath;
		return this;
	}

	protected Session wrapSession(final lotus.domino.Session raw) {
		org.openntf.domino.impl.Session sess = (org.openntf.domino.impl.Session) Factory.fromLotus(raw, Session.SCHEMA, null);
		sess.setSessionFactory(this);

		for (Fixes fix : fixes_) {
			sess.setFixEnable(fix, true);
		}
		sess.setAutoMime(autoMime_);
		if (currentApiPath_ != null) {
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
