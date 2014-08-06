package org.openntf.domino.helpers;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import org.openntf.domino.Database;
import org.openntf.domino.Session;

/**
 * This class is mainly used in the DbDirectory to hold database references, without to reference the Database-object itself.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class DatabaseHolder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String filePath_;
	private final Date lastModifiedDate_;
	private final String apiPath_;
	private final String title_;
	private final String server_;
	private Database closedDb_;

	/**
	 * Comparator to allow easy checking whether two databases have the same API path (server!!filepath)
	 * 
	 * @since org.openntf.domino 5.0.0
	 */
	public final static Comparator<DatabaseHolder> APIPATH_COMPARATOR = new Comparator<DatabaseHolder>() {
		@Override
		public int compare(final DatabaseHolder o1, final DatabaseHolder o2) {
			return o1.getApiPath().compareToIgnoreCase(o2.getApiPath());
		}
	};

	/**
	 * Comparator to allow easy checking whether two databases have the same filepath (e.g. on different servers)
	 * 
	 * @since org.openntf.domino 5.0.0
	 */
	public final static Comparator<DatabaseHolder> FILEPATH_COMPARATOR = new Comparator<DatabaseHolder>() {
		@Override
		public int compare(final DatabaseHolder o1, final DatabaseHolder o2) {
			return o1.getFilePath().compareToIgnoreCase(o2.getFilePath());
		}
	};

	/**
	 * Comparator to alow easy checking whether database A was modified before/after database B
	 * 
	 * @since org.openntf.domino 5.0.0
	 */
	public final static Comparator<DatabaseHolder> LASTMOD_COMPARATOR = new Comparator<DatabaseHolder>() {
		@Override
		public int compare(final DatabaseHolder o1, final DatabaseHolder o2) {
			return o1.getLastModifiedDate().compareTo(o2.getLastModifiedDate());
		}
	};

	/**
	 * Comparator to allow easy checking whether two databases have the same title
	 * 
	 * @since org.openntf.domino 5.0.0
	 */
	public final static Comparator<DatabaseHolder> TITLE_COMPARATOR = new Comparator<DatabaseHolder>() {
		@Override
		public int compare(final DatabaseHolder o1, final DatabaseHolder o2) {
			return o1.getTitle().compareToIgnoreCase(o2.getTitle());
		}
	};

	public DatabaseHolder(final Database db) {
		apiPath_ = db.getApiPath();
		title_ = db.getTitle();
		server_ = db.getServer();
		filePath_ = db.getFilePath();

		if (db.isOpen()) {
			lastModifiedDate_ = db.getLastModifiedDate();
		} else {
			closedDb_ = db; // as we cannot get closed databases from the session, we must store this here
			lastModifiedDate_ = null;
		}
	}

	public String getTitle() {
		return title_;
	}

	public String getApiPath() {
		return apiPath_;
	}

	public String getFilePath() {
		return filePath_;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate_;
	}

	public String getServer() {
		return server_;
	}

	public Database getDatabase(final Session session) {
		if (closedDb_ != null)
			return closedDb_;

		// we must not cache this DB, otherwise it cannot get recycled
		return session.getDatabase(getApiPath());
	}
}
