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

	protected transient Database internalDb;
	protected SessionHolder sessionHolder;
	public String apiPath;

	/**
	 * create a new Holder for this database
	 * 
	 * @param db
	 */
	public DatabaseHolder(final Database db, final SessionHolder sh) {
		internalDb = db;
		sessionHolder = sh;
		apiPath = db.getApiPath();
	}

	/**
	 * get or reopen the database from the current session
	 * 
	 * @return the database
	 */
	public Database getDatabase() {
		if (internalDb == null || internalDb.isDead()) {
			internalDb = sessionHolder.getSession().getDatabase(apiPath);
		}
		return internalDb;
	}
}
