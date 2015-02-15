package org.openntf.domino.helpers;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

/**
 * This class is mainly used in the DbDirectory to hold database metadata, without to reference the Database-object itself.
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class DatabaseMetaData implements Serializable {
	private static final Logger log_ = Logger.getLogger(DatabaseMetaData.class.getName());
	private static final long serialVersionUID = 1L;
	// These attributes will never change for a certain database object
	private final String fileName_;
	private final String filePath_;
	private final String server_;
	private final String replicaID_;

	// These attributes may change during the life time of a database. So we will update these, if we have an open database
	private String title_;
	private Date lastModifiedDate_;
	private String templateName_;
	private String designTemplateName_;
	private double size_;
	private String categories_;

	//private int fileFormat_;
	//private double limitRevisions;
	//private double limitUpdatedBy;

	// The comparators must be serializable, so put them in separate classes
	static class ApiPathComparator implements Comparator<DatabaseMetaData>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(final DatabaseMetaData o1, final DatabaseMetaData o2) {
			return o1.getApiPath().compareToIgnoreCase(o2.getApiPath());
		}

	}

	static class FilePathComparator implements Comparator<DatabaseMetaData>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(final DatabaseMetaData o1, final DatabaseMetaData o2) {
			return o1.getFilePath().compareToIgnoreCase(o2.getFilePath());
		}
	}

	static class TitleComparator implements Comparator<DatabaseMetaData>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(final DatabaseMetaData o1, final DatabaseMetaData o2) {
			return o1.getTitle().compareToIgnoreCase(o2.getTitle());
		}
	}

	static class LastModComparator implements Comparator<DatabaseMetaData>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(final DatabaseMetaData o1, final DatabaseMetaData o2) {
			if (o1.getLastModifiedDate() == null)
				return -1;
			if (o2.getLastModifiedDate() == null)
				return 1;
			return o1.getLastModifiedDate().compareTo(o2.getLastModifiedDate());
		}
	}

	/**
	 * Comparator to allow easy checking whether two databases have the same API path (server!!filepath)
	 * 
	 * @since org.openntf.domino 5.0.0
	 */
	public final static Comparator<DatabaseMetaData> APIPATH_COMPARATOR = new ApiPathComparator();
	/**
	 * Comparator to allow easy checking whether two databases have the same filepath (e.g. on different servers)
	 * 
	 * @since org.openntf.domino 5.0.0
	 */
	public final static Comparator<DatabaseMetaData> FILEPATH_COMPARATOR = new FilePathComparator();

	/**
	 * Comparator to allow easy checking whether two databases have the same title
	 * 
	 * @since org.openntf.domino 5.0.0
	 */
	public final static Comparator<DatabaseMetaData> TITLE_COMPARATOR = new TitleComparator();

	/**
	 * Comparator to allow easy checking whether two databases have the same title
	 * 
	 * @Deprecated as this takes a lot of performance and requires to open the database, it should not be used
	 * @since org.openntf.domino 5.0.0
	 */
	@Deprecated
	public final static Comparator<DatabaseMetaData> LASTMOD_COMPARATOR = new LastModComparator();

	public DatabaseMetaData(final lotus.domino.Database db) throws NotesException {

		templateName_ = db.getTemplateName();
		designTemplateName_ = db.getDesignTemplateName();
		fileName_ = db.getFileName();
		filePath_ = db.getFilePath();
		server_ = db.getServer();
		size_ = db.getSize();
		title_ = db.getTitle();
		replicaID_ = db.getReplicaID();
		categories_ = db.getCategories();

		if (db.isOpen()) {

			// These things are only available, if the DB is open
			lastModifiedDate_ = DominoUtils.toJavaDateSafe(db.getLastModified());
			//		sizeQuota_ = db.getSizeQuota();
			//		sizeWarning_ = db.getSizeWarning();
			//		created_ = db.getCreated();
			//		fileFormat_ = db.getFileFormat();
			//		limitRevisions = db.getLimitRevisions();
			//		limitUpdatedBy = db.getLimitUpdatedBy();
			//		lastModifiedDate_ = db.getLastModifiedDate();
		} else {
			lastModifiedDate_ = null;
		}

	}

	public String getTitle() {
		return title_;
	}

	public String getApiPath() {
		if (server_.length() > 0)
			return server_ + "!!" + filePath_;
		return filePath_;
	}

	public String getMetaReplicaID() {
		if (server_.length() > 0)
			return server_ + "!!" + replicaID_;
		return replicaID_;
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

	public String getTemplateName() {
		return templateName_;
	}

	public String getDesignTemplateName() {
		return designTemplateName_;
	}

	public String getFileName() {
		return fileName_;
	}

	public double getSize() {
		return size_;
	}

	public String getReplicaID() {
		return replicaID_;
	}

	public String getCategories() {
		return categories_;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filePath_ == null) ? 0 : filePath_.hashCode());
		result = prime * result + ((server_ == null) ? 0 : server_.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatabaseMetaData other = (DatabaseMetaData) obj;
		if (filePath_ == null) {
			if (other.filePath_ != null)
				return false;
		} else if (!filePath_.equals(other.filePath_))
			return false;
		if (server_ == null) {
			if (other.server_ != null)
				return false;
		} else if (!server_.equals(other.server_))
			return false;
		return true;
	}

}
