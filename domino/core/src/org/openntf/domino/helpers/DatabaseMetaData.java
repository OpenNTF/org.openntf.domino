package org.openntf.domino.helpers;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.DateTime;
import lotus.domino.NotesException;
import lotus.notes.addins.DominoServer;

import org.openntf.domino.ACL;
import org.openntf.domino.ACL.Level;
import org.openntf.domino.Agent;
import org.openntf.domino.AutoMime;
import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.Form;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.Outline;
import org.openntf.domino.Replication;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.events.IDominoEventFactory;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.schema.IDatabaseSchema;
import org.openntf.domino.transactions.DatabaseTransaction;
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

	/**
	 * Returns a ClosedDatabase represented by this metaData. The Database is only opened (and uses a NotesHandle), if you try to read
	 * properties (or call methods) which are not part of the MetaData
	 * 
	 */
	public Database getDatabase(final Session session) {
		return new ClosedDatabase(session);
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

	/**
	 * This is an inner class of the DatabaseMetaData and represents a closed database. The Database will be opened at demand and should
	 * behave the same way as an open database
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	protected class ClosedDatabase implements Database {
		private final Session session_;
		private Database db_;
		private DatabaseHolder databaseHolder_;

		/**
		 * Create a new "ClosedDatabase" for this session. (Use DatabaseMetaData.getDatabase(session) to get such a database)
		 * 
		 * @param session
		 *            the session for which this ClosedDatabase is
		 */
		public ClosedDatabase(final Session session) {
			session_ = session;
		}

		/**
		 * Instantiate the database from the current session_
		 * 
		 * @return
		 */
		protected Database getDatabase() {
			if (db_ == null) {
				db_ = session_.getDatabase(server_, filePath_);
				if (db_ == null) {
					throw new UserAccessException("User " + session_.getEffectiveUserName() + " cannot open database " + filePath_
							+ " on server " + server_ + ". Database does not exist or is probably locked by other process!");
				}
			}
			return db_;
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getApiPath()
		 */
		@Override
		public String getApiPath() {
			return DatabaseMetaData.this.getApiPath();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getApiPath()
		 */
		@Override
		public String getMetaReplicaID() {
			return DatabaseMetaData.this.getMetaReplicaID();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getServer()
		 */
		@Override
		public String getServer() {
			return server_;
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getFileName()
		 */
		@Override
		public String getFileName() {
			return fileName_;
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getFilePath()
		 */
		@Override
		public String getFilePath() {
			return filePath_;
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getTemplateName()
		 */
		@Override
		public String getTemplateName() {
			if (db_ != null)
				templateName_ = db_.getTemplateName(); // the templateName may have changed in the meantime
			return templateName_;
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getDesignTemplateName()
		 */
		@Override
		public String getDesignTemplateName() {
			if (db_ != null)
				designTemplateName_ = db_.getDesignTemplateName();
			return designTemplateName_;
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getSize()
		 */
		@Override
		public double getSize() {
			if (db_ != null)
				size_ = db_.getSize();
			return size_;
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getTitle()
		 */
		@Override
		public String getTitle() {
			if (db_ != null)
				title_ = db_.getTitle();
			return title_;

		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getCategories()
		 */
		@Override
		public String getCategories() {
			if (db_ != null)
				categories_ = db_.getCategories();
			return categories_;

		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getLastModifiedDate()
		 */
		@Override
		public Date getLastModifiedDate() {
			// RPr: this is a little hack to support the lastMod-date
			if (db_ != null || lastModifiedDate_ == null)
				lastModifiedDate_ = getDatabase().getLastModifiedDate();
			return lastModifiedDate_;
		}

		/*
		 * (non-Javadoc)
		 * @see lotus.domino.Base#recycle()
		 */
		@Override
		@Deprecated
		public void recycle() throws NotesException {
			if (db_ != null)
				db_.recycle();
			db_ = null;
		}

		/*
		 * (non-Javadoc)
		 * @see lotus.domino.Base#recycle(java.util.Vector)
		 */
		@SuppressWarnings("rawtypes")
		@Override
		@Deprecated
		public void recycle(final Vector paramVector) throws NotesException {
			org.openntf.domino.impl.Base.s_recycle(paramVector);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
		 */
		@Override
		public Session getAncestorSession() {
			return session_;
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getParent()
		 */
		@Override
		public Session getParent() {
			return session_;
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Base#addListener(org.openntf.domino.events.IDominoListener)
		 */
		@Override
		public void addListener(final IDominoListener listener) {
			getDatabase().addListener(listener);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Base#removeListener(org.openntf.domino.events.IDominoListener)
		 */
		@Override
		public void removeListener(final IDominoListener listener) {
			getDatabase().removeListener(listener);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Base#getListeners()
		 */
		@Override
		public List<IDominoListener> getListeners() {
			return getDatabase().getListeners();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Base#isDead()
		 */
		@Override
		public boolean isDead() {
			return getDatabase().isDead();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Base#getListeners(org.openntf.domino.events.EnumEvent)
		 */
		@Override
		public List<IDominoListener> getListeners(final EnumEvent event) {
			return getDatabase().getListeners(event);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Base#fireListener(org.openntf.domino.events.IDominoEvent)
		 */
		@Override
		public boolean fireListener(final IDominoEvent event) {
			return getDatabase().fireListener(event);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getEventFactory()
		 */
		@Override
		public IDominoEventFactory getEventFactory() {
			return getDatabase().getEventFactory();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#setEventFactory(org.openntf.domino.events.IDominoEventFactory)
		 */
		@Override
		public void setEventFactory(final IDominoEventFactory factory) {
			getDatabase().setEventFactory(factory);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#generateEvent(org.openntf.domino.events.EnumEvent, org.openntf.domino.Base, java.lang.Object)
		 */
		@Override
		public IDominoEvent generateEvent(final EnumEvent event, final Base<?> source, final Object payload) {
			return getDatabase().generateEvent(event, source, payload);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#createMergableDocumentCollection()
		 */
		@Override
		public DocumentCollection createMergableDocumentCollection() {
			return getDatabase().createMergableDocumentCollection();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#compactWithOptions(java.util.Set)
		 */
		@Override
		public int compactWithOptions(final Set<CompactOption> options) {
			return getDatabase().compactWithOptions(options);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#compactWithOptions(java.util.Set, java.lang.String)
		 */
		@Override
		public int compactWithOptions(final Set<CompactOption> options, final String spaceThreshold) {
			return getDatabase().compactWithOptions(options, spaceThreshold);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#createFTIndex(java.util.Set, boolean)
		 */
		@Override
		public void createFTIndex(final Set<FTIndexOption> options, final boolean recreate) {
			getDatabase().createFTIndex(options, recreate);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#fixup(java.util.Set)
		 */
		@Override
		public void fixup(final Set<FixupOption> options) {
			getDatabase().fixup(options);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#FTDomainSearch(java.lang.String, int, org.openntf.domino.Database.FTDomainSortOption, java.util.Set, int, int, java.lang.String)
		 */
		@Override
		public Document FTDomainSearch(final String query, final int maxDocs, final FTDomainSortOption sortOpt,
				final Set<FTDomainSearchOption> otherOpt, final int start, final int count, final String entryForm) {
			return getDatabase().FTDomainSearch(query, maxDocs, sortOpt, otherOpt, start, count, entryForm);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#FTSearch(java.lang.String, int, org.openntf.domino.Database.FTSortOption, java.util.Set)
		 */
		@Override
		public DocumentCollection FTSearch(final String query, final int maxDocs, final FTSortOption sortOpt,
				final Set<FTSearchOption> otherOpt) {
			return getDatabase().FTSearch(query, maxDocs, sortOpt, otherOpt);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#FTSearchRange(java.lang.String, int, org.openntf.domino.Database.FTSortOption, java.util.Set, int)
		 */
		@Override
		public DocumentCollection FTSearchRange(final String query, final int maxDocs, final FTSortOption sortOpt,
				final Set<FTSearchOption> otherOpt, final int start) {
			return getDatabase().FTSearchRange(query, maxDocs, sortOpt, otherOpt, start);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getDesign()
		 */
		@Override
		public DatabaseDesign getDesign() {
			return getDatabase().getDesign();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getDesign(boolean)
		 */
		@Override
		public org.openntf.domino.Database getXPageSharedDesignTemplate() throws FileNotFoundException {
			return getDatabase().getXPageSharedDesignTemplate();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getDocumentWithKey(java.io.Serializable)
		 */
		@Override
		public Document getDocumentWithKey(final Serializable key) {
			return getDatabase().getDocumentWithKey(key);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getDocumentWithKey(java.io.Serializable, boolean)
		 */
		@Override
		public Document getDocumentWithKey(final Serializable key, final boolean createOnFail) {
			return getDatabase().getDocumentWithKey(key, createOnFail);
		}

		@Override
		public Document getDocumentByKey(final Serializable key) {
			return getDocumentWithKey(key);
		}

		@Override
		public Document getDocumentByKey(final Serializable key, final boolean createOnFail) {
			return getDocumentWithKey(key, createOnFail);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getModifiedDocuments(lotus.domino.DateTime, org.openntf.domino.Database.ModifiedDocClass)
		 */
		@Override
		public DocumentCollection getModifiedDocuments(final DateTime since, final ModifiedDocClass noteClass) {
			return getDatabase().getModifiedDocuments(since, noteClass);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getModifiedDocuments(java.util.Date, org.openntf.domino.Database.ModifiedDocClass)
		 */
		@Override
		public DocumentCollection getModifiedDocuments(final Date since, final ModifiedDocClass noteClass) {
			return getDatabase().getModifiedDocuments(since, noteClass);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getModifiedDocuments(java.util.Date)
		 */
		@Override
		public DocumentCollection getModifiedDocuments(final Date since) {
			return getDatabase().getModifiedDocuments(since);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getModifiedNoteCount(java.util.Date, java.util.Set)
		 */
		@Override
		public int getModifiedNoteCount(final Date since, final Set<SelectOption> noteClass) {
			return getDatabase().getModifiedNoteCount(since, noteClass);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getACLActivityLog()
		 */
		@Override
		@Deprecated
		public Vector<String> getACLActivityLog() {
			return getDatabase().getACLActivityLog();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#compact()
		 */
		@Override
		public int compact() {
			return getDatabase().compact();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getModifiedNoteCount(java.util.Date)
		 */
		@Override
		public int getModifiedNoteCount(final Date since) {
			return getDatabase().getModifiedNoteCount(since);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#compactWithOptions(int)
		 */
		@Override
		@Deprecated
		public int compactWithOptions(final int options) {
			return getDatabase().compactWithOptions(options);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#compactWithOptions(int, java.lang.String)
		 */
		@Override
		@Deprecated
		public int compactWithOptions(final int options, final String spaceThreshold) {
			return getDatabase().compactWithOptions(options, spaceThreshold);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#compactWithOptions(java.lang.String)
		 */
		@Override
		public int compactWithOptions(final String options) {
			return getDatabase().compactWithOptions(options);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getLastFixupDate()
		 */
		@Override
		public Date getLastFixupDate() {
			return getDatabase().getLastFixupDate();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createCopy(java.lang.String, java.lang.String)
		 */
		@Override
		public Database createCopy(final String server, final String dbFile) {
			return getDatabase().createCopy(server, dbFile);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getLastFTIndexedDate()
		 */
		@Override
		public Date getLastFTIndexedDate() {
			return getDatabase().getLastFTIndexedDate();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createCopy(java.lang.String, java.lang.String, int)
		 */
		@Override
		public Database createCopy(final String server, final String dbFile, final int maxSize) {
			return getDatabase().createCopy(server, dbFile, maxSize);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getOption(org.openntf.domino.Database.DBOption)
		 */
		@Override
		public boolean getOption(final DBOption optionName) {
			return getDatabase().getOption(optionName);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createDocument()
		 */
		@Override
		public Document createDocument() {
			return getDatabase().createDocument();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createDocument(java.util.Map)
		 */
		@Override
		public Document createDocument(final Map<String, Object> itemValues) {
			return getDatabase().createDocument(itemValues);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createDocument(java.lang.Object[])
		 */
		@Override
		public Document createDocument(final Object... keyValuePairs) {
			return getDatabase().createDocument(keyValuePairs);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createDocumentCollection()
		 */
		@Override
		public DocumentCollection createDocumentCollection() {
			return getDatabase().createDocumentCollection();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#setFTIndexFrequency(org.openntf.domino.Database.FTIndexFrequency)
		 */
		@Override
		public void setFTIndexFrequency(final FTIndexFrequency frequency) {
			getDatabase().setFTIndexFrequency(frequency);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createFromTemplate(java.lang.String, java.lang.String, boolean)
		 */
		@Override
		public Database createFromTemplate(final String server, final String dbFile, final boolean inherit) {
			return getDatabase().createFromTemplate(server, dbFile, inherit);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createFromTemplate(java.lang.String, java.lang.String, boolean, int)
		 */
		@Override
		public Database createFromTemplate(final String server, final String dbFile, final boolean inherit, final int maxSize) {
			return getDatabase().createFromTemplate(server, dbFile, inherit, maxSize);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createFTIndex(int, boolean)
		 */
		@Override
		@Deprecated
		public void createFTIndex(final int options, final boolean recreate) {
			getDatabase().createFTIndex(options, recreate);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createNoteCollection(boolean)
		 */
		@Override
		public NoteCollection createNoteCollection(final boolean selectAllFlag) {
			return getDatabase().createNoteCollection(selectAllFlag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createOutline(java.lang.String)
		 */
		@Override
		public Outline createOutline(final String name) {
			return getDatabase().createOutline(name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createOutline(java.lang.String, boolean)
		 */
		@Override
		public Outline createOutline(final String name, final boolean defaultOutline) {
			return getDatabase().createOutline(name, defaultOutline);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createQueryView(java.lang.String, java.lang.String)
		 */
		@Override
		public View createQueryView(final String viewName, final String query) {
			return getDatabase().createQueryView(viewName, query);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createQueryView(java.lang.String, java.lang.String, lotus.domino.View)
		 */
		@Override
		public View createQueryView(final String viewName, final String query, final lotus.domino.View templateView) {
			return getDatabase().createQueryView(viewName, query, templateView);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createQueryView(java.lang.String, java.lang.String, lotus.domino.View, boolean)
		 */
		@Override
		public View createQueryView(final String viewName, final String query, final lotus.domino.View templateView,
				final boolean prohibitDesignRefresh) {
			return getDatabase().createQueryView(viewName, query, templateView, prohibitDesignRefresh);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createReplica(java.lang.String, java.lang.String)
		 */
		@Override
		public Database createReplica(final String server, final String dbFile) {
			return getDatabase().createReplica(server, dbFile);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createView()
		 */
		@Override
		public View createView() {
			return getDatabase().createView();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createView(java.lang.String)
		 */
		@Override
		public View createView(final String viewName) {
			return getDatabase().createView(viewName);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createView(java.lang.String, java.lang.String)
		 */
		@Override
		public View createView(final String viewName, final String selectionFormula) {
			return getDatabase().createView(viewName, selectionFormula);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createView(java.lang.String, java.lang.String, lotus.domino.View)
		 */
		@Override
		public View createView(final String viewName, final String selectionFormula, final lotus.domino.View templateView) {
			return getDatabase().createView(viewName, selectionFormula, templateView);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#startTransaction()
		 */
		@Override
		public DatabaseTransaction startTransaction() {
			return getDatabase().startTransaction();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#createView(java.lang.String, java.lang.String, lotus.domino.View, boolean)
		 */
		@Override
		public View createView(final String viewName, final String selectionFormula, final lotus.domino.View templateView,
				final boolean prohibitDesignRefresh) {
			return getDatabase().createView(viewName, selectionFormula, templateView, prohibitDesignRefresh);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#closeTransaction()
		 */
		@Override
		public void closeTransaction() {
			getDatabase().closeTransaction();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#enableFolder(java.lang.String)
		 */
		@Override
		public void enableFolder(final String folder) {
			getDatabase().enableFolder(folder);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getTransaction()
		 */
		@Override
		public DatabaseTransaction getTransaction() {
			return getDatabase().getTransaction();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#fixup()
		 */
		@Override
		public void fixup() {
			getDatabase().fixup();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getUNID(java.lang.String)
		 */
		@Override
		public String getUNID(final String noteid) {
			return getDatabase().getUNID(noteid);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#fixup(int)
		 */
		@Override
		@Deprecated
		public void fixup(final int options) {
			getDatabase().fixup(options);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getUNID(int)
		 */
		@Override
		public String getUNID(final int noteid) {
			return getDatabase().getUNID(noteid);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getDocumentByUNID(java.lang.String, boolean)
		 */
		@Override
		public Document getDocumentByUNID(final String unid, final boolean deferDelegate) {
			return getDatabase().getDocumentByUNID(unid, deferDelegate);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getDocumentByID(java.lang.String, boolean)
		 */
		@Override
		public Document getDocumentByID(final String noteid, final boolean deferDelegate) {
			return getDatabase().getDocumentByID(noteid, deferDelegate);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#FTDomainSearch(java.lang.String, int, int, int, int, int, java.lang.String)
		 */
		@Override
		@Deprecated
		public Document FTDomainSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt, final int start,
				final int count, final String entryForm) {
			return getDatabase().FTDomainSearch(query, maxDocs, sortOpt, otherOpt, start, count, entryForm);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getDocumentByID(int, boolean)
		 */
		@Override
		public Document getDocumentByID(final int noteid, final boolean deferDelegate) {
			return getDatabase().getDocumentByID(noteid, deferDelegate);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#setTransaction(org.openntf.domino.transactions.DatabaseTransaction)
		 */
		@Override
		public void setTransaction(final DatabaseTransaction txn) {
			getDatabase().setTransaction(txn);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#FTDomainSearch(java.lang.String, int, org.openntf.domino.Database.FTSortOption, int, int, int, java.lang.String)
		 */
		@Override
		public Document FTDomainSearch(final String query, final int maxDocs, final FTSortOption sortOpt, final int otherOpt,
				final int start, final int count, final String entryForm) {
			return getDatabase().FTDomainSearch(query, maxDocs, sortOpt, otherOpt, start, count, entryForm);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getDominoServer()
		 */
		@Override
		public DominoServer getDominoServer() {
			return getDatabase().getDominoServer();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#refreshDesign()
		 */
		@Override
		public void refreshDesign() {
			getDatabase().refreshDesign();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#openMail()
		 */
		@Override
		public void openMail() {
			getDatabase().openMail();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#FTSearch(java.lang.String)
		 */
		@Override
		public DocumentCollection FTSearch(final String query) {
			return getDatabase().FTSearch(query);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getMail()
		 */
		@Override
		public Database getMail() {
			return getDatabase().getMail();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#FTSearch(java.lang.String, int)
		 */
		@Override
		public DocumentCollection FTSearch(final String query, final int maxDocs) {
			return getDatabase().FTSearch(query, maxDocs);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getDocumentMap()
		 */
		@Override
		public Map<Serializable, Document> getDocumentMap() {
			return getDatabase().getDocumentMap();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#FTSearch(java.lang.String, int, int, int)
		 */
		@Override
		@Deprecated
		public DocumentCollection FTSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt) {
			return getDatabase().FTSearch(query, maxDocs, sortOpt, otherOpt);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#FTSearch(java.lang.String, int, org.openntf.domino.Database.FTSortOption, int)
		 */
		@Override
		public DocumentCollection FTSearch(final String query, final int maxDocs, final FTSortOption sortOpt, final int otherOpt) {
			return getDatabase().FTSearch(query, maxDocs, sortOpt, otherOpt);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getSchema()
		 */
		@Override
		public IDatabaseSchema getSchema() {
			return getDatabase().getSchema();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#setSchema(org.openntf.domino.schema.IDatabaseSchema)
		 */
		@Override
		public void setSchema(final IDatabaseSchema schema) {
			getDatabase().setSchema(schema);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#FTSearchRange(java.lang.String, int, int, int, int)
		 */
		@Override
		@Deprecated
		public DocumentCollection FTSearchRange(final String query, final int maxDocs, final int sortOpt, final int otherOpt,
				final int start) {
			return getDatabase().FTSearchRange(query, maxDocs, sortOpt, otherOpt, start);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#isReplicationDisabled()
		 */
		@Override
		public boolean isReplicationDisabled() {
			return getDatabase().isReplicationDisabled();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getHttpURL(boolean)
		 */
		@Override
		public String getHttpURL(final boolean usePath) {
			return getDatabase().getHttpURL(usePath);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#FTSearchRange(java.lang.String, int, org.openntf.domino.Database.FTSortOption, int, int)
		 */
		@Override
		public DocumentCollection FTSearchRange(final String query, final int maxDocs, final FTSortOption sortOpt, final int otherOpt,
				final int start) {
			return getDatabase().FTSearchRange(query, maxDocs, sortOpt, otherOpt, start);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getAutoMime()
		 */
		@Override
		public AutoMime getAutoMime() {
			return getDatabase().getAutoMime();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getACL()
		 */
		@Override
		public ACL getACL() {
			return getDatabase().getACL();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#setAutoMime(org.openntf.domino.AutoMime)
		 */
		@Override
		public void setAutoMime(final AutoMime autoMime) {
			getDatabase().setAutoMime(autoMime);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getAgent(java.lang.String)
		 */
		@Override
		public Agent getAgent(final String name) {
			return getDatabase().getAgent(name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getAgents()
		 */
		@Override
		public Vector<Agent> getAgents() {
			return getDatabase().getAgents();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.ext.Database#getLocale()
		 */
		@Override
		public Locale getLocale() {
			return getDatabase().getLocale();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getAllDocuments()
		 */
		@Override
		public DocumentCollection getAllDocuments() {
			return getDatabase().getAllDocuments();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getAllReadDocuments()
		 */
		@Override
		public DocumentCollection getAllReadDocuments() {
			return getDatabase().getAllReadDocuments();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getAllReadDocuments(java.lang.String)
		 */
		@Override
		public DocumentCollection getAllReadDocuments(final String userName) {
			return getDatabase().getAllReadDocuments(userName);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getAllUnreadDocuments()
		 */
		@Override
		public DocumentCollection getAllUnreadDocuments() {
			return getDatabase().getAllUnreadDocuments();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getAllUnreadDocuments(java.lang.String)
		 */
		@Override
		public DocumentCollection getAllUnreadDocuments(final String userName) {
			return getDatabase().getAllUnreadDocuments(userName);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getCreated()
		 */
		@Override
		public org.openntf.domino.DateTime getCreated() {
			return getDatabase().getCreated();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getCurrentAccessLevel()
		 */
		@Override
		public int getCurrentAccessLevel() {
			return getDatabase().getCurrentAccessLevel();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getDB2Schema()
		 */
		@Override
		public String getDB2Schema() {
			return getDatabase().getDB2Schema();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getDocumentByID(java.lang.String)
		 */
		@Override
		public Document getDocumentByID(final String noteid) {
			return getDatabase().getDocumentByID(noteid);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getDocumentByUNID(java.lang.String)
		 */
		@Override
		public Document getDocumentByUNID(final String unid) {
			return getDatabase().getDocumentByUNID(unid);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getDocumentByURL(java.lang.String, boolean)
		 */
		@Override
		public Document getDocumentByURL(final String url, final boolean reload) {
			return getDatabase().getDocumentByURL(url, reload);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getDocumentByURL(java.lang.String, boolean, boolean, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
		 */
		@Override
		public Document getDocumentByURL(final String url, final boolean reload, final boolean reloadIfModified, final boolean urlList,
				final String charSet, final String webUser, final String webPassword, final String proxyUser, final String proxyPassword,
				final boolean returnImmediately) {
			return getDatabase().getDocumentByURL(url, reload, reloadIfModified, urlList, charSet, webUser, webPassword, proxyUser,
					proxyPassword, returnImmediately);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getFileFormat()
		 */
		@Override
		public int getFileFormat() {
			return getDatabase().getFileFormat();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getFolderReferencesEnabled()
		 */
		@Override
		public boolean getFolderReferencesEnabled() {
			return getDatabase().getFolderReferencesEnabled();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getForm(java.lang.String)
		 */
		@Override
		public Form getForm(final String name) {
			return getDatabase().getForm(name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getForms()
		 */
		@Override
		public Vector<Form> getForms() {
			return getDatabase().getForms();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getFTIndexFrequency()
		 */
		@Override
		public int getFTIndexFrequency() {
			return getDatabase().getFTIndexFrequency();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getHttpURL()
		 */
		@Override
		public String getHttpURL() {
			return getDatabase().getHttpURL();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getLastFixup()
		 */
		@Override
		public org.openntf.domino.DateTime getLastFixup() {
			return getDatabase().getLastFixup();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getLastFTIndexed()
		 */
		@Override
		public org.openntf.domino.DateTime getLastFTIndexed() {
			return getDatabase().getLastFTIndexed();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getLastModified()
		 */
		@Override
		public org.openntf.domino.DateTime getLastModified() {
			return getDatabase().getLastModified();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getLimitRevisions()
		 */
		@Override
		public double getLimitRevisions() {
			return getDatabase().getLimitRevisions();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getLimitUpdatedBy()
		 */
		@Override
		public double getLimitUpdatedBy() {
			return getDatabase().getLimitUpdatedBy();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getListInDbCatalog()
		 */
		@Override
		public boolean getListInDbCatalog() {
			return getDatabase().getListInDbCatalog();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getManagers()
		 */
		@Override
		public Vector<String> getManagers() {
			return getDatabase().getManagers();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getMaxSize()
		 */
		@Override
		public long getMaxSize() {
			return getDatabase().getMaxSize();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getModifiedDocuments()
		 */
		@Override
		public DocumentCollection getModifiedDocuments() {
			return getDatabase().getModifiedDocuments();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getModifiedDocuments(lotus.domino.DateTime)
		 */
		@Override
		public DocumentCollection getModifiedDocuments(final DateTime since) {
			return getDatabase().getModifiedDocuments(since);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getModifiedDocuments(lotus.domino.DateTime, int)
		 */
		@Override
		@Deprecated
		public DocumentCollection getModifiedDocuments(final DateTime since, final int noteClass) {
			return getDatabase().getModifiedDocuments(since, noteClass);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getNotesURL()
		 */
		@Override
		public String getNotesURL() {
			return getDatabase().getNotesURL();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getOption(int)
		 */
		@Override
		@Deprecated
		public boolean getOption(final int optionName) {
			return getDatabase().getOption(optionName);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getOutline(java.lang.String)
		 */
		@Override
		public Outline getOutline(final String outlineName) {
			return getDatabase().getOutline(outlineName);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getPercentUsed()
		 */
		@Override
		public double getPercentUsed() {
			return getDatabase().getPercentUsed();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getProfileDocCollection(java.lang.String)
		 */
		@Override
		public DocumentCollection getProfileDocCollection(final String profileName) {
			return getDatabase().getProfileDocCollection(profileName);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getProfileDocument(java.lang.String, java.lang.String)
		 */
		@Override
		public Document getProfileDocument(final String profileName, final String profileKey) {
			return getDatabase().getProfileDocument(profileName, profileKey);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getReplicaID()
		 */
		@Override
		public String getReplicaID() {
			return getDatabase().getReplicaID();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getReplicationInfo()
		 */
		@Override
		public Replication getReplicationInfo() {
			return getDatabase().getReplicationInfo();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getSizeQuota()
		 */
		@Override
		public int getSizeQuota() {
			return getDatabase().getSizeQuota();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getSizeWarning()
		 */
		@Override
		public long getSizeWarning() {
			return getDatabase().getSizeWarning();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getType()
		 */
		@Override
		@Deprecated
		public int getType() {
			return getDatabase().getType();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getUndeleteExpireTime()
		 */
		@Override
		public int getUndeleteExpireTime() {
			return getDatabase().getUndeleteExpireTime();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getURL()
		 */
		@Override
		public String getURL() {
			return getDatabase().getURL();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getURLHeaderInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public String getURLHeaderInfo(final String url, final String header, final String webUser, final String webPassword,
				final String proxyUser, final String proxyPassword) {
			return getDatabase().getURLHeaderInfo(url, header, webUser, webPassword, proxyUser, proxyPassword);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getView(java.lang.String)
		 */
		@Override
		public View getView(final String name) {
			return getDatabase().getView(name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#getViews()
		 */
		@Override
		public Vector<View> getViews() {
			return getDatabase().getViews();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#grantAccess(java.lang.String, int)
		 */
		@Override
		@Deprecated
		public void grantAccess(final String name, final int level) {
			getDatabase().grantAccess(name, level);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#grantAccess(java.lang.String, org.openntf.domino.ACL.Level)
		 */
		@Override
		public void grantAccess(final String name, final Level level) {
			getDatabase().grantAccess(name, level);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isAllowOpenSoftDeleted()
		 */
		@Override
		public boolean isAllowOpenSoftDeleted() {
			return getDatabase().isAllowOpenSoftDeleted();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isClusterReplication()
		 */
		@Override
		public boolean isClusterReplication() {
			return getDatabase().isClusterReplication();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isConfigurationDirectory()
		 */
		@Override
		public boolean isConfigurationDirectory() {
			return getDatabase().isConfigurationDirectory();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isCurrentAccessPublicReader()
		 */
		@Override
		public boolean isCurrentAccessPublicReader() {
			return getDatabase().isCurrentAccessPublicReader();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isCurrentAccessPublicWriter()
		 */
		@Override
		public boolean isCurrentAccessPublicWriter() {
			return getDatabase().isCurrentAccessPublicWriter();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isDB2()
		 */
		@Override
		public boolean isDB2() {
			return getDatabase().isDB2();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isDelayUpdates()
		 */
		@Override
		public boolean isDelayUpdates() {
			return getDatabase().isDelayUpdates();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isDesignLockingEnabled()
		 */
		@Override
		public boolean isDesignLockingEnabled() {
			return getDatabase().isDesignLockingEnabled();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isDirectoryCatalog()
		 */
		@Override
		public boolean isDirectoryCatalog() {
			return getDatabase().isDirectoryCatalog();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isDocumentLockingEnabled()
		 */
		@Override
		public boolean isDocumentLockingEnabled() {
			return getDatabase().isDocumentLockingEnabled();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isFTIndexed()
		 */
		@Override
		public boolean isFTIndexed() {
			return getDatabase().isFTIndexed();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isInMultiDbIndexing()
		 */
		@Override
		public boolean isInMultiDbIndexing() {
			return getDatabase().isInMultiDbIndexing();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isInService()
		 */
		@Override
		public boolean isInService() {
			return getDatabase().isInService();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isLink()
		 */
		@Override
		public boolean isLink() {
			return getDatabase().isLink();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isMultiDbSearch()
		 */
		@Override
		public boolean isMultiDbSearch() {
			return getDatabase().isMultiDbSearch();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isOpen()
		 */
		@Override
		public boolean isOpen() {
			// isOpen does NOT open the database. Use open instead
			if (db_ == null)
				return false;
			return db_.isOpen();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isPendingDelete()
		 */
		@Override
		public boolean isPendingDelete() {
			return getDatabase().isPendingDelete();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isPrivateAddressBook()
		 */
		@Override
		public boolean isPrivateAddressBook() {
			return getDatabase().isPrivateAddressBook();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#isPublicAddressBook()
		 */
		@Override
		public boolean isPublicAddressBook() {
			return getDatabase().isPublicAddressBook();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#markForDelete()
		 */
		@Override
		public void markForDelete() {
			getDatabase().markForDelete();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#open()
		 */
		@Override
		public boolean open() {
			try {
				getDatabase().open();
			} catch (Exception e) {
				log_.log(java.util.logging.Level.FINE, "Could not open the database: " + getApiPath(), e);
			}
			return isOpen();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#openByReplicaID(java.lang.String, java.lang.String)
		 */
		@Override
		public boolean openByReplicaID(final String server, final String replicaId) {
			return getDatabase().openByReplicaID(server, replicaId);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#openIfModified(java.lang.String, java.lang.String, lotus.domino.DateTime)
		 */
		@Override
		public boolean openIfModified(final String server, final String dbFile, final DateTime modifiedSince) {
			return getDatabase().openIfModified(server, dbFile, modifiedSince);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#openWithFailover(java.lang.String, java.lang.String)
		 */
		@Override
		public boolean openWithFailover(final String server, final String dbFile) {
			return getDatabase().openWithFailover(server, dbFile);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#queryAccess(java.lang.String)
		 */
		@Override
		public int queryAccess(final String name) {
			return getDatabase().queryAccess(name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#queryAccessPrivileges(java.lang.String)
		 */
		@Override
		public int queryAccessPrivileges(final String name) {
			return getDatabase().queryAccessPrivileges(name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#queryAccessRoles(java.lang.String)
		 */
		@Override
		public Vector<String> queryAccessRoles(final String name) {
			return getDatabase().queryAccessRoles(name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#remove()
		 */
		@Override
		public void remove() {
			getDatabase().remove();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#removeFTIndex()
		 */
		@Override
		public void removeFTIndex() {
			getDatabase().removeFTIndex();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#replicate(java.lang.String)
		 */
		@Override
		public boolean replicate(final String server) {
			return getDatabase().replicate(server);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#revokeAccess(java.lang.String)
		 */
		@Override
		public void revokeAccess(final String name) {
			getDatabase().revokeAccess(name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#search(java.lang.String)
		 */
		@Override
		public DocumentCollection search(final String formula) {
			return getDatabase().search(formula);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#search(java.lang.String, lotus.domino.DateTime)
		 */
		@Override
		public DocumentCollection search(final String formula, final DateTime startDate) {
			return getDatabase().search(formula, startDate);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#search(java.lang.String, lotus.domino.DateTime, int)
		 */
		@Override
		public DocumentCollection search(final String formula, final DateTime startDate, final int maxDocs) {
			return getDatabase().search(formula, startDate, maxDocs);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setAllowOpenSoftDeleted(boolean)
		 */
		@Override
		public void setAllowOpenSoftDeleted(final boolean flag) {
			getDatabase().setAllowOpenSoftDeleted(flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setCategories(java.lang.String)
		 */
		@Override
		public void setCategories(final String categories) {
			getDatabase().setCategories(categories);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setDelayUpdates(boolean)
		 */
		@Override
		public void setDelayUpdates(final boolean flag) {
			getDatabase().setDelayUpdates(flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setDesignLockingEnabled(boolean)
		 */
		@Override
		public void setDesignLockingEnabled(final boolean flag) {
			getDatabase().setDesignLockingEnabled(flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setDocumentLockingEnabled(boolean)
		 */
		@Override
		public void setDocumentLockingEnabled(final boolean flag) {
			getDatabase().setDocumentLockingEnabled(flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setFolderReferencesEnabled(boolean)
		 */
		@Override
		public void setFolderReferencesEnabled(final boolean flag) {
			getDatabase().setFolderReferencesEnabled(flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setFTIndexFrequency(int)
		 */
		@Override
		@Deprecated
		public void setFTIndexFrequency(final int frequency) {
			getDatabase().setFTIndexFrequency(frequency);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setInMultiDbIndexing(boolean)
		 */
		@Override
		public void setInMultiDbIndexing(final boolean flag) {
			getDatabase().setInMultiDbIndexing(flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setInService(boolean)
		 */
		@Override
		public void setInService(final boolean flag) {
			getDatabase().setInService(flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setLimitRevisions(double)
		 */
		@Override
		public void setLimitRevisions(final double revisions) {
			getDatabase().setLimitRevisions(revisions);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setLimitUpdatedBy(double)
		 */
		@Override
		public void setLimitUpdatedBy(final double updatedBys) {
			getDatabase().setLimitUpdatedBy(updatedBys);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setListInDbCatalog(boolean)
		 */
		@Override
		public void setListInDbCatalog(final boolean flag) {
			getDatabase().setListInDbCatalog(flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setOption(int, boolean)
		 */
		@Override
		@Deprecated
		public void setOption(final int optionName, final boolean flag) {
			getDatabase().setOption(optionName, flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setOption(org.openntf.domino.Database.DBOption, boolean)
		 */
		@Override
		public void setOption(final DBOption optionName, final boolean flag) {
			getDatabase().setOption(optionName, flag);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setSizeQuota(int)
		 */
		@Override
		public void setSizeQuota(final int quota) {
			getDatabase().setSizeQuota(quota);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setSizeWarning(int)
		 */
		@Override
		public void setSizeWarning(final int warning) {
			getDatabase().setSizeWarning(warning);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setTitle(java.lang.String)
		 */
		@Override
		public void setTitle(final String title) {
			getDatabase().setTitle(title);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#setUndeleteExpireTime(int)
		 */
		@Override
		public void setUndeleteExpireTime(final int hours) {
			getDatabase().setUndeleteExpireTime(hours);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#sign()
		 */
		@Override
		public void sign() {
			getDatabase().sign();
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#sign(int)
		 */
		@Override
		public void sign(final int documentType) {
			getDatabase().sign(documentType);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType)
		 */
		@Override
		public void sign(final SignDocType documentType) {
			getDatabase().sign(documentType);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#sign(int, boolean)
		 */
		@Override
		@Deprecated
		public void sign(final int documentType, final boolean existingSigsOnly) {
			getDatabase().sign(documentType, existingSigsOnly);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType, boolean)
		 */
		@Override
		public void sign(final SignDocType documentType, final boolean existingSigsOnly) {
			getDatabase().sign(documentType, existingSigsOnly);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#sign(int, boolean, java.lang.String)
		 */
		@Override
		@Deprecated
		public void sign(final int documentType, final boolean existingSigsOnly, final String name) {
			getDatabase().sign(documentType, existingSigsOnly, name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType, boolean, java.lang.String)
		 */
		@Override
		public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name) {
			getDatabase().sign(documentType, existingSigsOnly, name);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#sign(int, boolean, java.lang.String, boolean)
		 */
		@Override
		@Deprecated
		public void sign(final int documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid) {
			getDatabase().sign(documentType, existingSigsOnly, name, nameIsNoteid);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#sign(org.openntf.domino.Database.SignDocType, boolean, java.lang.String, boolean)
		 */
		@Override
		public void sign(final SignDocType documentType, final boolean existingSigsOnly, final String name, final boolean nameIsNoteid) {
			getDatabase().sign(documentType, existingSigsOnly, name, nameIsNoteid);
		}

		/*
		 * (non-Javadoc)
		 * @see org.openntf.domino.Database#updateFTIndex(boolean)
		 */
		@Override
		public void updateFTIndex(final boolean create) {
			getDatabase().updateFTIndex(create);
		}

		@Override
		public void fillExceptionDetails(final List<Entry> result) {
			if (db_ != null) {
				db_.fillExceptionDetails(result);
			} else {
				if (session_ != null)
					session_.fillExceptionDetails(result);
				result.add(new ExceptionDetails.Entry(this, getApiPath()));
			}

		}

		@Override
		public Type getTypeEx() {
			return getDatabase().getTypeEx();
		}

		@Override
		public DatabaseHolder getDatabaseHolder() {
			if (databaseHolder_ == null) {
				databaseHolder_ = new DatabaseHolder(this);
			}
			return databaseHolder_;
		}

	}

}
