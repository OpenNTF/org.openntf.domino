package org.openntf.domino.helpers;

import java.io.Serializable;

import org.openntf.domino.Database;

/**
 * DatabaseHolder is a serializable object, so you can use this object to store a Database across several XPage-Requests
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class DatabaseHolder implements Serializable {
	private static final long serialVersionUID = 1L;

	protected SessionHolder sessionHolder;
	public String apiPath;

	private transient ThreadLocal<Database> threadDatabase = new ThreadLocal<Database>();

	public DatabaseHolder(final Database delegate) {
		threadDatabase.set(delegate);
		sessionHolder = delegate.getAncestorSession().getSessionHolder();
		apiPath = delegate.getApiPath();
	}

	public Database getDatabase() {

		synchronized (this) {
			if (threadDatabase == null) {
				threadDatabase = new ThreadLocal<Database>();
			}
		}

		Database ret = threadDatabase.get();
		if (ret == null || ret.isDead()) {
			ret = sessionHolder.getSession().getDatabase(apiPath);
			threadDatabase.set(ret);
			return ret;
		} else {
			return ret;
		}
	}

}
